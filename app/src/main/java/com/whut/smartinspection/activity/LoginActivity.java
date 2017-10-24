package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.whut.smartinspection.R;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends Activity {
	
	@BindView(R.id.tv_login_user_login)
	Button tvUserLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}
	
	@OnClick(R.id.tv_login_user_login)
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		// 登录
		case R.id.tv_login_user_login:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
