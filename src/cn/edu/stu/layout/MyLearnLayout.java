package cn.edu.stu.layout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.CircularImage;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyLearnLayout extends LinearLayout {
	private FragmentActivity mContext;
	public CircularImage ciPic;
	private TextView tvNickname, tvSkillNum, tvBegSkillNum, tvBeSupport;
	private LinearLayout llMyLearnContent;
	private MyLearnItem myLearnItem;
	public Bitmap myBitmap;
	public byte[] mContent;// 图片内容

	public MyLearnLayout(FragmentActivity context) {
		super(context);
		mContext = context;
		initView();
	}

	public MyLearnLayout(FragmentActivity context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	@SuppressLint("NewApi")
	public MyLearnLayout(FragmentActivity context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	private void initView() {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
		View vMyLearn = View
				.inflate(mContext, R.layout.personal_my_learn, null);
		vMyLearn.setLayoutParams(layoutParams);
		addView(vMyLearn);

		ciPic = (CircularImage) vMyLearn
				.findViewById(R.id.ci_personal_mylearn_pic);
		tvNickname = (TextView) vMyLearn.findViewById(R.id.tv_mylearn_nickname);
		tvSkillNum = (TextView) vMyLearn
				.findViewById(R.id.tv_mylearn_skill_num);
		tvBegSkillNum = (TextView) vMyLearn
				.findViewById(R.id.tv_mylearn_begskill_num);
		tvBeSupport = (TextView) vMyLearn
				.findViewById(R.id.tv_mylearn_besupport);
		llMyLearnContent = (LinearLayout) vMyLearn
				.findViewById(R.id.ll_mylearn_content);

		ciPic.setOnClickListener(new onChangePicListener());
	}

	public void setMyLearn(int uid, JSONArray jsonArray) {
		llMyLearnContent.removeAllViews();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = jsonArray.getJSONObject(i);
				myLearnItem = new MyLearnItem(mContext);
				myLearnItem.setData(uid, jsonObject.getInt("shid"),
						jsonObject.getInt("seid"),
						jsonObject.getInt("progress"),
						jsonObject.getString("title"));
				llMyLearnContent.addView(myLearnItem, i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * 设置个人信息
	 */
	public void setPersonalInfo(Map<String, Object> map) {
		String key = "";
		String value = "";
		for (Entry<String, Object> entry : map.entrySet()) {
			key = entry.getKey();
			value = entry.getValue().toString();
			if (key.equals("pic")) {
				// TODO 改用户头像
				ciPic.setBackgroundResource(R.drawable.pic);
			} else if (key.equals("nickname")) {
				tvNickname.setText(value);
			} else if (key.equals("skillnum")) {
				tvSkillNum.setText(value);
			} else if (key.equals("begskillnum")) {
				tvBegSkillNum.setText(value);
			} else {
				tvBeSupport.setText(value);
			}
		}
	}

	class onChangePicListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					mContext);
			builder.setTitle("选择照片");

			builder.setPositiveButton("相机",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface

						dialog, int which) {
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							mContext.startActivityForResult(intent, 0);

						}
					});
			builder.setNegativeButton("相册",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface

						dialog, int which) {
							Intent intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							mContext.startActivityForResult(intent, 1);

						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}
