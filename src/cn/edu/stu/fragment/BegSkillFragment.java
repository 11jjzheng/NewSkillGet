package cn.edu.stu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.stu.newskillget.BegSkillDetail;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.utils.HttpClients;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.JsonTools;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import cn.edu.stu.view.XListView;
import cn.edu.stu.view.XListView.IXListViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BegSkillFragment extends Fragment implements IXListViewListener {
	private static final String TAG = "BegSkillFragment";
	private View view;
	private XListView mListView;
	private Handler mHandler;
	private int start = 0;
	private static int i = 1;
	private static int refreshCnt = 0;
	private String userdata = "";
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	private int[] ids = { R.id.tv_begskill_list_item_tilte,
			R.id.tv_begskill_list_item_id, R.id.tv_begskill_list_item_uid,
			R.id.tv_share_list_item_content,
			R.id.tv_begskill_list_item_username,
			R.id.tv_begskill_list_item_date,
			R.id.tv_begskill_list_item_support,
			R.id.tv_begskill_list_item_comment };
	private String[] strs = { "title", "sid", "uid", "description", "username",
			"date", "agree_cnt", "comment_cnt" };

	private List<Map<String, Object>> lsBuff;
	Map<String, Object> map = JsonService.getBegSkills(0);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_beg_skill, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView = (XListView) view.findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);

		TaskManager taskManager = TaskManager.getInstance();
		Task task = new Task("getskills", map, 0, begSkillHandler);
		taskManager.addTask(task);

		SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
				.getApplicationContext(), listData,
				R.layout.begskill_list_item, strs, ids);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView iD = (TextView) arg1
						.findViewById(R.id.tv_begskill_list_item_id);
				TextView uiD = (TextView) arg1
						.findViewById(R.id.tv_begskill_list_item_uid);
				TextView title = (TextView) arg1
						.findViewById(R.id.tv_begskill_list_item_tilte);
				TextView description = (TextView) arg1
						.findViewById(R.id.tv_share_list_item_content);
				TextView date = (TextView) arg1
						.findViewById(R.id.tv_begskill_list_item_date);
				Intent intent = new Intent(getActivity(), BegSkillDetail.class);
				intent.putExtra("user", userdata);
				intent.putExtra("sid",
						Integer.parseInt(iD.getText().toString()));
				intent.putExtra("uid", Integer.parseInt(uiD.getText().toString()));
				intent.putExtra("title", title.getText().toString());
				intent.putExtra("description", description.getText().toString());
				intent.putExtra("date", date.getText().toString());
				startActivity(intent);
			}
		});
	}

	public BegSkillFragment(String userdata) {
		super();
		this.userdata = userdata;
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("");
	}

	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;

				listData.clear();
				map = JsonService.getBegSkills(0);
				i--;
				String jsonString = HttpClients.callClient("getskills", map, 0);// 从服务器重新获取信息
				lsBuff = JsonTools.getListMap("getskills", jsonString);
				for (Map<String, Object> map : lsBuff) {
					listData.add(map);
				}

				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.begskill_list_item, strs, ids);
				onLoad();
			}
		}, 2000);
	}

	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				map = JsonService.getBegSkills(i);
				i++;
				String jsonString = HttpClients.callClient("getskills", map, 0);// 从服务器重新获取信息
				lsBuff = JsonTools.getListMap("getskills", jsonString);
				for (Map<String, Object> map : lsBuff) {
					listData.add(map);
				}

				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.begskill_list_item, strs, ids);
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	private Handler begSkillHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			if (jsonString.length() > 3) {
				Log.i(TAG, jsonString);
				listData = JsonTools.getListMap("getskills", jsonString);
				lsBuff = new ArrayList<Map<String, Object>>();
				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.begskill_list_item, strs, ids);
				mListView.setAdapter(mAdapter);
			} else {
				Log.i(TAG, "获取失败");
			}
		}
	};
}
