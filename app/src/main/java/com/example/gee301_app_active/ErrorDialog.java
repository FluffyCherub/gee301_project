package com.example.gee301_app_active;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
//import androidx.support.v7.app.AlertDialog;
//import androidx.support.v7.app.AppCompatDialogFragment;


public class ErrorDialog extends AppCompatDialogFragment {

    String message = "Error occurred.";
    String type = "Null";
    public static ErrorDialog newInstance(String mess, String type) {
        ErrorDialog f = new ErrorDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("mess", mess);
        args.putString("type", type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getArguments().getString("mess");
        type = getArguments().getString("type");
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ALERT");
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type == "both") {
                    // change error status back to normal
                    updateParameters("fallen");
                    updateParameters("hr");
                } else if (type == "fallen") {
                    // change error status back to normal
                    updateParameters("fallen");
                } else if (type == "hr") {
                    // change error status back to normal
                    updateParameters("hr");
                }
            }
        });

        return builder.create();
    }
    public void updateParameters(String type){
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "Userdata", Context.MODE_PRIVATE);
        final String auth_id = prefs.getString("auth_id", "Null");
        final String auth_key = prefs.getString("auth_key", "Null");
        final String userid = prefs.getString("userid", "Null");
        final String usersecret = prefs.getString("usersecret", "Null");

        if (!"Null".equals(auth_id) && !"Null".equals(auth_key) && !"Null".equals(userid)){
            JSONObject putUserStatus = new JSONObject();
            try {
                // Assign values to data that will be sent
                putUserStatus.put("auth_id", auth_id);
                putUserStatus.put("auth_key", auth_key);
                putUserStatus.put("userid", userid);
                putUserStatus.put("usersecret", usersecret);

                JSONObject data = new JSONObject();
                if (type == "fallen") {
                    data.put(type, 0);
                }
                else if (type == "hr") {
                    data.put(type, "normal");
                }
                putUserStatus.put("data", data);

                final connect cS = new connect("https://medicap.auburnhr.com/user/status", "PATCH", putUserStatus.toString());
                final Thread cS_worker = new Thread(cS);
                new Thread(){
                    public void run(){
                        try{
                            cS_worker.start();
                            cS_worker.join();
                        }
                        catch (Exception e){
                            System.out.println("GENERAL EXCEPTION");
                            e.printStackTrace(System.out);
                        }
                        if(cS.status == true && cS.ResponseCode == 200){
                            try {
                                // get JSONObject from JSON file
                                String message = cS.ResponseMessage;
                                //String body = cS.ResponseBody.get(0);
                                System.out.println("Message Retrieved: "+message);
                                //System.out.println("Body Retrieved: "+body);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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
    }
}