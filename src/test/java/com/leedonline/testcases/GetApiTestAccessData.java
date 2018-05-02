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

public class GetApiTestAccessData extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void getApiTestAccessData(int rowNum, String SheetName) throws IOException {
		try {		
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("GetTestApiAccessData hedaer is: "+ header);
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/Api/testAccess?key="+data.getCellData(SheetName, "createKey", rowNum));		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			test = extent
					.startTest("Get Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Read the single or full data..")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("GetTestApiAccessData body res is: "+bodyAsString);
			System.out.println("GetTestApiAccessData response time is: "+CommonMethod.responsetime);
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
