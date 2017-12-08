package com.example.g91go.whatsfordinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class AdvFilters extends AppCompatActivity {

    public String Preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_filters);
        final Button saveButton = (Button)findViewById(R.id.button_Save);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sortPreference(v);
            }
        });

    }

    protected void sortPreference(View view){
        RadioButton distance = (RadioButton) findViewById(R.id.rButtonDistance);
        RadioButton rating = (RadioButton) findViewById(R.id.rButtonRating);
        RadioButton price = (RadioButton) findViewById(R.id.rButtonPrice);
        if(distance.isChecked()){
            setPreference("distance");
        }
        else if (rating.isChecked()){
            setPreference("rating");
        }
        else if (price.isChecked()){
            setPreference("price");
        }



        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", getPreference());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



    protected void setPreference(String filter){
        Preference = filter;
    }

    protected String getPreference(){
        return Preference;
    }
}
