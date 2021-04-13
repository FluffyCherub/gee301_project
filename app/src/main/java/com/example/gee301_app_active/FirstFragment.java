package com.example.gee301_app_active;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.json.simple.parser.*;
// import org.json.simple.*;
import org.json.*;

public class FirstFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        TextView first = (TextView) view.findViewById(R.id.textview_first);
        System.out.println("Start Test");
        return view;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        Context context = getActivity();
        SharedPreferences prefs = context.getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");


        if (!"Null".equals(auth_id) && !"Null".equals(auth_key)){
            JSONObject postData = new JSONObject();
            try {
                // Assign values to data that will be sent
                postData.put("auth_id", auth_id);
                postData.put("auth_key", auth_key);
                postData.put("userid", userid); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
                postData.put("usersecret", usersecret);

                /*
                {"auth_id":"main","auth_key":"bigsecret","userid":"G-HdhqYGX7OQIL8hD2F2","usersecret":"sYHaG0aaA3aXIiv2KxDBVWp51","part":""}
                postData.put("auth_id", "main");
                postData.put("auth_key", "bigsecret");
                postData.put("userid", "G-HdhqYGX7OQIL8hD2F2"); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
                postData.put("usersecret", "sUHaG0aaA3aXIiv2KxDBVWp51"); //postData.toString()
                //postData.put("auth_req", auth_req);*/

                postData.put("part", "");

                //String message = "{\"auth_id\":\"main\",\"auth_key\":\"bigsecret\",\"userid\":\"G-HdhqYGX7OQIL8hD2F2\",\"usersecret\":\"sUHaG0aaA3aXIiv2KxDBVWp51\",\"part\":\"\" }";

                final connect cS = new connect("https://medicap.auburnhr.com/user/info", "POST", postData.toString());
                final Thread cS_worker = new Thread(cS);

                final Snackbar connecting = Snackbar.make(view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
                new Thread(){
                    public void run(){
                        final TextView first = (TextView) view.findViewById(R.id.textview_first);
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        // get JSONObject from JSON file

                                        String body = cS.ResponseBody.get(0);
                                        JSONObject obj = new JSONObject(body);
                                        // get names
                                        String firstname = obj.getString("firstname");
                                        String lastname = obj.getString("lastname");
                                        // set name in TextView's
                                        first.setText("Hello, "+firstname+ " "+lastname+".");
                                        System.out.println("Data Retrieved!");
                                        System.out.println("Data: "+cS.ResponseBody);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                        else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Stuff that updates the UI
                                    first.setText("Hello Guest!!!");
                                }
                            });
                            System.out.println("Hello Guest!!! Status and code:"+ cS.status + +cS.ResponseCode);
                        }

                    }
                }.start();


            } catch (Exception e) {
                System.out.println("GENERAL EXCEPTION");
                e.printStackTrace(System.out);
            }

        }
    }
}
