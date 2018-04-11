package com.emraz.parkinghero.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.util.Log;
import com.emraz.parkinghero.util.UIUtil;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;

import static com.emraz.parkinghero.util.Common.push;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextCode;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextCode = findViewById(R.id.editTextCode);
        buttonSubmit = findViewById(R.id.buttonSubmitCode);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = editTextCode.getText().toString();
                if (tag.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Code is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    // Subscribe to the given tag

                    if (push == null) {
                        Toast.makeText(RegisterActivity.this, "Notification setting is not correct now!", Toast.LENGTH_SHORT).show();
                    } else {
                        push.subscribe(tag, new MFPPushResponseListener<String>() {

                            @Override
                            public void onSuccess(final String arg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIUtil.showSimpleAlertDialog(RegisterActivity.this, "", "Successfully Subscribed!");
                                    }
                                });
                            }

                            @Override
                            public void onFailure(final MFPPushException ex) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ex.getStatusCode() == 409)
                                            UIUtil.showSimpleAlertDialog(RegisterActivity.this, "", "You are already registered.");
                                        else
                                            UIUtil.showSimpleAlertDialog(RegisterActivity.this, "", "Wrong code number, please check and try again");
                                    }
                                });
                            }
                        });
                    }
                }

            }
        });

        setupActionBar();
    }

    private void setupActionBar() {

        // getSupportActionBar().setTitle("Confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
