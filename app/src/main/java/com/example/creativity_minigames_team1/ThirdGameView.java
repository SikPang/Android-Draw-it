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
import java.util.Random;

public class ThirdGameView extends SurfaceView implements Runnable {   // SurfaceView 사용

    private Thread thread;
    private boolean isPlaying;
    private Background background;
    private Player player;
    private Lion lion;
    private Buffalo buffs[];
    private Log log[];
    private int screenX, screenY;
    private Paint paint;
    private Random random;
    int river[] = {0, 0};
    public static boolean isGameOver = false;
    public int score = 0;
    public int stage = 1;
    public int cnt = 0;
    private ThirdGameActivity activity;
    private Score showScore;
    private boolean isSplashed = false;
    private SharedPreferences prefs;
    private float lionSpeed;
    private float buffSpeed;
    private float logSpeed;
    private float playerSpeed;


    public ThirdGameView(ThirdGameActivity activity, int screenX, int screenY, String imgpath) {
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

        buffs = new Buffalo[6];
        for (int i = 0; i < buffs.length; i++) {
            buffs[i] = new Buffalo(screenX, screenY, getResources());
        }
        lion = new Lion(screenX, screenY, getResources());
        log = new Log[2];
        for (int i = 0; i < log.length; i++) {
            log[i] = new Log(screenX, screenY, getResources());
        }

        player = new Player(screenX, screenY, getResources(), imgpath);
        background = new Background(screenX, screenY, getResources());

        lionSpeed=screenY/34;
        buffSpeed=screenY/80;
        logSpeed=screenY/160;
        playerSpeed=screenY/20;
    }

    @Override
    public void run() { // 계속 반복
        while (isPlaying) {
            update();   // 계속 화면을 업데이트할 요소들을 담을 함수
            draw();     // 업데이트 후 화면을 그리는 함수
            sleep();    // 조금의 sleep을 추가하여 너무 바르게 지나가는 것을 방지함
        }
    }

    private void init() {    // 오브젝트 위치 초기화 함수
        for (int i = 0; i < buffs.length; i++) {
            if (i < 3) {
                buffs[i].y = -screenY / 3;
                buffs[i].x = -1 * buffs[i].width * ((i + 1));
            } else {
                buffs[i].y = -screenY / 5;
                buffs[i].x = -1 * buffs[i].width * ((i + 1));
            }
        }
        for (int i = 0; i < log.length; i++) {
            log[i].y = -screenY;
            log[i].x = screenX;
        }
        lion.x = screenX * 4;
        lion.y = -screenY;
        river[0] = -100;
        river[1] = -100;
    }

    private void update() {
        if (player.y <= 100) {  // 스테이지 클리어 관리
            if (stage != 4) {   // 다음 단계
                stage++;
                player.y = screenY/6*5;
                cnt = 0;
                score += 1000;
            } else {           // 모두 클리어
                score += 2000;
                isGameOver = true;
                return;
            }
        }
        switch (stage) {      // 레벨 디자인
            case 1:         // 스테이지 1
                if (cnt == 0) {    // 시작할 때 한 번만 초기화 하기 위해서
                    init();     // 오브젝트 위치 초기화
                    background.background = BitmapFactory.decodeResource(getResources(), R.drawable.background4);
                    background.background = Bitmap.createScaledBitmap(background.background, screenX, screenY, false);
                    for (int i = 0; i < buffs.length; i++) {    // 물소
                        if (i < 3)
                            buffs[i].y = screenY * 0.5f;
                        else
                            buffs[i].y = screenY * 0.2f;
                    }
                }
                break;
            case 2:         // 스테이지 2
                if (cnt == 0) {
                    init();
                    background.background = BitmapFactory.decodeResource(getResources(), R.drawable.background6);
                    background.background = Bitmap.createScaledBitmap(background.background, screenX, screenY, false);
                    river[0] = (int) (screenY * 0.386); // 923;
                    river[1] = (int) (screenY * 0.512); // 1223;
                    for (int i = 0; i < buffs.length; i++) {    // 물소
                        if (i < 3)
                            buffs[i].y = screenY * 0.6f;
                        else
                            buffs[i].y = screenY * 0.15f;
                    }
                }
                log[0].y = screenY * 0.32f;
                if (log[0].x < -log[0].width) {     // 통나무가 화면 밖으로 나가면
                    log[0].x = screenX;             // 통나무 위치 초기화
                }
                log[0].x -= logSpeed;                     // 통나무 속도
                break;
            case 3:         // 스테이지 3
                if (cnt == 0) {
                    init();
                    background.background = BitmapFactory.decodeResource(getResources(), R.drawable.background5);
                    background.background = Bitmap.createScaledBitmap(background.background, screenX, screenY, false);
                }
                for (int i = 0; i < buffs.length; i++) {    // 물소
                    if (i < 3)
                        buffs[i].y = screenY * 0.65f;
                    else
                        buffs[i].y = screenY * 0.12f;
                }
                lion.y = screenY * 0.375f;
                break;
            case 4:         // 스테이지 4
                if (cnt == 0) {
                    init();
                    background.background = BitmapFactory.decodeResource(getResources(), R.drawable.background7);
                    background.background = Bitmap.createScaledBitmap(background.background, screenX, screenY, false);
                    river[0] = (int) (screenY * 0.3);  // 723
                    river[1] = (int) (screenY * 0.56); // 1323
                    log[0].y = screenY * 0.22f;
                    log[1].y = screenY * 0.42f;
                    lion.x = screenX;
                    lion.y = screenY * 0.65f;
                    for (int i = 0; i < buffs.length; i++) {    // 물소
                        if (i < 3)
                            buffs[i].y = screenY * 0.05f;
                        else
                            buffs[i].y = -screenY;
                    }
                }
                for (int i = 0; i < log.length; i++) {
                    if (i == 0) {           // 통나무 두개는 서로 반대로 움직임
                        if (log[i].x < -log[i].width) {
                            log[i].x = screenX;
                        }
                        log[i].x -= logSpeed;     // 하나는 왼쪽으로
                    } else {
                        if (log[i].x > screenX) {
                            log[i].x = -log[i].width;
                        }
                        log[i].x += logSpeed;     // 하나는 오른쪽으로
                    }
                }
                break;
        }   // switch 끝

        if (lion.x < -lion.width) {      // lion이 화면 밖으로 나가면
            lion.x = screenX * 2.5f;    // lion 위치 초기화
        }
        if (Rect.intersects(lion.getCollisionShape(), player.getCollisionShape())) {    // lion 충돌처리
            isGameOver = true;
            return;
        }
        lion.x -= lionSpeed;   // lion 속도

        for (int i = 0; i < log.length; i++) {   // 강, 통나무 충돌처리
            if (player.y + player.height <= river[1] && player.y + player.height >= river[0]) { // 캐릭터가 강 위에 있으면
                if (Rect.intersects(log[0].getCollisionShape(), player.getCollisionShape())
                        || Rect.intersects(log[1].getCollisionShape(), player.getCollisionShape())) {
                    // 강 위에 있지만 통나무 위에 있으면 아무 일도 일어나지 않음
                } else {    // 통나무 위에 없으면
                    isSplashed = true;
                    isGameOver = true;
                    return;
                }
            }
        }
        for (int i = 0; i < buffs.length; i++) {
            if (buffs[i].x > screenX) {                          // 물소가 화면 밖으로 나가면
                buffs[i].x = -1 * buffs[i].width - screenX;     // 물소 위치 초기화
            }
            if (Rect.intersects(buffs[i].getCollisionShape(), player.getCollisionShape())) {      // 물소 충돌처리
                isGameOver = true;
                return;
            }
            buffs[i].x += buffSpeed;           // 물소 속도
        }
        //System.out.println(screenY);  2392
        if (cnt == 0) cnt = 1;
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);

            for (Buffalo buffalo : buffs) {
                canvas.drawBitmap(buffalo.buffalo, buffalo.x, buffalo.y, paint);
            }
            canvas.drawBitmap(lion.lion, lion.x, lion.y, paint);
            for (Log log : log) {
                canvas.drawBitmap(log.log, log.x, log.y, paint);
            }


            showScore = new Score(score);
            showScore.Show(score);
            canvas.drawText("" + showScore.showScore, screenX / 2 - 150, 100, paint);
            canvas.drawText("Stage " + stage, 50, 100, paint);

            if (isGameOver) {
                if (isSplashed) {
                    player.zebra = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
                    player.x -= player.width / 2;
                } else {
                    player.zebra = BitmapFactory.decodeResource(getResources(), R.drawable.dead);
                }
                player.width = player.zebra.getWidth() / 4;      // 350
                player.height = player.zebra.getHeight() / 4;    // 841
                player.zebra = Bitmap.createScaledBitmap(player.zebra, player.width, player.height, false);
                canvas.drawBitmap(player.zebra, player.x, player.y, paint);
                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);
                for (int i = 0; i < 25; i++)
                    sleep();

                Dead();
                return;
            }
            canvas.drawBitmap(player.zebra, player.x, player.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
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
                player.y -= playerSpeed;
                score += 100;
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
        Bitmap zebra;

        Player(int screenX, int screenY, Resources res, String imgpath) {
            File files = new File(imgpath);//파일 유무를 확인
            if (files.exists() == true) { //파일이 있을시
                zebra = BitmapFactory.decodeFile(imgpath);
                width = zebra.getWidth() / 3;      // 300/4 = 75
                height = zebra.getHeight() / 3;
            } else { //파일이 없을시
                zebra = BitmapFactory.decodeResource(res, R.drawable.zebra);
                width = zebra.getWidth() / 4;      // 350
                height = zebra.getHeight() / 4;    // 841
            }



            zebra = Bitmap.createScaledBitmap(zebra, width, height, false);

            y = screenY/6*5;
            x = screenX / 2 - width / 2;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y + height / 4, (int) x + width, (int) y + height / 3);
        }
    }

    class Lion {
        public int speed = 20;
        public float x, y;
        public int width, height;
        Bitmap lion;

        Lion(int screenX, int screenY, Resources res) {
            lion = BitmapFactory.decodeResource(res, R.drawable.lion);

            width = lion.getWidth() / 4;      // 293
            height = lion.getHeight() / 4;    // 300

            lion = Bitmap.createScaledBitmap(lion, width, height, false);

            x = screenX;
            y = -screenY;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Buffalo {
        public int speed = 20;
        public float x, y;
        public int width, height;
        Bitmap buffalo;

        Buffalo(int screenX, int screenY, Resources res) {
            buffalo = BitmapFactory.decodeResource(res, R.drawable.buffalo);

            width = buffalo.getWidth() / 6;      // 293
            height = buffalo.getHeight() / 6;    // 300

            buffalo = Bitmap.createScaledBitmap(buffalo, width, height, false);

            x = -1 * width;
            y = screenY / 5;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Log {
        public int speed = 20;
        public float x, y;
        public float width, height;
        Bitmap log;

        Log(int screenX, int screenY, Resources res) {
            log = BitmapFactory.decodeResource(res, R.drawable.log);

/*            width = log.getWidth() * 0.45f;      // 293
            height = log.getHeight() * 0.45f;    // 300*/
            width = screenX/1.5f;
            height = screenX/2;

            log = Bitmap.createScaledBitmap(log, (int) width, (int) height, false);

            x = screenX;
            y = -screenY;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + (int) width, (int) y + (int) height);
        }
    }

    class Background {

        int x = 0, y = 0;
        Bitmap background;

        Background(int screenX, int screenY, Resources res) {
            background = BitmapFactory.decodeResource(res, R.drawable.background4);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        }
    }

}