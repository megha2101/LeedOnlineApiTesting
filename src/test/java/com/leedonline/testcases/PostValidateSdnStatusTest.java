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
import com.jayway.restassured.response.ResponseBody;
import com.leedOnline.driver.BaseClass;
import com.leedOnline.driver.CommonMethod;
import com.relevantcodes.extentreports.LogStatus;

public class PostValidateSdnStatusTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PostValidateSdnStatus(int rowNum, String SheetName) throws IOException {
		try {	
			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println(CommonMethod.jsonNonceResponse.get(1));						
			System.out.println("PostValidateSdnStatus hedaer is: "+ header);
			CommonMethod.res = given()
					.header("Content-Type","multipart/formData")
					.header("Authorization", header)
					.header("X-Nonce", CommonMethod.jsonNonceResponse.get(1))
					.spec(reqSpec)					
					.multiPart( "orgName", data.getCellData(SheetName, "orgName", rowNum))
					.multiPart( "person", data.getCellData(SheetName, "person", rowNum))
					.multiPart( "country", data.getCellData(SheetName, "countryCode", rowNum))
					.when()
					.post("/Validate/sdnStatus").then().extract().response();		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			 CommonMethod.test =  CommonMethod.extent
					.startTest("Create Api Test Access Data "+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Validate one or more organizations/persons.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PostValidateSdnStatus body res is: "+bodyAsString);
			System.out.println("PostValidateSdnStatus response time is: "+CommonMethod.responsetime);
			System.out.println("PostValidateSdnStatus hedaer is: "+header);
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
