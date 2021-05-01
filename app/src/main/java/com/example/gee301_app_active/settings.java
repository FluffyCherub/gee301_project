package com.example.gee301_app_active;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        /*
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_ui, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        //Context context = getActivity();
        SharedPreferences prefs = this.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");

        //UserDetails ud = new UserDetails(auth_id,auth_key,userid,usersecret);
        //System.out.println("First Name: "+ud.toString());

        if (!"Null".equals(auth_id) && !"Null".equals(auth_key)) {
            System.out.println("runs in if statement");
            JSONObject postUserInfo = new JSONObject();
            try {
                System.out.println("runs in try");
                // Assign values to data that will be sent
                postUserInfo.put("auth_id", auth_id);
                postUserInfo.put("auth_key", auth_key);
                postUserInfo.put("userid", userid); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
                postUserInfo.put("usersecret", usersecret);

                /*{"auth_id":"main","auth_key":"bigsecret","userid":"G-HdhqYGX7OQIL8hD2F2","usersecret":"sYHaG0aaA3aXIiv2KxDBVWp51","part":""}
                postData.put("auth_id", "main");
                postData.put("auth_key", "bigsecret");
                postData.put("userid", "G-HdhqYGX7OQIL8hD2F2"); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
                postData.put("usersecret", "sUHaG0aaA3aXIiv2KxDBVWp51"); //postData.toString()
                //postData.put("auth_req", auth_req);*/

                postUserInfo.put("part", "");

                final connect cS = new connect("https://medicap.auburnhr.com/user/info", "POST", postUserInfo.toString());
                final Thread cS_worker = new Thread(cS);
                final View setting_view = findViewById(R.id.settings_ui);
                final Snackbar connecting = Snackbar.make(setting_view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                new Thread() {
                    public void run() {
                        System.out.println("runs in thread run");
                        final TextView first = (TextView) setting_view.findViewById(R.id.user_setting);
                        connecting.show();
                        try {
                            cS_worker.start();
                            cS_worker.join();
                        } catch (Exception e) {
                            System.out.println("GENERAL EXCEPTION");
                            e.printStackTrace(System.out);
                        }
                        connecting.dismiss();

                        if (cS.ResponseCode == 200) {
                            System.out.println("YEET");
                        }

                        if (cS.status == true && cS.ResponseCode == 200) {
                            settings.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("runs in second try");
                                        // get JSONObject from JSON file
                                        String body = cS.ResponseBody.get(0);
                                        JSONObject obj = new JSONObject(body);
                                        // get names
                                        String name = obj.getString("name");
                                        //String lastname = obj.getString("lastname");
                                        // set name in TextView's
                                        first.setText("Hello, " + name);
                                        System.out.println("Data Retrieved!");
                                        System.out.println("Data: " + cS.ResponseBody);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        } else {
                            settings.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Stuff that updates the UI
                                    first.setText("Hello Guest!!!");
                                }
                            });
                            System.out.println("Hello Guest!!! Status and code:" + cS.status + +cS.ResponseCode);
                        }

                    }
                }.start();


            } catch (Exception e) {
                System.out.println("GENERAL EXCEPTION");
                e.printStackTrace(System.out);
            }
        }
        }
        /*
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }*/
}