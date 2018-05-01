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

import net.minidev.json.JSONObject;

public class ApiTestAccessUpdateData extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void putApiTestAccess(int rowNum, String SheetName) throws IOException {
		try {
			CommonMethod.ExtentReportConfig();		
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			CommonMethod.res = given()
					.header("Content-Type",CommonMethod.contentType)
					.header("Authorization", header)
					.spec(reqSpec)
					.params(
						  "key", data.getCellData(SheetName, "createKey", rowNum),
						  "value", data.getCellData(SheetName, "updatedKeyValue", rowNum)
						  )
					.when()
					.put("/Api/testAccess??key="+data.getCellData(SheetName, "createKey", rowNum));		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test = CommonMethod.extent
					.startTest("PutApiTestAccess Api"+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Update the created test data.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PutTestApiAccessData body res is: "+bodyAsString);
			System.out.println("PutApiTestAccess api response time is: "+CommonMethod.responsetime);
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
