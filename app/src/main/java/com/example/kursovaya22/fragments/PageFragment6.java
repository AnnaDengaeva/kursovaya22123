package com.example.kursovaya22.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kursovaya22.R;


public class PageFragment6 extends Fragment {
    public String[] mTime1;
    public String[] mLesson1;
    public String[] mTeacher1;
    public String[] mCabinet1;
    public String[] mTypeless1;
    public String[] mTime2;
    public String[] mLesson2;
    public String[] mTeacher2;
    public String[] mCabinet2;
    public String[] mTypeless2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater
                .inflate(R.layout.saturday, container
                        ,false);

        //String mTime[] = {"11:11", "22:22"};
        //String mLesson[] = {"Test1Test1Test1Test1Test1Test1Test1Test1Test1", "Test2"};
        //String mTeacher[] = {"Test1", "Test2Test2Test2Test2Test2Test2"};
        //String mCabinet[] = {"101", "202"};
        //String mTypeless[] = {"AAA", "OOO"};

        ListView LVgroup1 = rootView.findViewById(R.id.listViewGroup1);
        ListView LVgroup2 = rootView.findViewById(R.id.listViewGroup2);
        ListAdapter myAdapter1 = new PageFragment6.MyAdapter(getActivity(), mTime1, mLesson1, mTeacher1, mCabinet1, mTypeless1);
        if(myAdapter1.isEmpty()){
            TextView tv = rootView.findViewById(R.id.empty1);
            tv.setVisibility(View.VISIBLE);
        }
        ListAdapter myAdapter2 = new PageFragment6.MyAdapter(getActivity(), mTime2, mLesson2, mTeacher2, mCabinet2, mTypeless2);
        if(myAdapter2.isEmpty()){
            TextView tv = rootView.findViewById(R.id.empty2);
            tv.setVisibility(View.VISIBLE);
        }
        if (LVgroup1 != null && LVgroup2 != null) {
            LVgroup1.setAdapter(myAdapter1);
            LVgroup2.setAdapter(myAdapter2);
        }
        return rootView;
    }

    public class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] time;
        String[] lesson;
        String[] teacher;
        String[] cabinet;
        String[] typeless;

        public MyAdapter(Context c, String[] time, String[] lesson, String[] teacher, String[] cabinet, String[] typeless){
            super(c, R.layout.row, time);
            this.context = c;
            this.time = time;
            this.lesson = lesson;
            this.teacher = teacher;
            this.cabinet = cabinet;
            this.typeless = typeless;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.row, parent,false);
            TextView myTime = (TextView) row.findViewById(R.id.textViewTime);
            TextView myLesson = (TextView) row.findViewById(R.id.textViewLesson);
            TextView myTeacher = (TextView) row.findViewById(R.id.textViewTeacher);
            TextView myCabinet = (TextView) row.findViewById(R.id.textViewCabinet);
            TextView myTypeLesson = (TextView) row.findViewById(R.id.textViewTypeLesson);

            myTime.setText(time[position]);
            myLesson.setText(lesson[position]);
            myTeacher.setText(teacher[position]);
            myCabinet.setText(cabinet[position]);
            myTypeLesson.setText(typeless[position]);

            return row;
        }
    }
}