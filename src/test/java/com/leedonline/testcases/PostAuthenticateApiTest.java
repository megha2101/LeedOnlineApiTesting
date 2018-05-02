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
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

public class PostAuthenticateApiTest extends BaseClass{
	String contentType;
	int statusCode;
	String token;

	@Test
	@Parameters({"rowNum", "SheetName" })
	public void authenticate(int rowNum, String SheetName) throws IOException {		
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		CommonMethod.res = given()
				.header("Content-Type",CommonMethod.contentType)
				.params(
					  "username", data.getCellData(SheetName, "username", rowNum),
					  "password", data.getCellData(SheetName, "password", rowNum),
					  "guid", "")
				.spec(reqSpec)
				.when()
				.post("/authenticate").then().extract().response();
		System.out.println("Token is:" + CommonMethod.res.asString());
		CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
		test = extent
				.startTest("Authentication api " + CommonMethod.getLabel(CommonMethod.responsetime),
						"genrating token.")
				.assignCategory("api test");
		CommonMethod.res.then().assertThat().statusCode(200);		  
		CommonMethod.res.then().assertThat().contentType(ContentType.JSON);
		CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
        CommonMethod.testlog("Info", "API responded in "+ CommonMethod.responsetime + " Milliseconds");
	}

}
