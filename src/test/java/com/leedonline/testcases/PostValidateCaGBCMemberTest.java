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
			CommonMethod.res = given()
					.header("Content-Type","multipart/formData")
					.header("Authorization", header)
					.header("X-Nonce", CommonMethod.jsonNonceResponse.get(2))
					.spec(reqSpec)					
					.multiPart("memberEmail","cagbcusgbcorg1@gmail.com")				
					.when()
					.post("/Validate/CaGBCMember")
					.then()
					.extract()
					.response();		

			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("Create Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Validate one or more CaGBC member ID or Email.")
					.assignCategory("api test");

			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);

			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responsetime);

			CommonMethod.testlog("Pass", "Authorization Token generated" + "<br>" + header);
			CommonMethod.testlog("Info", "Content Type is : " + CommonMethod.res.getContentType());
			CommonMethod.testlog("Info", "Status Code is : " + CommonMethod.res.getStatusCode());
			CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
			CommonMethod.testlog("Info", "API responded in " + CommonMethod.responsetime + " Milliseconds");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}