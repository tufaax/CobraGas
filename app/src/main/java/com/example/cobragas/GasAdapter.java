package com.example.cobragas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GasAdapter extends ArrayAdapter<Gas> {

    private ArrayList<Gas> items;
    private Context adapterContext;

    public GasAdapter(Context context, ArrayList<Gas> items) {
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Gas gas = items.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }


            TextView gasName = (TextView) v.findViewById(R.id.textGasName);
            if(position % 2 == 0){
              gasName.setTextColor(Color.parseColor("#3aa8c1"));
            }
            else{
                gasName.setTextColor(Color.parseColor("#ba160c"));
            }
            TextView gasNumber = (TextView) v.findViewById(R.id.textPhoneNumber);
            TextView gasStreetAdd = (TextView) v.findViewById(R.id.textStreetAddress);
            TextView gasState = (TextView) v.findViewById(R.id.textState);
            TextView gasZip = (TextView) v.findViewById(R.id.textZIP);

            Button b = (Button) v.findViewById(R.id.buttonDeleteGas);


            gasName.setText(gas.getStationName());
            gasNumber.setText("Phone: "+ gas.getPhoneNumber());
            gasStreetAdd.setText(gas.getDistance());
            gasState.setText(gas.getState());
            gasZip.setText(gas.getDistance());


/*
            Log.w(gas.getBff() +"", "hello");

            if(gas.getBff() == 0 ){
                star.setVisibility(View.INVISIBLE);
            }
            else{
                star.setVisibility(View.VISIBLE);
            }
    */
            b.setVisibility(View.INVISIBLE);

        }


        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;


    }

    public void showDelete(final int position, final View convertView,final Context context, final Gas gas) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteGas);
        //2
        if(b.getVisibility()==View.INVISIBLE) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView, context);
                    items.remove(gas);
                    deleteOption(gas.getStationID(), context);
                }
            });
        }
        else {
            hideDelete(position, convertView, context);
        }
    }
    //3
    private void deleteOption(int gasToDelete, Context context) {
        GasDataSource db = new GasDataSource(context);
        try {
            db.open();
            db.deleteGas(gasToDelete);
            db.close();
        }
        catch (Exception e) {
            Toast.makeText(adapterContext, "Delete gas Failed", Toast.LENGTH_LONG).show();
        }
        this.notifyDataSetChanged();
    }
    //4
    public void hideDelete(int position, View convertView, Context context) {
        View v = convertView;
        final Button b = (Button) v.findViewById(R.id.buttonDeleteGas);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }

}
