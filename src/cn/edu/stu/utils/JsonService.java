package cn.edu.stu.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.mock.MockApplication;

public class JsonService {
	/**
	 * @author GT 请求服务器的数据集合
	 */
	public JsonService() {
	}

	// 请求登陆验证
	public static Map<String, Object> getLogin() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "login");
		return map;
	}

	// 发送注册的信息
	public static Map<String, Object> getRegister() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "register");

		return map;
	}

	// 请求服务器，发送新技能主界面基本信息给服务器校验
	public static Map<String, Object> getNewSkills(int index) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "newskills");
		map.put("pagesize", 5);
		map.put("index", index);

		return map;
	}
	
	// 请求服务器，发送求技能主界面基本信息给服务器校验
	public static Map<String, Object> getBegSkills(int index) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "getskills");
		map.put("pagesize", 5);
		map.put("index", index);

		return map;
	}

	// 请求新技能详细内容
	public static Map<String, Object> getSkillDetail(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shid", id);

		return map;
	}
	
	//请求评论
	public static Map<String, Object> getComment(int shid, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typed_id", shid);
		map.put("type", type);
		
		return map;
	}

	/**
	 * 技能详细页面点击我要学习
	 */
	public static JSONObject getWantLearnSkill() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", "wantlearn");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static Map<String, Object> getPublish() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "push");
		return map;
	}
}
