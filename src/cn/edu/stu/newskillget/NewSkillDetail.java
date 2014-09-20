package cn.edu.stu.newskillget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.edu.stu.layout.NewSkillDetailItem;
import cn.edu.stu.utils.HttpClients;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.JsonTools;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;

public class NewSkillDetail extends Activity implements OnClickListener {
	private static final String tag = "NewSkillDetail";
	private TextView tvBack;
	private ImageView ivShare;
	private TextView tvLearn, tvComment;
	private ImageView ivSupport;
	private LinearLayout llDetailContent;
	private NewSkillDetailItem nDetailItem;
	
	private TaskManager taskManager;
	
	private String userdata = "";
	private int shid;
	private String title = "";
	private String date = "";
	private String content = "";
	private int uid;
	private int uid2;

	private int num = 0;// 要学习的技能号，从技能列表上去
	// private JSONObject jsonObject = JsonService.getSkillDetail();
	private Map<String, Object> map;
	private Map<String, Object> map2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newskill_detail);

		Intent intent = this.getIntent();
		userdata = intent.getStringExtra("user");
		shid = intent.getIntExtra("shid", 0);
		uid2 = intent.getIntExtra("uid", 0);
		title = intent.getStringExtra("title");
		date = intent.getStringExtra("date");
		content = intent.getStringExtra("content");
		
		try {
			JSONObject jsonObject = new JSONObject(userdata);
			uid = jsonObject.getInt("uid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map = JsonService.getSkillDetail(shid);
		map2 = JsonService.getComment(shid, "newskill");
		
		taskManager = TaskManager.getInstance();
		Task task = new Task("skilldetail", map, 0, newSkillDetailHandler);
		taskManager.addTask(task);
		Task task2 = new Task("comments", map2, 0, commentHandler);
		taskManager.addTask(task2);
		
		tvBack = (TextView) findViewById(R.id.tv_newskill_back);
		ivShare = (ImageView) findViewById(R.id.iv_newskill_share);
		tvLearn = (TextView) findViewById(R.id.tv_newskill_detail_learn);
		ivSupport = (ImageView) findViewById(R.id.iv_newskill_detail_support);
		tvComment = (TextView) findViewById(R.id.tv_newskill_detail_comment);

		llDetailContent = (LinearLayout) findViewById(R.id.ll_newskill_detail_content);
		nDetailItem = new NewSkillDetailItem(this);
		// TODO 设置新技能内容,
		// nDetailItem.setSkillContent(map, who1, who2, commentStr);
		// nDetailItem.setSkillContent(map);
		llDetailContent.addView(nDetailItem);

		tvLearn.setText("我要学");
		
		tvLearn.setOnClickListener(new onLearnListener());
		ivSupport.setOnClickListener(new onSupportListener());
		tvComment.setOnClickListener(new onCommentListener());
		tvBack.setOnClickListener(this);
		ivShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_newskill_back:
//			Intent intent = new Intent(NewSkillDetail.this, MainActivity.class);
//			intent.putExtra("user", userdata);
//			startActivity(intent);
			finish();
			break;
		case R.id.iv_newskill_share:
			shareText(this, "分享到", "一个有趣的app，新技能·get√，让每天都能学到新技能！");
			break;
		default:
			break;
		}
	}

	public static void shareText(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(Intent.createChooser(intent, title));
	}

	/*
	 * 监听“我要学”
	 */
	class onLearnListener implements OnClickListener {
		private Map<String, Object> map3 = new HashMap<String, Object>();
		private int flag = 0;
		@Override
		public void onClick(View arg0) {
			if (flag == 0) {
				tvLearn.setText("在学");
				// 我要学---》在学
				map3.put("shid", shid);
				map3.put("uid", uid);
				map3.put("type", "learnskills");
				Task task = new Task("learnskills", map3, num, learnAlikeHandler);
				taskManager.addTask(task);

				flag = 1;
			} else {
//				tvLearn.setText("我要学");
				// 在学---》我要学
				map3.put("shid", shid);
				map3.put("uid", uid);
				map3.put("type", "nowantlearn");
//				Task task = new Task("nowantlearn", map3, num, learnAlikeHandler);
//				taskManager.addTask(task);
				
				flag = 0;
			}
			// TODO
		}
	}

	/*
	 * 监听点赞
	 */
	class onSupportListener implements OnClickListener {
		private Map<String, Object> map3 = new HashMap<String, Object>();
		private int flag = 0;
		@Override
		public void onClick(View arg0) {
			// TODO
			if (flag == 0) {
//				tvLearn.setText("点赞");
				// 我要学---》在学
				map3.put("shid", shid);
				map3.put("uid", uid);
				map3.put("type", "like");
//				Task task = new Task("like", map3, num, learnAlikeHandler);
//				taskManager.addTask(task);
				
				flag = 1;
			} else {
//				tvLearn.setText("取消点赞");
				// 在学---》我要学
				map3.put("shid", shid);
				map3.put("uid", uid);
				map3.put("type", "nolike");
//				Task task = new Task("nolike", map3, num, learnAlikeHandler);
//				taskManager.addTask(task);
				
				flag = 0;
			}
			// TODO

		}
	}

	class onCommentListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO
			Intent intent = new Intent(NewSkillDetail.this, Comment.class);
			intent.putExtra("user", userdata);
			intent.putExtra("type", "newskill");
			intent.putExtra("uid", uid);
			intent.putExtra("uid2", uid2);
			intent.putExtra("typed_id", shid);
			intent.putExtra("title", title);
			intent.putExtra("date", date);
			startActivity(intent);
			finish();
		}
	}
	
	private Handler newSkillDetailHandler = new Handler() {
		public void handleMessage (Message msg) {
			String jsonString = msg.obj.toString();
			if(jsonString.length() > 3) {
				StringBuffer data = new StringBuffer();
				List<Map<String, Object>> listData = JsonTools.getListMap("newskilldetail", jsonString);
				Map<String, Object> Data = new HashMap<String, Object>();
				Data.put("title", title);
				Data.put("time", date);
				Data.put("content", content);
				Iterator it = listData.iterator();
				while(it.hasNext()) {
					Map<String, Object> map = (Map<String, Object>)it.next();
					data.append(map.get("step").toString()).append(". ");
					data.append(map.get("content").toString()).append("\n");
				}
				Data.put("content", data);
				nDetailItem.setSkillContent(Data);
			} else {
				Log.i(tag, "获取失败");
			}
		}
	};
	
	private Handler commentHandler = new Handler() {
		public void handleMessage (Message msg) {
			String jsonString = msg.obj.toString();
			if(jsonString.length() > 3) {
				Log.i(tag, "comment= " + jsonString);
				List<Map<String, Object>> listData = JsonTools.getListMap("comments", jsonString);
				nDetailItem.setComment(listData);
			} else {
				Log.i(tag, "获取失败");
			}
		}
	};
	
	private Handler learnAlikeHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			Log.i(tag, jsonString);
			if(jsonString.contains("SUC")) {
				
			} else {
				
			}
		}
	};
}
