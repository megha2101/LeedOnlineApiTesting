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

public class PostValidatePostalCodeTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PostValidatePostalCode(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("PostValidatePostalCodeTest hedaer is: "+ header);
			CommonMethod.res = given()
					.header("Content-Type",CommonMethod.contentType)
					.header("Authorization", header)
					.spec(reqSpec)
					.params(
						  "country", data.getCellData(SheetName, "countryCode", rowNum),
						  "state", data.getCellData(SheetName, "stateCode", rowNum),
						  "postalCode", data.getCellData(SheetName, "zipCode", rowNum)
						  )
					.when()
					.post("/Validate/postalCode").then().extract().response();		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("Create Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Validate Postal Code.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PostValidatePostalCodeTest body res is: "+bodyAsString);
			System.out.println("PostValidatePostalCodeTest response time is: "+CommonMethod.responsetime);
			System.out.println("PostValidatePostalCodeTest hedaer is: "+header);
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
