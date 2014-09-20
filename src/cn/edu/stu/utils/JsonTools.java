package cn.edu.stu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools {

	public JsonTools() {
	}

	public static List<Map<String, Object>> getListMap(String key,
			String jsonString) {
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			for (int i = 0; i < jsonArray.length(); ++i) {
				// 缓存每一个map键值对，然后add进list表中
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				// 迭代器关联一个json对象
				Iterator<String> jsonIter = jsonObject2.keys();
				while (jsonIter.hasNext()) {
					// 对于每一json对象解析出每个键值对放在一个map对象里面
					String json_key = jsonIter.next();
					Object json_value = jsonObject2.get(json_key);
					map.put(json_key, json_value);
				}
				listMaps.add(map);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return listMaps;
	}

	public static Map<String, Object> getSkillDatail(String key,
			String jsonString) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject();
			JSONObject jsonObject2 = jsonObject.getJSONObject(key);

			// 迭代器关联一个json对象
			Iterator<String> jsonIter = jsonObject2.keys();
			while (jsonIter.hasNext()) {
				// 对于每一json对象解析出每个键值对放在一个map对象里面
				String json_key = jsonIter.next();
				Object json_value = jsonObject2.get(json_key);
				map.put(json_key, json_value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static String createJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		JSONObject tem = new JSONObject();
		try {

			HashMap<String, Object> map = (HashMap<String, Object>) value;

			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				tem.put(entry.getKey().toString(), entry.getValue());
			}

			jsonObject.put(key, tem);
			// jsonObject.put(key, (HashMap<String, Object>) value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("createJson:" + jsonObject);
		return jsonObject.toString();
	}
}
