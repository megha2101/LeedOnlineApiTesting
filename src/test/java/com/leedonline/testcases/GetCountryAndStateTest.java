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

public class GetCountryAndStateTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void GetCountryAndStates(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/Common/getCountriesAndStates");		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("GetCountryAndStates Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get all supported country/state codes.")
					.assignCategory("api test");
			System.out.println("GetCountryAndStates response time is: "+CommonMethod.res.asString());
			System.out.println("GetCountryAndStates hedaer is: "+header);
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);
	        CommonMethod.testlog("Pass", "Authorization Token generated" + "<br>" + header);
			//CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
	        CommonMethod.testlog("Info", "API responded in "+ CommonMethod.responsetime + " Milliseconds");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}