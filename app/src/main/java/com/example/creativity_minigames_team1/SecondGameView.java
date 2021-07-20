package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Context;
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
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondGameView extends SurfaceView implements Runnable {   // SurfaceView 사용

    private Thread thread;
    private boolean isPlaying;
    private Background background1, background2;
    private Player player;
    private List<Bullet> bullets;
    private int screenX, screenY, bulletCnt;
    private Paint paint;
    private Enemy[] enemies;
    private Explosion[] explosions;
    private Random random;
    public static boolean isGameOver = false;
    public int score = 0;
    public int cnt[] = {0, 0, 0, 0};
    private SecondGameActivity activity;
    private Score showScore;
    private SharedPreferences prefs;
    private float firstGetX;
    private float secondGetX;
    private float airSpeed;
    private float bulletSpeed;

    public SecondGameView(SecondGameActivity activity, int screenX, int screenY, String imgpath) {
        super(activity);
        this.activity = activity;
        random = new Random();
        bulletCnt = 0;
        this.screenX = screenX;
        this.screenY = screenY;
        isGameOver = false;
        isPlaying = true;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        firstGetX=0.0f;
        secondGetX=0.0f;
        // 배경 2개를 꼬리물려 이동하며 무한하게 이어감

        background2.y = -1 * screenY; // background1의 윗부분에 background2를 이어줌 (화면에 보이지 않음)

        player = new Player(screenX, screenY, getResources(), imgpath);
        bullets = new ArrayList<>();
        enemies = new Enemy[4];
        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy(getResources());
            enemies[i] = enemy;
            enemy.x = 400 * i;
            if (i == 1 || i == 2) {
                enemy.y = -2 * enemy.height;
            }
        }
        explosions = new Explosion[4];
        for (int i = 0; i < 4; i++) {
            Explosion explosion = new Explosion(getResources());
            explosions[i] = explosion;
        }

        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void run() { // 계속 반복
        while (isPlaying) {
            update();   // 계속 화면을 업데이트할 요소들을 담을 함수
            draw();     // 업데이트 후 화면을 그리는 함수
            sleep();    // 조금의 sleep을 추가하여 너무 바르게 지나가는 것을 방지함
            bulletCnt++;
        }
    }

    private void update() {
        background1.y += 10;        // background1이 점점 아래로 내려감
        background2.y += 10;        // background2도 점점 아래로 내려감
        // background2는 background1의 위에 그려지므로 2개가 이어진 기다란 그림이 동시에 내려감을 뜻함

        if (background1.y > screenY) {   // background1이 화면에서 완전히 벗어났을 때
            background1.y = -1 * screenY; // 다시 background1을 background2의 윗부분에 이어줌으로 인해 반복
        }
        if (background2.y > screenY) {   // background2도 1과 같이 반복하여 1->2->1->2->...무한히 반복
            background2.y = -1 * screenY;
        }

        if (bulletCnt % 7 == 0)
            newBullet();
        List<Bullet> trash = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.y < 0)
                trash.add(bullet);
            bullet.y -= 50;

            for (int i = 0; i < enemies.length; i++) {
                if (Rect.intersects(enemies[i].getCollisionShape(), bullet.getCollisionShape())) {
                    if (enemies[i].hp <= 1) {       // 적 전투기 격추
                        explosions[i].x = enemies[i].x;
                        explosions[i].y = enemies[i].y;
                        enemies[i].y = screenY + 500;
                        bullet.y = -100;
                        score += 200;

                    } else {                  // 적 전투기 체력 깎임
                        enemies[i].hp--;
                        bullet.y = -100;
                    }
                }
            }
        }
        for (Bullet bullet : trash) {
            bullets.remove(bullet);
        }

        for (Enemy enemy : enemies) {
            enemy.y += enemy.speed;
            if (enemy.y > screenY) {
                enemy.speed = random.nextInt(40);                   // 적의 속도 랜덤
                if (enemy.speed < 10)
                    enemy.speed += 10;
                enemy.x = random.nextInt(screenX - enemy.width);    // 적의 위치 랜덤
                enemy.y = -1 * screenY / 3;
                enemy.hp = 2;
            }
            if (Rect.intersects(enemy.getCollisionShape(), player.getCollisionShape())) {              // 적과 충돌 시
                isGameOver = true;       // 게임 오버
                return;
            }
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Enemy enemy : enemies) {
                canvas.drawBitmap(enemy.enemy, enemy.x, enemy.y, paint);
            }
            showScore = new Score(score);
            showScore.Show(score);
            canvas.drawText("" + showScore.showScore, screenX / 2 - 150, 100, paint);

            if (isGameOver) {
                player.air = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);    // 캐릭터 폭발 이미지
                player.width = player.air.getWidth() / 4;
                player.height = player.air.getHeight() / 4;
                player.air = Bitmap.createScaledBitmap(player.air, player.width, player.height, false);
                canvas.drawBitmap(player.air, player.x, player.y, paint);

                isPlaying = false;
                getHolder().unlockCanvasAndPost(canvas);

                for (int i = 0; i < 25; i++)
                    sleep();

                Dead();
                return;
            }
            canvas.drawBitmap(player.air, player.x, player.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            for (int i = 0; i < explosions.length; i++) {
                if (explosions[i].x > 0) {
                    canvas.drawBitmap(explosions[i].explosion, explosions[i].x, explosions[i].y, paint);
                    cnt[i]++;
                    explosions[i].y += 10;
                    if (cnt[i] >= 20) {
                        explosions[i].x = -1 * explosions[i].width;
                        explosions[i].y = -1 * explosions[i].height;
                        cnt[i] = 0;
                    }
                }
            }

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

    public void newBullet() {
        Bullet bullet = new Bullet(getResources());
        bullet.x = player.x + player.width / 2 - bullet.width / 2;
        bullet.y = player.y - player.height / 3;
        bullets.add(bullet);
    }

    public void Dead() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("score", score);
        editor.apply();
        activity.startActivity(new Intent(activity, ScoreActivity.class));
        activity.finish();
    }


    class Player {
        public float x, y;
        public int width, height;
        Bitmap air;

        Player(int screenX, int screenY, Resources res, String imgpath) {
            File files = new File(imgpath);//파일 유무를 확인
            if (files.exists() == true) { //파일이 있을시
                air = BitmapFactory.decodeFile(imgpath);
                width = air.getWidth() / 3;      // 300/4 = 75
                height = air.getHeight() / 3;
            } else { //파일이 없을시
                air = BitmapFactory.decodeResource(res, R.drawable.me);
                width = air.getWidth() / 4;
                height = air.getHeight() / 4;
            }


            air = Bitmap.createScaledBitmap(air, width, height, false);

            y = screenY/6*5;
            x = screenX / 2 - width / 2;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Enemy {

        public int speed = 20;
        public float x, y = 0;
        public int width, height;
        public int hp = 2;
        Bitmap enemy;

        Enemy(Resources res) {
            enemy = BitmapFactory.decodeResource(res, R.drawable.enemy);

            width = enemy.getWidth() / 4;
            height = enemy.getHeight() / 4;

            enemy = Bitmap.createScaledBitmap(enemy, width, height, false);

            y = -3 * height;
        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

    class Explosion {

        public float x, y = 0;
        public int width, height;
        Bitmap explosion;

        Explosion(Resources res) {
            explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);

            width = explosion.getWidth() / 4;
            height = explosion.getHeight() / 4;

            explosion = Bitmap.createScaledBitmap(explosion, width, height, false);
            x = -width;
            y = -height;
        }
    }

    class Background {

        int x = 0, y = 0;
        Bitmap background;

        Background(int screenX, int screenY, Resources res) {
            background = BitmapFactory.decodeResource(res, R.drawable.background);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

        }
    }

    class Bullet {
        public float x, y;
        public int width, height;
        Bitmap bullet;

        Bullet(Resources res) {
            bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

            width = bullet.getWidth() / 10;
            height = bullet.getHeight() / 10;

            bullet = Bitmap.createScaledBitmap(bullet, width, height, false);

        }

        Rect getCollisionShape() {
            return new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        }
    }

}