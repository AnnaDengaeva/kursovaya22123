package com.example.kursovaya22;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences sPref;
    public static final String APP_PREFERENCES = "Authentication";
    public static final String APP_PREFERENCES_KEY = "key";

    public Boolean NeedSignIn = true;
    public String userKey;
    int startWeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        if(sPref.contains(APP_PREFERENCES_KEY)){
            NeedSignIn = false;
            userKey = sPref.getString(APP_PREFERENCES_KEY,"");
            new RequestAsync().execute(NeedSignIn);
        }
    }

    public void showDataActivity(String data, Integer week){
        Intent DataActivity = new Intent(this, DataActivity.class);
        DataActivity.putExtra("data", data);
        DataActivity.putExtra("week", week);
        startActivity(DataActivity);
    }

    public void onClickSingIn(View view){

        new RequestAsync().execute(NeedSignIn);
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public class RequestAsync extends AsyncTask<Boolean,String,String> {
        String userKeyTime = null;
        String userGroupIdTime = null;
        String userDataTime = null;
        String userScheduleTime = null;
        @Override
        protected String doInBackground(Boolean... bool) {
            try {
                if(bool[0]){
                    EditText LoginBox = findViewById(R.id.LoginBox);
                    EditText PasswordBox = findViewById(R.id.PasswordBox);
                    String login = LoginBox.getText().toString();
                    String pass = PasswordBox.getText().toString();

                    JSONObject AccountLogin = new JSONObject();
                    AccountLogin.put("AccountLogin","Android");
                    AccountLogin.put("login", login);
                    AccountLogin.put("password",pass);
                    AccountLogin.put("api", "5ddaeefea78230e2134be1f0263490bd6caed86f");

                    userKeyTime = RequestHandler.sendPost("https://iti-khsu.ru/api", AccountLogin);
                }else{
                    userKeyTime = userKey;
                }

                if(!userKeyTime.equals("")){
                    JSONObject GetAccountInfoApi = new JSONObject();
                    GetAccountInfoApi.put("GetAccountInfoApi","Android");
                    GetAccountInfoApi.put("api", "5ddaeefea78230e2134be1f0263490bd6caed86f");
                    GetAccountInfoApi.put("key", userKeyTime);

                    userDataTime = RequestHandler.sendPost("https://iti-khsu.ru/api", GetAccountInfoApi);
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
                }else{
                    return "Authentication error";
                }

            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            String[] exc = s.split(" ");
            if(s.equals("Authentication error")){
                Toast.makeText(getApplicationContext(),"Неверный логин или пароль", Toast.LENGTH_LONG).show();
            } else if(exc[0].equals("Exception:")) {
                Toast.makeText(getApplicationContext(),"Нет подключения к интернету", Toast.LENGTH_LONG).show();
            }else{
                sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(APP_PREFERENCES_KEY, userKeyTime);
                ed.apply();
                showDataActivity(s, startWeek);
            }
        }
    }
}