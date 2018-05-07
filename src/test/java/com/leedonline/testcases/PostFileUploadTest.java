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
					.multiPart("linkedTo",data.getCellData(SheetName, "getFilesListLinkedTo", rowNum))				//data.getCellData(SheetName, "orgName", rowNum)	
					.when()
					.post("/Files/upload").then().extract().response();		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("PostFileUploadApi "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Upload a file to the project.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PostFileUploadApi body res is: "+bodyAsString);
			System.out.println("PostFileUploadApi response time is: "+CommonMethod.responsetime);
			System.out.println("PostFileUploadApi hedaer is: "+header);
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
