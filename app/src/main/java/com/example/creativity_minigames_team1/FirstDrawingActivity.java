package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FirstDrawingActivity extends Activity {
    // < 그림판1 ver2
    public boolean draw_check = false; // ver2 그림그렷는지 확인 없으면 디폴트출력

    class Point {
        float x;
        float y;
        boolean check;
        int color;

        public Point(float x, float y, boolean check, int color) {
            this.x = x;
            this.y = y;
            this.check = check;
            this.color = color;
        }
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setStrokeWidth(20); //선두께
            p.setAntiAlias(true); //선 안티앨리어싱
            p.setStrokeJoin(Paint.Join.ROUND); //선스타일 라운드
            p.setStrokeCap(Paint.Cap.ROUND); //선 끝부분 둥글게
            p.setDither(true); //선 그라디언트


            for (int i = 1; i < points.size(); i++) {
                p.setColor(points.get(i).color); //색상
                if (!points.get(i).check)
                    continue;
                canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    points.add(new Point(x, y, false, color));
                    draw_check = true; // 그림 그렷는지 확인
                case MotionEvent.ACTION_MOVE:
                    points.add(new Point(x, y, true, color));
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            invalidate();
            return true;
        }
    }

    ArrayList<Point> points = new ArrayList<Point>();
    Button draw_red_btn, draw_blue_btn, draw_black_btn, draw_yellow_btn, clearbtn; // 색상 바로 불러옴 (버튼 보류)
    Bitmap captureView1; //ver2 비트맵캡쳐
    LinearLayout drawlinear;
    int color = Color.BLACK;

    //그림판1 ver2 >


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_drawing_screen);
        ImageView imgExample1 = (ImageView) findViewById(R.id.imgExample1);
        imgExample1.setImageResource(R.drawable.my_apple);

        try {
            String imgpath = getCacheDir() + "/" + "drawing_player.PNG";
            File file = new File(imgpath);
            file.delete();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "최초 시작", Toast.LENGTH_SHORT).show();
        }

        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectGameActivity.class);
                startActivity(intent);
            }
        });

        Button btLoad = (Button) findViewById(R.id.btLoad);
        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                LoadActivity.whatIsYourNumber = 1;
                startActivity(intent);
            }
        });

        Button btStart = (Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // <---캐치 ver2
                if (draw_check == true) { //그림을 그렷을시
                    try {
                        drawlinear.destroyDrawingCache(); // 드로잉 캐치 클리어
                    } catch (Exception e) {
                        drawlinear.buildDrawingCache(); //드로잉 캐치 활성화
                        Bitmap captureView = drawlinear.getDrawingCache(); //가져오기
                        captureView1 = captureView;
                    } finally {
                        drawlinear.buildDrawingCache(); //드로잉 캐치 활성화
                        Bitmap captureView = drawlinear.getDrawingCache(); //가져오기
                        captureView1 = captureView;

                        // -------------------------------- 백업3 캐쉬저장 ( 파이널리 내부)
                        try {
                            File storage = getCacheDir();//캐시경로 받아오기
                            String fileName = "drawing_player.PNG"; //저장파일이름
                            File tempFile = new File(storage, fileName); //인스턴스생성
                            tempFile.createNewFile(); //빈파일생성
                            FileOutputStream out = new FileOutputStream(tempFile); //스트림준비
                            captureView1.compress(Bitmap.CompressFormat.PNG, 100, out); //스트림에 비트맵 저장
                            out.flush();//스트림 비우기
                            out.close(); //스트림 닫기

                            //------------ 외부저장 ver.3

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Player";
                            File file = new File(path);

                            if (!file.exists()) {
                                file.mkdirs();
                                Toast.makeText(getApplicationContext(), "폴더 생성", Toast.LENGTH_SHORT).show();
                            }

                            SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date date = new Date();

                            drawlinear.buildDrawingCache();
                            Bitmap captureView3 = drawlinear.getDrawingCache();
                            FileOutputStream fos;
                            try {
                                fos = new FileOutputStream(path + "/Player" + day.format(date) + ".PNG");
                                captureView3.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + "/Player" + day.format(date) + ".PNG")));
                                fos.flush();
                                fos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "이미지 저장 실패", Toast.LENGTH_LONG).show();
                            }

                            //------------ 외부저장 ver.3

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "그림을 다시 그리세요.", Toast.LENGTH_SHORT).show();
                        }
                        // ----------------------------- 백업3

                    }//온클릭 파이널리
                    Intent intent = new Intent(getApplicationContext(), FirstGameActivity.class);
                    startActivity(intent);
                } else { //그림 안그렷을시
                    Intent intent = new Intent(getApplicationContext(), FirstGameActivity.class);
                    startActivity(intent);
                }
                //---캐치ver2 >
            }
        });

//그림판2
        final MyView m = new MyView(this);
        //색변경 버튼
        findViewById(R.id.draw_red_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.RED;
            }
        });
        findViewById(R.id.draw_blue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.BLUE;
            }
        });
        findViewById(R.id.draw_yellow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.YELLOW;
            }
        });
        findViewById(R.id.draw_black_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.BLACK;
            }
        });

        clearbtn = findViewById(R.id.clear_btn);
        drawlinear = findViewById(R.id.draw_frame1); //드로우 프레임 id 확인
        clearbtn.setOnClickListener(new View.OnClickListener() { //지우기 버튼 눌렸을때
            @Override
            public void onClick(View v) {
                points.clear();
                m.invalidate();
                draw_check = false; // ver2 그림 그렷는지 확인
            }
        });
        drawlinear.addView(m);
        //그림판2
    }
}







