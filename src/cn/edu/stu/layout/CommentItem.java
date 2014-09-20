package cn.edu.stu.layout;

import cn.edu.stu.newskillget.R;
import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentItem extends LinearLayout{
	private Activity mContext;
	private TextView tvComment;
	public CommentItem(Activity context) {
		super(context);
		mContext=context;
		initView();
	}

	public CommentItem(Activity context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View detailItem = View.inflate(mContext, R.layout.comment_item, null);
		detailItem.setLayoutParams(layoutParams);
		addView(detailItem);
		
		tvComment=(TextView)findViewById(R.id.tv_comment);
	}
	public void setComment(String who1,String who2,String comment){
		if(who2.equals("")){
			tvComment.setText(who1+" : "+comment);
		}else{
			tvComment.setText(who1+"»Ø¸´"+who2+":"+comment);
		}
	}
}
