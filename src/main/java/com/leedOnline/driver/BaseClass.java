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
	static Format formatter = new SimpleDateFormat("YYYY-MM-dd");
	static Date date = new Date();
	public static XlsReader data;
	public static ResponseSpecBuilder builder;
	public static RequestSpecification reqSpec;
	public static ResponseSpecification respSpec;
	public static String Token;
    public static String header;
    public static ExtentReports extent;
	public static ExtentTest test;
    public String apiUrl = "https://leedonline-api-stg.usgbc.org/v1/json";
    public static File extentconfigfile = new File(System.getProperty("user.dir") +"/src/main/resources/listener/extent-config.xml");
    public static String Reportfile = System.getProperty("user.dir") +"/Report/Leedonline-AutomationReport" + "_" + formatter.format(date) + ".html";
    
    
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
    
    @BeforeTest
    public static void ExtentReportConfig() {
    	extent = new ExtentReports(Reportfile, true);
    	extent.loadConfig(extentconfigfile);
        Map<String, String> sysInfo = new HashMap<String, String>();
    	sysInfo.put("Selenium Version", "2.53");
    	sysInfo.put("Environment", "Staging");
        extent.addSystemInfo(sysInfo);
    	
    }
    

	@AfterMethod
	 public void teardown(ITestResult result) {		
	  if (result.getStatus() == ITestResult.FAILURE) {
	   test.log(LogStatus.FAIL, result.getThrowable());
	  } else if (result.getStatus() == ITestResult.SKIP) {
	   test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
	  } else {		  
	   test.log(LogStatus.PASS, "Test passed");
	  }
	  extent.endTest(CommonMethod.test);
	  extent.flush();
	 }
}

