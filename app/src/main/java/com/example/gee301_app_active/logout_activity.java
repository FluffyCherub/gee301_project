package com.example.gee301_app_active;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;


public class logout_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_ui);

        final Context context = this;
        SharedPreferences prefs = context.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");

        final TextView login_status = findViewById(R.id.login_status);
        Button button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(!"Null".equals(auth_id) && !"Null".equals(auth_key) && !"Null".equals(userid) && !"Null".equals(usersecret)) {
                    Context context = logout_activity.this;
                    SharedPreferences prefs = context.getSharedPreferences(
                            "Userdata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("auth_id", "Null");
                    editor.putString("auth_key", "Null");
                    editor.putString("userid", "Null");
                    editor.putString("usersecret", "Null");
                    editor.apply();
                    login_status.setText("You have been logged out.");
                    System.out.println("User has been logged out");
                }
            }
        });

    }
}
