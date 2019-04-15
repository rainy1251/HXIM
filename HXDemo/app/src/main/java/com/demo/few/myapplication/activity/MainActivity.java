package com.demo.few.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.few.myapplication.DemoHelper;
import com.demo.few.myapplication.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class MainActivity extends AppCompatActivity {

    private Button btn_signin;
    private Button btn_signup;
    private Button btn_logout;
    private Button btn_chatlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_signin = findViewById(R.id.btn_signin);
        btn_signup = findViewById(R.id.btn_signup);
        btn_logout = findViewById(R.id.btn_logout);
        btn_chatlist = findViewById(R.id.btn_chatlist);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().createAccount("test2", "11few1");//同步方法
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (!MainActivity.this.isFinishing())

                                                // save current user
                                                DemoHelper.getInstance().setCurrentUserName("test2");
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    Log.d("main", "登录聊天服务器成功！" + e.getErrorCode() + "," + e.getMessage());
                                }
                            }
                        }
                ).start();

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().login("test2", "111", new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！" + message + "," + code);
                    }
                });
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DemoHelper.getInstance().logout(false, null);
            }
        });
        btn_chatlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);

            }
        });
    }
}
