package cn.edu.stu.newskillget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.layout.BegSkillDetailItem;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.JsonTools;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
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
import android.widget.TextView;

public class BegSkillDetail extends Activity implements OnClickListener {
	private static final String tag = "BegSkillDetail";
	private TextView tvBack;
	private ImageView ivShare;
	private ImageView ivSupport;
	private TextView tvComment;
	private LinearLayout llDetailContent;
	private BegSkillDetailItem bDetailItem;

	private String userdata = "";
	private int uid, uid2;
	private int sid;
	private String title = "";
	private String description = "";
	private String date = "";
	private TaskManager taskManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		userdata = intent.getStringExtra("user");
		sid = intent.getIntExtra("sid", 0);
		uid2 = intent.getIntExtra("uid2", 0);
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		date = intent.getStringExtra("date");
		try {
			JSONObject jsonObject = new JSONObject(userdata);
			uid = jsonObject.getInt("uid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		map.put("description", description);
		map.put("time", date);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2 = JsonService.getComment(sid, "begskill");
		taskManager = TaskManager.getInstance();
		Task task = new Task("comments", map2, 0, commentHandler);
		taskManager.addTask(task);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.begskill_detail);

		tvBack = (TextView) findViewById(R.id.tv_begskill_back);
		ivShare = (ImageView) findViewById(R.id.iv_begskill_share);
		ivSupport = (ImageView) findViewById(R.id.iv_begskill_detail_support);
		tvComment = (TextView) findViewById(R.id.tv_begskill_detail_comment);

		llDetailContent = (LinearLayout) findViewById(R.id.ll_begskill_detail_content);
		bDetailItem = new BegSkillDetailItem(this);
		// TODO 设置求技能内容
		bDetailItem.setSkillContent(map);
		llDetailContent.addView(bDetailItem);

		tvBack.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		ivSupport.setOnClickListener(new onSupportListener());
		tvComment.setOnClickListener(new onCommentListener());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_begskill_back:
			// overridePendingTransition(R.anim.push_left_out,
			// R.anim.push_left_in);
			finish();
			break;
		case R.id.iv_begskill_share:
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
	 * 监听点赞，存储点赞数
	 */
	class onSupportListener implements OnClickListener {
		private Map<String, Object> map3 = new HashMap<String, Object>();
		private int flag = 0;

		@Override
		public void onClick(View arg0) {
			if (flag == 0) {
				// 我要学---》在学
				map3.put("sid", sid);
				map3.put("uid", uid);
				map3.put("type", "like");
				Task task = new Task("like", map3, 0, likeHandler);
				taskManager.addTask(task);

				flag = 1;
			} else {
				// 在学---》我要学
				map3.put("sid", sid);
				map3.put("uid", uid);
				map3.put("type", "nolike");
				Task task = new Task("nolike", map3, 0, likeHandler);
				taskManager.addTask(task);

				flag = 0;
			}
		}

	}

	/*
	 * 监听评论
	 */
	class onCommentListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(BegSkillDetail.this, Comment.class);
			intent.putExtra("user", userdata);
			intent.putExtra("type", "getskill");
			intent.putExtra("uid", uid);
			intent.putExtra("uid2", uid2);
			intent.putExtra("typed_id", sid);
			intent.putExtra("title", title);
			intent.putExtra("description", description);
			intent.putExtra("date", date);
			startActivity(intent);
			finish();
		}

	}

	private Handler likeHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			if (jsonString.length() > 3) {

			} else {

			}
		}
	};
	
	private Handler commentHandler = new Handler() {
		public void handleMessage (Message msg) {
			String jsonString = msg.obj.toString();
			if(jsonString.length() > 3) {
				Log.i(tag, "comment= " + jsonString);
				List<Map<String, Object>> listData = JsonTools.getListMap("comments", jsonString);
				bDetailItem.setComment(listData);
			} else {
				Log.i(tag, "获取失败");
			}
		}
	};
}
