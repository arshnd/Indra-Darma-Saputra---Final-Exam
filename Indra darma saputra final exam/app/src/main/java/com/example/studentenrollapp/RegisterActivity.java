package com.example.studentenrollapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonRegister;
    TextView textViewLogin;
    ExecutorService executor;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (validateInputs(name, email, password)) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity directly
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close RegisterActivity
            }
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        return !name.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6;
    }

    private void registerUser(String name, String email, String password) {
        executor.execute(() -> {
            String result = performRegistration(name, email, password);
            handler.post(() -> {
                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result.equalsIgnoreCase("Registration successful")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close RegisterActivity
                }
            });
        });
    }

    private String performRegistration(String name, String email, String password) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://10.0.2.2/db_operations.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String postData = "register=true&name=" + name + "&email=" + email + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    if (result.toString().contains("Email already exists") || result.toString().contains("Username already exists")) {
                        return "Email or Username already registered. Please use a different one.";
                    }

                    return result.toString();
                }
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
