package com.hyeonsik.boot.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyeonsik.boot.mapper.BlacklistRepository;
import com.hyeonsik.boot.mapper.MemberRepository;
import com.hyeonsik.boot.mapper.RestaurantRepository;
import com.hyeonsik.boot.mapper.UserRepository;
import com.hyeonsik.boot.service.BlacklistService;
import com.hyeonsik.boot.service.FavoriteService;
import com.hyeonsik.boot.service.ReviewService;
import com.hyeonsik.boot.service.Smsservice;
import com.hyeonsik.boot.service.UploadFile;
import com.hyeonsik.boot.service.UserService;
import com.hyeonsik.boot.service.WaitlistService;
import com.hyeonsik.boot.vo.BlackList;
import com.hyeonsik.boot.vo.Favorite;
import com.hyeonsik.boot.vo.MapVo;
import com.hyeonsik.boot.vo.Member;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.Review;
import com.hyeonsik.boot.vo.UserVo;
import com.hyeonsik.boot.vo.UserWaitlist;
import com.hyeonsik.boot.vo.Waitlist;
import com.hyeonsik.boot.vo.WaitlistHistory;

@Controller
public class StoreController {
 
    @Autowired
    private UserService userService;   
    @Autowired
    private WaitlistService waitlistService;  
    @Autowired
    private Smsservice smsService; 
    @Autowired
    private ReviewService reviewService; 
    @Autowired
    private BlacklistService blacklistService; 
    @Autowired
    private UploadFile uploadFile;
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    UserRepository userRepository;  
    @Autowired
    MemberRepository memberRepository;  
    @Autowired
    BlacklistRepository blacklistRepository;  
    @Autowired
    RestaurantRepository restaurantRepository;
    
    
    private String createLink(String url) {
        return url;
    }
    
 
    @GetMapping("/store")
    public String storeView(@RequestParam int adminNo, Authentication authentication) {
    	
    	MapVo map = userService.storefound(adminNo);
    	System.out.println(adminNo);
    	System.out.println(map);
        return "store"; // HTML 뷰 반환
    }
 
    @GetMapping("/api/store")
    @ResponseBody
    public MapVo storeData(@RequestParam int adminNo) {
    	MapVo map = new MapVo();
        map=  userService.storefound(adminNo);

        return map;
    }
    
    @GetMapping("/store/location")
    @ResponseBody
    public MapVo storelocation(@RequestParam int adminNo) {
    	MapVo map = new MapVo();
        map = userService.locationfound(adminNo);
        return map;
    }
    
    
    @GetMapping("/findReviews")
    @ResponseBody
    public List<Review> pageReview(@RequestParam Integer adminNo){
    	List<Review> review = reviewService.findReview(adminNo);
    	return review;
    
    }
    

    
    
 // 도메인 로그인 시 대기열 추가
    @PostMapping("/add/{admin_no}")
    public ResponseEntity<Waitlist> addWaitlist(@PathVariable("admin_no") Integer adminNo, @RequestBody Waitlist waitlist, Authentication authentication) throws 
    NoSuchAlgorithmException, InvalidKeyException, IOException, RestClientException, URISyntaxException {
        String phoneNumber = null;
        String userName = null;
        Long userNo = null;
        String loginType = null;

        if (authentication.getPrincipal() instanceof UserVo) {
            // 도메인 로그인일 경우
            UserVo userVo = (UserVo) authentication.getPrincipal();
            phoneNumber = userVo.getUserPhone();
            userName = userVo.getUserName();
            userNo = userVo.getUserNo();
            loginType = "DOMAIN";         
            if (waitlistService.isUserAlreadyInWaitlist(userNo, adminNo)) {
                return ResponseEntity.badRequest().build();
            }
                
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email").toString();
            Member member = memberRepository.findByEmail(email);
            phoneNumber = member.getPhoneNumber();
            userName = member.getName();
            userNo = member.getId();
            loginType = member.getProvider();
            
            if (waitlistService.isUserAlreadyInWaitlist(userNo, adminNo)) {
                return ResponseEntity.badRequest().build();
            }                                   
        }
     
        waitlist.setUserPhone(phoneNumber);
        waitlist.setUserName(userName);
        waitlist.setUserNo(userNo);
        waitlist.setLoginType(loginType);
        

        waitlistService.addWaitlist(waitlist, adminNo);
   
        
        String message ="[푸드잇] "  + waitlist.getAdminCafe() + " 매장"  + 
        "의 " + waitlist.getQueueNumber() + "번으로 예약 되었습니다. 대기 취소를 원하시면 홈페이지를 방문해 주세요.:\n "
        		+ createLink("https://www.foodeat.store");
        
        smsService.sendSms(phoneNumber, message);
        

        return ResponseEntity.ok().body(waitlist);
    }
 
    
    
    @GetMapping("Waitlistdelete")
    public String waitlistdelete() {
    	return "Waitlistdelete";
    }
 
    
    //사용자 네비게이션 바 매안페이지
    @GetMapping("/waitlist")
    public ResponseEntity<List<Waitlist>> getWaitlistByUserNo(Authentication authentication) {
    	
    	Long userNo = null;
    	   if (authentication == null || !authentication.isAuthenticated()) {
               // 인증되지 않은 경우 로그인 페이지로 이동
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
           }
    	   if (authentication.getPrincipal() instanceof UserVo) {
          UserVo userVo = (UserVo) authentication.getPrincipal();
          userNo = userVo.getUserNo();
  
    	   }
    	   
    	  else  if (authentication.getPrincipal() instanceof OAuth2User) {
               // 소셜 로그인일 경우
               OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
               String email = oAuth2User.getAttribute("email").toString();
               Member member = memberRepository.findByEmail(email);
                userNo = member.getId();
                System.out.println(userNo);
    	  }
    	List<Waitlist> waitlist = waitlistService.getWaitlistByUserNo(userNo);
        return ResponseEntity.ok(waitlist);
  
    }
    
    // ADMIN 관리자 로그인시 각 가게에 맞는 대기열 정보 조회 API
    @GetMapping("/OwnerWait")
    public ResponseEntity<List<UserWaitlist>> getWaitlistByUserNo(Principal principal) {
    	 String username = principal.getName(); // 로그인한 사용자의 아이디
    	 UserVo user = userService.findByUserId(username); // 아이디로 사용자 정보 조회
    	 int adminNo = user.getAdmin_id(); // 사용자 정보에서 adminNo 추출
    	 List<UserWaitlist> waitlist = waitlistService.getWaitlistByAdminNo(adminNo);
    	 System.out.println(waitlist);
    	 return ResponseEntity.ok(waitlist);  
    }
    
    
    // 예약ON/OFF 버튼 누를시 예약가능/불가능 상태 변경 컨트롤러
    @PostMapping("/toggleIsRevable")
    public ResponseEntity<String> toggleIsRevable(Principal principal) {
        String username = principal.getName();
        UserVo user = userService.findByUserId(username);
        int adminNo = user.getAdmin_id();
        waitlistService.toggleIsRevable(adminNo);
        return ResponseEntity.ok("Success");
    }
   
    @PostMapping("/updatePhone")
    @ResponseBody
    public String updatePhone(@RequestParam String phoneNumber, Authentication authentication) {
         OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
         String email = oAuth2User.getAttribute("email").toString();
         Member member = memberRepository.findByEmail(email);
         member.setPhoneNumber(phoneNumber);
         JSONObject response1 = new JSONObject();
         response1.put("phoneNumber", phoneNumber);
         memberRepository.save(member);
   
		return phoneNumber.toString();
    }
    
    @GetMapping("/SNS")
    @ResponseBody
    public String SNS(Authentication authentication) {
    	if(authentication.getPrincipal() instanceof OAuth2User) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
  
        String email = oAuth2User.getAttribute("email").toString();
        Member member = memberRepository.findByEmail(email);
   
        System.out.println(member);
        JSONObject response = new JSONObject();
        response.put("email", email);
        
        if(member!=null && member.getPhoneNumber() != null) {
            response.put("PhoneNumber", true);
        } else {
            response.put("PhoneNumber", false);
        }
        
        return response.toString();
    }
		return null;
    }


    // 예약 대기열 삭제 API(식당입장)
    @DeleteMapping("/delete/{adminNo}")
    public ResponseEntity<Void> deleteWaitlist(@PathVariable Integer adminNo, Authentication authentication) {
       
        // 예약 대기열을 삭제합니다.
        waitlistService.deleteWaitlist(adminNo);

        return ResponseEntity.ok().build();
    }
    
    // 예약 대기열 취소 API(취소 입장)
    @DeleteMapping("/delete/{adminNo}/{userNo}")
    public ResponseEntity<Void> deleteWaitlist2(@PathVariable Integer adminNo, @PathVariable Long userNo, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 경우 로그인 페이지로 이동
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 예약 대기열을 삭제
        waitlistService.deleteWaitlist2(userNo,adminNo);

        return ResponseEntity.ok().build();
            
    }
    
    // 예약 대기열 점주입장 삭제 API(문자 전송)
    @DeleteMapping("/admindelete/{adminNo}/{userNo}")
    public ResponseEntity<Void> admindelete(@PathVariable Integer adminNo, @PathVariable Long userNo, @RequestParam String phoneNumber, 
    		Authentication authentication, Principal principal) 
    		throws JsonProcessingException, RestClientException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, URISyntaxException {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 경우 로그인 페이지로 이동
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName(); // 로그인한 사용자의 아이디
   	 	UserVo user = userService.findByUserId(username); // 아이디로 사용자 정보 조회
   	 	adminNo = user.getAdmin_id(); // 사용자 정보에서 adminNo 추출
   	    String message = "[푸드잇] 지금 매장에 입장해 주세요. 3분 내 미 입장 시 예약이 취소됩니다.";
   	 	List<UserWaitlist> waitlist = waitlistService.getWaitlistByAdminNo(adminNo);
   	  if (!waitlist.isEmpty() && waitlist.get(0).getQueueNumber() == 1) {
         smsService.sendSms(phoneNumber, message);
     }
        waitlistService.deleteWaitlist2(userNo,adminNo);

        return ResponseEntity.ok().build();
            
    }

   
    @GetMapping("/userinformation1")
    @ResponseBody
    public Member userInformation(Authentication authentication) {
    	if(authentication.getPrincipal() instanceof OAuth2User) {
            // 소셜 로그인
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email").toString();
            Member member = memberRepository.findByEmail(email);
            return member;
    }
		return null;
    }
    
    @GetMapping("/pagereview")
    @ResponseBody
    public ResponseEntity<List<WaitlistHistory>> getWaitlistHistory(Authentication authentication) { 	
    	Long userNo = null;
   	     if (authentication.getPrincipal() instanceof UserVo) {   	 
         UserVo userVo = (UserVo) authentication.getPrincipal();
         userNo = userVo.getUserNo();
 
   	   }
   	   
   	  else  if (authentication.getPrincipal() instanceof OAuth2User) {
              // 소셜 로그인일 경우
              OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
              String email = oAuth2User.getAttribute("email").toString();
              Member member = memberRepository.findByEmail(email);
               userNo = member.getId();
               System.out.println(userNo);
   	  }
     	
        List<WaitlistHistory> waitlistHistoryList = waitlistService.getWaitlistHistory(userNo);
        return ResponseEntity.ok(waitlistHistoryList);
    }
    
    
    @GetMapping("/Mypage_History")
	public String MypageHistory(Model model, Authentication authentication) {
		 if (authentication.getPrincipal() instanceof UserVo) {   	 
		UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
	       model.addAttribute("infoName", userVo.getUserName());
	       model.addAttribute("infoEmail", userVo.getUserEmail()); 
	       model.addAttribute("infoId", userVo.getUserId());

		return "Mypage_History";
		 }
	     else  if (authentication.getPrincipal() instanceof OAuth2User) {
             // 소셜 로그인일 경우
             OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
	     } 

			return "Mypage_History";
		
	}
    

    //리뷰 추가하기
    @PostMapping("/reviews")
    @ResponseBody
    public Review createReview(@RequestParam("file") MultipartFile file, 
            @RequestParam("content") String content,
            @RequestParam("adminNo") int adminNo,
            @RequestParam("rating") double rating,
       
            Authentication authentication) throws Exception {
         
    	String name = null;
        if (authentication.getPrincipal() instanceof UserVo) {   	 
            UserVo userVo = (UserVo) authentication.getPrincipal(); 
             name = userVo.getUserName();
            
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            // 소셜 로그인일 경우
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email").toString();
            Member member = memberRepository.findByEmail(email);
            name = member.getName();
        }
        System.out.println(content);
        System.out.println(file);
        System.out.println(rating);
        System.out.println(adminNo);
        
        String imageNamed = uploadFile.saveFile(file);
        return reviewService.createReview(rating, content, adminNo, imageNamed, name);	
    }
 
    
    //블랙리스트 추가
    @PostMapping("/add/blacklist")
    @ResponseBody
    public BlackList addToBlacklist(@RequestBody Map<String, Object> request) {
       Long userNo = Long.parseLong(request.get("userNo").toString());
       int adminNo = Integer.parseInt(request.get("adminNo").toString());
       String content = request.get("content").toString();

       System.out.println(adminNo);
       System.out.println(userNo);

       return blacklistService.addToBlacklist(adminNo, userNo, content);
    }
    
    //블랙리스트 삭제
    @DeleteMapping("/remove/{adminNo}/{userNo}")
    
    public BlackList removeFromBlacklist(@PathVariable Integer adminNo, @PathVariable Long userNo) {
    	 return blacklistService.deleteBlacklist(adminNo, userNo);
    	}
    
    //검색 기능
    @GetMapping("/search")
    @ResponseBody
    public List<Restaurant> searchRestaruant(@RequestParam("keyword") String keyword){
    	return restaurantRepository.findByKeyword(keyword);
    }
    
    
    // 즐겨찾기 추가
    @PostMapping("/favorites")
    @ResponseBody
    public ResponseEntity<Favorite> addFavorite(@RequestBody Map<String, Object> request) {
    	 Long userNo = Long.parseLong(request.get("userNo").toString());
        Integer adminNo = Integer.parseInt(request.get("adminNo").toString());
        Favorite favorite = favoriteService.addFavorite(userNo, adminNo);
        return ResponseEntity.ok(favorite);
    }

    
    // 즐겨찾기 삭제
    @DeleteMapping("/delete2/{adminNo}/{userNo}")
    public ResponseEntity<?> removeFavorite(@PathVariable Integer adminNo, @PathVariable Long userNo) {
        favoriteService.removeFavorite(adminNo,userNo);
        return ResponseEntity.ok("Favorite removed");
    }
    
    //즐겨 찾기 조회
    @GetMapping("/favorite")
    @ResponseBody
    public List<Favorite> getFavorites() {
        return favoriteService.getAllFavorites();
    }
}