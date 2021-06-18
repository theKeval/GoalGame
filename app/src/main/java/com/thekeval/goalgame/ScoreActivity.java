package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = "ScoreActivity";

    Button btnNewGame;
    TextView txtPlayer1, txtPlayer2, txtPlayer3;
    TextView txtGameWon, txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        btnNewGame = findViewById(R.id.btnNewGame);
        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);
        txtPlayer3 = findViewById(R.id.txtPlayer3);

        txtGameWon = findViewById(R.id.txtGameWon);
        txtScore = findViewById(R.id.txtScore);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(ScoreActivity.this, GameActivity.class);
                startActivity(newGameIntent);
            }
        });

        int score = 0;  // getIntent().getExtras().getInt("score");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("score");
        }

        Log.d(TAG, "onCreate: Score = " + score);
        if (score > 0) {
            txtScore.setText("SCORE: " + score);

            txtGameWon.setVisibility(View.VISIBLE);
            txtScore.setVisibility(View.VISIBLE);

        }

    }
}