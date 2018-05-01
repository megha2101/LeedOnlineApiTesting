package com.leedOnline.driver;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.leedOnline.driver.CommonMethod;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.leedOnline.driver.BaseClass;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CommonMethod extends BaseClass {
	static Format formatter = new SimpleDateFormat("YYYY-MM-dd");
	static Date date = new Date();
	public static String nonceValue;
	public static long responsetime;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Response res;
	public static String contentType = "application/x-www-form-urlencoded";
    public static File extentconfigfile = new File(System.getProperty("user.dir") +"/src/main/resources/listener/extent-config.xml");
    public static String Reportfile = System.getProperty("user.dir") +"/Report/Leedonline-AutomationReport" + "_" + formatter.format(date) + ".html";
    
	public static void GeneratingAuthCode() {
		Token = given()
				.header("Content-Type",CommonMethod.contentType)
				.spec(reqSpec)
				.params(
						  "username", "newserver2@gmail.com",
						  "password", "initpass",
						  "guid", "")
				.expect().statusCode(200).when()
				.post("/authenticate").then().contentType(ContentType.JSON)
				.extract().response().path("token").toString();		
		header = "Basic " + Token;	
	}
	
public static String getLabel(long responsetime) {
		
		if (responsetime <= 4000){
			
	    return "<span class='label outline info'>" + CommonMethod.responsetime + " Milliseconds" + "</span>";
		}
		
		else
		{
		return "<span class='label outline fatal'>" + CommonMethod.responsetime + " Milliseconds" + "</span>";
		}
	    
	    
	}

public static void ExtentReportConfig() {
	extent = new ExtentReports(Reportfile, true);
	extent.loadConfig(extentconfigfile);
    Map<String, String> sysInfo = new HashMap<String, String>();
	sysInfo.put("Selenium Version", "2.53");
	sysInfo.put("Environment", "Staging");
    extent.addSystemInfo(sysInfo);
	
}

public static void testlog(String log, String message){
	switch(log){	
	case "Pass":
		test.log(LogStatus.PASS, message);
		break;		
	case "Info":
		test.log(LogStatus.INFO, message);
		break;		
	 default:     	
     	System.out.println("Not Valid Input");
	}
}

	
	
	
	
}