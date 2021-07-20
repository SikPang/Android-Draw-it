package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LoadActivity extends Activity {
    public static int whatIsYourNumber = 0; // 몇 번 째 게임인지 기록하는 변수
    public boolean load_check = false;
    Intent intent;

    Button btngallery, btstart;
    ImageView galImage;
    Bitmap img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_drawing_screen);
        btngallery = findViewById(R.id.btGallery);
        galImage = findViewById(R.id.galImage);
        btstart = findViewById(R.id.btStart);

        Button btStart = (Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (whatIsYourNumber) {
                    case 1:
                        intent = new Intent(getApplicationContext(), FirstGameActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), SecondGameActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), ThirdGameActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), FourthGameActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        //-- ver3
        btngallery.setOnClickListener(new View.OnClickListener() { //갤러리 오픈
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        //-- ver3
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (whatIsYourNumber) {
                    case 1:
                        intent = new Intent(getApplicationContext(), FirstDrawingActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), SecondDrawingActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), ThirdDrawingActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), FourthDrawingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (load_check == true) {
                    try {
                        File storage = getCacheDir();//캐시경로 받아오기
                        String fileName = "drawing_player.PNG"; //저장파일이름
                        File tempFile = new File(storage, fileName); //인스턴스생성
                        tempFile.createNewFile(); //빈파일생성
                        FileOutputStream out = new FileOutputStream(tempFile); //스트림준비
                        img.compress(Bitmap.CompressFormat.PNG, 100, out); //스트림에 비트맵 저장
                        out.flush();//스트림 비우기
                        out.close(); //스트림 닫기

                        switch (whatIsYourNumber) {
                            case 1:
                                intent = new Intent(getApplicationContext(), FirstGameActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(getApplicationContext(), SecondGameActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(getApplicationContext(), ThirdGameActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(getApplicationContext(), FourthGameActivity.class);
                                startActivity(intent);
                                break;
                        }//--게임화면


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "캐릭터를 불러오세요", Toast.LENGTH_SHORT).show();
                    }
                } else { //그림 안 불러올시
                    Toast.makeText(getApplicationContext(), "캐릭터를 불러오세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//on craete
    //---ver.3

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    galImage.setImageBitmap(img);// 이미지 표시
                    load_check = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }//갤러리호출

    //---ver.3
}
