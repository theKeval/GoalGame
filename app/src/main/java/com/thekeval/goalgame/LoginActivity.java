package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin_loginScreen);
        etUsername = findViewById(R.id.et_userName);
        etPassword = findViewById(R.id.et_password_loginScreen);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etUsername.getText().toString().equals("keval") && etPassword.getText().toString().equals("123")) {
                    Intent scoreIntent = new Intent(LoginActivity.this, ScoreActivity.class);
                    startActivity(scoreIntent);
                }

            }
        });

    }
}