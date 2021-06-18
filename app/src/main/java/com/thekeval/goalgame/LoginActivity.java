package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thekeval.goalgame.Model.PlayerModel;
import com.thekeval.goalgame.database.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUsername, etPassword;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin_loginScreen);
        etUsername = findViewById(R.id.et_userName);
        etPassword = findViewById(R.id.et_password_loginScreen);

        dbHandler = new DatabaseHandler(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                PlayerModel player = dbHandler.getPlayer(username);
                if (player == null) {
                    Toast.makeText(LoginActivity.this, "No player exist with that name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!password.equals(player.password)){
                    Toast.makeText(LoginActivity.this, "Username and Password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    // Login successful

                    // Create an object of SharedPreferences.
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    //now get Editor
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //put your value
                    editor.putString("userName", player.name);
                    //commits your edits
                    editor.apply();

                    Intent scoreIntent = new Intent(LoginActivity.this, ScoreActivity.class);
                    startActivity(scoreIntent);
                    etPassword.setText("");
                    etUsername.setText("");
                }

//                if (etUsername.getText().toString().equals("keval") && etPassword.getText().toString().equals("123")) {
//
//
//                }

            }
        });

    }
}