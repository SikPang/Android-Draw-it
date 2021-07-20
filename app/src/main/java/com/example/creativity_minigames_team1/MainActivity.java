package com.example.creativity_minigames_team1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends RegistrationActivity {
    int IdFlag = 0;
    int PwFlag = 0;
    public static String ID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //저장소권한 묻기 ver.3

/*        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);*/

        EditText etLoginID = (EditText) findViewById(R.id.etLoginID);
        EditText etLoginPW = (EditText) findViewById(R.id.etLoginPW);
        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo); //ver3
        imgLogo.setImageResource(R.drawable.logo_p); //ver3

        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;
                sqlDB = myHelper.getReadableDatabase();
                cursor = sqlDB.rawQuery("SELECT * FROM Join_info;", null);
                String edt1 = null;
                String edt2 = null;
                String pass1 = null;
                String pass2 = null;
                IdFlag = 0;
                PwFlag = 0;
                while (cursor.moveToNext()) {
                    edt2 = cursor.getString(0);
                    pass2 = cursor.getString(1);
                    edt1 = etLoginID.getText().toString();
                    pass1 = etLoginPW.getText().toString();
                    if (edt2.equals(edt1)) {
                        IdFlag = 1;
                        if (pass2.equals(pass1)) {
                            PwFlag = 1;
                            Toast.makeText(getApplicationContext(), "로그인 되었습니다",
                                    Toast.LENGTH_LONG).show();
                            ID = edt1;
                            Intent intent = new Intent(getApplicationContext(), SelectGameActivity.class);
                            intent.putExtra("ID", edt1);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                }
                cursor.close();
                sqlDB.close();
                if (IdFlag == 0 && PwFlag == 0) {
                    Toast.makeText(getApplicationContext(), "회원 정보가 존재하지 않습니다",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btRegi = (Button) findViewById(R.id.btRegi);
        btRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }
}