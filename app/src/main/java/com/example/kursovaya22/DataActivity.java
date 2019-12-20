package com.example.kursovaya22;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kursovaya22.fragments.PageFragment1;
import com.example.kursovaya22.fragments.PageFragment2;
import com.example.kursovaya22.fragments.PageFragment3;
import com.example.kursovaya22.fragments.PageFragment4;
import com.example.kursovaya22.fragments.PageFragment5;
import com.example.kursovaya22.fragments.PageFragment6;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DataActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    public SharedPreferences sPref;
    List<Fragment> list = new ArrayList<>();
    public static final String APP_PREFERENCES = "Authentication";
    public static final String APP_PREFERENCES_KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        int week = getIntent().getIntExtra("week",0);
        if (week == 0){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Верхняя неделя");
        }else{
            Objects.requireNonNull(getSupportActionBar()).setTitle("Нижняя неделя");
        }

        String data = getIntent().getStringExtra("data");

        String[][][] ArrayData = new String[6][7][2];
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
            for (int i1 = 0; i1 < 6; i1++) {
                List<String> time_1 = new ArrayList<String>();
                List<String> id_para_1 = new ArrayList<String>();
                List<String> cabinet_1 = new ArrayList<String>();
                List<String> predmet_type_1 = new ArrayList<String>();
                List<String> teacher_1 = new ArrayList<String>();
                List<String> time_2 = new ArrayList<String>();
                List<String> id_para_2 = new ArrayList<String>();
                List<String> cabinet_2 = new ArrayList<String>();
                List<String> predmet_type_2 = new ArrayList<String>();
                List<String> teacher_2 = new ArrayList<String>();

                if (obj.has(String.valueOf(i1+1))){
                    for (int i2 = 0; i2 < 7; i2++){
                        if (obj.getJSONObject(String.valueOf(i1+1)).has(String.valueOf(i2+1))){
                            for (int i3 = 0; i3 < 2; i3++){
                                if (obj.getJSONObject(String.valueOf(i1+1)).getJSONObject(String.valueOf(i2+1)).has(String.valueOf(i3+1))) {
                                    ArrayData[i1][i2][i3] = obj.getJSONObject(String.valueOf(i1+1)).getJSONObject(String.valueOf(i2+1)).getString(String.valueOf(i3+1));
                                    JSONObject lessons = new JSONObject(ArrayData[i1][i2][i3]);
                                    String TimeClocks = null;
                                    String TypePregmet = null;
                                    switch (lessons.getString("predmet_type")){
                                        case "1":
                                            TypePregmet = "Лаб";
                                            break;
                                        case "2":
                                            TypePregmet = "Пр";
                                            break;
                                        case "3":
                                            TypePregmet = "Лек";
                                            break;
                                    }
                                    switch(String.valueOf(i2)){
                                        case "0":
                                            TimeClocks = "08:00";
                                            break;
                                        case "1":
                                            TimeClocks = "09:40";
                                            break;
                                        case "2":
                                            TimeClocks = "11:30";
                                            break;
                                        case "3":
                                            TimeClocks = "13:10";
                                            break;
                                        case "4":
                                            TimeClocks = "15:00";
                                            break;
                                        case "5":
                                            TimeClocks = "16:40";
                                            break;
                                        case "6":
                                            TimeClocks = "18:20";
                                            break;
                                    }
                                    if(i3 == 0){
                                        time_1.add(TimeClocks);
                                        id_para_1.add(lessons.getString("id_para"));
                                        cabinet_1.add(lessons.getString("cabinet"));
                                        predmet_type_1.add(TypePregmet);
                                        teacher_1.add(lessons.getString("teacher"));
                                    }else{
                                        time_2.add(TimeClocks);
                                        id_para_2.add(lessons.getString("id_para"));
                                        cabinet_2.add(lessons.getString("cabinet"));
                                        predmet_type_2.add(TypePregmet);
                                        teacher_2.add(lessons.getString("teacher"));
                                    }


                                }
                                //меняются подгруппы
                            }
                        }
                        //меняются пары
                    }
                }
                setDataToFragment(i1, time_1, id_para_1, cabinet_1, predmet_type_1, teacher_1, time_2, id_para_2, cabinet_2, predmet_type_2, teacher_2);
                //меняются дни
            }
        } catch (JSONException e) {
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //неправильно определяет день, на 1 в будущем
        pager.setCurrentItem(dayOfWeek-2);
    }

    public void setDataToFragment(int id, List<String> time_1, List<String> id_para_1, List<String> cabinet_1, List<String> predmet_type_1, List<String> teacher_1, List<String> time_2, List<String> id_para_2, List<String> cabinet_2, List<String> predmet_type_2, List<String> teacher_2){
        switch (id){
            case 0:
                PageFragment1 pf1 = new PageFragment1();
                pf1.mTime1 = time_1.toArray(new String[0]);
                pf1.mLesson1 = id_para_1.toArray(new String[0]);
                pf1.mTeacher1 = teacher_1.toArray(new String[0]);
                pf1.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf1.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf1.mTime2 = time_2.toArray(new String[0]);
                pf1.mLesson2 = id_para_2.toArray(new String[0]);
                pf1.mTeacher2 = teacher_2.toArray(new String[0]);
                pf1.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf1.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf1);
                break;
            case 1:
                PageFragment2 pf2 = new PageFragment2();
                pf2.mTime1 = time_1.toArray(new String[0]);
                pf2.mLesson1 = id_para_1.toArray(new String[0]);
                pf2.mTeacher1 = teacher_1.toArray(new String[0]);
                pf2.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf2.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf2.mTime2 = time_2.toArray(new String[0]);
                pf2.mLesson2 = id_para_2.toArray(new String[0]);
                pf2.mTeacher2 = teacher_2.toArray(new String[0]);
                pf2.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf2.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf2);
                break;
            case 2:
                PageFragment3 pf3 = new PageFragment3();
                pf3.mTime1 = time_1.toArray(new String[0]);
                pf3.mLesson1 = id_para_1.toArray(new String[0]);
                pf3.mTeacher1 = teacher_1.toArray(new String[0]);
                pf3.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf3.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf3.mTime2 = time_2.toArray(new String[0]);
                pf3.mLesson2 = id_para_2.toArray(new String[0]);
                pf3.mTeacher2 = teacher_2.toArray(new String[0]);
                pf3.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf3.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf3);
                break;
            case 3:
                PageFragment4 pf4 = new PageFragment4();
                pf4.mTime1 = time_1.toArray(new String[0]);
                pf4.mLesson1 = id_para_1.toArray(new String[0]);
                pf4.mTeacher1 = teacher_1.toArray(new String[0]);
                pf4.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf4.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf4.mTime2 = time_2.toArray(new String[0]);
                pf4.mLesson2 = id_para_2.toArray(new String[0]);
                pf4.mTeacher2 = teacher_2.toArray(new String[0]);
                pf4.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf4.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf4);
                break;
            case 4:
                PageFragment5 pf5 = new PageFragment5();
                pf5.mTime1 = time_1.toArray(new String[0]);
                pf5.mLesson1 = id_para_1.toArray(new String[0]);
                pf5.mTeacher1 = teacher_1.toArray(new String[0]);
                pf5.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf5.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf5.mTime2 = time_2.toArray(new String[0]);
                pf5.mLesson2 = id_para_2.toArray(new String[0]);
                pf5.mTeacher2 = teacher_2.toArray(new String[0]);
                pf5.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf5.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf5);
                break;
            case 5:
                PageFragment6 pf6 = new PageFragment6();
                pf6.mTime1 = time_1.toArray(new String[0]);
                pf6.mLesson1 = id_para_1.toArray(new String[0]);
                pf6.mTeacher1 = teacher_1.toArray(new String[0]);
                pf6.mCabinet1 = cabinet_1.toArray(new String[0]);
                pf6.mTypeless1 = predmet_type_1.toArray(new String[0]);
                pf6.mTime2 = time_2.toArray(new String[0]);
                pf6.mLesson2 = id_para_2.toArray(new String[0]);
                pf6.mTeacher2 = teacher_2.toArray(new String[0]);
                pf6.mCabinet2 = cabinet_2.toArray(new String[0]);
                pf6.mTypeless2 = predmet_type_2.toArray(new String[0]);
                list.add(pf6);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.data_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.Logout:
                sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.clear();
                ed.apply();

                Intent MainActivity = new Intent(this, MainActivity.class);
                startActivity(MainActivity);

                break;
        }
        return true;
    }
}
