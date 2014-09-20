package cn.edu.stu.newskillget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.stu.layout.NewSkillStepItem;
import cn.edu.stu.utils.HttpClients;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;

public class NewSkillPublish extends Activity implements OnClickListener {
	private static final String tag = "NewSkillPublish";
	private TextView tvNewSkillBack;
	private TextView tvNewSkillPublish;
	private Button btnPublish;
	private EditText etTitle, etDescription, etStep1;
	private List<NewSkillStepItem> nStepItem = new ArrayList<NewSkillStepItem>();
	private LinearLayout llStep;
	private int stepId = 0;
	private String userdata = "";

	// 通信部分map
	private Map<String, Object> map = JsonService.getPublish();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newskill_publish);
		
		Intent intent = this.getIntent();
		userdata = intent.getStringExtra("user");

		initView();
	}

	private void initView() {
		tvNewSkillBack = (TextView) findViewById(R.id.tv_newskill_back);
		tvNewSkillPublish = (TextView) findViewById(R.id.tv_newskill_publish);
		btnPublish = (Button) findViewById(R.id.btn_add_step);
		etTitle = (EditText) findViewById(R.id.et_newskill_publish_title);
		etDescription = (EditText) findViewById(R.id.et_newskill_publish_description);
		etStep1 = (EditText) findViewById(R.id.et_newskill_publish_step1);
		llStep = (LinearLayout) findViewById(R.id.ll_step_content);

		tvNewSkillBack.setOnClickListener(this);
		tvNewSkillPublish.setOnClickListener(this);
		btnPublish.setOnClickListener(new onStepListener());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_newskill_back:
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("user", userdata);
			startActivity(intent);
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
			finish();
			break;
		case R.id.tv_newskill_publish:
			String title = etTitle.getText().toString();
			String description = etDescription.getText().toString();
			String step1 = etStep1.getText().toString();
			List<String> otherStep = new ArrayList<String>();
			for (int i = 0; i < nStepItem.size(); i++) {
				otherStep.add(nStepItem.get(i).getEditTextStr());
			}
			sendNewSkill(title, description, step1, otherStep);
			break;

		default:
			break;
		}
	}

	class onStepListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			nStepItem.add(new NewSkillStepItem(NewSkillPublish.this));
			nStepItem.get(stepId).setHint(stepId + 2);
			// TODO
			llStep.addView(nStepItem.get(stepId), stepId++);
		}
	}

	/*
	 * 发送新技能到服务器
	 */
	private void sendNewSkill(String title, String description,
			String step1, List<String> otherStep) {
		// TODO
		int i = 1;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userdata);
			map.put("uid", jsonObject.getInt("uid"));
			map.put("username", jsonObject.getString("username"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("title", title);
		map.put("description", description);
		map.put("step1", i);
		map.put("content1", step1);
		//split
		Iterator it = otherStep.iterator();
		while(it.hasNext()) {
			i++;
			String step = (String)it.next();
			map.put("step" + i, i);
			map.put("content" + i, step);
		}
		Log.i(tag, map.toString());
		
		TaskManager taskManager = TaskManager.getInstance();
		Task task = new Task("upload_newskill", map, i, pushSkillHandler);
		taskManager.addTask(task);
	}
	
	private Handler pushSkillHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			Log.i(tag, jsonString);
			if(jsonString.contains("SUC")) {
				Toast.makeText(NewSkillPublish.this, "发布成功！", 2000).show();
				Intent intent = new Intent(NewSkillPublish.this, MainActivity.class);
				intent.putExtra("user", userdata);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(NewSkillPublish.this, "发布失败！", 2000).show();
			}
		}
	};
}
