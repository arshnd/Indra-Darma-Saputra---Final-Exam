package com.example.studentenrollapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    TextView textViewSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        textViewSummary = findViewById(R.id.textViewSummary);
        int totalCredits = getIntent().getIntExtra("totalCredits", 0);
        boolean subject1Selected = getIntent().getBooleanExtra("IOTSelected", false);
        boolean subject2Selected = getIntent().getBooleanExtra("CyberSecuritySelected", false);
        boolean subject3Selected = getIntent().getBooleanExtra("AISelected", false);
        boolean subject4Selected = getIntent().getBooleanExtra("GameDevelopmentSelected", false);

        // Initialize the summary string
        StringBuilder summary = new StringBuilder("Subject List:\n");

        // Add subjects based on user selection
        if (subject1Selected) summary.append("1. IOT (3 credits)\n");
        if (subject2Selected) summary.append("2. Cyber Security (4 credits)\n");
        if (subject3Selected) summary.append("3. AI (3 credits)\n");
        if (subject4Selected) summary.append("4. Game Development (4 credits)\n");

        // Add the total credits
        summary.append("\nTotal Credits: ").append(totalCredits);

        // Set the summary text
        textViewSummary.setText(summary.toString());
    }
}
