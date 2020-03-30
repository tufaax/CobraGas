package com.example.cobragas;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GasActivity extends AppCompatActivity {


    private Gas currentStation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initMapButton();
        initSettingsButton();
        initTextChangedEvents();
        initSaveButton();
        initToggleButton();

        currentStation = new Gas();
    }
    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasActivity.this, GasActivity.class);
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
                Intent intent = new Intent(GasActivity.this, GasMapActivity.class);
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
                Intent intent = new Intent(GasActivity.this, GasSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initToggleButton(){
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private  void setForEditing(boolean enabled){
        EditText editName = (EditText) findViewById(R.id.editGasName);
        EditText editAddress = (EditText) findViewById(R.id.editStreet);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editZipCode = (EditText) findViewById(R.id.editZip);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        EditText editRPrice = (EditText) findViewById(R.id.editRegular);
        EditText editPPrice = (EditText) findViewById(R.id.editPremium);
        EditText editDPrice = (EditText) findViewById(R.id.editDiesel);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);


        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editRPrice.setEnabled(enabled);
        editPPrice.setEnabled(enabled);
        editDPrice.setEnabled(enabled);
        buttonSave.setEnabled(enabled);


        if (enabled) {
            editName.requestFocus();

            editCell.setInputType(InputType.TYPE_CLASS_PHONE);

            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else {
            ScrollView s = (ScrollView) findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
            editCell.setInputType(InputType.TYPE_NULL);

            editCell.setInputType(InputType.TYPE_NULL);
        }
    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                boolean wasSuccessful = false;
                GasDataSource ds = new GasDataSource(GasActivity.this);
                try {
                    ds.open();

                    if (currentStation.getStationID() == -1) {
                        wasSuccessful = ds.insertStation(currentStation);
                        int newId = ds.getLastStationId();
                        currentStation.setStationID(newId);
                    } else {
                        wasSuccessful = ds.updateStation(currentStation);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    Log.w(GasDBHelper.class.getName(), currentStation.getContactName());
                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                    Log.w(GasDBHelper.class.getName(), "success");
                }
            }
        });
    }

    private void hideKeyboard() {
    }

    private void initTextChangedEvents() {
        final EditText etStationName = (EditText) findViewById(R.id.editGasName);
        etStationName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentStation.setContactName(etStationName.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etStreetAddress = (EditText) findViewById(R.id.editStreet);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setStreetAddress(etStreetAddress.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etCity = (EditText) findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setCity(etCity.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etState = (EditText) findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setState(etState.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etZipCode = (EditText) findViewById(R.id.editZip);
        etZipCode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setZipCode(etZipCode.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etCellPhone = (EditText) findViewById(R.id.editCell);
        etCellPhone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setPhoneNumber(etCellPhone.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etRegular = (EditText) findViewById(R.id.editRegular);
        etRegular.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setrPrice(etRegular.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etPremium = (EditText) findViewById(R.id.editPremium);
        etRegular.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setpPrice(etPremium.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final EditText etDiesel = (EditText) findViewById(R.id.editDiesel);
        etRegular.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentStation.setdPrice(etDiesel.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        etCellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
}
