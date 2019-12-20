package com.example.kursovaya22;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SplashActivity extends Activity {
    public SharedPreferences sPref;
    public static final String APP_PREFERENCES = "Authentication";
    public static final String APP_PREFERENCES_KEY = "key";
    int startWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean NeedSignIn = true;
        String userKey = null;
        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        if(sPref.contains(APP_PREFERENCES_KEY)){
            NeedSignIn = false;
            userKey = sPref.getString(APP_PREFERENCES_KEY,"");
        }

        if(NeedSignIn){
            Intent MainActivity = new Intent(this, MainActivity.class);
            startActivity(MainActivity);
        }else{
            new RequestAsync().execute(userKey);
        }

    }

    public void showMainActivity(){
        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.apply();

        Intent MainActivity = new Intent(this, MainActivity.class);
        startActivity(MainActivity);
    }

    public void showDataActivity(String data, Integer week){
        Intent DataActivity = new Intent(this, DataActivity.class);
        DataActivity.putExtra("data", data);
        DataActivity.putExtra("week", week);
        startActivity(DataActivity);
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        String userGroupIdTime = null;
        String userDataTime = null;
        String userScheduleTime = null;

        @Override
        protected String doInBackground(String... userKey) {
            try {
                JSONObject GetAccountInfoApi = new JSONObject();
                GetAccountInfoApi.put("GetAccountInfoApi","Android");
                GetAccountInfoApi.put("api", "5ddaeefea78230e2134be1f0263490bd6caed86f");
                GetAccountInfoApi.put("key", userKey[0]);

                userDataTime = RequestHandler.sendPost("https://iti-khsu.ru/api", GetAccountInfoApi);
                if (userDataTime.equals("")){
                    return "Key invalid";
                }

                JSONObject obj = new JSONObject(userDataTime);
                userGroupIdTime = obj.getString("group");

                JSONObject GetSchedule  = new JSONObject();
                GetSchedule.put("GetSchedule","Android");
                Calendar c1 = new GregorianCalendar();
                startWeek = c1.get(Calendar.WEEK_OF_YEAR) % 2;
                GetSchedule.put("week", startWeek + 1);
                GetSchedule.put("group", userGroupIdTime);

                userScheduleTime = RequestHandler.sendPost("https://iti-khsu.ru/api", GetSchedule);

                return userScheduleTime;
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Key invalid")){
                showMainActivity();
            }else{
                String[] exc = s.split(" ");
                if(exc[0].equals("Exception:")) {
                    Toast.makeText(getApplicationContext(),"Нет подключения к интернету", Toast.LENGTH_LONG).show();
                }else{
                    showDataActivity(s, startWeek);
                }
            }
        }
    }
}
