package com.codingstuff.startdialer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public final class MainActivity extends AppCompatActivity {
    private EditText inputNumberEt;
    private Button startDialerBtn;
    private Button dialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVars();
        registerClickListener();
    }

    private void initVars() {
        // Properly initialize views with null checks
        inputNumberEt = findViewById(R.id.inputNumberEt);
        startDialerBtn = findViewById(R.id.openDialerBtn);
        dialBtn = findViewById(R.id.dialBtn);

        if (inputNumberEt == null || startDialerBtn == null || dialBtn == null) {
            Log.e("MainActivity", "Views could not be initialized");
        }
    }

    private void registerClickListener() {
        // Ensure buttons are not null before assigning listeners
        if (startDialerBtn != null) {
            startDialerBtn.setOnClickListener(view -> onButtonClick(false));
        }

        if (dialBtn != null) {
            dialBtn.setOnClickListener(view -> onButtonClick(true));
        }
    }

    private void onButtonClick(boolean isDial) {
        if (inputNumberEt == null) {
            Log.e("MainActivity", "Input field is null");
            return;
        }

        String inputNumber = inputNumberEt.getText().toString();
        if (inputNumber.isEmpty()) {
            Log.e("MainActivity", "Input number is empty");
            return;
        }

        Uri numberUri = Uri.parse("tel:" + inputNumber);
        Intent intent;

        if (isDial) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") == 0) {
                intent = new Intent(Intent.ACTION_CALL, numberUri);
                startIntent(intent);
            } else {
                // Request CALL_PHONE permission
                String[] permissions = {"android.permission.CALL_PHONE"};
                requestPermissions(permissions, 1);
            }
        } else {
            intent = new Intent(Intent.ACTION_DIAL, numberUri);
            startIntent(intent);
        }
    }

    private void startIntent(Intent intent) {
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            Log.e("MainActivity", "Permission denied: " + e.getMessage());
        }
    }
}
