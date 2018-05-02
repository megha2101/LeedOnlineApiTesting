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

public class GetProjectDetailsTypeTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void ProjectDetailsType(int rowNum, String SheetName) throws IOException {
		try {	
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.when()
					.get("/Project/details/"+data.getCellData(SheetName, "projectType", rowNum)+
							"/"+data.getCellData(SheetName, "leedProjectId", rowNum));
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			test = extent
					.startTest("ProjectDetailsType Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get project details.")
					.assignCategory("api test");
			System.out.println("ProjectDetailsType response time is: "+CommonMethod.res.asString());
			System.out.println("ProjectDetailsType hedaer is: "+header);
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
