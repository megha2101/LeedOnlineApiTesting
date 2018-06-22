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

public class PutPaymentsSendTransactionDataToSapTest extends BaseClass{
	@Test
	@Parameters({"rowNum", "SheetName" })
	public void PutPaymentsSendTransactionDataToSap(int rowNum, String SheetName) throws IOException {
		try {	

			CommonMethod.ExtentReportConfig();
			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
					   
			JSONObject jsonAsMap = new JSONObject();			
			jsonAsMap.put("programName", data.getCellData(SheetName, "programName", rowNum));
			jsonAsMap.put("processId", data.getCellData(SheetName, "processId", rowNum));
			jsonAsMap.put("transOrderId", data.getCellData(SheetName, "transOrderId", rowNum));
			jsonAsMap.put("orderId", data.getCellData(SheetName, "orderId", rowNum));
	        	        
			CommonMethod.res = given().log().ifValidationFails()
					.header("Content-type",CommonMethod.contentType)
					.header("Authorization", header)
					.header("X-Caller-Id", "20297672fa1247ccf00ce8e0a14013ac")
					.spec(reqSpec)
					//.body(jsonAsMap)
					.formParam("programName", data.getCellData(SheetName, "programName", rowNum))
			        .formParam("processId", data.getCellData(SheetName, "processId", rowNum))
			        .formParam("transOrderId", data.getCellData(SheetName, "transOrderId", rowNum))
			        .formParam("orderId", data.getCellData(SheetName, "orderId", rowNum))
					.when()
					.put("/Payments/sendTransactionDataToSap")
					.then()
					.extract()
					.response();	
			
			CommonMethod.responsetime = CommonMethod.res.getTimeIn(TimeUnit.MILLISECONDS);
			CommonMethod.test =  CommonMethod.extent
					.startTest("PutPaymentsSendTransactionDataToSap Api"+ CommonMethod.getLabel(CommonMethod.responsetime),
							"Send and link transaction details to SAP for the generated order.")
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
