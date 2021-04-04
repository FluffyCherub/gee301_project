package com.example.gee301_app_active;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

public class connect implements Runnable {
    String[] params;
    int ResponseCode;
    String ResponseMessage;
    boolean status;

    @Override
    public void run() {
        executeHttpsRequest(params[0], params[1], params[2]);
    }


    connect(String... params){
        this.params = params;
    }


    private void executeHttpsRequest(String URL, String METHOD, String Payload) {
        HttpsURLConnection con = null;
        try {
            java.net.URL url = new java.net.URL(URL);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod(METHOD);
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("User-Agent", "PrivateConnection");
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
