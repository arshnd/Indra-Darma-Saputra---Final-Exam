package com.example.studentenrollapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentMenuActivity extends AppCompatActivity {

    CheckBox checkBoxSubject1, checkBoxSubject2, checkBoxSubject3, checkBoxSubject4;
    Button buttonSubmit;
    int totalCredits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_menu);

        checkBoxSubject1 = findViewById(R.id.checkBoxIOT);
        checkBoxSubject2 = findViewById(R.id.checkBoxCyberSecurity);
        checkBoxSubject3 = findViewById(R.id.checkBoxAI);
        checkBoxSubject4 = findViewById(R.id.checkBoxGameDevelopment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCredits = 0;

                // Track subject selection and total credits
                boolean subject1Selected = checkBoxSubject1.isChecked();
                boolean subject2Selected = checkBoxSubject2.isChecked();
                boolean subject3Selected = checkBoxSubject3.isChecked();
                boolean subject4Selected = checkBoxSubject4.isChecked();

                if (subject1Selected) totalCredits += 3;
                if (subject2Selected) totalCredits += 4;
                if (subject3Selected) totalCredits += 3;
                if (subject4Selected) totalCredits += 4;

                // Check if total credits exceed the limit
                if (totalCredits > 24) {
                    Toast.makeText(EnrollmentMenuActivity.this, "Credit limit exceeded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EnrollmentMenuActivity.this, "Your enrollment has been submitted", Toast.LENGTH_SHORT).show();
                    // Pass data to EnrollmentSummaryActivity
                    Intent intent = new Intent(EnrollmentMenuActivity.this, EnrollmentSummaryActivity.class);
                    intent.putExtra("totalCredits", totalCredits);
                    intent.putExtra("IOTSelected", subject1Selected);
                    intent.putExtra("CyberSecuritySelected", subject2Selected);
                    intent.putExtra("AISelected", subject3Selected);
                    intent.putExtra("GameDevelopmentSelected", subject4Selected);
                    startActivity(intent);
                }
            }
        });
    }
}
