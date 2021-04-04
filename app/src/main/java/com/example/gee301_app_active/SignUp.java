package com.example.gee301_app_active;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button button = findViewById(R.id.button_signup);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                JSONObject usersignup_key = new JSONObject();

                mTextView = (TextView) findViewById(R.id.Greeting);
                mTextView.setText("Sign up page!!");

                // Generating user key
                JSONObject postData = new JSONObject();
                JSONObject userData = new JSONObject();
                try {
                    userData.put("admin", false);
                    postData.put("addNum", 1);
                    postData.put("userdata", userData);

                    final connect cS = new connect("https://medicap.auburnhr.com/user/add", "POST", postData.toString());
                    final Thread cS_worker = new Thread(cS);

                    final View login_view = findViewById(R.id.login_ui);
                    final Snackbar connecting = Snackbar.make(login_view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                    new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    connecting.show();
                                }
                            });
                            try {
                                cS_worker.start();
                                cS_worker.join();
                            } catch (Exception e) {
                                System.out.println("GENERAL EXCEPTION");
                                e.printStackTrace(System.out);
                            }
                            connecting.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (cS.status == true && cS.ResponseCode == 200) {
                                        mTextView = (TextView) findViewById(R.id.Greeting);
                                        String usersignup_key = cS.ResponseMessage;
                                        mTextView.setText("New usersignup_key created:" + usersignup_key);
                                    } else {
                                        mTextView = (TextView) findViewById(R.id.Greeting);
                                        mTextView.setText("Usersignup_key creation Failed");
                                    }
                                }
                            });
                        }
                    }.start();


                } catch (Exception e) {
                    System.out.println("GENERAL EXCEPTION");
                    e.printStackTrace(System.out);
                }

                // Gain values for the user registration
                TextView editName = findViewById(R.id.editName);
                String name = editName.getText().toString();
                try {
                    //packing user data
                    userData.put("name", name);

                    //packaging up for being posted
                    postData.put("usersignup_key", usersignup_key);
                    postData.put("userdata", userData);

                    final connect cS = new connect("https://medicap.auburnhr.com/user/register", "POST", postData.toString());
                    final Thread cS_worker = new Thread(cS);

                    final View login_view = findViewById(R.id.login_ui);
                    final Snackbar connecting = Snackbar.make(login_view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                    new Thread() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    connecting.show();
                                }
                            });
                            try {
                                cS_worker.start();
                                cS_worker.join();
                            } catch (Exception e) {
                                System.out.println("GENERAL EXCEPTION");
                                e.printStackTrace(System.out);
                            }
                            connecting.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (cS.status == true && cS.ResponseCode == 200) {
                                        mTextView = (TextView) findViewById(R.id.Greeting);
                                        mTextView.setText("Account created!");
                                    } else {
                                        mTextView = (TextView) findViewById(R.id.Greeting);
                                        mTextView.setText("Account creation Failed.");
                                    }
                                }
                            });
                        }
                    }.start();


                } catch (Exception e) {
                    System.out.println("GENERAL EXCEPTION");
                    e.printStackTrace(System.out);
                }
            }
        });
    }
}