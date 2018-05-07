package com.leedonline.testcases;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ResponseBody;
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

public class PostValidateCaGBCMemberTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PostValidateCaGBCMember(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();			
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("Nonce value is: "+ CommonMethod.jsonNonceResponse.get(2));
			CommonMethod.res = given()
					.header("Content-Type","multipart/formData")
					.header("Authorization", header)
					.header("X-Nonce", CommonMethod.jsonNonceResponse.get(2))
					.spec(reqSpec)					
					.multiPart("memberEmail","cagbcusgbcorg1@gmail.com")				//data.getCellData(SheetName, "orgName", rowNum)	
					.when()
					.post("/Validate/CaGBCMember").then().extract().response();		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("Create Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Validate one or more CaGBC member ID or Email.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PostValidateCaGBCMember body res is: "+bodyAsString);
			System.out.println("PostValidateCaGBCMember response time is: "+CommonMethod.responsetime);
			System.out.println("PostValidateCaGBCMember hedaer is: "+header);
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);
	        CommonMethod.testlog("Pass", "Authorization Token generated" + "<br>" + header);
			CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
	        CommonMethod.testlog("Info", "API responded in "+ CommonMethod.responsetime + " Milliseconds");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
