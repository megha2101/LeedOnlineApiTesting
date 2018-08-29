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
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

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
import com.jayway.restassured.response.ResponseBody;
import com.leedOnline.driver.BaseClass;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class CommonMethod extends BaseClass {
	static Format formatter = new SimpleDateFormat("YYYY-MM-dd");
	static Date date = new Date();
	public static XlsReader result = new XlsReader(System.getProperty("user.dir") + "/Excel/Leedonline-AutomationReport.xlsx");
	public static List<String> jsonNonceResponse;
	public static Float responsetime;
	public static Response res;
	public static ExtentReports extent;
    public static ExtentTest test;
    public static FileInputStream fis;
    public static int num = 0;
    public static String apiRequestId;
    public static short Green = IndexedColors.GREEN.getIndex();
    public static short Yellow = IndexedColors.YELLOW.getIndex();
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
	 	 
	 public static void checkforKeysNullValues() {
		 Map jsonMap = new HashMap();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (Object key : jsonMap.keySet()) {
		    	 System.out.println("key is: "+ key +" value is: " + jsonMap.get(key));		    	 
		    	 if(jsonMap.get(key).toString()!=null) {
		    		 if (jsonMap.get(key).toString().isEmpty()) {
						 Assert.assertFalse(key + " is empty.", jsonMap.get(key).toString().isEmpty());						 
			    	}else if(key.toString().toLowerCase().contains("errorMessage")) {
			    		 Assert.assertTrue("Error message is: "+ jsonMap.get(key).toString() , false);
			    	} 
		    	 }else {
		    		 Assert.assertFalse(key + " value is null.", true); 
		    	 }
		    			    	
		    }
	 }
	 
	 public static void checkforRespContainsValue(String value) {
		    ResponseBody body = CommonMethod.res.getBody();
			String bodyAsString = body.asString();
			Assert.assertEquals(bodyAsString.contains(value), true);
	 }
	 
	
	 public static void checkforSpecificKeyValue(String key1, String key2) {
		 Map jsonMap = new HashMap();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
			Set<Map.Entry> entryMap = jsonMap.entrySet();
			for (Entry entry : entryMap) {
				Object key = entry.getKey();
				if (key.toString().equals(key1)) {
					if(entry.getValue()!=null) {
						System.out.println("key is: "+ key +" value is:" + entry.getValue().toString());
						 Assert.assertFalse(key + "value is empty.", entry.getValue().toString().isEmpty());		
					}else {
						Assert.assertFalse(key + " value is null.", true);
					}
					 					
		    	}else if(key.toString().equals(key2)){
		    		if(entry.getValue()!=null) {
		    			System.out.println("key is: "+ key +" value is:"+ entry.getValue().toString());
			    		Assert.assertFalse(key + " value is empty.", entry.getValue().toString().isEmpty());		
					}else {
						Assert.assertFalse(key + " value is null.", true);
					}
		    		 
		    	}	
		}
	 }
	 
	 public static void checkForSpecificSubHashMapKeyValue(String keyName,String key1, String key2) {
		    Map<String, HashMap<String, String>> jsonMap = new HashMap<String, HashMap<String, String>>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	if (key.equals(keyName)) {
		    		Map<String, String> jsonMap2 = new HashMap<String, String>();
		    		jsonMap2 = jsonMap.get(keyName);		    			 
	    			 //System.out.println("value are: " + jsonMap2);
	    			 Set<Map.Entry<String, String>> entryMap = jsonMap2.entrySet();
	    			 for (Entry entry : entryMap) {
	    					Object key3 = entry.getKey();
	    					if (key3.toString().equals(key1)) {
	    						if(entry.getValue()!=null) {
	    			    			System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
	    				    		Assert.assertFalse(key3 + " value is empty.", entry.getValue().toString().isEmpty());		
	    						}else {
	    							Assert.assertFalse(key3 + " value is null.", true);
	    						}
	    										
	    			    	}else if(key3.toString().equals(key2)){
	    			    		if(entry.getValue()!=null) {
	    			    			System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
	    				    		Assert.assertFalse(key3 + " value is empty.", entry.getValue().toString().isEmpty());		
	    						}else {
	    							Assert.assertFalse(key3 + " value is null.", true);
	    						}
	    			    		
	    			    	}	
	    			  }				    
		    				    			
		    	}		
		    	else if(key.toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}		    	
		    }
	 }
	 
	 public static void checkForSpecificSubHashMapListHashMapKeyValue(String keyName,String keyName2, String key1, String key2) {
		 Map<String, HashMap<String, List<HashMap>>> jsonMap = new HashMap<String, HashMap<String, List<HashMap>>>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	//System.out.println("Key is: " +key +" keyname is :" +keyName);
		    	if (key.equals(keyName)) {
		    		Map <String, List<HashMap>> jsonMap2 = new HashMap<String, List<HashMap>>();
		    		jsonMap2 = jsonMap.get(key);
		    		List<HashMap> list = new ArrayList<HashMap>();
		    		list = jsonMap2.get(keyName2);
		    		Map jsonMap3 = new HashMap();
	    			 jsonMap3 = list.get(0);
	    			 //System.out.println("value are: " + jsonMap2);
	    			 Set<Map.Entry> entryMap = jsonMap3.entrySet();
	    			 for (Entry entry : entryMap) {
	    					Object key3 = entry.getKey();
	    					if (key3.toString().equals(key1)) {
	    						if(entry.getValue()!=null) {
	    							System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
	    							Assert.assertFalse(key1 + "value is empty.", entry.getValue().toString().isEmpty());		
	    						}else {
	    							Assert.assertFalse(key1 + " value is null.", true);
	    						}
	    									
	    			    	}else if(key3.toString().equals(key2)){
	    			    		if(entry.getValue()!=null) {
	    			    			System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
	    							Assert.assertFalse(key2 + "value is empty.", entry.getValue().toString().isEmpty());		
	    						}else {
	    							Assert.assertFalse(key2 + " value is null.", true);
	    							
	    						}
	    			    	}	
	    			  }
		    				    			
		    		}		
		    	else if(key.toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}		    	
		    }
	 }
	 
	 public static void checkForSpecificSubHashMapHashMapKeyValue(String keyName,String keyName2, String key1, String key2) {
		 Map<String, HashMap<String, HashMap<String, String>>> jsonMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	System.out.println("key is:" +key);
		    	if (key.equals(keyName)) {
		    		Map <String, HashMap<String, String>> jsonMap2= new HashMap<String, HashMap<String, String>>();
		    		jsonMap2 = jsonMap.get(key);
		    		Set<Entry<String, HashMap<String, String>>> entryMap = jsonMap2.entrySet();
	    			 for (Entry entry : entryMap) {
	    					Object key3 = entry.getKey();
	    					System.out.println("keyname is:" +key3);
	    					if (key3.toString().equals(keyName2)) {
	    						Map jsonMap3 = new HashMap();
	    						jsonMap3 = jsonMap2.get(key3);
	    						System.out.println("jsonMap3 is:" +jsonMap3);
	    						Set<Map.Entry<String, String>> entryMap2 = jsonMap3.entrySet();
	    						for (Entry entry2 : entryMap2) {
	    	    					Object key4 = entry2.getKey();
	    	    					if (key4.toString().equals(key1)) {
	    	    						if(entry2.getValue()!=null) {
	    	    							System.out.println("key is: "+ key4 +" value is:"+ entry2.getValue().toString());
	    	    							Assert.assertFalse(key1 + "value is empty.", entry2.getValue().toString().isEmpty());		
	    	    						}else {
	    	    							Assert.assertFalse(key1 + " value is null.", true);
	    	    						}
	    	    									
	    	    			    	}else if(key4.toString().equals(key2)){
	    	    			    		if(entry.getValue()!=null) {
	    	    			    			System.out.println("key is: "+ key4 +" value is:"+ entry2.getValue().toString());
	    	    							Assert.assertFalse(key2 + "value is empty.", entry2.getValue().toString().isEmpty());		
	    	    						}else {
	    	    							Assert.assertFalse(key2 + " value is null.", true);
	    	    							
	    	    						}
	    	    			    	}	
	    	    			  }
	    						 
	    			    	}
	    			  }
			    
		    		
		    	}else if(key.toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}
		    	
		    }
	 }
	 
	 public static void checkForSpecificListArrayNullValue(String keyName,String keyName2, String key1, String key2, String key3) {
		 Map<String, HashMap<String, List<HashMap>>> jsonMap = new HashMap<String, HashMap<String, List<HashMap>>>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	if (key.equals(keyName)) {
		    		Map <String, List<HashMap>> jsonMap2 = new HashMap<String, List<HashMap>>();
		    		jsonMap2 = jsonMap.get(key);
		    		List<HashMap> list = new ArrayList<HashMap>();
		    		list = jsonMap2.get(keyName2);
		    		if(list.size()>0) {
			    		System.out.println("key is: " + keyName2+" value is:" + list.toString());
			    		Assert.assertFalse(keyName2 + " list is empty.", list.toString().isEmpty());
		    		}else {
		    			Assert.assertFalse(keyName2 + " list is null.", true);
		    		}		    				    			
		    	}else if(key.toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}else if(key.equals(key1)) {
		    		List list = new ArrayList();
		    		list = (List) jsonMap.get(key1);
		    		if(list.size()>0) {	    			
			    		System.out.println("key is: " + key1+" value is:" + list.toString());
			    		Assert.assertFalse(key1 + " list is empty.", list.toString().isEmpty());	
		    		}else {
		    			Assert.assertFalse(key1 + " value is null.", true);
		    		}
		    			
		    	}else if(key.equals(key2)) {
		    		List list = new ArrayList();
		    		list = (List) jsonMap.get(key2);
		    		if(list.size()>0) {		    			
			    		System.out.println("key is: " + key2+" value is:" + list.toString());
			    		Assert.assertFalse(key2 + " list is empty.", list.toString().isEmpty());		
			    	}else {
		    			Assert.assertFalse(key2 + " value is null.", true);
		    		}
		    		
		    	}else if(key.equals(key3)) {
		    		List list = new ArrayList();
		    		list = (List) jsonMap.get(key3);
		    		if(list.size()>0) {
			    		System.out.println("key is: " + key3+" value is:" + list.toString());
			    		Assert.assertFalse(key3 + " list is empty.", list.toString().isEmpty());		
			    	}else {
		    			Assert.assertFalse(key3 + " value is null.", true);
		    		}
		    				
		    	}
		    }
	 }
	 
	 public static void checkForSpecificSubListHashmapKeyValue(String keyName,String key1, String key2) {
		 Map<String, List<HashMap>> jsonMap = new HashMap<String, List<HashMap>>();
			jsonMap = (HashMap) CommonMethod.res.jsonPath().getMap("$");
		    for (String key : jsonMap.keySet()) {
		    	if (key.equals(keyName)) {
		    		List<HashMap> list = new ArrayList<HashMap>();
		    		list = jsonMap.get(keyName);
		    		for(int i = 0; i < list.size(); i++)
				    {
		    			 Map jsonMap2 = new HashMap();
		    			 jsonMap2 = list.get(i);
		    			 //System.out.println("value are: " + jsonMap2);
		    			 Set<Map.Entry> entryMap = jsonMap2.entrySet();
		    			 for (Entry entry : entryMap) {
		    					Object key3 = entry.getKey();
		    					if (key3.toString().equals(key1)) {
		    						if(entry.getValue()!=null) {
		    			    			System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
		    				    		Assert.assertFalse(key3 + " value is empty.", entry.getValue().toString().isEmpty());		
		    						}else {
		    							Assert.assertFalse(key3 + " value is null.", true);
		    						}
		    						 
		    			    	}else if(key3.toString().equals(key2)){
		    			    		if(entry.getValue()!=null) {
		    			    			System.out.println("key is: "+ key3 +" value is:"+ entry.getValue().toString());
		    				    		Assert.assertFalse(key3 + " value is empty.", entry.getValue().toString().isEmpty());		
		    						}else {
		    							Assert.assertFalse(key3 + " value is null.", true);
		    						}
		    			    	}	
		    			  }
				    
				    }
		    				    			
		    	}		
		    	else if(key.toLowerCase().contains("error")) {
		    		Assert.assertTrue("Error is present in the response.", false);
		    	}		    	
		    }
	 }
	 
	
	
	 public static Float responsetime() {
		 return Float.valueOf(CommonMethod.res.header("X-Api-Process-Time"));		 
	 }
	
	
	public static String getLabel(float responseTime) {			
		if (responseTime <= 4000){
			
	    return "<span class='label outline info'>" + responseTimeInMS() + " Milliseconds" + "</span>";
		}
		
		else
		{
		return "<span class='label outline fatal'>" + responseTimeInMS() + " Milliseconds" + "</span>";
		}	    
	}
	
	public static String getLabel(String responsetime) {	
		
	    return "<span class='label outline info'>" + responseTimeInMS() + " Milliseconds" + "</span>";    
	}
	 
	 public static String responseTimeInMS(){
		 DecimalFormat df = new DecimalFormat("#.###");
		 Float time = responsetime*1000;
		 String value = df.format(time);
		 return  value;
	 }
	
	
	public static String getStatus(int statusCode) {
		if(statusCode == 200) {
			return "Pass";
		}else {
			return "Need Discussion";
		}
	}
	
	public static short getColor(String status) {
		if(status == "Pass") {			
			return Green;
		}else {
			return Yellow;
		}
	}
	
	 public static void writeInExcel(String serviceName, String ResponseTime, String status) throws IOException {
		 		
		   fis = new FileInputStream(resultFile);
		   Workbook workbook = new XSSFWorkbook(fis);
		   Sheet sheet = workbook.getSheet("Sheet1");
		   String sheetName = sheet.getSheetName();
		   ++num;
		   int rownum = num+1;
		   String TimeinMSec = CommonMethod.responseTimeInMS();
		   short color = getColor(status);
		   result.setCellData(sheetName, "RestServiceName", rownum, serviceName);
		   result.setCellData(sheetName, "ResponseTime (MilliSeconds)", rownum, TimeinMSec);
		   result.setCellData(sheetName, "Status", rownum, status, fontWhite,color);
    }
	 
	 
	
	 public static String randomNumber() throws IOException, InterruptedException{
	    	
	    	String random;
	    	int random_num = 1;
		    Random t = new Random();
		    
		    // random integers in [1000, 800000]
		    random_num=	(t.nextInt(800000));
		    random = String.valueOf(random_num);
		    
		   // System.out.println(random);
			Thread.sleep(1000);
			return random;
			
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