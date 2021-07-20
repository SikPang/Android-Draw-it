package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SecondGameActivity extends Activity {

    private SecondGameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScoreActivity.gameNumber = 2;
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        String imgpath = getCacheDir() + "/" + "drawing_player.PNG";//ver2 캐시 경로
        gameView = new SecondGameView(this, point.x, point.y,imgpath);//ver2 인자추가
        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
