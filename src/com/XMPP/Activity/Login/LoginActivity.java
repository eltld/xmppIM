package com.XMPP.Activity.Login;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.XMPP.R;
import com.XMPP.Activity.Mainview.MainviewActivity;
import com.XMPP.Service.ContactsService;
import com.XMPP.Service.MessageService;
import com.XMPP.Service.ReconnectService;
import com.XMPP.smack.ConnectionHandler;
import com.XMPP.smack.Smack;
import com.XMPP.smack.SmackImpl;
import com.XMPP.util.Constants;
import com.XMPP.util.L;
import com.XMPP.util.LoadingDialog;
import com.XMPP.util.T;

public class LoginActivity extends FragmentActivity implements OnClickListener {

	private String username;
	private String password;
	private TextView submitLogin;
	private TextView forget;
	private Smack smack;
	private Timer mTimer;
	private LoadingDialog loading;
	private MTimerTask mTask;
	private final static Long DELAY_CONNECT = 10 * 1000L;  // n second;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init();
	}

	public void init() {
		smack = SmackImpl.getInstance();
		submitLogin = (TextView) findViewById(R.id.submitLogin);
		forget = (TextView) findViewById(R.id.forget);
		submitLogin.setOnClickListener(this);
	}

	public void startAllServices() {
		Intent contacts_intent = new Intent(this, ContactsService.class);
		this.startService(contacts_intent);

		Intent message_intent = new Intent(this, MessageService.class);
		this.startService(message_intent);

		Intent reconnect_intent = new Intent(this, ReconnectService.class);
		this.startService(reconnect_intent);

	}

	class MTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handleProgress.sendEmptyMessage(0);
		}

	}

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			loading.dismiss();
			mTask = null;
			T.mToast(LoginActivity.this, "Internet doesn't  works");
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitLogin:
			username = ((EditText) findViewById(R.id.username)).getText()
					.toString();
			password = ((EditText) findViewById(R.id.password)).getText()
					.toString();
			loading = new LoadingDialog(LoginActivity.this);
			loading.show(LoginActivity.this.getSupportFragmentManager(), "tag");
			loading.setCancelable(false);

			if (mTask != null) {
				mTask.cancel();
				mTask = null;

			}
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			mTimer = new Timer();
			mTask = new MTimerTask();
			mTimer.schedule(mTask, DELAY_CONNECT);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// this thread isn't suit to placed here ,it should be
					// placed in the login onclick

					smack.connect(Constants.SERVER_IP, Constants.SERVER_PORT);

					final int login_result = smack.login(username, password);
					L.i("user if authenticated "
							+ ConnectionHandler.getConnection()
									.isAuthenticated());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							switch (login_result) {
							case Constants.LOGIN_SUCCESS:
								loading.dismiss();
								startAllServices();
								Intent intent = new Intent(LoginActivity.this,
										MainviewActivity.class);
								LoginActivity.this.startActivity(intent);
								break;
							case Constants.LOGIN_CONNECT_FAIL:
								loading.dismiss();

								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.connect_fail),
										Toast.LENGTH_SHORT).show();
								break;
							case Constants.LOGIN_USERNAME_PSW_ERROR:
								loading.dismiss();

								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.name_psw_wrong),
										Toast.LENGTH_SHORT).show();
							}

						}
					});

				}
			}).start();
			break;

		case R.id.forget:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

}