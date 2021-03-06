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

public class Put_LEED_projectDetailsTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void Put_LEED_projectDetails(int rowNum, String SheetName) throws IOException {
		try {	

			CommonMethod.ExtentReportConfig();
			
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			String email = 	"test_"+CommonMethod.randomNumber()+"@gmail.org";	   
			JSONObject jsonAsMap = new JSONObject();			
			jsonAsMap.put("resultKey", "status");
			jsonAsMap.put("action", "team.activate");
			jsonAsMap.put("bpNumber", "10540521");
			
			/*jsonAsMap.put("email", email);
			jsonAsMap.put("role", data.getCellData(SheetName, "role", rowNum));
			jsonAsMap.put("action", data.getCellData(SheetName, "action", rowNum));
			*/
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(jsonAsMap);
		
			JSONObject obj = new JSONObject();
			obj.put("request", jsonArray);
	        //System.out.println(obj);        
			CommonMethod.res = given().log().ifValidationFails()
					.header("Content-type", "application/json")
					.header("Authorization", header)
					.header("X-Caller-Id", "20297672fa1247ccf00ce8e0a14013ac")
					.spec(reqSpec)
					.body(obj)
					.when()
					.put("/Project/details/"+data.getCellData(SheetName, "projectType", rowNum)+"/"+data.getCellData(SheetName, "leedProjectId", rowNum))
					.then()
					.extract()
					.response();	
			
			CommonMethod.apiRequestId = CommonMethod.res.header("X-Api-Request-Id");
			CommonMethod.responsetime = CommonMethod.responsetime();
			CommonMethod.test =  CommonMethod.extent
					.startTest("Put_LEED_projectDetails Api"+ CommonMethod.getLabel(CommonMethod.responseTimeInMS()),
							"Update the created test data.")
					.assignCategory("api test");
			

			System.out.println("Api Request Id " + CommonMethod.apiRequestId);
			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responseTimeInMS());
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
			
			CommonMethod.checkforRespContainsValue("success");
			CommonMethod.checkForSpecificSubHashMapKeyValue("result", "status", "status");
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