package com.example.gee301_app_active;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class connectAlt implements Runnable {
    String[] params;
    int ResponseCode;
    String ResponseMessage;
    boolean status;

    @Override
    public void run() {
        executeHttpsRequest(params[0], params[1], params[2]);
    }


    connectAlt(String... params){
        this.params = params;
    }


    private void executeHttpsRequest(String URL, String METHOD, String Payload) {
        HttpsURLConnection con = null;
        try {
            java.net.URL url = new URL(URL);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(METHOD);
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("User-Agent", "PrivateConnection");

            /*
            JSONObject auth_req = new JSONObject();
            auth_req.put("auth_id", "main");
            auth_req.put("auth_key", "bigsecret");
            auth_req.put("userid", "G-HdhqYGX7OQIL8hD2F2");
            auth_req.put("usersecret", "sUHaG0aaA3aXIiv2KxDBVWp51");
            auth_req.put("part", "");
            con.setRequestProperty("auth_req", auth_req.toString());*/


            con.setRequestProperty("auth_id","main");
            con.setRequestProperty("auth_key","bigsecret");
            con.setRequestProperty("userid","G-HdhqYGX7OQIL8hD2F2");
            con.setRequestProperty("usersecret","sUHaG0aaA3aXIiv2KxDBVWp51");
            con.setRequestProperty("part","");

            //  "auth_id": "main",
            //    "auth_key": "bigsecret",
            //    "userid":"G-HdhqYGX7OQIL8hD2F2",
            //    "usersecret":"sUHaG0aaA3aXIiv2KxDBVWp51"

            con.setDoInput(true);
            con.setDoOutput(true);

            System.out.println(Payload);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());

            output.writeBytes(Payload);

            output.close();

            DataInputStream input = new DataInputStream(con.getInputStream());

            this.ResponseCode =  con.getResponseCode();
            this.ResponseMessage = con.getResponseMessage();
            this.status = true;
            con.disconnect();

        }
        catch (MalformedURLException e){
            this.status = false;
        }
        catch (IOException e) {
            this.status = false;
            System.out.println("IOException: " + e.getMessage());

            try {
                System.out.println("Resp Code:"+con.getResponseCode());

            } catch (IOException ex) {
                this.status = false;
                ex.printStackTrace();
            }
            con.disconnect();
        }


    }

}
