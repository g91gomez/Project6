package com.example.g91go.whatsfordinner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public String result = "distance";
    ImageView imgClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgClick = (ImageView)findViewById(R.id.imageView);

        imgClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               sendSelected(v);

            }
        });
    }

    public void sendSelected(View view){
        Intent intent = new Intent(this, DisplayResultsActivity.class);
        int resultIndex;
        ArrayList<CheckBox> SelectedFilters = new ArrayList() ;

        CheckBox Mexican = (CheckBox) findViewById(R.id.checkBox_Mexican);
        if (Mexican.isChecked())
            SelectedFilters.add(Mexican);
        CheckBox Italian = (CheckBox) findViewById(R.id.checkBox_Italian);
        if (Italian.isChecked())
            SelectedFilters.add(Italian);
        CheckBox Chinese = (CheckBox) findViewById(R.id.checkBox_Chinese);
        if (Chinese.isChecked())
            SelectedFilters.add(Chinese);
        CheckBox Vietnamese = (CheckBox) findViewById(R.id.checkBox_Vietnamese);
        if (Vietnamese.isChecked())
            SelectedFilters.add(Vietnamese);
        CheckBox Japanese = (CheckBox) findViewById(R.id.checkBox_Japanese);
        if (Japanese.isChecked())
            SelectedFilters.add(Japanese);
        CheckBox Greek = (CheckBox) findViewById(R.id.checkBox_French);
        if (Greek.isChecked())
            SelectedFilters.add(Greek);
        CheckBox Korean = (CheckBox) findViewById(R.id.checkBox_Korean);
        if (Korean.isChecked())
            SelectedFilters.add(Korean);
        CheckBox American = (CheckBox) findViewById(R.id.checkBox_American);
        if (American.isChecked())
            SelectedFilters.add(American);

        if (SelectedFilters.size() < 2){
            Context context = getApplicationContext();
            CharSequence text = "Please Select at least 2 filters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            //Vibration method
            Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(100);
            resultIndex = RandomSelect(SelectedFilters);
            CheckBox Choice = SelectedFilters.get(resultIndex);
            String resultFilter = Choice.getText().toString();
            Bundle extras = new Bundle();
            int duration = Toast.LENGTH_SHORT;


            extras.putString(EXTRA_MESSAGE, resultFilter);
            extras.putString("Sort",result);
            intent.putExtras(extras);
            startActivity(intent);
        }

    }


    protected int RandomSelect(ArrayList<CheckBox> selectedFilters) {
        Random selectionIndex = new Random();
        int resultIndex = selectionIndex.nextInt(selectedFilters.size());
        return resultIndex;
    }

    public void advFilters(View view){
        Intent advFilters = new Intent(this, AdvFilters.class);
        startActivityForResult(advFilters,1);

    }

    public void randomizeButton(View view){
        ArrayList <CheckBox> Cuisines = new ArrayList<CheckBox>();

        CheckBox Mexican = (CheckBox) findViewById(R.id.checkBox_Mexican);
        Mexican.setChecked(false);
        Cuisines.add(Mexican);

        CheckBox Italian = (CheckBox) findViewById(R.id.checkBox_Italian);
        Italian.setChecked(false);
        Cuisines.add(Italian);

        CheckBox Chinese = (CheckBox) findViewById(R.id.checkBox_Chinese);
        Chinese.setChecked(false);
        Cuisines.add(Chinese);

        CheckBox Vietnamese = (CheckBox) findViewById(R.id.checkBox_Vietnamese);
        Vietnamese.setChecked(false);
        Cuisines.add(Vietnamese);

        CheckBox Japanese = (CheckBox) findViewById(R.id.checkBox_Japanese);
        Japanese.isChecked();
        Cuisines.add(Japanese);

        CheckBox French = (CheckBox) findViewById(R.id.checkBox_French);
        French.setChecked(false);
        Cuisines.add(French);

        CheckBox Korean = (CheckBox) findViewById(R.id.checkBox_Korean);
        Korean.setChecked(false);
        Cuisines.add(Korean);

        CheckBox American = (CheckBox) findViewById(R.id.checkBox_American);
        American.setChecked(false);
        Cuisines.add(American);

        Random selector = new Random();

        for (int i =0; i < 5; i++){
            int index = Cuisines.size();
            int rIndex=selector.nextInt(index);
            CheckBox selected = Cuisines.get(rIndex);
            if(selected.isChecked()){
                i = i-1;
            }
            else{
                selected.setChecked(true);
                Cuisines.remove(rIndex);
            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                this.result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
