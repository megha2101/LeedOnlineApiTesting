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

public class DeleteApiTestAccessDataTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void deleteApiTestAccessData(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			
			CommonMethod.res = given()
					.header("Content-Type",CommonMethod.contentType)
					.header("Authorization", header)
					.spec(reqSpec)
					.params(
							"key", data.getCellData(SheetName, "createKey", rowNum)
						   )
					.when()
					.delete("/Api/testAccess")
					.then()
					.extract()
					.response();
			
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("Delete Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Delete single or full data.")
					.assignCategory("api test");
			
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);
			
			System.out.println("DeleteTestApiAccessData body res is: "+CommonMethod.res.asString());
			System.out.println("DeleteTestApiAccessData response time is: "+CommonMethod.responsetime);
			System.out.println("DeleteTestApiAccessData hedaer is: "+header);
			
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
