package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thekeval.goalgame.Model.PlayerModel;
import com.thekeval.goalgame.database.DatabaseHandler;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = "ScoreActivity";

    Button btnNewGame;
    TextView txtPlayer1, txtPlayer2, txtPlayer3;
    TextView txtGameWon, txtScore;

    DatabaseHandler dbHandler;

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//    }

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

        dbHandler = new DatabaseHandler(this);

        ArrayList<PlayerModel> top3Players = dbHandler.getTop3Players();

        if (top3Players.size() > 0) {
            for (int i = 0; i < top3Players.size(); i++) {
                if (i == 0) {
                    txtPlayer1.setText(top3Players.get(i).name + ":  " + top3Players.get(i).highestScore);
                }
                else if (i == 1) {
                    txtPlayer2.setText(top3Players.get(i).name + ":  " + top3Players.get(i).highestScore);
                }
                else if (i == 2) {
                    txtPlayer3.setText(top3Players.get(i).name + ":  " + top3Players.get(i).highestScore);
                }
            }

        }

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(ScoreActivity.this, GameActivity.class);
                startActivity(newGameIntent);
                finish();
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(backIntent);
        finish();
    }
}