package cn.edu.stu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpClients {

	private static String PATH = "http://10.30.25.11:8080/";
	private static final String TAG_STRING = "HttpClients";

	public HttpClients() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 
	 * @param action_flag
	 *            
	 * @param map
	 *            
	 * @param num
	 *            
	 * @return
	 */
	public static String callClient(String action_flag,
			Map<String, Object> map, int num) {

		// JsonService jsonService = new JsonService();
		String jsonString = "";
		String result = "fail";

		/**
		 * 
		 * 
		 */
		if (action_flag.equals("login")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("register")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("newskills")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("skilldetail")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("getskills")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("sendbegskill")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("comments")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if (action_flag.equals("upload_newskill")) {
			//格式有点复杂，特殊处理
			JSONArray jsonArray = new JSONArray();
			for(int i = 1; i <= num; i++) {
				try {
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("step", Integer.parseInt(map.get("step" + i).toString()));
					jsonObject2.put("content", map.get("content" + i).toString());
					jsonArray.put(jsonObject2);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONObject jsonObject3 = new JSONObject();
			try {
				jsonObject3.put("uid", Integer.parseInt(map.get("uid").toString()));
				jsonObject3.put("username", map.get("username").toString());
				jsonObject3.put("title", map.get("title").toString());
				jsonObject3.put("description", map.get("description").toString());
				jsonObject3.put("steps", jsonArray);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("upload_newskill", jsonObject3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i(TAG_STRING, jsonObject.toString());
			jsonString = jsonObject.toString();
		} else if(action_flag.equals("getskill_up")){
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else if(action_flag.equals("personal")) {
			jsonString = JsonTools.createJsonString(action_flag, map);
		} else {
			jsonString = JsonTools.createJsonString(action_flag, map);
		}

		try {
			URL url = new URL(PATH + action_flag);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setConnectTimeout(3000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-type", "text/html");
			urlConnection.setRequestProperty("Accept-Charset", "utf-8");
			urlConnection.setRequestProperty("contentType", "utf-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true); 
			
			byte[] mydata = jsonString.getBytes("utf-8");
			// 
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			urlConnection.setRequestProperty("Content-Length",
					String.valueOf(mydata.length));
			// 
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();
			// 
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200) {
				return changeInputStream(urlConnection.getInputStream());
			} else {
				System.out.println("no connected  " + responseCode);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private static String changeInputStream(InputStream inputStream) {
		// TODO Auto-generated method stub
		String result = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024];
		try {
			while ((len = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, len);
			}
			result = new String(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
