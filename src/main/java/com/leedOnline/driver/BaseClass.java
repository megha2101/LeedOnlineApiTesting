package com.leedOnline.driver;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import com.leedOnline.driver.CommonMethod;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;
import com.leedOnline.driver.XlsReader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseClass {
	
	public static XlsReader data;
	public static ResponseSpecBuilder builder;
	public static RequestSpecification reqSpec;
	public static ResponseSpecification respSpec;
	public static String Token;
    public static String header;
    public String apiUrl = "https://leedonline-api-stg.usgbc.org/v1/json";
    
    
    @BeforeClass
    public void setBaseUri() {    
      data = new XlsReader(System.getProperty("user.dir") + "/src/main/resources/listener/leedonlineApi.xlsx");	
      reqSpec  = new RequestSpecBuilder()
         .setBaseUri(apiUrl)
         .build();     
      respSpec = new ResponseSpecBuilder()
         .expectStatusCode(200)
         .build();
     }	
    
  
	@AfterMethod
	 public void teardown(ITestResult result) {		
	  if (result.getStatus() == ITestResult.FAILURE) {
	   CommonMethod.test.log(LogStatus.FAIL, result.getThrowable());
	  } else if (result.getStatus() == ITestResult.SKIP) {
	   CommonMethod.test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
	  } else {		  
	   CommonMethod.test.log(LogStatus.PASS, "Test passed");
	  }
	  CommonMethod.extent.endTest(CommonMethod.test);
	  CommonMethod.extent.flush();
	 }
}

