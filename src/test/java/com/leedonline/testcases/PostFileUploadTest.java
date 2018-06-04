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

public class PostFileUploadTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PostFileUpload(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();			
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("Nonce value is: "+ CommonMethod.jsonNonceResponse.get(3));
			CommonMethod.res = given()
					.header("Content-Type","multipart/formData")
					.header("Authorization", header)
					.header("X-Nonce", CommonMethod.jsonNonceResponse.get(3))
					.spec(reqSpec)					
					.multiPart("projectId",data.getCellData(SheetName, "leedProjectId", rowNum))
					.multiPart("linkedTo",data.getCellData(SheetName, "getFilesListLinkedTo", rowNum))
					.when()
					.post("/Files/upload")
					.then()
					.extract()
					.response();	

			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("PostFileUploadApi "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Upload a file to the project.")
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