package com.example.gee301_app_active;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;


public class login_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        Button button = findViewById(R.id.login_done);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                TextView auth_id_view = findViewById(R.id.auth_id);
                TextView auth_secret_view = findViewById(R.id.auth_secret);

                String auth_id = auth_id_view.getText().toString();
                String auth_secret = auth_secret_view.getText().toString();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("auth_id", auth_id);
                    postData.put("auth_key", auth_secret);

                    final connect cS = new connect("https://medicap.auburnhr.com/validatelogin", "POST", postData.toString());
                    final Thread cS_worker = new Thread(cS);

                    final View login_view = findViewById(R.id.login_ui);
                    final Snackbar connecting = Snackbar.make(login_view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                    new Thread(){
                        public void run(){
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  connecting.show();
                              }
                          });
                          try{
                              cS_worker.start();
                              cS_worker.join();
                          }
                          catch (Exception e){
                              System.out.println("GENERAL EXCEPTION");
                              e.printStackTrace(System.out);
                          }
                          connecting.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(cS.status == true && cS.ResponseCode == 200){
                                        Snackbar.make(login_view, "Success!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        TextView login_status = (TextView) findViewById(R.id.login_status);

                                        login_status.setText("Login Succeeded");

                                    }
                                    else{
                                        Snackbar.make(login_view, "Failed!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        TextView login_status = (TextView) findViewById(R.id.login_status);
                                        login_status.setText("Login Failed");
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
