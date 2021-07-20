package com.example.creativity_minigames_team1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.util.Random;

public class FirstGameView extends SurfaceView implements Runnable {   // SurfaceView 사용

    private Thread thread;
    private boolean isPlaying;
    private Background background;
    private Player player;
    Fruit[] fruits;
    private int screenX, screenY;
    private Paint paint;
    private Random random;
    public static boolean isGameOver = false;
    public int score = 0;
    // public int hp=3;
    private FirstGameActivity activity;
    int limit[] = {0, 0, 0, 0, 0, 0, 0};
    int cnt = 0;
    int smashed = 10;
    private Score showScore;
    private SharedPreferences prefs;
    private float firstGetX;
    private float secondGetX;
    private float fallingSpeed;
    //Bitmap hp_img;


    public FirstGameView(FirstGameActivity activity, int screenX, int screenY, String imgpath) {
        super(activity);
        this.activity = activity;
        random = new Random();
        this.screenX = screenX;
        this.screenY = screenY;

        isGameOver = false;
        isPlaying = true;
        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        fruits = new Fruit[7];
        for (int i = 0; i < fruits.length; i++) {
            fruits[i] = new Fruit(getResources(), imgpath);
            fruits[i].x = new Random().nextInt(screenX - 200);
            fruits[i].y = new Random().nextInt(screenY / 3);
            limit[i] = new Random().nextInt(100) + 10;
        }
        player = new Player(screenX, screenY, getResources());
        background = new Background(screenX, screenY, getResources());
        firstGetX=0.0f;
        secondGetX=0.0f;
        fallingSpeed=screenY/120;
        // hp_img = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        //hp_img = Bitmap.createScaledBitmap(hp_img, hp_img.getWidth()/8, hp_img.getHeight()/8, false);
    }

    @Override
    public void run() { // 계속 반복
        while (isPlaying) {
            update();   // 계속 화면을 업데이트할 요소들을 담을 함수
            draw();     // 업데이트 후 화면을 그리는 함수
            sleep();    // 조금의 sleep을 추가하여 너무 바르게 지나가는 것을 방지함
        }
    }

    private void update() {
        if (score >= 2000 && score < 4000) {
            player.girl = BitmapFactory.decodeResource(getResources(), R.drawable.girl_1);
            changePlayerImage();
        } else if (score >= 4000 && score < 6000) {
            player.girl = BitmapFactory.decodeResource(getResources(), R.drawable.girl_2);
            changePlayerImage();
        } else if (score >= 6000) {
            player.girl = BitmapFactory.decodeResource(getResources(), R.drawable.girl_3);
            changePlayerImage();
        }
        System.out.println(screenY);
        for (int i = 0; i < fruits.length; i++) {     // 과일 떨어지는 함수
            if (cnt >= limit[i])
                fruits[i].y += fallingSpeed;  // 떨어지는 속도
            // 플레이어 천장과 과일 바닥이 맞닿으면
            // 플레이어 길이와 과일 길이 좌우 좌표가 맞닿으면
            // 플레이어 : 350,841
            // 과일 : 293,300
            if (Rect.intersects(fruits[i].getCollisionShape(), player.getCollisionShape())) {
                score += 200;
                fruits[i].x = new Random().nextInt(screenX - 200);
                fruits[i].y = new Random().nextInt(screenY / 2);
                limit[i] = new Random().nextInt(100) + cnt + 20;
            }
            if (fruits[i].y > (player.y + (player.height / 2))) {    // 게임 오버 조건
                /*hp-=1;
                fruits[i].x = new Random().nextInt(screenX-200);
                fruits[i].y = new Random().nextInt(screenY/2);
                limit[i] = new Random().nextInt(100)+cnt+20;*/
                smashed = i;
                isGameOver = true;
                return;
            }
        }

        cnt++;
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);
            canvas.drawBitmap(player.girl, player.x, player.y, paint);
            showScore = new Score(score);
            showScore.Show(score);
            canvas.drawText("" + showScore.showScore, screenX / 2 - 150, 100, paint);

            for (int i = 0; i < fruits.length; i++) {
                if (i != smashed)
                    canvas.drawBitmap(fruits[i].fruit, fruits[i].x, fruits[i].y, paint);
            }

            if (isGameOver) {
                fruits[smashed].fruit = BitmapFactory.decodeResource(getResources(), R.drawable.smashed);   // 터진 과일
                fruits[smashed].width = fruits[smashed].fruit.getWidth() / 4;      // 293
                fruits[smashed].height = fruits[smashed].fruit.getHeight() / 4;    // 300
                fruits[smashed].fruit = Bitmap.createScaledBitmap(fruits[smashed].fruit, fruits[smashed].width, fruits[smashed].height, false);
                canvas.drawBitmap(fruits[smashed].fruit, fruits[smashed].x, fruits[smashed].y, paint);

                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);

                for (int i = 0; i < 25; i++)
                    sleep();

                Dead();
                return;
            }


            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void changePlayerImage() {
        player.width = player.girl.getWidth() / 2;      // 350
        player.height = player.girl.getHeight() / 2;    // 841
        player.girl = Bitmap.createScaledBitmap(player.girl, player.width, player.height, false);
    }

    private void sleep() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //player.x = event.getX() - player.width / 2;
                firstGetX=event.getX();     // 처음 찍는 곳
                break;
            case MotionEvent.ACTION_MOVE:
                //player.x = event.getX() - player.width / 2;
                secondGetX=event.getX();    // 두번 째 찍는 곳
                player.x += (secondGetX - firstGetX);
                if(player.x < 0)
                    player.x = 0;
                if(player.x > screenX - player.width)
                    player.x = screenX - player.width;
                firstGetX=event.getX();     // 처음 찍는 곳 업데이트
                break;
        }
        return true;
    }

    public void Dead() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("score", score);
        editor.apply();
        activity.startActivity(new Intent(activity, ScoreActivity.class));
        activity.finish();
    }

    public void resume() {      // 계속하기
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {        // 일시정지
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Player {
        public float x, y;
        public int width, height;
        Bitmap girl;

        Player(int screenX, int screenY, Resources res) {
            girl = BitmapFactory.decodeResource(res, R.drawable.girl);

            width = girl.getWidth() / 2;      // 350
            height = girl.getHeight() / 2;    // 841

            girl = Bitmap.createScaledBitmap(girl, width, height, false);

            y = screenY-(height*1.2f);
            x = screenX / 2 - width / 2;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y + height / 4, (int) x + width, (int) y + height / 3);
        }
    }

    class Fruit {

        public int speed = 20;
        public float x, y = 0;
        public int width, height;
        Bitmap fruit;

        Fruit(Resources res, String imgpath) {
            File files = new File(imgpath);//파일 유무를 확인
            if (files.exists() == true) { //파일이 있을시
                fruit = BitmapFactory.decodeFile(imgpath);
                width = fruit.getWidth() / 3;
                height = fruit.getHeight() / 3;
            } else { //파일이 없을시
                fruit = BitmapFactory.decodeResource(res, R.drawable.my_apple);
                width = fruit.getWidth() / 4;      // 293
                height = fruit.getHeight() / 4;    // 300
            }

            fruit = Bitmap.createScaledBitmap(fruit, width, height, false);
            y = -2 * height;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y + height / 2 + height / 3, (int) x + width, (int) y + height);
        }
    }

    class Background {

        int x = 0, y = 0;
        Bitmap background;

        Background(int screenX, int screenY, Resources res) {
            background = BitmapFactory.decodeResource(res, R.drawable.background3);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

        }
    }

}