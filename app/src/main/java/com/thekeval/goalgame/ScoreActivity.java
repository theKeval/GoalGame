package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    Button btnNewGame;
    TextView txtPlayer1, txtPlayer2, txtPlayer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        btnNewGame = findViewById(R.id.btnNewGame);
        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);
        txtPlayer3 = findViewById(R.id.txtPlayer3);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(ScoreActivity.this, GameActivity.class);
                startActivity(newGameIntent);
            }
        });

    }
}