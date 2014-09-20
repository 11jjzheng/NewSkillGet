package cn.edu.stu.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.mock.MockApplication;

public class JsonService {
	/**
	 * @author GT ��������������ݼ���
	 */
	public JsonService() {
	}

	// �����½��֤
	public static Map<String, Object> getLogin() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "login");
		return map;
	}

	// ����ע�����Ϣ
	public static Map<String, Object> getRegister() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "register");

		return map;
	}

	// ����������������¼��������������Ϣ��������У��
	public static Map<String, Object> getNewSkills(int index) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "newskills");
		map.put("pagesize", 5);
		map.put("index", index);

		return map;
	}
	
	// ������������������������������Ϣ��������У��
	public static Map<String, Object> getBegSkills(int index) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "getskills");
		map.put("pagesize", 5);
		map.put("index", index);

		return map;
	}

	// �����¼�����ϸ����
	public static Map<String, Object> getSkillDetail(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shid", id);

		return map;
	}
	
	//��������
	public static Map<String, Object> getComment(int shid, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typed_id", shid);
		map.put("type", type);
		
		return map;
	}

	/**
	 * ������ϸҳ������Ҫѧϰ
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
