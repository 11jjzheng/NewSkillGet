package cn.edu.stu.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.stu.newskillget.BegSkillDetail;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.CircularImage;

public class BegSkillDetailItem extends LinearLayout{
	private TextView tvTitle,tvTime,tvContent;
	private CircularImage circularImg;
	private BegSkillDetail mContext;
	private LinearLayout llComment;
	private CommentItem cItem;
	public BegSkillDetailItem(BegSkillDetail context) {
		super(context);
		mContext=context;
		initView();
	}

	public BegSkillDetailItem(BegSkillDetail context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	@SuppressLint("NewApi")
	public BegSkillDetailItem(BegSkillDetail context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		initView();
	}
	
	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View detailItem = View.inflate(mContext, R.layout.begskill_detail_item, null);
		detailItem.setLayoutParams(layoutParams);
		addView(detailItem);
		
		tvTitle=(TextView)detailItem.findViewById(R.id.tv_begskill_detail_item_title);
		circularImg=(CircularImage)detailItem.findViewById(R.id.ci_begskill_detail_item_pic);
		tvTime=(TextView)detailItem.findViewById(R.id.tv_begskill_detail_item_time);
		tvContent=(TextView)detailItem.findViewById(R.id.tv_begskill_detail_item_content);
		
//		llComment=(LinearLayout)findViewById(R.id.ll_begskill_comment_content);
//		cItem=new CommentItem(mContext);
//		llComment.addView(cItem);
	}
	
	/*
	 * 处理求技能详细页面的主体部分
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
		llComment=(LinearLayout)findViewById(R.id.ll_begskill_comment_content);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator it = listmap.iterator();
		while(it.hasNext()) {
			map = (Map<String, Object>)it.next();
			String who = map.get("username").toString();
			String commentstr = map.get("content").toString();
			cItem=new CommentItem(mContext);
			cItem.setComment(who, "", commentstr);
			llComment.addView(cItem);
		}
	}
}
