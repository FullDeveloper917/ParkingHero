package com.emraz.parkinghero.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.util.Constants;

import static com.emraz.parkinghero.util.Common.displayRadius;

public class PreferenceActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        setInit();

        radioGroup = findViewById(R.id.radioGroupRadius);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.threeHundred:
                        displayRadius = 300;
                        break;
                    case R.id.fiveHundred:
                        displayRadius = 500;
                        break;
                    case R.id.eightHundred:
                        displayRadius = 800;
                        break;
                    case R.id.thousand:
                        displayRadius = 1000;
                        break;
                    default:
                        displayRadius = 500;
                        break;
                }
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.TAG_DISPLAY_RADIUS, displayRadius);
                editor.apply();
            }
        });

        setupActionBar();
    }

    private void setInit() {
        switch (displayRadius) {
            case 300:
                ((RadioButton)findViewById(R.id.threeHundred)).setChecked(true);
                break;
            case 500:
                ((RadioButton)findViewById(R.id.fiveHundred)).setChecked(true);
                break;
            case 800:
                ((RadioButton)findViewById(R.id.eightHundred)).setChecked(true);
                break;
            case 1000:
                ((RadioButton)findViewById(R.id.thousand)).setChecked(true);
                break;
            default:
                ((RadioButton)findViewById(R.id.fiveHundred)).setChecked(true);
                break;
        }
    }

    private void setupActionBar() {

        // getSupportActionBar().setTitle("Confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
