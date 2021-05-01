package com.example.gee301_app_active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
import org.json.*;

import java.util.Hashtable;

import androidx.fragment.app.Fragment;
//import com.bottomnavigationview.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
  //  Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public HomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static HomeFragment newInstance(String param1, String param2) {
    HomeFragment fragment = new HomeFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    //return inflater.inflate(R.layout.fragment_home, container, false);
    View view =  inflater.inflate(R.layout.fragment_home, container, false);
    TextView first = (TextView) view.findViewById(R.id.textview_first);
    System.out.println("Start Test for Home Fragment");
    return view;
  }

  public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final Context context = getActivity();
    SharedPreferences prefs = context.getSharedPreferences(
            "Userdata", Context.MODE_PRIVATE);
    final String auth_id = prefs.getString("auth_id", "Null");
    final String auth_key = prefs.getString("auth_key", "Null");
    final String userid = prefs.getString("userid", "Null");
    final String usersecret = prefs.getString("usersecret", "Null");

    //UserDetails ud = new UserDetails(auth_id,auth_key,userid,usersecret);
    //System.out.println("First Name: "+ud.toString());

    if (!"Null".equals(auth_id) && !"Null".equals(auth_key)){
      JSONObject postUserInfo = new JSONObject();
      try {
        // Assign values to data that will be sent
        postUserInfo.put("auth_id", auth_id);
        postUserInfo.put("auth_key", auth_key);
        postUserInfo.put("userid", userid);
        postUserInfo.put("usersecret", usersecret);

        //{"auth_id":"main","auth_key":"bigsecret","userid":"G-HdhqYGX7OQIL8hD2F2","usersecret":"sUHaG0aaA3aXIiv2KxDBCWp51"}
        /*{"auth_id":"main","auth_key":"bigsecret","userid":"G-HdhqYGX7OQIL8hD2F2","usersecret":"sUHaG0aaA3aXIiv2KxDBVWp51"}
        postUserInfo.put("auth_id", "main");
        postUserInfo.put("auth_key", "bigsecret");
        postUserInfo.put("userid", "G-HdhqYGX7OQIL8hD2F2"); // If44ITk5TGdJ52YO6POT //G-HdhqYGX7OQIL8hD2F2
        postUserInfo.put("usersecret", "sUHaG0aaA3aXIiv2KxDBVWp51"); //postData.toString()
        //postData.put("auth_req", auth_req);*/

        postUserInfo.put("part", "");

        final connect cS = new connect("https://medicap.auburnhr.com/user/info", "POST", postUserInfo.toString());
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
                    String name = obj.getString("name");
                    //String lastname = obj.getString("lastname");
                    // set name in TextView's
                    first.setText("Hello, "+name+".");
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

      // -------------------------------------------------    Retrieving data     ----------------------------------------------------------- //
      // Getting data for user
      JSONObject postData = new JSONObject();
      try {
        // Assign values to data that will be sent
        postData.put("auth_id", auth_id);
        postData.put("auth_key", auth_key);
        postData.put("userid", userid);
        postData.put("usersecret", usersecret);

        final connect cS = new connect("https://medicap.auburnhr.com/data", "POST", postData.toString());
        final Thread cS_worker = new Thread(cS);

        final Snackbar connecting = Snackbar.make(view, "Connecting... ", Snackbar.LENGTH_LONG).setAction("Action", null);
        new Thread(){
          public void run(){
            final TextView first = (TextView) view.findViewById(R.id.textview_first);
            //final GraphView graph = (GraphView) view.findViewById(R.id.graph);
            /*LineGraphSeries <DataPoint> series = new LineGraphSeries<>();
            graph.addSeries(series); */

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

            if(cS.status == true && cS.ResponseCode == 200){

              try {
                Hashtable<String, String> temperature = new Hashtable<String, String>();

                /*
                Hashtable<String, String> pressure = new Hashtable<String, String>();
                Hashtable<String, String> humidity = new Hashtable<String, String>();
                Hashtable<String, String> oxidised = new Hashtable<String, String>();
                Hashtable<String, String> reduced = new Hashtable<String, String>();
                Hashtable<String, String> nh3 = new Hashtable<String, String>();
                Hashtable<String, String> heartRate = new Hashtable<String, String>();
                Hashtable<String, String> movement = new Hashtable<String, String>();*/

                //System.out.println("Data Retrieved!");
                //System.out.println("Data: "+cS.ResponseBody);
                JSONObject obj = new JSONObject(cS.ResponseBody.get(0));
                JSONArray dataArray = obj.getJSONArray("result");
                for (int i = 0; i < dataArray.length(); i++) {
                  JSONObject resultData = dataArray.getJSONObject(i);
                  //System.out.println("Data: "+resultData);
                  JSONObject data = resultData.getJSONObject("data");

                  String date = resultData.getString("date_added");
                  System.out.println("Data Collected: "+data+"Time: "+date);
                  try {
                    String temp = data.getString("Temperature");
                    temperature.put(date, temp);
                    /*String pres = data.getString("Pressure");
                    pressure.put(date, pres);
                    String humi = data.getString("Humidity");
                    humidity.put(date, humi);
                    String oxid = data.getString("Oxidised");
                    oxidised.put(date, oxid);
                    String redu = data.getString("Reduced");
                    reduced.put(date, redu);
                    String nh = data.getString("NH3");
                    nh3.put(date, nh);
                    String hr = data.getString("HR");
                    String hr2 = data.getString("heartRate");
                    heartRate.put(date, hr);
                    heartRate.put(date, hr2);
                    String move = data.getString("movement");
                    movement.put(date, move);*/

                    //System.out.println(temperature);
                    //System.out.println("Temperature: "+temp+". Date: "+date);

                    try {
                      /*
                      graph.setVisibility(View.VISIBLE);
                      LineGraphSeries <DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                              new DataPoint(1, 1),
                              new DataPoint(12, 6)
                      });
                      graph.addSeries(series);

                      series.resetData(new DataPoint[] {new DataPoint(0, 0), new DataPoint(5, 5)});*/
                    } catch (IllegalArgumentException e) {
                      Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    catch (NullPointerException e) {
                      // something to handle the NPE.
                    }
                  } catch (JSONException e) {
                    //e.printStackTrace();
                  }
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
                            /*
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }); */

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
