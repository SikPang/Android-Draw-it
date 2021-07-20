package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RegistrationActivity extends Activity {
    public SQLiteDatabase sqlDB;
    public DBHelper myHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        EditText etRegiID = (EditText) findViewById(R.id.etRegiID);
        EditText etRegiPW = (EditText) findViewById(R.id.etRegiPW);
        Button btRegi = (Button) findViewById(R.id.btRegi);
        Button btBack = (Button) findViewById(R.id.btBack);

        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo); //ver3
        imgLogo.setImageResource(R.drawable.logo_p); //ver3

        myHelper = new DBHelper(this);

        btRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //ver3 추가
                if (etRegiID.getText().toString().equals("") || etRegiPW.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 모두 입력 하세요.", Toast.LENGTH_LONG).show();
                } else {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO Join_info VALUES ('" +
                            etRegiID.getText().toString() + "','" +
                            etRegiPW.getText().toString() + "');");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "가입됨", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    sqlDB = myHelper.getReadableDatabase();
                } // -- ver3 추가
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public class DBHelper extends SQLiteOpenHelper {
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Join_info(uID TEXT, uPassword TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Join_info");
            onCreate(db);
        }

        public DBHelper(Context context) {
            super(context, "LoginDB", null, 1);
        }

    }
}
