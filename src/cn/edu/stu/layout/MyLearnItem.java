package cn.edu.stu.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.stu.newskillget.R;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.JsonTools;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyLearnItem extends LinearLayout{
	private FragmentActivity mContext;
	private TextView tvTitle,tvContent,tvStepNow;
	private Button bStepUp;
	private int uid, shid, seid, progress = 1;
	private int steps_cnt = 0;
	private static int GONE=8;  //控件不可见，并且不占位置
	private Map<String, Object> map;
	private TaskManager taskManager ;
	
	public MyLearnItem(FragmentActivity context) {
		super(context);
		mContext=context;
		initView();
	}

	public MyLearnItem(FragmentActivity context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}

	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View vMyLearnItem = View.inflate(mContext, R.layout.personal_my_learn_item, null);
		vMyLearnItem.setLayoutParams(layoutParams);
		addView(vMyLearnItem);
		
		tvTitle=(TextView)vMyLearnItem.findViewById(R.id.tv_my_learn_title);
		tvContent=(TextView)vMyLearnItem.findViewById(R.id.tv_my_learn_content);
		tvStepNow=(TextView)vMyLearnItem.findViewById(R.id.tv_my_learn_stepnow);
		bStepUp=(Button)vMyLearnItem.findViewById(R.id.b_learnstepup);
		
		tvTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(tvContent.getVisibility()==8){
					tvContent.setVisibility(VISIBLE);  //当控件状态不可见时，设置为可见
				}else{
					tvContent.setVisibility(GONE);
				}
			}
		});
		
		bStepUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(progress < steps_cnt) {
					Map<String, Object> map3 = new HashMap<String, Object>();
					map3.put("uid", uid);
					map3.put("shid", shid);
					taskManager = TaskManager.getInstance();
					Task task = new Task("learnstepup", map3, 0, stepUpHandler);
					taskManager.addTask(task);
				}
			}
		});
	}
	
	public void setData(int uid, int shid, int seid, int progress, String title){
		this.uid = uid;
		this.shid = shid;
		this.seid = seid;
		this.progress = progress;
		tvTitle.setText(title);	
		tvStepNow.setText("现在进行的步骤： " + progress);
		
		map = JsonService.getSkillDetail(shid);
		taskManager = TaskManager.getInstance();
		Task task = new Task("skilldetail", map, 0, newSkillDetailHandler);
		taskManager.addTask(task);
	}
	
	private Handler newSkillDetailHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			if(jsonString.length() > 3) {
				StringBuffer data = new StringBuffer();
				List<Map<String, Object>> listData = JsonTools.getListMap("newskilldetail", jsonString);
				Iterator it = listData.iterator();
				while(it.hasNext()) {
					Map<String, Object> map2 = (Map<String, Object>)it.next();
					data.append(map2.get("step").toString()).append(". ");
					data.append(map2.get("content").toString()).append("\n");
					steps_cnt++;
				}
				tvContent.setText(data);
				
				if(progress >= steps_cnt) {
					bStepUp.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
					bStepUp.setClickable(false);
				}
			} else {
				Toast.makeText(mContext, "获取失败", 2000).show();
			}
		}
	};
	
	private Handler stepUpHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			if(jsonString.contains("SUC")) {
				Toast.makeText(mContext, "StepUp Success", 1000).show();
				progress++;
				tvStepNow.setText("现在进行的步骤： " + progress);
			} else {
				Toast.makeText(mContext, "StepUp Fail", 1000).show();
			}
		}
	};
}
