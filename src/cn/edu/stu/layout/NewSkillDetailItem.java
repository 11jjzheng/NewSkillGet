package cn.edu.stu.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.stu.newskillget.NewSkillDetail;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.CircularImage;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewSkillDetailItem extends LinearLayout{
	private static final String tag = "NewSkillDetailItem";
	private NewSkillDetail mContext;
	private TextView tvTitle,tvTime,tvContent;
	private CircularImage circularImg;
	private LinearLayout llComment;
	private CommentItem cItem;
	private View detailItem;
	private RelativeLayout rlSend;
	private Button btnSend;
	private EditText etComment;
	private int commentNum=0;
	public NewSkillDetailItem(NewSkillDetail context) {
		super(context);
		mContext=context;
		initView();
	}

	public NewSkillDetailItem(NewSkillDetail context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		detailItem = View.inflate(mContext, R.layout.newskill_detail_item, null);
		detailItem.setLayoutParams(layoutParams);
		addView(detailItem);
		
		tvTitle=(TextView)detailItem.findViewById(R.id.tv_newskill_detail_item_title);
		circularImg=(CircularImage)detailItem.findViewById(R.id.ci_newskill_detail_item_pic);
		tvTime=(TextView)detailItem.findViewById(R.id.tv_newskill_detail_item_time);
		tvContent=(TextView)detailItem.findViewById(R.id.tv_newskill_detail_item_content);
		
		llComment=(LinearLayout)detailItem.findViewById(R.id.ll_newskill_comment_content);
//		cItem=new CommentItem(mContext);
//		for(int i=0;i<5;i++){
			cItem=new CommentItem(mContext);
//			llComment.addView(cItem,commentNum++);
//		}
		
		rlSend=(RelativeLayout)detailItem.findViewById(R.id.rl_send_comment);
		btnSend=(Button)findViewById(R.id.btn_newskill_send);
		etComment=(EditText)detailItem.findViewById(R.id.et_newskill_comment);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String commentStr=etComment.getText().toString();
				CommentItem commentItem=new CommentItem(mContext);
				commentItem.setComment("", "", commentStr);
				llComment.addView(commentItem,commentNum++);
			}
		});
		
		cItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				rlSend.setVisibility(VISIBLE);
			}
		});
	}
	/*
	 * 处理详细新技能的主体部分
	 */
	public void setSkillContent(Map<String,Object> map){
		String key="";
		String value="";
		for (Entry<String, Object> entry : map.entrySet()) {
			key=entry.getKey();
			value=entry.getValue().toString();
			if(key.equals("title")){
				tvTitle.setText(value);
			}else if(key.equals("image")){
				//TODO
				circularImg.setBackgroundResource(R.drawable.pic);
			}else if(key.equals("time")){
				tvTime.setText(value);
			}else{
				tvContent.setText(value);
			}
		}
		

	}
	
	public void setComment(List<Map<String, Object>> listmap) {
		llComment=(LinearLayout)detailItem.findViewById(R.id.ll_newskill_comment_content);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator it = listmap.iterator();
		while(it.hasNext()) {
			map = (Map<String, Object>)it.next();
			String who1 = map.get("username").toString();
			String commentStr = map.get("content").toString();
			cItem=new CommentItem(mContext);
			cItem.setComment(who1, "", commentStr);
			llComment.addView(cItem);
		}
	}
}
