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

public class Get_LEED_creditsCreditTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void Get_LEED_creditsCredit(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			CommonMethod.GeneratingAuthCode(SheetName, rowNum);
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			/*String Token = given()
					.header("Content-Type",CommonMethod.contentType)
					.spec(reqSpec)
					.params(
							"username", "jaypeejay@hotmail.com",
							"password", "initpass")
					.expect().statusCode(200).when()
					.post("/authenticate").then().contentType(ContentType.JSON)
					.extract().response().path("token").toString();	*/
			
			//String header = "Basic " + Token;	
			CommonMethod.res = given().log().ifValidationFails()
					.header("Authorization", header)
					.spec(reqSpec)
					/*.params("creditId", "EHS1SS113",
							"creditInfo", "false",
							"return", "thresholds",//data.getCellData(SheetName, "return", rowNum), // check for linked to value it could be order or credit
							"forceSync",true)*/
					.when()
					.get("Credits/credit/GRP/1000151592?creditId=EHS1SS111L-1000151592&creditInfo=false&return=thresholds&forceSync=true")
					.then()
					.extract()
					.response();
			
			CommonMethod.apiRequestId = CommonMethod.res.header("X-Api-Request-Id");
			CommonMethod.responsetime = CommonMethod.responsetime();
			CommonMethod.test =  CommonMethod.extent
					.startTest("Get_LEED_creditsCredit Api "+ CommonMethod.getLabel(CommonMethod.responseTimeInMS()),
							"Get credit details.")
					.assignCategory("api test");
			
			System.out.println("Api Request Id " + CommonMethod.apiRequestId);
			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responseTimeInMS());
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
			
			CommonMethod.checkForSpecificSubHashMapListHashMapKeyValue("thresholds", "options", "id", "status");
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);

			CommonMethod.testlog("Pass", "Authorization Token generated" + "<br>" + header);
			CommonMethod.testlog("Pass", "Api Request Id is : " + "<br>" + CommonMethod.apiRequestId);
			CommonMethod.testlog("Info", "Content Type is : " + CommonMethod.res.getContentType());
			CommonMethod.testlog("Info", "Status Code is : " + CommonMethod.res.getStatusCode());
			CommonMethod.testlog("Pass", "verifies response from API" + "<br>" + CommonMethod.res.asString());
			CommonMethod.testlog("Info", "API responded in : " + CommonMethod.responseTimeInMS() + " Milliseconds");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
