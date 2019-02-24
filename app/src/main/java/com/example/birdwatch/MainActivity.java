package com.example.birdwatch;

import static com.example.birdwatch.Constant.FIRST_COLUMN;
import static com.example.birdwatch.Constant.SECOND_COLUMN;
import static com.example.birdwatch.Constant.THIRD_COLUMN;
import static com.example.birdwatch.Constant.FOURTH_COLUMN;
import static com.example.birdwatch.Constant.FIFTH_COLUMN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

    public static final String mySavings = "mySave";
    public static final String Bird = "Bird:";
    private ArrayList<HashMap> list = new ArrayList<HashMap>();

    Gson gson = new Gson();


    @InjectView(R.id.listView) ListView lView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        populateList();
        listViewAdapter adapter = new listViewAdapter(this,list);
        lView.setAdapter(adapter);
    }

    private void populateList(){

        List<Bird> birdList = getData(getApplicationContext());
        if (birdList == null) return;

        HashMap header = new HashMap();
        header.put(FIRST_COLUMN,FIRST_COLUMN.toUpperCase());
        header.put(SECOND_COLUMN, SECOND_COLUMN.toUpperCase());
        header.put(THIRD_COLUMN,THIRD_COLUMN.toUpperCase());
        header.put(FOURTH_COLUMN,FOURTH_COLUMN.toUpperCase());
        header.put(FIFTH_COLUMN,FIFTH_COLUMN.toUpperCase());
        list.add(header);

        for (int i=0; i<birdList.size(); i++){
            HashMap temp = new HashMap();
            temp.put(FIRST_COLUMN,birdList.get(i).getName());
            temp.put(SECOND_COLUMN,birdList.get(i).getRarity());
            temp.put(THIRD_COLUMN,birdList.get(i).getNote());
            try {
                temp.put(FOURTH_COLUMN, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").
                        parse(birdList.get(i).getDate()));
            }catch (ParseException e){
                e.printStackTrace();
            }
            if (birdList.get(i).getLocation()!=null)
                temp.put(FIFTH_COLUMN, birdList.get(i).getLocation());
            else
                temp.put(FIFTH_COLUMN, "None");
            list.add(temp);
        }


    }

    // Go to ADD FORM
    public void addMore(View view){
        Intent intent = new Intent(this,FormActivity.class);
        startActivity(intent);
    }

    // get saved Data
    private ArrayList<Bird> getData(Context context){
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

