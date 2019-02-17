package com.example.birdwatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

    SharedPreferences settings;

    public static final String mySavings = "mySave";
    public static final String Id = "ID:";
    public static final String Name = "Name:";
    public static final String Rarity = "Rarity:";
    public static final String Note = "Note:";
    public static final String Date = "Date:";

    @InjectView(R.id.text_view_idName) TextView name;
    @InjectView(R.id.text_view_idNote) TextView note;
    @InjectView(R.id.text_view_idRarity) TextView rarity;
    @InjectView(R.id.text_view_idDate) TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        settings = getSharedPreferences(mySavings, Context.MODE_PRIVATE);
        if(settings.contains(Name)){
            name.setText(settings.getString(Name,""));
        }
        if(settings.contains(Note)){
            note.setText(settings.getString(Note,""));
        }
        if(settings.contains(Rarity)){
            rarity.setText(settings.getString(Rarity,""));
        }
        if(settings.contains(Date)){
            date.setText(settings.getString(Date,""));
        }

    }

    public void addMore(View view){
        Intent intent = new Intent(this,FormActivity.class);
        startActivity(intent);
    }
}

