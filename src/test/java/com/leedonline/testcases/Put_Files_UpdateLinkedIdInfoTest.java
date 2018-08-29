package com.leedonline.testcases;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Put_Files_UpdateLinkedIdInfoTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void Put_Files_UpdateLinkedIdInfo(int rowNum, String SheetName) throws IOException {
		try {	

			CommonMethod.ExtentReportConfig();
			//CommonMethod.GeneratingAuthCode(SheetName, rowNum);
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
					   
			JSONObject jsonAsMap1 = new JSONObject();			
			jsonAsMap1.put("projectId", data.getCellData(SheetName, "leedProjectId", rowNum));
			jsonAsMap1.put("linkedTo", data.getCellData(SheetName, "linkedTo", rowNum));
			jsonAsMap1.put("linkedId", data.getCellData(SheetName, "linkedId", rowNum));
			
			JSONObject jsonAsMap2 = new JSONObject();			
			jsonAsMap2.put("likedId", data.getCellData(SheetName, "linkedIdNew", rowNum));
			jsonAsMap2.put("linkedId1", data.getCellData(SheetName, "linkedId1", rowNum));
			jsonAsMap2.put("lockModify", data.getCellData(SheetName, "lockModify", rowNum));
			jsonAsMap2.put("lockDelete", data.getCellData(SheetName, "lockDelete", rowNum));
			
			JSONObject jsonAsMap = new JSONObject();
			jsonAsMap.put("ids", data.getCellData(SheetName, "fileId", rowNum));//"P1000150846_5b22c23124aca00434506fb2");
			jsonAsMap.put("search", jsonAsMap1);
			jsonAsMap.put("update", jsonAsMap2);
			
			CommonMethod.res = given().log().all()
					.header("Content-type", "application/json")
					.header("Authorization", header)
					.header("X-Caller-Id", "20297672fa1247ccf00ce8e0a14013ac")
					.spec(reqSpec)
					.body(jsonAsMap)
					.when()
					.put("Files/updateLinkedIdInfo")
					.then()
					.extract()
					.response();	
			
			CommonMethod.apiRequestId = CommonMethod.res.header("X-Api-Request-Id");
			CommonMethod.responsetime = CommonMethod.responsetime();
			CommonMethod.test =  CommonMethod.extent
					.startTest("Put_Files_UpdateLinkedIdInfo Api"+ CommonMethod.getLabel(CommonMethod.responseTimeInMS()),
							"Update file linked IDs details.")
					.assignCategory("api test");
			

			System.out.println("Api Request Id " + CommonMethod.apiRequestId);
			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responseTimeInMS());
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
			
			CommonMethod.checkforKeysNullValues();
			CommonMethod.checkforSpecificKeyValue("id", "orgName");
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