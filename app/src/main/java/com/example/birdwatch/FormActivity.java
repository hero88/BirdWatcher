package com.example.birdwatch;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DecimalFormat;
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

    private LocationManager locationManager;
    private LocationListener locationListener;

    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location:", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        addListenerOnSpinnerItemSelection();
        cancelForm();
        createForm();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
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
    public void createForm()
    {
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            Bird bird = new Bird();
            @Override
            public void onClick(View v)
            {
                Double lat,lng;
                DecimalFormat decimalFormat = new DecimalFormat("#.####");

                if (ActivityCompat.checkSelfPermission(FormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                String name = inputName.getText().toString();
                String note = inputNote.getText().toString();
                String rarity = String.valueOf(spinner1.getSelectedItem());
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

                lat = location.getLatitude();
                lat = Double.valueOf(decimalFormat.format(lat)); // format latitude to 4 decimal places
                lng = location.getLongitude();
                lng = Double.valueOf(decimalFormat.format(lng)); // format longitude to 4 decimal places

                LatLng myPosition = new LatLng(lat,lng);

                bird.setName(name);
                bird.setNote(note);
                bird.setRarity(rarity);
                bird.setDate(date);
                bird.setLocation(myPosition);

                List<Bird> oldList = getData(getApplicationContext());
                if (oldList == null) {
                    bird.setId(1);
                }
                else{
                    bird.setId(oldList.size()+1);
                    /* create log to test
                    String oldData, newData, currentData;
                    oldData = gson.toJson(oldList);
                    Log.d("old data:", oldData);
                    List<Bird> newList = new ArrayList<Bird>();
                    newList.add(bird);
                    newData = gson.toJson(newList);
                    Log.d("new data:", newData);

                    currentData = oldData + newData;
                    Log.d("current data:", currentData);
                    */
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
            return new ArrayList<Bird>();
        return (ArrayList<Bird>)birdLists;
    }
}
