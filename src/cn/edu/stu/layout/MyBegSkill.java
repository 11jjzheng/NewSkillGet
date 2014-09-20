package cn.edu.stu.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.newskillget.BegSkillDetail;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.XListView;
import cn.edu.stu.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyBegSkill extends LinearLayout implements IXListViewListener {

	private FragmentActivity mContext;
	private XListView xlvMyBegSKill;
	private Handler mHandler;
	private List<Map<String, Object>> listData;
	private String userdata = "";

	public MyBegSkill(FragmentActivity context) {
		super(context);
		mContext = context;
		initView();
	}

	@SuppressLint("NewApi")
	public MyBegSkill(FragmentActivity context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View vMyBegSkill = View.inflate(mContext,
				R.layout.personal_my_begskill, null);
		vMyBegSkill.setLayoutParams(layoutParams);
		addView(vMyBegSkill);

		xlvMyBegSKill = (XListView) vMyBegSkill
				.findViewById(R.id.xlv_my_begskill);
		xlvMyBegSKill.setPullLoadEnable(true);
		xlvMyBegSKill.setXListViewListener(this);
		mHandler = new Handler();
		xlvMyBegSKill.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView shidtv = (TextView) arg1
						.findViewById(R.id.tv_personal_shid);
				TextView titletv = (TextView) arg1
						.findViewById(R.id.tv_personal_my_list_item);
				TextView datetv = (TextView) arg1
						.findViewById(R.id.tv_personal_date);
				TextView descriptiontv = (TextView) arg1
						.findViewById(R.id.tv_personal_description);
				Intent intent = new Intent(mContext, BegSkillDetail.class);
				intent.putExtra("user", userdata);
				intent.putExtra("sid",
						Integer.parseInt(shidtv.getText().toString()));
				intent.putExtra("date", datetv.getText().toString());
				intent.putExtra("title", titletv.getText().toString());
				intent.putExtra("description", descriptiontv.getText()
						.toString());
				mContext.startActivity(intent);
			}
		});
	}

	// private List<Map<String, Object>> getData() {
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// for (int i = 0; i < 6; i++) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("title","如何优雅地使用Google？");
	// list.add(map);
	// }
	// return list;
	// }

	public void setListData(JSONArray jsonArray) {
		listData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				map.put("comment_cnt", jsonObject.getInt("comment_cnt"));
				map.put("title", jsonObject.getString("title"));
				map.put("shid", jsonObject.getInt("sid"));
				map.put("date", jsonObject.getString("date"));
				map.put("description", jsonObject.getString("description"));
				listData.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SimpleAdapter mAdapter = new SimpleAdapter(
				mContext.getApplicationContext(), listData,
				R.layout.personal_my_list_item, new String[] { "comment_cnt",
						"title", "shid", "date", "description" }, new int[] {
						R.id.tv_personal_comment_cnt,
						R.id.tv_personal_my_list_item, R.id.tv_personal_shid,
						R.id.tv_personal_date, R.id.tv_personal_description });
		xlvMyBegSKill.setAdapter(mAdapter);
	}

	public void setUserData(String userdata) {
		this.userdata = userdata;
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

			}
		}, 2000);
	}

}
