package com.leedonline.testcases;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

public class GetNonceApiTest extends BaseClass{

	@Test
	@Parameters({"rowNum", "SheetName" })
	public void GetNonceApi(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.param("total", data.getCellData(SheetName, "nonceNum", rowNum))
					.when()
					.get("/getNonce")
					.then()
					.extract()
					.response();

			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("GetNonceApi "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Generates one time use nonces.")
					.assignCategory("api test");
			
			CommonMethod.jsonNonceResponse = CommonMethod.res.jsonPath().getList("nonces");

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
