package com.leedonline.testcases;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class Post_Api_authenticateTest extends BaseClass{

	@Test
	@Parameters({"rowNum", "SheetName" })
	public void Post_Api_authenticate(int rowNum, String SheetName) throws IOException {	
		try {
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given().log().ifValidationFails()
					.header("Content-Type",CommonMethod.contentType)
					.params(
							"username", data.getCellData(SheetName, "username", rowNum),
							"password", data.getCellData(SheetName, "password", rowNum),
							"guid", "")
					.spec(reqSpec)
					.when()
					.post("/authenticate")
					.then()
					.extract()
					.response();
			
			CommonMethod.apiRequestId = CommonMethod.res.header("X-Api-Request-Id");
			CommonMethod.responsetime = CommonMethod.responsetime();
			CommonMethod.test =  CommonMethod.extent
					.startTest("Post_Api_authenticate Api " + CommonMethod.getLabel(CommonMethod.responseTimeInMS()),
							"genrating token.")
					.assignCategory("api test");
			
			System.out.println("Api Request Id " + CommonMethod.apiRequestId);
			System.out.println("Responsetime of API : " + CommonMethod.responseTimeInMS());
			System.out.println("Response received from API " + CommonMethod.res.asString());
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
		    
			Map<String,String> jsonMap = new HashMap<String,String>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	if (key.equals("token")) {
		    		 String token = jsonMap.get("token");
		    		 Assert.assertFalse("Token value is empty.", jsonMap.get("token").isEmpty());
					 header = "Basic " + token;
		    	}else {
		    		Assert.assertTrue("Token is not present in the response", false);
		    	}
		    	
		    }		    
			
		    CommonMethod.checkforKeysNullValues();
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);
					   
			CommonMethod.testlog("Pass", "Authorization Token generated" + "<br>" + header);
			CommonMethod.testlog("Pass", "Api Request Id is : " + "<br>" + CommonMethod.apiRequestId);
			CommonMethod.testlog("Info", "Content Type is : " + CommonMethod.res.getContentType());
			CommonMethod.testlog("Info", "Status Code is : " + CommonMethod.res.getStatusCode());
			CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
			CommonMethod.testlog("Info", "API responded in : " + CommonMethod.responseTimeInMS() + " Milliseconds");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}