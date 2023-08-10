package com.hyeonsik.boot.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.hyeonsik.boot.vo.Waitlist;

@Service
public class Smsservice {

	  
	    private String accessKey="jb6zDcGGSJqi3dsOrAuK";
	    private String secretKey="mvUNgVf45PLFWaFwQLS3qsI1EgsGHVwEM69fkW1g";
	    private String serviceId="ncp:sms:kr:286878629678:dotcom";
	    private String senderPhone="01097553489";
	    
	   
    
	    public void sendSms(String phoneNumber, String message) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
    	
        Long time = System.currentTimeMillis();
  
        
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        
        JSONObject requestBody = new JSONObject();
        requestBody.put("type", "SMS");
        requestBody.put("from", senderPhone);
        requestBody.put("content", message);


        JSONArray messagesArray = new JSONArray();
        JSONObject messageObj = new JSONObject();
        messageObj.put("to", phoneNumber);
        messageObj.put("content", message);
        messagesArray.put(messageObj);
        requestBody.put("messages", messagesArray);

     
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages";
        System.out.println(httpEntity);
        System.out.println("API URL: " + apiUrl);

        try {
            restTemplate.postForEntity(apiUrl, httpEntity, String.class);
            System.out.println("SMS sent successfully.");
        } catch (HttpClientErrorException ex) {
            System.out.println("Failed to send SMS. Error: " + ex.getMessage());
        }
    }

    
 
    // 시간 정보 생성 메소드
  
    // 시그니처 생성 메소드
    private String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException  {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + serviceId  + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        System.out.println(timestamp);
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();
        
        System.out.println(message);

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String  = Base64.getEncoder().encodeToString(rawHmac);
        



        return encodeBase64String;
    }
}
    