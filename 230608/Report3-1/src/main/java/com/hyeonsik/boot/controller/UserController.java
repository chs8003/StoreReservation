package com.hyeonsik.boot.controller;



import com.hyeonsik.boot.mapper.UserMapper;
import com.hyeonsik.boot.service.RegisterMail;
import com.hyeonsik.boot.service.UploadFile;
import com.hyeonsik.boot.service.UserService;
import com.hyeonsik.boot.service.WaitlistService;

import com.hyeonsik.boot.vo.MapVo;

import com.hyeonsik.boot.vo.UserVo;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WaitlistService waitlistService;
    private final RegisterMail registerMail;
    private final UploadFile uploadFile;
    
    @Autowired
	SqlSession ss; 
    
    @Autowired
    UserMapper userMapper;
    
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    
    @GetMapping("/login_proc2")
    public String loginsearch(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "exception", required = false) String exception,
                              Model model) {
    	
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }

  
     @GetMapping("/")
     public String rootform(Authentication authentication){
    	 if (authentication != null) {
        	 UserVo userVo = (UserVo) authentication.getPrincipal();
             String Auth = userVo.getUserAuth();
             if (Auth.contains("ROLE_USER")) {
                 return "redirect:/mainpage";
             } else if (Auth.contains("ROLE_ADMIN")) {
                 return "redirect:/Owner_Main";
             }
         }
     return "redirect:/mainpage";
    }
     
    /**
     * 로그인 폼
     * @return
     */
     // isUserRole= ROLE_USER가 TRUE면 메인페이지로 이동=> FALSE면? ADMIN_ADMIN이 FALSE라는 근거가있나?
     // isUserRole2 = ROLE_ADMIN가 TRUE면 Owner_Main으로 이동=> FALSE면?
     // 아래 코드에서 로그인한 사용자가 ROLE_USER인지, ROLE_ADMIN인지를 명확히 구분하려면 다른 방법을 사용해야함
     //예를 들어, UserVo 클래스에 isUserAuth와 같이 현재 사용자의 권한을 반환하는 메서드를 추가하여 사용하거나, UserVo 객체에서 getUserAuth를 사용하여 권한 값을 직접 얻어와 구분하는 방법을 사용
     @PostMapping("/Waitlistdelete")
     public String Waitlistdelete(@RequestParam String username, @RequestParam String password, @AuthenticationPrincipal OAuth2User principal, Authentication authentication) {
    	 
    	 if(authentication != null) {
    	 if (authentication.getPrincipal() instanceof UserVo){
    	     Authentication authentication1 = new UsernamePasswordAuthenticationToken(username, password);
    	     Authentication authenticated = authenticationManager.authenticate(authentication1);
             SecurityContextHolder.getContext().setAuthentication(authenticated);
             return "Waitlistdelete";
    	 }
    	 else if 
    	 (authentication.getPrincipal() instanceof OAuth2User) {
    		    Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
    		    String authorizedClientRegistrationId = ((OAuth2AuthenticationToken) authentication2).getAuthorizedClientRegistrationId();
    		    Authentication authentication1 = new OAuth2AuthenticationToken(principal, null, authorizedClientRegistrationId);
    		    Authentication authenticated = authenticationManager.authenticate(authentication1);
    		    SecurityContextHolder.getContext().setAuthentication(authenticated);
    		    return "Waitlistdelete";
    		}
    	 }
		return "Waitlistdelete";
    	 }
     
     @GetMapping("/login")
     public String login() {
     
             return "login";
        
     }
     
     @PostMapping("/login_proc")
     @ResponseBody
     public String processLogin(@RequestParam("username") String userId, @RequestParam("password") String password) {
         try {
             // Spring Security의 loadUserByUsername 메소드를 호출하여 인증을 수행합니다.
             UserVo userVo = userService.loadUserByUsername(userId);
             
             // 패스워드 검증 등 추가적인 로직을 수행할 수 있습니다.
             
             // 로그인 성공 시에는 원하는 동작을 수행하고, 예를 들어 세션을 설정할 수 있습니다.
             
             // 성공 시에는 특정 값을 반환하여 클라이언트에서 로그인 성공을 처리할 수 있도록 합니다.
             return "success";
         } catch (UsernameNotFoundException e) {
             // 로그인 실패 시에는 예외 메시지를 반환합니다.
             return e.getMessage();
         }
     }
     

     @GetMapping("/deletelogin")
     public String deletelogin() {
     
             return "deletelogin";
        
     }
     
     
     @GetMapping("/Owner_Main")
     public String Owner(Model model, Principal principal){
     String username = principal.getName(); // 로그인한 사용자의 아이디
    	  UserVo user = userService.findByUserId(username); // 아이디로 사용자 정보 조회
    	 model.addAttribute("user", user); // 사용자 정보를 모델에 담아서 화면에 전달
    	System.out.println(user);
         return "Owner_Main";
       }
     
     
     @GetMapping("/userinformation")
     @ResponseBody
     public UserVo userinformation(Authentication authentication) {
             if (authentication.getPrincipal() instanceof UserVo) {
                 // 도메인 로그인1UserVo userVo = (UserVo) authentication.getPrincipal();
          
     String username = authentication.getName(); // 로그인한 사용자의 아이디
     UserVo user = userService.findByUserId(username); // 아이디로 사용자 정보 조회
     return user;
             }
 			return null;
     }
     	
     
   

    
    
    
    @GetMapping("/Owner_wait")
    public String OwnerWait(){
    
        return "Owner_wait";
    }
    
    
 
    @GetMapping("/signUp")
    public String signUpForm() {
        return "signUp";
    }
    
    @GetMapping("/signUp_host")
    public String signUphostForm() {
        return "signUp_host";
    }
    
     
    @GetMapping("/findinfo")
    public String findfindForm() {
        return "findinfo";
    }
   
    @ResponseBody
    @PostMapping("/mailCheck")
    String mailConfirm(@RequestParam("userEmail") String userEmail) throws Exception {
       String code = registerMail.sendSimpleMessage(userEmail);
       return code;
    }
    
    
  
   
	List<MapVo> list = new ArrayList<>(); 
    @GetMapping("/access_denied")
    public String mapfo(Model model) throws Exception{
    	list = ss.selectList("com.hyeonsik.boot.mapper.UserMapper.Mapfound");
		model.addAttribute("list",list);
		System.out.println(list);
		return "access_denied";
    }
    
  
    
    @GetMapping("/SignUp_Class")
    public String signupclass() {
        return "SignUp_Class";
    }
    
  
    
    @PostMapping("/signUp_host")
    public String signUp_host(UserVo userVo) {
        waitlistService.createUser(userVo);
        return "redirect:/login";
    }
    
   
    @PostMapping("/signUp")
    public String signUp(UserVo userVo) {
        userService.joinUser(userVo);
       
        return "redirect:/login";

    }
    
    //아이디 중복
    @ResponseBody
	@PostMapping("/idCheck")
    public int idOverlap(@RequestParam("userId") String userId ) throws Exception {
        int count = userService.idOverlap(userId);
        return count;  
	 }
    
    
    @PostMapping("/findinfo")
  	@ResponseBody
  	public String userIdSearch(@RequestParam("userName") String userName, 
  			@RequestParam("userEmail") String userEmail) {
  		String result = registerMail.get_searchId(userName,userEmail);
  		return result;
  	}
    

    
    @GetMapping("/passfind")
    public String passfindform() {
        return "passfind";
    }
    
    
	@PostMapping("/passfind")
	@ResponseBody
	public String passwordSearch(@RequestParam("id")String userId,
			@RequestParam("email")String userEmail,
			HttpServletRequest request) throws Exception {
		
		
		registerMail.mailSendWithPassword(userId, userEmail, request);
		
		return "passfind";
	}
	
	@GetMapping("/Mypage_Account")
	public String mypageAccount(Model model, Authentication authentication) throws IOException {
		if (authentication.getPrincipal() instanceof UserVo) {
            UserVo userVo = (UserVo) authentication.getPrincipal();
            model.addAttribute("infoImage", userVo.getSavedNm());
 	        model.addAttribute("infoName", userVo.getUserName());
 	        model.addAttribute("infoEmail", userVo.getUserEmail()); 
 	        model.addAttribute("infoId", userVo.getUserId()); 
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            // 소셜 로그인
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            model.addAttribute("infoName", attributes.get("name").toString());
            model.addAttribute("infoEmail",attributes.get("email").toString());     
        }    return "Mypage_Account";
	}
		
	
	
	@PostMapping("/Mypage_Account/profile")
	public String mypageAccount2(Authentication authentication, Model model, @RequestParam("file") MultipartFile file, String userId) throws Exception {
		String savedNm = uploadFile.saveFile(file);	
		System.out.println(savedNm);
		 UserVo userVo = (UserVo) authentication.getPrincipal();
		 userId = userVo.getUserId();
		 System.out.println("userId = " + userId);
		 userService.upImg(savedNm, userId);
		 userVo.setSavedNm(savedNm);	 
		   return "redirect:/Mypage_Account";
	}
	
	
	@GetMapping("/Mypage_Fav")
	public String MypageFav(Model model, Authentication authentication) {
		if (authentication.getPrincipal() instanceof UserVo) {
		UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
	       model.addAttribute("infoName", userVo.getUserName());
	       model.addAttribute("infoEmail", userVo.getUserEmail()); 
	       model.addAttribute("infoId", userVo.getUserId());
		
		return "Mypage_Fav";
	}
		 else if (authentication.getPrincipal() instanceof OAuth2User) {
             // 소셜 로그인일 경우
             OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
	     } 
		 return "Mypage_Fav";
	}
	@GetMapping("/Mypage_Review")
	public String MypageReview(Model model, Authentication authentication) {
		if (authentication.getPrincipal() instanceof UserVo) {
			UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
		       model.addAttribute("infoName", userVo.getUserName());
		       model.addAttribute("infoEmail", userVo.getUserEmail()); 
		       model.addAttribute("infoId", userVo.getUserId());
			
			return "Mypage_Review";
		}
			 else if (authentication.getPrincipal() instanceof OAuth2User) {
	             // 소셜 로그인일 경우
	             OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		     } 
			 return "Mypage_Review";
	}
	
	@GetMapping("/Mypage_Restaurant")
	public String MypageRes(Model model, Authentication authentication) {
		if (authentication.getPrincipal() instanceof UserVo) {
			UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
		       model.addAttribute("infoName", userVo.getUserName());
		       model.addAttribute("infoEmail", userVo.getUserEmail()); 
		       model.addAttribute("infoId", userVo.getUserId());
			
			return "Mypage_Restaurant";
		}
			 else if (authentication.getPrincipal() instanceof OAuth2User) {
	             // 소셜 로그인일 경우
	             OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		     } 
			 return "Mypage_Restaurant";
	}
	
	  
	  
	  // 도메인로그인과, 소셜 로그인을 동시에 가능하게 해주고, 인증 받았을떄의 메인 홈페이지에 Name 객체를 받아a
	  List<MapVo> list2 = new ArrayList<>(); 	
	    @GetMapping("/mainpage")
		    public String usercAcess(Model model, Authentication authentication) {
	    	if (authentication != null) {
	            if (authentication.getPrincipal() instanceof UserVo) {
	                // 도메인 로그인1
	                UserVo userVo = (UserVo) authentication.getPrincipal();
	                String Auth = userVo.getUserAuth();
	           
	                model.addAttribute("info", userVo.getUserName() + "님");
	                return "mainpage";
	            } else if (authentication.getPrincipal() instanceof OAuth2User) {
	                // 소셜 로그인
	                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
	                Map<String, Object> attributes = oAuth2User.getAttributes();
	          
	                return "mainpage";
	            }
	        }
		    
	      	  	
		   return "mainpage" ;
		    }
    // 현재 데이터베이스 안에있는 가게 데이터를 뿌려주는 api
    @GetMapping("/api/mainpage")
    @ResponseBody
    public List<MapVo> usercAcess3() throws Exception {
    	System.out.println(userService.selectAll());
     return userService.selectAll();
	 }   
    
    // 내 위치 찍고 조회 눌렀을떄 나오는 api
    @PostMapping("/mainpage/location")
    @ResponseBody
    public List<MapVo> saveLocation(Model model) {
        double latitude = 37.3834711;
        double longitude = 126.9218479;
    	 List<MapVo> Result = userService.found(latitude,longitude);
         model.addAttribute("result", Result);
     	 System.out.println(latitude);
     	 System.out.println(longitude);
     	 System.out.println(Result);
        return Result;
    }
    
   
    
   
   
    
}