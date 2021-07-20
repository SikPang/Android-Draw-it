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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FourthGameView extends SurfaceView implements Runnable {   // SurfaceView 사용

    private Thread thread;
    private boolean isPlaying;
    private Background background;
    private Player player;
    private Platform[] pl;
    private Bird bird;
    private int screenX, screenY;
    private Paint paint;
    private Random random;
    public static boolean isGameOver = false;
    public int score = 0;
    private FourthGameActivity activity;
    int h = screenY / 2;
    float dy = 0;
    private Score showScore;
    private SharedPreferences prefs;
    private float firstGetX;
    private float secondGetX;

    public FourthGameView(FourthGameActivity activity, int screenX, int screenY, String imgpath) {
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
        pl = new Platform[15];
        for (int i = 0; i < 15; i++) {
            pl[i] = new Platform(getResources());
            pl[i].x = new Random().nextInt(screenX - pl[i].width);
            pl[i].y = new Random().nextInt(screenY - pl[i].height);
        }
        bird = new Bird(screenX, screenY, getResources());
        player = new Player(screenX, screenY, getResources(), imgpath);
        background = new Background(screenX, screenY, getResources());
        h = screenY / 3;
        firstGetX=0.0f;
        secondGetX=0.0f;
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
        System.out.println(screenY + " " + h);
        dy += 0.7;      // 내려가는 속도
        player.y += dy;
        if (player.y < h) {     // 만약 플레이어가 화면 특정 이상 올라간다면
            for (int i = 0; i < pl.length; i++) {
                player.y = h;    // 플레이어 높이는 고정
                pl[i].y = pl[i].y - dy;   // 구름은 내려감
                if (pl[i].y > screenY) {  // 구름이 화면에서 벗어나면
                    pl[i].y = 0;          // 구름 y 초기화
                    pl[i].x = new Random().nextInt(screenX - 100);  // 구름 x 초기화
                }
            }
            bird.y = bird.y - dy; // 새
            if (bird.y > screenY) {
                bird.y = 0;
                bird.x = new Random().nextInt(screenX - bird.width);
            }
            score += 5;
        }
        for (int i = 0; i < pl.length; i++) {
            if ((player.x + player.width > pl[i].x) && (player.x < pl[i].x + pl[i].width)    // 75, 0, 0, 100
                    && (player.y + player.height > pl[i].y) && (player.y + player.height < pl[i].y + 30)
                    && dy > 0) {
                dy = -35;
            }
        }
        if (player.y > screenY || Rect.intersects(bird.getCollisionShape(), player.getCollisionShape())) {    // 게임 오버 조건
            isGameOver = true;
            return;
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);

            for (Platform platform : pl) {
                canvas.drawBitmap(platform.platform, platform.x, platform.y, paint);
            }
            canvas.drawBitmap(bird.bird, bird.x, bird.y, paint);

            showScore = new Score(score);
            showScore.Show(score);
            canvas.drawText("" + showScore.showScore, screenX / 2 - 150, 100, paint);

            if (isGameOver) {
                player.frog = BitmapFactory.decodeResource(getResources(), R.drawable.dead);
                player.width = player.frog.getWidth() / 4;      // 350
                player.height = player.frog.getHeight() / 4;    // 841
                player.frog = Bitmap.createScaledBitmap(player.frog, player.width, player.height, false);
                canvas.drawBitmap(player.frog, player.x, player.y, paint);
                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);

                for (int i = 0; i < 25; i++)
                    sleep();

                Dead();
                return;
            }
            canvas.drawBitmap(player.frog, player.x, player.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
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
        Bitmap frog;

        Player(int screenX, int screenY, Resources res, String imgpath) {
            File files = new File(imgpath);//파일 유무를 확인
            if (files.exists() == true) { //파일이 있을시
                frog = BitmapFactory.decodeFile(imgpath);
                width = frog.getWidth() / 3;      // 300/4 = 75
                height = frog.getHeight() / 3;    // 300/4 = 75
            } else { //파일이 없을시
                frog = BitmapFactory.decodeResource(res, R.drawable.my_frog);
                width = frog.getWidth() / 4;      // 300/4 = 75
                height = frog.getHeight() / 4;    // 300/4 = 75
            }

            frog = Bitmap.createScaledBitmap(frog, width, height, false);

            y = screenY / 2;
            x = screenX / 2 - width / 2;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Platform {
        public int speed = 20;
        public float x, y = 0;
        public int width, height;
        public int hp = 2;
        public int cnt = 0;
        Bitmap platform;

        Platform(Resources res) {
            platform = BitmapFactory.decodeResource(res, R.drawable.cloud);

            width = platform.getWidth() / 4;      // 200/2 = 100
            height = platform.getHeight() / 4;    // 118/4 = 29.5

            platform = Bitmap.createScaledBitmap(platform, width, height, false);

            y = -2 * height;
            System.out.println(width + " , " + height);
        }
    }

    class Bird {
        public float x, y;
        public int width, height;
        Bitmap bird;

        Bird(int screenX, int screenY, Resources res) {
            bird = BitmapFactory.decodeResource(res, R.drawable.bird);

            width = bird.getWidth() / 6;      // 300/4 = 75
            height = bird.getHeight() / 6;    // 300/4 = 75

            bird = Bitmap.createScaledBitmap(bird, width, height, false);
            x = screenX / 5;
            y = screenY / 5;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Background {

        int x = 0, y = 0;
        Bitmap background;

        Background(int screenX, int screenY, Resources res) {
            background = BitmapFactory.decodeResource(res, R.drawable.background2);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

        }
    }

}