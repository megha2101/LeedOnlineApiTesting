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

public class GetLeedProjectDownloadTest extends BaseClass{
	
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void GetLeedProjectDownload(int rowNum, String SheetName) throws IOException {
		try {	
		    CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given().log().ifValidationFails()
					.header("Authorization", header)
					.header("X-Caller-Id", "20297672fa1247ccf00ce8e0a14013ac")
					.spec(reqSpec)
					.params("docType", data.getCellData(SheetName, "docType", rowNum),
							"docId", data.getCellData(SheetName, "docId", rowNum))
					.when()
					.get("/Project/download/"+data.getCellData(SheetName, "projectType", rowNum)+"/"+data.getCellData(SheetName, "leedProjectId", rowNum))
					.then()
					.extract()
					.response();
			
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("GetLeedProjectDownload Api "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Get document download link.")
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