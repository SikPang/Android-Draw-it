package com.example.creativity_minigames_team1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;

public class ScoreActivity extends Activity {
    public static int gameNumber=0;
    Intent intent;
    public SQLiteDatabase sqlDB;
    public DBHelpers myHelper;
    private int score=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);
        myHelper=new DBHelpers(this);
        TextView edtNum = (TextView)findViewById(R.id.edtNum);
        TextView edtID = (TextView)findViewById(R.id.edtID);
        TextView edtScore = (TextView)findViewById(R.id.edtScore);

        SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        TextView txtScore = (TextView)findViewById(R.id.TxtScore);
        score = prefs.getInt("score", 0);
        txtScore.setText("점수 : "+ score);

        sqlDB=myHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO Score_info VALUES ('" +
                MainActivity.ID+"','" +
                score+"','" +
                gameNumber+"');");
        sqlDB.close();

        sqlDB=myHelper.getReadableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT * FROM Score_info WHERE number=" + gameNumber + " ORDER BY uScore DESC;";
        cursor = sqlDB.rawQuery(selectQuery,null);

        String strNum = "";
        String strID = "";
        String strScore = "";
        int i =0;
        while(cursor.moveToNext()){
            i++;
            strNum+=i + "\r\n";
            strID+=cursor.getString(0) + "\r\n";
            strScore+= cursor.getInt(1) + "\r\n";

            if(i>=5) break;
        }
        edtNum.setText(strNum);
        edtID.setText(strID);
        edtScore.setText(strScore);

        cursor.close();

        Button btReplay = (Button)findViewById(R.id.btReplay);
        btReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameNumber) {
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

        Button btToSelect = (Button)findViewById(R.id.btToSelect);
        btToSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-----------ver2 캐시 파일삭제 (적당한곳에 삽입)
                try {
                    String imgpath = getCacheDir() + "/" + "drawing_player.PNG";
                    File file = new File(imgpath);
                    file.delete();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "최초 시작", Toast.LENGTH_SHORT).show();
                }
                //--------ver2 캐시 파일삭제(적당한곳에 삽입)


                intent = new Intent(getApplicationContext(), SelectGameActivity.class);
                startActivity(intent);
            }
        });
    }

    public class DBHelpers extends SQLiteOpenHelper {
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Score_info(uID TEXT, uScore INTEGER, number INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Score_info");
            onCreate(db);
        }

        public DBHelpers(Context context) {
            super(context, "ScoreDB", null, 1);
        }

    }
}
