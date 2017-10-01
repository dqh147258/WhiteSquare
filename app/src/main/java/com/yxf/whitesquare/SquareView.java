package com.yxf.whitesquare;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 2017/5/12.
 */
public class SquareView extends View {
    public int width = 0, height = 0;
    private int sWidth, sHeight;
    private int topOffset;
    private List<int[]> list = new ArrayList<int[]>();

    private static final int MAX_COUNT_Y = 5;
    private static final int MAX_Count_X = 4;

    private boolean stop = false;
    private int score = 0;

    int lastX, lastY;

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        clear();
    }

    public SquareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < MAX_COUNT_Y; i++) {
            int[] line = list.get(i);
            for (int j = 0; j < MAX_Count_X; j++) {
                if (line[j] == 0) {
                    paint.setColor(Color.WHITE);
                } else if (line[j] == 1) {
                    paint.setColor(Color.BLACK);
                } else {
                    paint.setColor(Color.RED);
                }
                canvas.drawRect(j * sWidth, topOffset + i * sHeight, j * sWidth + sWidth, topOffset + i * sHeight + sHeight, paint);
                if (i == MAX_COUNT_Y - 1 && j != 0) {
                    paint.setColor(Color.BLACK);
                    canvas.drawLine(j * sWidth, 0, j * sWidth, height, paint);
                }
            }
            if (i != 0) {
                paint.setColor(Color.BLACK);
                canvas.drawLine(0, topOffset + i * sHeight, width, topOffset + i * sHeight, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        sWidth = width / MAX_Count_X;
        sHeight = height / (MAX_COUNT_Y - 1);
        topOffset = -sHeight;
    }

    public void updateList() {
        int[] line;
        line = list.get(list.size() - 1);
        for (int j = 0; j < line.length; j++) {
            if (line[j] == 1) {
                stop = true;
            }
        }
        list.remove(list.size() - 1);
        line = new int[MAX_Count_X];
        for (int i = 0; i < line.length; i++) {
            line[i] = 0;
        }
        /*int[] black = new int[2];
        black[0] = (int) (4 * Math.random());
        black[1] = 2 + (int) (4 * Math.random());
        if (black[0] < 2) {
            line[black[0]] = 1;
        }
        if (black[1] < 4) {
            line[black[1]] = 1;
        }
        if (black[0] > 1 && black[1] > 3) {
            int b = (int) (4 * Math.random());
            line[b] = 1;
        }*/
        int b = (int) (4 * Math.random());
        line[b] = 1;
        list.add(0, line);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:

            case MotionEvent.ACTION_DOWN:
                int id = event.getActionIndex();
                x = event.getX(id);
                y = event.getY(id);
                lastX = (int) x;
                lastY = (int) y;
                if (!stop) {
                    int i = (int) ((y - topOffset) / sHeight);
                    int j = (int) (x / sWidth);
                    int[] line = list.get(i);
                    if (line[j] == 1) {
                        line[j] = 0;
                        score++;
                    } else if (line[j] == 0) {
                        if (y - i * sHeight > sHeight / 2) {
                            if (i < list.size() - 1) {
                                if (list.get(i + 1)[j] == 1) {
                                    list.get(i + 1)[j] = 0;
                                    score++;
                                    return true;
                                }
                            }
                        } else {
                            if (i > 0) {
                                if (list.get(i - 1)[j] == 1) {
                                    list.get(i - 1)[j] = 0;
                                    score++;
                                    return true;
                                }
                            }
                        }
                        line[j] = 2;
                        stop = true;
                    }
                }
                break;
        }
        return true;
    }

    public void setOffset(int topOffset) {
        this.topOffset = topOffset;
        invalidate();
    }

    public int getTopOffset() {
        return topOffset;
    }

    public int getsWidth() {
        return sWidth;
    }

    public int getsHeight() {
        return sHeight;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean getStop() {
        return stop;
    }

    public int getScore() {
        return score;
    }

    public void clear() {
        stop = false;
        topOffset = -sHeight;
        score = 0;
        list.clear();
        for (int i = 0; i < MAX_COUNT_Y; i++) {
            int[] line = new int[MAX_Count_X];
            for (int j = 0; j < line.length; j++) {
                line[j] = 0;
            }
            list.add(line);
        }
        invalidate();
    }
}
