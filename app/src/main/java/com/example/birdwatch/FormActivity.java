package com.example.birdwatch;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FormActivity extends AppCompatActivity
{
    @InjectView(R.id.spinner1) Spinner spinner1;
    @InjectView(R.id.btnCreate) Button btnCreate;
    @InjectView(R.id.btnCancel) Button btnCancel;
    @InjectView(R.id.inputName) EditText inputName;
    @InjectView(R.id.inputNote) EditText inputNote;

    Gson gson = new Gson();

    SharedPreferences settings;

    public static final String mySavings = "mySave";
    public static final String Id = "ID:";
    public static final String Name = "Name:";
    public static final String Rarity = "Rarity:";
    public static final String Note = "Note:";
    public static final String Date = "Date:";

    public static int count = 0;



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
        final String rarity = String.valueOf(spinner1.getSelectedItem());
        final String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String note = inputNote.getText().toString();

                settings = getSharedPreferences(mySavings, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString(Id, "1");
                editor.putString(Name, name);
                editor.putString(Note, note);
                editor.putString(Rarity, rarity);
                editor.putString(Date, date);

                editor.commit();
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


    public void putJson(Context context, JSONObject jsonObject) {
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(mySavings,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(Id, jsonObject.toString());
        editor.commit();
    }

    public ArrayList<Bird> getJson(Context context, String category) {
        String json;
        JSONArray jPdtArray;
        Bird bird;
        ArrayList<Bird> birds = null;

        settings = context.getSharedPreferences(mySavings, Context.MODE_PRIVATE);
        json = settings.getString("JSONString", null);

        JSONObject jsonObj = null;
        try {
            if (json != null) {
                jsonObj = new JSONObject(json);
                jPdtArray = jsonObj.optJSONArray(category);
                if (jPdtArray != null) {
                    birds = new ArrayList<Bird>();
                    for (int i = 0; i < jPdtArray.length(); i++) {
                        bird = new Bird();
                        JSONObject pdtObj = jPdtArray.getJSONObject(i);
                        bird.setName(pdtObj.getString("name"));
                        bird.setId(pdtObj.getString("id"));
                        birds.add(bird);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return birds;
    }
}
