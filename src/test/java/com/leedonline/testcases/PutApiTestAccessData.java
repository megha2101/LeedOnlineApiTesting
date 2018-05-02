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

public class PutApiTestAccessData extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void putApiTestAccess(int rowNum, String SheetName) throws IOException {
		try {	
			System.out.println("header is: " +header + " createkey value is "+data.getCellData(SheetName, "createKey", rowNum));
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			JSONObject jsonAsMap = new JSONObject();
			jsonAsMap.put("key", data.getCellData(SheetName, "createKey", rowNum));
			jsonAsMap.put("value", data.getCellData(SheetName, "updatedKeyValue", rowNum));
	
			CommonMethod.res = given()
					.header("Content-Type",CommonMethod.contentType)
					.header("Authorization", header)
					.spec(reqSpec)
					.body(jsonAsMap)
					.when()
					.put("/Api/testAccess/Test12");		
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			test = extent
					.startTest("PutApiTestAccess Api"+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Update the created test data.")
					.assignCategory("api test");
			ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			System.out.println("PutTestApiAccessData body res is: "+bodyAsString);
			System.out.println("PutApiTestAccess api response time is: "+CommonMethod.responsetime);
			CommonMethod.res.then().log().all();
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
