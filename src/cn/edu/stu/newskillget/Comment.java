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

public class Comment extends Activity implements OnClickListener {
	private static final String tag = "Comment";
	private TextView tvCommentBack;
	private TextView tvCommentPublish;
	private EditText etDescription;
	private String userdata = "";
	private String type = "";
	private int uid, uid2, typed_id;
	private String title = "";
	private String description = "";
	private String date = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment);
		
		Intent intent = this.getIntent();
		userdata = intent.getStringExtra("user");
		type = intent.getStringExtra("type");
		uid = intent.getIntExtra("uid", 0);
		uid2 = intent.getIntExtra("uid2", 0);
		typed_id = intent.getIntExtra("typed_id", 0);
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		date = intent.getStringExtra("date");
		
		initView();
	}

	private void initView() {
		tvCommentBack = (TextView) findViewById(R.id.tv_comment_back);
		tvCommentPublish = (TextView) findViewById(R.id.tv_comment_publish);
		etDescription=(EditText)findViewById(R.id.et_comment_publish_description);
		
		tvCommentBack.setOnClickListener(this);
		tvCommentPublish.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_comment_back:
//			overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
			finish();
			break;
		case R.id.tv_comment_publish:
			String description=etDescription.getText().toString();
			sendComment(description);
			break;
		default:
			break;
		}
	}
	/*
	 * 发送求技能信息
	 */
	private void sendComment(String description) {
		//TODO
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("uid2", uid2);
		map.put("typed_id", typed_id);
		map.put("content", description);
		Log.i(tag, map.toString());
		
		TaskManager taskManager = TaskManager.getInstance();
		Task task = new Task("setcomment", map, 0, commentHandler);
		taskManager.addTask(task);
	}

	private Handler commentHandler = new Handler(){
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			Log.i(tag, jsonString);
			if(jsonString.contains("SUC")) {
				Toast.makeText(Comment.this, "发布成功！", 2000).show();
				Intent intent;
				if(type.contains("newskill")) {
					intent = new Intent(Comment.this, NewSkillDetail.class);
					intent.putExtra("shid", typed_id);
				} else {
					intent = new Intent(Comment.this, BegSkillDetail.class);
					intent.putExtra("sid", typed_id);
				}
				intent.putExtra("user", userdata);
				intent.putExtra("uid2", uid2);
				intent.putExtra("title", title);
				intent.putExtra("description", description);
				intent.putExtra("date", date);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(Comment.this, "发布失败！", 2000).show();
			}
		}
	};
}
