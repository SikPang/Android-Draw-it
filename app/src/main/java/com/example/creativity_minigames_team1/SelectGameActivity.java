package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectGameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_game_screen);

        Button btLogout = (Button) findViewById(R.id.btLogout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button btToGame1 = (Button) findViewById(R.id.btToGame1);
        btToGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstDrawingActivity.class);
                startActivity(intent);
            }
        });

        Button btToGame2 = (Button) findViewById(R.id.btToGame2);
        btToGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondDrawingActivity.class);
                startActivity(intent);
            }
        });

        Button btToGame3 = (Button) findViewById(R.id.btToGame3);
        btToGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdDrawingActivity.class);
                startActivity(intent);
            }
        });

        Button btToGame4 = (Button) findViewById(R.id.btToGame4);
        btToGame4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FourthDrawingActivity.class);
                startActivity(intent);
            }
        });
    }
}
