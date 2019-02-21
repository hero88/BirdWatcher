package com.example.birdwatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

    public static final String mySavings = "mySave";
    public static final String Bird = "Bird:";

    Gson gson = new Gson();

    @InjectView(R.id.list_view_idName) ListView name;
    @InjectView(R.id.list_view_idNote) ListView note;
    @InjectView(R.id.list_view_idRarity) ListView rarity;
    @InjectView(R.id.list_view_idDate) ListView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        List<Bird> birdList = getData(getApplicationContext());
        List<String> names = new ArrayList<String>();
        List<String> notes = new ArrayList<String>();
        List<String> rarities = new ArrayList<String>();
        List<Date> dates = new ArrayList<Date>();

        for (int i=0; i<birdList.size(); i++){
            names.add(birdList.get(i).getName());
            notes.add(birdList.get(i).getNote());
            rarities.add(birdList.get(i).getRarity());
            try {
                dates.add(i, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").
                        parse(birdList.get(i).getDate()));
            }catch (ParseException e){
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,R.id.text_view,names);
        ArrayAdapter<String> noteAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,R.id.text_view,notes);
        ArrayAdapter<String> rarityAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,R.id.text_view,rarities);
        ArrayAdapter<Date> dateAdapter = new ArrayAdapter<Date>(this,R.layout.activity_listview,R.id.text_view,dates);

        name.setAdapter(nameAdapter);
        note.setAdapter(noteAdapter);
        rarity.setAdapter(rarityAdapter);
        date.setAdapter(dateAdapter);

    }

    // Go to add FORM
    public void addMore(View view){
        Intent intent = new Intent(this,FormActivity.class);
        startActivity(intent);
    }

    // get saved Data
    public ArrayList<Bird> getData(Context context){
        SharedPreferences settings;
        settings = context.getSharedPreferences(mySavings,Context.MODE_PRIVATE);
        List<Bird> birdLists;

        if (settings.contains(Bird)) {
            String jsonData = settings.getString(Bird,null);
            Bird[] bird = gson.fromJson(jsonData, Bird[].class);

            birdLists = Arrays.asList(bird);
            birdLists = new ArrayList<Bird>(birdLists);
        }
        else
            return null;
        return (ArrayList<Bird>)birdLists;
    }
}

