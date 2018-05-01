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

public class GetClientKeyApiTest extends BaseClass{

	@Test
	@Parameters({"rowNum", "SheetName" })
	public void getClientKey(int rowNum, String SheetName) throws IOException {
		try {
			CommonMethod.ExtentReportConfig();		
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.GeneratingAuthCode();
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/getClientKey");		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test = CommonMethod.extent
					.startTest("GetClientKey Api"+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Generate and get client key to sign in API .")
					.assignCategory("api test");
			System.out.println("Getclient api response time is: "+CommonMethod.responsetime);
			System.out.println("Getclient api response is: "+CommonMethod.res.asString());
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
