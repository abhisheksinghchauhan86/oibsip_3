package com.firstapp.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView stopwatchText;
    private Button startButton;
    private Button stopButton;
    private Button holdButton;
    private Button resetButton;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private int seconds = 0;
    private int minutes = 0;
    private int milliseconds = 0;
    private boolean isRunning = false;

    // To store and display hold times
    private TextView holdTimesTextView;

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                timeInMillis = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMillis;

                seconds = (int) (updatedTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliseconds = (int) (updatedTime % 1000);
                milliseconds = milliseconds / 10;

                stopwatchText.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));

                handler.postDelayed(this, 0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchText = findViewById(R.id.stopwatchText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        holdButton = findViewById(R.id.holdButton);
        resetButton = findViewById(R.id.resetButton);
        holdTimesTextView = findViewById(R.id.holdTimesTextView); // Initialize hold times TextView

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    isRunning = true;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    timeSwapBuff += timeInMillis;
                    handler.removeCallbacks(updateTimer);
                    isRunning = false;
                }
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record the current time and display it
                long holdTime = updatedTime;
                String holdTimeFormatted = String.format("%02d:%02d:%02d",
                        (int) (holdTime / 60000),
                        (int) ((holdTime % 60000) / 1000),
                        (int) ((holdTime % 1000) / 10));

                // Append hold time to the TextView
                String currentText = holdTimesTextView.getText().toString();
                holdTimesTextView.setText(currentText + holdTimeFormatted + "\n");
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMillis = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                seconds = 0;
                minutes = 0;
                milliseconds = 0;
                stopwatchText.setText("00:00:00");
                handler.removeCallbacks(updateTimer);
                isRunning = false;
                holdTimesTextView.setText(""); // Clear recorded times
            }
        });
    }
}

