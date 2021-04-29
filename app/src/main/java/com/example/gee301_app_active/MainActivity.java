package com.example.gee301_app_active;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer timer;

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkAlert();
        //openDialog("There is a yet unidentified problem.");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance("", ""));

        //loop to check alerts
        timer = new CountDownTimer(60000, 20) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                try{
                    checkAlert();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();



        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This doesn't do anything.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        /*
                        findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 NavHostFragment.findNavController(NotificationFragment.this)
                                         .navigate(R.id.action_SecondFragment_to_FirstFragment);
                             }
                        });*/

                        openFragment(HomeFragment.newInstance("", ""));
                        return true;
                    case R.id.navigation_sms:
                        openFragment(SmsFragment.newInstance("", ""));
                        return true;
                    case R.id.navigation_notifications:
                        openFragment(NotificationFragment.newInstance("", ""));
                        return true;
                }
                return false;
            }
        };

    public void openDialog(String error) {
        //ErrorDialog dialog = new ErrorDialog();
        ErrorDialog dialog = ErrorDialog.newInstance(error);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void checkAlert(){
        final Context context = this;
        SharedPreferences prefs = context.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");

        if (!"Null".equals(auth_id) && !"Null".equals(auth_key)){
            JSONObject postUserStatus = new JSONObject();
            try {
                // Assign values to data that will be sent
                postUserStatus.put("auth_id", auth_id);
                postUserStatus.put("auth_key", auth_key);
                postUserStatus.put("userid", userid);
                postUserStatus.put("usersecret", usersecret);
                postUserStatus.put("part", "");

                final connect cS = new connect("https://medicap.auburnhr.com/user/status", "POST", postUserStatus.toString());
                final Thread cS_worker = new Thread(cS);

                final View view = findViewById(R.id.activity_main);
                final Snackbar connecting = Snackbar.make(view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                new Thread(){
                    public void run(){
                        connecting.show();
                        try{
                            cS_worker.start();
                            cS_worker.join();
                        }
                        catch (Exception e){
                            System.out.println("GENERAL EXCEPTION");
                            e.printStackTrace(System.out);
                        }
                        connecting.dismiss();

                        if(cS.ResponseCode == 200){
                            System.out.println("YEET");
                        }

                        if(cS.status == true && cS.ResponseCode == 200){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // get JSONObject from JSON file
                                        String message = cS.ResponseMessage;
                                        String body = cS.ResponseBody.get(0);
                                        System.out.println("Message Retrieved: "+message);
                                        System.out.println("Body Retrieved: "+body);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                        else{
                            System.out.println("Hello Guest!!! Status and code:"+ cS.status + +cS.ResponseCode);
                        }

                    }
                }.start();


            } catch (Exception e) {
                System.out.println("GENERAL EXCEPTION");
                e.printStackTrace(System.out);
            }
        }

        timer.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_settings){
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }
        else if(id == R.id.activity_login){
            Intent login_intent = new Intent(this, login_activity.class);
            startActivity(login_intent);
            return true;
        }
        else if(id == R.id.action_logout){
            Intent logout_intent = new Intent(this, logout_activity.class);
            startActivity(logout_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
