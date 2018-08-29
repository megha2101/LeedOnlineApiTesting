package com.leedonline.testcases;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.jayway.restassured.http.ContentType;
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class Get_Api_memberGetInfoTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void Get_Api_memberGetInfo(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given().log().ifValidationFails()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/Member/GetInfo")
					.then()
					.extract()
					.response();
	
			CommonMethod.apiRequestId = CommonMethod.res.header("X-Api-Request-Id");
			CommonMethod.responsetime = CommonMethod.responsetime();
			CommonMethod.test =  CommonMethod.extent
					.startTest("Get_Api_memberGetInfo Api "+ CommonMethod.getLabel(CommonMethod.responseTimeInMS()),
							"Get authenticated person details.")
					.assignCategory("api test");

			System.out.println("Api Request Id " + CommonMethod.apiRequestId);
			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responseTimeInMS());
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
			
			Map jsonMap = new HashMap();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (Object key : jsonMap.keySet()) {
		    	if (key.toString().equals("bpNumber") || key.toString().equals("firstName")) {
					 Assert.assertFalse(key + " is empty.", jsonMap.get(key).toString().isEmpty());					
		    	}else if(key.toString().toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}		    	
		    }	
			CommonMethod.checkforSpecificKeyValue("bpNumber", "firstName");
			CommonMethod.checkForSpecificSubHashMapKeyValue("personal", "jobTitle", "companyName");
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
