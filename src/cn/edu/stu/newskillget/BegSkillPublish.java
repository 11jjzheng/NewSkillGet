package cn.edu.stu.newskillget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BegSkillPublish extends Activity implements OnClickListener {
	private static final String tag = "BegSkillPublish";
	private TextView tvBegskillBack;
	private TextView tvBegskillPublish;
	private EditText etTitle,etTag,etDescription;
	private String userdata = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.begskill_publish);
		
		Intent intent = this.getIntent();
		userdata = intent.getStringExtra("user");
		
		initView();
	}

	private void initView() {
		tvBegskillBack = (TextView) findViewById(R.id.tv_begskill_back);
		tvBegskillPublish = (TextView) findViewById(R.id.tv_begskill_publish);
		etTitle=(EditText)findViewById(R.id.et_begskill_publish_title);
		etTag=(EditText)findViewById(R.id.et_begskill_publish_tag);
		etDescription=(EditText)findViewById(R.id.et_begskill_publish_description);
		
		tvBegskillBack.setOnClickListener(this);
		tvBegskillPublish.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_begskill_back:
//			overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
			finish();
			break;
		case R.id.tv_begskill_publish:
			String title=etTitle.getText().toString();
			String tag=etTag.getText().toString();
			String description=etDescription.getText().toString();
			sendBegSkill(title, tag, description);
			break;
		default:
			break;
		}
	}
	/*
	 * 发送求技能信息
	 */
	private void sendBegSkill(String title,String tag,String description) {
		//TODO
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(userdata);
			map.put("uid", jsonObject.getInt("uid"));
			map.put("username", jsonObject.getString("username"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("title", title);
		map.put("tag", tag);
		map.put("description", description);
		Log.i(tag, map.toString());
		
		TaskManager taskManager = TaskManager.getInstance();
		Task task = new Task("getskill_up", map, 0, begSkillPHandler);
		taskManager.addTask(task);
	}

	private Handler begSkillPHandler = new Handler(){
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			Log.i(tag, jsonString);
			if(jsonString.contains("SUC")) {
				Toast.makeText(BegSkillPublish.this, "发布成功！", 2000).show();
				finish();
			} else {
				Toast.makeText(BegSkillPublish.this, "发布失败！", 2000).show();
			}
		}
	};
}
