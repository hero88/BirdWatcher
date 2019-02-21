package com.example.birdwatch;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FormActivity extends AppCompatActivity
{
    @InjectView(R.id.spinner1) Spinner spinner1;
    @InjectView(R.id.btnCreate) Button btnCreate;
    @InjectView(R.id.btnCancel) Button btnCancel;
    @InjectView(R.id.inputName) EditText inputName;
    @InjectView(R.id.inputNote) EditText inputNote;

    public static final String mySavings = "mySave";
    public static final String Bird = "Bird:";

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);


        addListenerOnSpinnerItemSelection();
        cancelForm();
        createForm();
    }

    public void addListenerOnSpinnerItemSelection()
    {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "Selected rarity: " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // create new bird observation when clicked
    public void createForm(){

        btnCreate.setOnClickListener(new View.OnClickListener() {
            Bird bird = new Bird();
            @Override
            public void onClick(View v)
            {
                String oldData, newData, currentData;
                String name = inputName.getText().toString();
                String note = inputNote.getText().toString();
                String rarity = String.valueOf(spinner1.getSelectedItem());
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

                bird.setName(name);
                bird.setNote(note);
                bird.setRarity(rarity);
                bird.setDate(date);

                List<Bird> oldList = getData(getApplicationContext());
                if (oldList == null) {
                    bird.setId(0);
                }
                else{
                    oldData = gson.toJson(oldList);
                    Log.d("old data:", oldData);
                    bird.setId(oldList.size()+1);

                    List<Bird> newList = new ArrayList<Bird>();
                    newList.add(bird);
                    newData = gson.toJson(newList);
                    Log.d("new data:", newData);

                    currentData = oldData + newData;
                    Log.d("current data:", currentData);
                }
                oldList.add(bird);
                saveData(getApplicationContext(), oldList);
            }
        });
    }

    // Clear and reset form when clicked
    public void cancelForm()
    {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName.getText().clear();
                inputNote.getText().clear();
                spinner1.setSelection(0);
            }
        });
    }


    public void saveData(Context context, List<Bird> list) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(mySavings,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        String jsonData = gson.toJson(list);
        editor.putString(Bird, jsonData);
        editor.commit();
    }

    public ArrayList<Bird> getData(Context context){
        SharedPreferences settings;
        settings = context.getSharedPreferences(mySavings,Context.MODE_PRIVATE);
        List <Bird> birdLists;

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
