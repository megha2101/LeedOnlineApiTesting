package com.leedOnline.driver;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	public static XlsReader result = new XlsReader(System.getProperty("user.dir") + "/Excel/Leedonline-AutomationReport.xlsx");
	public static List<String> jsonNonceResponse;
	public static long responsetime;
	public static Response res;
	public static ExtentReports extent;
    public static ExtentTest test;
    public static FileInputStream fis;
    public static int num = 0;
    public static short Green = IndexedColors.GREEN.getIndex();
    public static short Red = IndexedColors.RED.getIndex();
    public static short fontWhite = IndexedColors.WHITE.getIndex();
	public static String contentType = "application/x-www-form-urlencoded";
	public static File extentconfigfile = new File(System.getProperty("user.dir") +"/src/main/resources/listener/extent-config.xml");
    public static String Reportfile = System.getProperty("user.dir") +"/Report/Leedonline-AutomationReport" + "_" + formatter.format(date) + ".html";
    public static String resultFile = System.getProperty("user.dir") + "/Excel/Leedonline-AutomationReport.xlsx";
                                                                               
	public static void GeneratingAuthCode(String SheetName, int rowNum) {
		Token = given()
				.header("Content-Type",CommonMethod.contentType)
				.spec(reqSpec)
				.params(
						"username", data.getCellData(SheetName, "username", rowNum),
						"password", data.getCellData(SheetName, "password", rowNum))
				.expect().statusCode(200).when()
				.post("/authenticate").then().contentType(ContentType.JSON)
				.extract().response().path("token").toString();		
		header = "Basic " + Token;	
	}
	
	 public static void ExtentReportConfig() {
    	extent = new ExtentReports(Reportfile, false);
    	extent.loadConfig(extentconfigfile);
        Map<String, String> sysInfo = new HashMap<String, String>();
    	sysInfo.put("Selenium Version", "2.53");
    	sysInfo.put("Environment", "Staging");
        extent.addSystemInfo(sysInfo);
	    	
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
	
	public static String getStatus(int statusCode) {
		if(statusCode == 200) {
			return "Pass";
		}else {
			return "Fail";
		}
	}
	
	public static short getColor(String status) {
		if(status == "Pass") {			
			return Green;
		}else {
			return Red;
		}
	}
	
	 public static void writeInExcel(String serviceName, String ResponseTime, String status) throws IOException {
		 		
		   fis = new FileInputStream(resultFile);
		   Workbook workbook = new XSSFWorkbook(fis);
		   Sheet sheet = workbook.getSheet("Sheet1");
		   String sheetName = sheet.getSheetName();
		   ++num;
		   int rownum = num+1;
		   Double time =  Double.valueOf(ResponseTime)/1000;
		   String TimeinSec = time.toString();
		   short color = getColor(status);
		   result.setCellData(sheetName, "RestServiceName", rownum, serviceName);
		   result.setCellData(sheetName, "ResponseTime (Seconds)", rownum, TimeinSec);
		   result.setCellData(sheetName, "Status", rownum, status, fontWhite,color);
    }
		     
	public static void testlog(String log, String message){
		switch(log){	
		case "Pass":
			CommonMethod.test.log(LogStatus.PASS, message);
			break;		
		case "Info":
			CommonMethod.test.log(LogStatus.INFO, message);
			break;		
		 default:     	
	     	System.out.println("Not Valid Input");
		}
	}
	
}