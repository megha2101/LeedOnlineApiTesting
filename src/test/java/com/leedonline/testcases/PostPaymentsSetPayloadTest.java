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

public class PostPaymentsSetPayloadTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PostPaymentsSetPayload(int rowNum, String SheetName) throws IOException {
		try {	

			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
			
			JSONObject jsonBillToObj = new JSONObject();			
			jsonBillToObj.put("firstName", data.getCellData(SheetName, "firstName", rowNum));
			jsonBillToObj.put("lastName", data.getCellData(SheetName, "lastName", rowNum));
			jsonBillToObj.put("company", data.getCellData(SheetName, "company", rowNum));
			jsonBillToObj.put("email", data.getCellData(SheetName, "email", rowNum));
			jsonBillToObj.put("address1", data.getCellData(SheetName, "address1", rowNum));
			jsonBillToObj.put("address2", data.getCellData(SheetName, "address2", rowNum));
			jsonBillToObj.put("city", data.getCellData(SheetName, "city", rowNum));
			jsonBillToObj.put("state", data.getCellData(SheetName, "state", rowNum));
			jsonBillToObj.put("country", data.getCellData(SheetName, "country", rowNum));
			jsonBillToObj.put("zipcode", data.getCellData(SheetName, "zipcode", rowNum));
			jsonBillToObj.put("phone", data.getCellData(SheetName, "phone", rowNum));
			
			
			JSONObject jsonReqpayload = new JSONObject();
			jsonReqpayload.put("currency",data.getCellData(SheetName, "currency", rowNum));
			jsonReqpayload.put("payableAmount", data.getCellData(SheetName, "payableAmount", rowNum));
			jsonReqpayload.put("billTo", jsonBillToObj );
			
		//data.getCellData(SheetName, "payableAmount", rowNum)   
			JSONObject jsonAsMap = new JSONObject();			
			jsonAsMap.put("programName", data.getCellData(SheetName, "programName", rowNum));
			jsonAsMap.put("projectId", data.getCellData(SheetName, "leedProjectId", rowNum));
			jsonAsMap.put("orderId", data.getCellData(SheetName, "orderId", rowNum));
			jsonAsMap.put("transOrderId", data.getCellData(SheetName, "transOrderId", rowNum));
			jsonAsMap.put("redirectUrl", data.getCellData(SheetName, "redirectUrl", rowNum));
			jsonAsMap.put("gatewayEnvironment", data.getCellData(SheetName, "gatewayEnvironment", rowNum));
			jsonAsMap.put("reqPayload", jsonReqpayload);
			
	        	        
			CommonMethod.res = given().log().ifValidationFails()
					.header("Content-type", "application/json")
					.header("Authorization", header)
					.header("X-Caller-Id", "20297672fa1247ccf00ce8e0a14013ac")
					.spec(reqSpec)
					.body(jsonAsMap)
					.when()
					.post("/Payments/setPayload")
					.then()
					.extract()
					.response();	
			
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("PostPaymentsSetPayload Api"+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Set payment request data and get payment URL in response.")
					.assignCategory("api test");
			

			System.out.println("Authorization Token Generated " + header);
			System.out.println("Response received from API " + CommonMethod.res.asString());
			System.out.println("Responsetime of API " + CommonMethod.responsetime);
			
			String status = CommonMethod.getStatus(CommonMethod.res.getStatusCode());
			String time = String.valueOf(CommonMethod.responsetime);
			CommonMethod.writeInExcel(Thread.currentThread().getStackTrace()[1].getMethodName(), time, status);
					
			CommonMethod.res.then().assertThat().statusCode(200);		  
			CommonMethod.res.then().assertThat().contentType(ContentType.JSON);

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
