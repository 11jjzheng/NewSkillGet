package cn.edu.stu.layout;

import cn.edu.stu.newskillget.NewSkillPublish;
import cn.edu.stu.newskillget.R;
import android.annotation.SuppressLint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NewSkillStepItem extends LinearLayout{
	private NewSkillPublish mContext;
	private EditText etStepItem;
	public NewSkillStepItem(NewSkillPublish context) {
		super(context);
		mContext=context;
		initView();
	}

	public NewSkillStepItem(NewSkillPublish context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}

	@SuppressLint("NewApi")
	public NewSkillStepItem(NewSkillPublish context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		initView();
	}

	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View vStep = View.inflate(mContext, R.layout.newskill_publish_step_item, null);
		vStep.setLayoutParams(layoutParams);
		addView(vStep);
		
		etStepItem=(EditText)findViewById(R.id.et_newskill_publish_step_item);
		
	}
	
	public void setHint(int stepId){
		etStepItem.setHint("≤Ω÷Ë"+stepId);
	}
	
	public String getEditTextStr(){
		return etStepItem.getText().toString();
	}
}
