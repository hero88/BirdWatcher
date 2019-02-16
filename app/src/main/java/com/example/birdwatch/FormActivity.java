package com.example.birdwatch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FormActivity extends AppCompatActivity
{
    @InjectView(R.id.spinner1) Spinner spinner1;
    @InjectView(R.id.btnCreate) Button btnCreate;
    @InjectView(R.id.btnCancel) Button btnCancel;
    @InjectView(R.id.inputName) EditText inputName;
    @InjectView(R.id.inputNote) EditText inputNote;

    ArrayList<HashMap<String,String>> birdLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        birdLists = new ArrayList<>();

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
        final String name = inputName.getText().toString();
        final String note = inputNote.getText().toString();
        final String rarity = String.valueOf(spinner1.getSelectedItem());
        final String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdLists = new ArrayList<>();
                int count = 0;
                try {
                    boolean isFilePresent = isFilePresent(getApplicationContext(), "storage.txt");
                    if(isFilePresent) {
                        String jsonString = read(getApplicationContext(), "storage.txt");
                        //do the json parsing here and do the rest of functionality of app
                        JSONArray jsonArray = new JSONArray(jsonString);
                        count = jsonArray.length()+1;

                    } else {
                        count++;
                    }
                    // temporary hashmap for single bird
                    HashMap<String, String> bird = new HashMap<>();
                    bird.put("Id: ", Integer.toString(count));
                    bird.put("Name: ", name);
                    bird.put("Rarity: ", rarity);
                    bird.put("Note: ", note);
                    bird.put("Date: ", date);

                    birdLists.add(bird);

                    boolean isFileCreated = create(getApplicationContext(), "storage.txt", birdLists.toString());
                    if(isFileCreated) {
                        //proceed with storing
                        Toast.makeText(getApplicationContext(),"Created Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        //show error or try again.
                    }

                }
                catch (JSONException e) {
                    Log.d("JSONException: ", e.toString());
                    e.printStackTrace();
                }
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

    ////
    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    private boolean create(Context context, String fileName, String jsonString){
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }


}
