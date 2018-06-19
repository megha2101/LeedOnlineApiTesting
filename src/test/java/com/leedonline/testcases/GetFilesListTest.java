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

public class GetFilesListTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void GetFilesList(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			CommonMethod.GeneratingAuthCode(SheetName, rowNum);
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("GetFilesListApi hedaer is: "+header);
			CommonMethod.res = given()
					.header("Authorization", header)
					.spec(reqSpec)
					.params("projectId", data.getCellData(SheetName, "leedProjectId", rowNum),
							"linkedTo", data.getCellData(SheetName, "getFilesListLinkedTo", rowNum))
					.when()
					.get("/Files/getList")
					.then()
					.extract()
					.response();
			
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("GetFilesList Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get files list.")
					.assignCategory("api test");
			
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
