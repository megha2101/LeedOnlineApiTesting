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
			CommonMethod.res = given().log().ifValidationFails()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/Common/getCountriesAndStates")
					.then()
					.extract()
					.response();	

			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("GetCountryAndStates Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get all supported country/state codes.")
					.assignCategory("api test");
			
			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responsetime);
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
					
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);

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
