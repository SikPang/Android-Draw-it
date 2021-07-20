package com.example.creativity_minigames_team1;

public class Score {
    public String showScore;
    int score;

    public Score(int score){
        this.score=score;
    }
    public void Show(int score){
        if(score==0)
            showScore = ("000000");
        else if(score<100)		// 점수를 6자리로 유지하기 위함
            showScore = ("0000").concat(Integer.toString(score));
        else if(score>=100 && score<1000)
            showScore = ("000").concat(Integer.toString(score));
        else if (score>=1000 && score<10000)
            showScore = ("00").concat(Integer.toString(score));
        else if (score >=10000 && score<100000)
            showScore = ("0").concat(Integer.toString(score));
        else if (score >= 100000)
            showScore = Integer.toString(score);
    }
}
