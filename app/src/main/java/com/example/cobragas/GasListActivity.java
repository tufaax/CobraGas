package com.example.cobragas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GasListActivity extends AppCompatActivity {

    ArrayList<Gas> gas;

    boolean isDeleting = false;

    private Gas currentStation;

    GasAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initMapButton();
        initListButton();
        initSettingsButton();


        GasDataSource ds = new GasDataSource(this);

        String sortBy = getSharedPreferences("MyContactListPreferences",

                Context.MODE_PRIVATE).getString("sortfield", "stationname");

        String sortOrder = getSharedPreferences("MyContactListPreferences",

                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        try{
            ds.open();
            gas = ds.getStations(sortBy, sortOrder);
            ds.close();
            ListView listView = (ListView)findViewById(R.id.lvContacts);
            adapter = new GasAdapter(this, gas);
            listView.setAdapter(adapter);

        }
        catch(Exception e){
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }


        initDeleteButton();
        initItemClick();
        initAddContactButton();
    }

    @Override
    public void onResume() {

        super.onResume();

        GasDataSource ds = new GasDataSource(this);

        String sortBy = getSharedPreferences("MyContactListPreferences",

                Context.MODE_PRIVATE).getString("sortfield", "stationname");

        String sortOrder = getSharedPreferences("MyContactListPreferences",

                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        try{
            ds.open();
            gas = ds.getStations(sortBy, sortOrder);
            ds.close();
            if(gas.size() > 0){
                ListView listView = (ListView)findViewById(R.id.lvContacts);
                adapter = new GasAdapter(this, gas);
                listView.setAdapter(adapter);
            }
            else{
                Intent intent = new Intent(GasListActivity.this, GasActivity.class);
                startActivity(intent);
            }

        }
        catch(Exception e){
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }



    }


    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasListActivity.this, GasListActivity.class);
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
                Intent intent = new Intent(GasListActivity.this, GasMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasListActivity.this, GasSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initItemClick(){
        ListView listView = (ListView) findViewById(R.id.lvContacts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Gas selectedContact = gas.get(position);
                if(isDeleting){
                    adapter.showDelete(position, itemClicked, GasListActivity.this, selectedContact);
                }
                else{
                    Intent intent = new Intent(GasListActivity.this, GasActivity.class);
                    intent.putExtra("stationsid", selectedContact.getStationID());
                    startActivity(intent);
                }
            }
        });
    }

    private void initAddContactButton() {
        Button newContact = (Button) findViewById(R.id.buttonAdd);
        newContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GasListActivity.this, GasActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteButton(){
        final Button deleteButton = (Button)findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeleting){
                    deleteButton.setText("Delete");
                    isDeleting = false;
                    adapter.notifyDataSetChanged();
                }
                else{
                    deleteButton.setText("Done Deleting");
                    isDeleting = true;
                }
            }
        });
    }



}
