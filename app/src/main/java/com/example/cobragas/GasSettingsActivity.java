package com.example.cobragas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GasSettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initListButton();
        initMapButton();
        initSettingsButton();
        initSettings();
        initSortByClick();


    }

    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasSettingsActivity.this, GasListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initMapButton() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasSettingsActivity.this, GasMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = (ImageButton)findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyStationListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "stationname");

        String sortOrder = getSharedPreferences("MyStationListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        String sortColor = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortColr","default");

        RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
        RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
        RadioButton rbDistance = (RadioButton) findViewById(R.id.radioDistance);

        if (sortBy.equalsIgnoreCase("stationname")) {
            rbName.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("city")) {
            rbCity.setChecked(true);
        } else {
            rbDistance.setChecked(true);
        }

        //Color

        RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
        RadioButton rbDescending = (RadioButton) findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("DESC")) {
            rbDescending.setChecked(true);

        } else {
            rbAscending.setChecked(true);
        }

        RadioButton red = (RadioButton) findViewById(R.id.radioRed);
        RadioButton blue = (RadioButton)findViewById(R.id.radioBlue);
        RadioButton Default =(RadioButton)findViewById(R.id.radioDefault);
        ScrollView setting =findViewById(R.id.settingsScroll);
        if(sortColor.equalsIgnoreCase("red")){
            red.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            //settings.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        else if (sortColor.equalsIgnoreCase("blue")) {
            blue.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            settings.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (sortColor.equalsIgnoreCase("default")) {
            Default.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            settings.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
                RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
                if (rbName.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "stationname").commit();
                }
                else if (rbCity.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "city").commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "distance").commit();
                }
            }
        });

    }

}
