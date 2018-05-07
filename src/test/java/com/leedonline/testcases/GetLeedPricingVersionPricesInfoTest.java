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

public class GetLeedPricingVersionPricesInfoTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void GetLeedPricingVersionPricesInfo(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/LEEDPricing/getVersionPricesInfo?versionOrCurrency="+data.getCellData(SheetName, "currencyCode", rowNum));		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("Get Member Info Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get the pricing details using either version number or currency code.")
					.assignCategory("api test");
			System.out.println("GetLeedPricingVersionPricesInfoApi response is: "+CommonMethod.res.asString());
			System.out.println("GetLeedPricingVersionPricesInfoApi hedaer is: "+header);
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
