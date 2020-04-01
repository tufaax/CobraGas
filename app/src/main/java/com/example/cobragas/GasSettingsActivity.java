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
        initSortOrderClick();
        initSortByClick();
        initSetColor();


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

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = (RadioGroup) findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").commit();
                }
                else {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").commit();
                }
            }
        });
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MySListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "stationname");

        String sortOrder = getSharedPreferences("MySListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        String sortColor = getSharedPreferences("MySListPreferences",
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

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }

        RadioButton red = (RadioButton) findViewById(R.id.radioRed);
        RadioButton blue = (RadioButton)findViewById(R.id.radioBlue);
        RadioButton Default =(RadioButton)findViewById(R.id.radioDefault);
        if(sortColor.equalsIgnoreCase("red")){
            red.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            settings.setBackgroundColor(getResources().getColor(R.color.rb_red));
        }

        else if (sortColor.equalsIgnoreCase("blue")) {
            blue.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            settings.setBackgroundColor(getResources().getColor(R.color.rb_blue));
        }
        else if (sortColor.equalsIgnoreCase("default")) {
            Default.setChecked(true);
            ScrollView settings = (ScrollView) findViewById(R.id.settingsScroll);
            settings.setBackgroundColor(getResources().getColor(R.color.rb_default));
        }
    }

    private void initSetColor() {
        RadioGroup rgSetColor = (RadioGroup) findViewById(R.id.colorGroup);
        rgSetColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbRed = (RadioButton) findViewById(R.id.radioRed);
                RadioButton rbBlue = (RadioButton) findViewById(R.id.radioBlue);
                RadioButton rbDefault=(RadioButton)findViewById(R.id.radioDefault);

                ScrollView settings= (ScrollView) findViewById(R.id.settingsScroll);

                if (rbRed.isChecked()) {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortColr", "red").commit();
                    settings.setBackgroundColor(getResources().getColor(R.color.rb_red));


                }
                else if (rbBlue.isChecked()) {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortColr", "blue").commit();
                    settings.setBackgroundColor(getResources().getColor(R.color.rb_blue));
                }
                else if ( rbDefault.isChecked()){
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortColr", "default").commit();
                    settings.setBackgroundColor(getResources().getColor(R.color.rb_default));
                }
            }
        });



    }

    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
                RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
                if (rbName.isChecked()) {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "stationname").apply();
                }
                else if (rbCity.isChecked()) {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "city").apply();
                }
                else {
                    getSharedPreferences("MySListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "distance").apply();;
                }
            }
        });

    }

}
