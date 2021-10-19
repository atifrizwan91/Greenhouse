package com.mcl.app.test_client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;

import org.iotivity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import SQLDatabase.DatabaseHelper;

public class Main {
	static int i = 0;

	public static void main(String argv[]) throws InterruptedException {
		OCBufferSettings.setMaxAppDataSize(1000000);
		int init_ret = OCMain.mainInit(new OCMainInitHandler() {
			@Override
			public int initialize() {
				System.out.println("-----------------------------------------------initiliaze()");
				int ret = OCMain.initPlatform("MCL");
				ret |= OCMain.addDevice("/oic/d", "oic.d.iot", "MCL-IoT001", "ocf.0.0.0", "ocf.res.0.0.0");
				return ret;
			}

			@Override
			public void registerResources() {
				System.out.println("-----------------------------------------------registerResources()");
			}

			@Override
			public void requestEntry() {
				System.out.println("-----------------------------------------------requestEntry()");
			}
		});

		if (init_ret < 0) {
			System.exit(init_ret);
		}
		
		for(int i = 0; i<100; i++) {
			String result;
			result = new OcfClient().doDiscoeryAndReqeust("/light", "", null, "oic.r.light", OCMethod.OC_GET);
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
			System.out.println("Result: "+timeStamp + result);
//			String payloadJsonStr = "{\"data\":123}";
//			result = new OcfClient().doDiscoeryAndReqeust("/calc", "?a=123&b=abc", payloadJsonStr, "oic.r.calc", OCMethod.OC_POST);
//			
			result = result.replace("\n", "");
			
			System.out.println("End Client---------------------------------");
			System.out.println(result);
			//result = result.substring( 1, result.length() - 1 );
			
			result = "[" + result +"]";
			
			result = result.substring(0, 12) + result.substring(13, result.length());
			result = result.substring(0, result.length()-3) + result.substring(result.length()-2, result.length());
			
			System.out.println(OcfClient.L + "payload:----------- Result " + result);
			JSONArray array = new JSONArray(result);  
			System.out.println(array);
			
			JSONObject object = array.getJSONObject(0);  
			JSONObject object1 = object.getJSONObject("Climate");
			Double co2 = object1.getDouble("CO2");
			Double humidity = object1.getDouble("Humidity");
			Double temp = object1.getDouble("temperature");
			Double energy = object1.getDouble("Energy Consumption");
			
//			String co2 = result.substring(7+12,12+12);
//			String humidity = result.substring(24+12,29+12);
//			String temp = result.substring(44+12,48+12);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		    LocalDateTime now = LocalDateTime.now();  
			String dateTime = dtf.format(now);  
			
			
			String q="INSERT INTO `temp_data` VALUES ('"+ dateTime +"','"+ temp+"','"+ humidity +"','"+co2+"');";
			
			DatabaseHelper.insertData(q);

			Thread.sleep(10 * 1000);
		}
		
		
//		String q="INSERT INTO `temp_data` VALUES ('6','23','31','41');";
		
		
		
//		result = new OcfClient().doReqeust("coap+tcp://192.168.1.2:12345", "/light", "", null, OCMethod.OC_GET);
//		System.out.println(OcfClient.L + "payload: " + result);
//		result = new OcfClient().doReqeust("coap://192.168.1.101:12345", "/calc", "?a=123&b=abc", payloadJsonStr, OCMethod.OC_POST);
//		System.out.println(OcfClient.L + "payload: " + result);
//		result = new OcfClient().doDiscoeryAndReqeust("/binaryswitch", "", null, "oic.r.switch.binary", OCMethod.OC_GET);
//		System.out.println(OcfClient.L + "payload: " + result);
//		result = new OcfClient().doReqeust("coap://192.168.1.101:18208", "/scenemember1", "?if=oic.if.baseline", null, OCMethod.OC_GET);
//		System.out.println(OcfClient.L + "payload: " + result);
		
//		byte[] decode = Base64.getDecoder().decode(rep.getValue().getString());
//		System.out.println(decode.length);
//		FileOutputStream out;
//		try {
//			out = new FileOutputStream("saved_model.pb");
//			out.write(decode);
//			out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
}
