package com.yxf.whitesquare;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SquareView squareView;
    TextView score;
    TextView begin;

    ObjectAnimator animator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        squareView = (SquareView) findViewById(R.id.square);
        score = (TextView) findViewById(R.id.score);
        begin = (TextView) findViewById(R.id.begin);
        begin.setOnClickListener(this);

    }
    public void display(){
        animator=ObjectAnimator.ofInt(squareView, "offset", -squareView.getsHeight(), 0);
        animator.setDuration(250-squareView.getScore()/3);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(animatorListener);
        animator.addUpdateListener(animatorUpdateListener);
        animator.start();
    }
    Animator.AnimatorListener animatorListener=new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            squareView.updateList();
            if (squareView.getStop()) {
                begin.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).start();
                begin.setClickable(true);
                animator.removeAllListeners();
                animator.start();
            }else{
                display();
            }

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    ValueAnimator.AnimatorUpdateListener animatorUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            score.setText(squareView.getScore()+"");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin:
                begin.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500).start();
                begin.setClickable(false);
                squareView.clear();
                display();
                break;
        }
    }
}
