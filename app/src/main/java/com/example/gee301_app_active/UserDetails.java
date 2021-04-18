package com.example.gee301_app_active;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class UserDetails implements Runnable {
    //Context context;
    String age;
    String firstname;
    String lastname;
    String[] params;
    String out;

    @Override
    public void run() {
        returnUserDetails(params[0], params[1], params[2], params[3]);
        System.out.println("why?");
    }

    UserDetails(String... params) {
        //this.context = context;
        this.params = params;
    }

    private String returnUserDetails(String auth_id, String auth_key, String userid, String usersecret) {
        /*
        SharedPreferences prefs = context.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");*/
        Log.i("Audio", "Running Audio Thread");

        System.out.println("food");
        System.out.println("Stuff:"+auth_id+auth_key+userid+usersecret);

        if (!"Null".equals(auth_id) && !"Null".equals(auth_key)) {
            JSONObject postUserInfo = new JSONObject();
            try {
                // Assign values to data that will be sent
                postUserInfo.put("auth_id", auth_id);
                postUserInfo.put("auth_key", auth_key);
                postUserInfo.put("userid", userid); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
                postUserInfo.put("usersecret", usersecret);

                postUserInfo.put("part", "");

                final connect cS = new connect("https://medicap.auburnhr.com/user/info", "POST", postUserInfo.toString());
                final Thread cS_worker = new Thread(cS);

                //final Snackbar connecting = Snackbar.make(view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                //new Thread() {

                    //public void run() {
                        //final TextView first = (TextView) view.findViewById(R.id.textview_first);
                        //connecting.show();
                        try {
                            cS_worker.start();
                            cS_worker.join();
                        } catch (Exception e) {
                            System.out.println("GENERAL EXCEPTION");
                            e.printStackTrace(System.out);
                        }
                        //connecting.dismiss();


                        if (cS.status == true && cS.ResponseCode == 200) {
                            try {
                                // get JSONObject from JSON file

                                String body = cS.ResponseBody.get(0);
                                out = "Body test"+body;
                                JSONObject obj = new JSONObject(body);
                                // get names
                                String first = obj.getString("firstname");
                                String last = obj.getString("lastname");
                                String a = obj.getString("age");
                                this.firstname = first;
                                this.lastname = last;
                                this.age = a;
                                System.out.println("User Info Retrieved!");
                                return firstname;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Hello Guest!!! Status and code:" + cS.status + +cS.ResponseCode);
                            return "Nope";
                        }

                    /*}
                }.start();*/


            } catch (Exception e) {
                System.out.println("GENERAL EXCEPTION");
                e.printStackTrace(System.out);
                return "Nope";
            }
        }
        return "Nope";
    }
}