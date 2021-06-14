package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thekeval.goalgame.Model.PlayerModel;
import com.thekeval.goalgame.database.DatabaseHandler;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignUp;
    EditText etUsername, etPassword, etRepeatPassword;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp_signUpScreen);
        etUsername = findViewById(R.id.et_userName);
        etPassword = findViewById(R.id.et_password);
        etRepeatPassword = findViewById(R.id.et_repeat_password);

        dbHandler = new DatabaseHandler(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etUsername.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid username", Toast.LENGTH_SHORT).show();
                    etUsername.requestFocus();
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }

                if (etPassword.getText().toString() != etRepeatPassword.getText().toString()) {
                    Toast.makeText(SignUpActivity.this, "Password and repeat password don't match", Toast.LENGTH_SHORT).show();
                    etRepeatPassword.requestFocus();
                    return;
                }

                PlayerModel player = new PlayerModel(etUsername.getText().toString(), etPassword.getText().toString(), 0);
                dbHandler.addPlayer(player);
                Toast.makeText(SignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();

            }
        });

    }


}