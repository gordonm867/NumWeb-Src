package com.miloappdev.numweb.game;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.miloappdev.numweb.C0459R;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class GameActivity extends AppCompatActivity {
    MediaPlayer bad;
    Board board;
    int ems;
    public boolean global;
    volatile FrameLayout.LayoutParams imgParams;
    ColorFilter initColorFilter;
    Tile lastTile;
    int left = 12;
    volatile ImageView myImg;
    volatile FrameLayout.LayoutParams myParams;
    volatile TextView myView;
    TextView myView0;
    MediaPlayer player;
    PopupWindow popupWindow;
    volatile double safetimer;
    volatile boolean started;
    int sum = 0;
    Thread thread;
    Rect[] tilepoints = new Rect[16];
    ImageView[] tiles = new ImageView[16];
    volatile double time;
    TextView[] toFind = new TextView[12];
    volatile int xpostime = 0;
    volatile int ypostime = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(2);
        this.global = getIntent().getBooleanExtra("global", false);
        setContentView((int) C0459R.layout.play);
        try {
            this.thread = new Thread() {
                public void run() {
                    GameActivity gameActivity = GameActivity.this;
                    gameActivity.myView = (TextView) gameActivity.findViewById(C0459R.C0461id.time);
                    GameActivity gameActivity2 = GameActivity.this;
                    gameActivity2.myImg = (ImageView) gameActivity2.findViewById(C0459R.C0461id.TimeIcon);
                    GameActivity.this.myView.setText("0");
                    GameActivity.this.myView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    GameActivity.this.myView.measure(-2, -2);
                    GameActivity gameActivity3 = GameActivity.this;
                    gameActivity3.ems = 8;
                    gameActivity3.myParams = new FrameLayout.LayoutParams(-2, -2);
                    GameActivity.this.imgParams = new FrameLayout.LayoutParams(-2, -2);
                    while (GameActivity.this.xpostime == 0 && GameActivity.this.ypostime == 0 && !Thread.interrupted()) {
                        Thread.yield();
                    }
                    try {
                        Thread.sleep(1);
                        GameActivity.this.myParams.setMargins(GameActivity.this.xpostime - 52, GameActivity.this.ypostime, 0, 0);
                        GameActivity.this.myView.setLayoutParams(GameActivity.this.myParams);
                        GameActivity.this.myView.setVisibility(0);
                        GameActivity.this.imgParams.setMargins(GameActivity.this.xpostime - 92, GameActivity.this.ypostime, 0, 0);
                        GameActivity.this.myImg.setLayoutParams(GameActivity.this.imgParams);
                        GameActivity.this.myImg.setVisibility(0);
                        GameActivity.this.time = (double) System.currentTimeMillis();
                        while (!GameActivity.this.started) {
                            double currentTimeMillis = (double) System.currentTimeMillis();
                            double d = GameActivity.this.time;
                            Double.isNaN(currentTimeMillis);
                            if (currentTimeMillis - d >= 1500.0d) {
                                break;
                            }
                            Thread.yield();
                        }
                        GameActivity.this.time = (double) System.currentTimeMillis();
                        Runnable updatetime = new Runnable() {
                            public void run() {
                                Locale locale = Locale.getDefault();
                                double currentTimeMillis = (double) System.currentTimeMillis();
                                double d = GameActivity.this.time;
                                Double.isNaN(currentTimeMillis);
                                String newtext = String.format(locale, "%.1f", new Object[]{Double.valueOf((currentTimeMillis - d) / 1000.0d)});
                                GameActivity.this.myParams.leftMargin = (GameActivity.this.xpostime - (GameActivity.this.ems * (newtext.length() + 1))) - 20;
                                GameActivity.this.imgParams.leftMargin = GameActivity.this.myParams.leftMargin - 40;
                                GameActivity.this.myImg.setLayoutParams(GameActivity.this.imgParams);
                                GameActivity.this.myView.setLayoutParams(GameActivity.this.myParams);
                                GameActivity.this.myView.setText(newtext);
                            }
                        };
                        while (!Thread.interrupted()) {
                            GameActivity.this.runOnUiThread(updatetime);
                            Thread.sleep(100);
                            GameActivity gameActivity4 = GameActivity.this;
                            double currentTimeMillis2 = (double) System.currentTimeMillis();
                            double d2 = GameActivity.this.time;
                            Double.isNaN(currentTimeMillis2);
                            gameActivity4.safetimer = (currentTimeMillis2 - d2) / 1000.0d;
                        }
                    } catch (Exception e) {
                    }
                }
            };
            this.thread.start();
            mkBoard(this.global);
            this.player = MediaPlayer.create(this, C0459R.raw.yay);
            this.bad = MediaPlayer.create(this, C0459R.raw.wrong);
        } catch (Exception e) {
            Thread thread2 = this.thread;
            if (thread2 != null && thread2.isAlive()) {
                this.thread.interrupt();
            }
            finish();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
        }
    }

    public void onStop() {
        super.onStop();
        Thread thread2 = this.thread;
        if (thread2 != null && thread2.isAlive()) {
            this.thread.interrupt();
        }
        finish();
    }

    public void onBackPressed() {
        this.popupWindow = new PopupWindow(((LayoutInflater) getSystemService("layout_inflater")).inflate(C0459R.layout.popup, (ViewGroup) null), -2, -2, true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, C0459R.C0460drawable.white));
        this.popupWindow.showAtLocation(findViewById(C0459R.C0461id.backbutton), 17, 0, 0);
    }

    public void mkBoard(boolean global2) throws InterruptedException, ExecutionException {
        if (!global2) {
            Callable<Board> r3 = new Callable<Board>() {
                public Board call() {
                    return Board.generateBoard();
                }
            };
            FutureTask<Board> future = new FutureTask<>(r3);
            Thread thread2 = new Thread(future);
            thread2.start();
            int[] drawables = {C0459R.C0460drawable.one, C0459R.C0460drawable.two, C0459R.C0460drawable.three, C0459R.C0460drawable.four, C0459R.C0460drawable.five, C0459R.C0460drawable.six, C0459R.C0460drawable.seven, C0459R.C0460drawable.eight, C0459R.C0460drawable.nine};
            int i = 16;
            FrameLayout.LayoutParams[] params = new FrameLayout.LayoutParams[16];
            FrameLayout.LayoutParams[] tparams = new FrameLayout.LayoutParams[12];
            int[] ids = {C0459R.C0461id.ImageViewT0, C0459R.C0461id.ImageViewT1, C0459R.C0461id.ImageViewT2, C0459R.C0461id.ImageViewT3, C0459R.C0461id.ImageViewT4, C0459R.C0461id.ImageViewT5, C0459R.C0461id.ImageViewT6, C0459R.C0461id.ImageViewT7, C0459R.C0461id.ImageViewT8, C0459R.C0461id.ImageViewT9, C0459R.C0461id.ImageViewT10, C0459R.C0461id.ImageViewT11, C0459R.C0461id.ImageViewT12, C0459R.C0461id.ImageViewT13, C0459R.C0461id.ImageViewT14, C0459R.C0461id.ImageViewT15};
            int[] tids = {C0459R.C0461id.ToFind0, C0459R.C0461id.ToFind1, C0459R.C0461id.ToFind2, C0459R.C0461id.ToFind3, C0459R.C0461id.ToFind4, C0459R.C0461id.ToFind5, C0459R.C0461id.ToFind6, C0459R.C0461id.ToFind7, C0459R.C0461id.ToFind8, C0459R.C0461id.ToFind9, C0459R.C0461id.ToFind10, C0459R.C0461id.ToFind11};
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int sheight = displayMetrics.heightPixels;
            int swidth = displayMetrics.widthPixels;
            while (!future.isDone()) {
                Thread.yield();
            }
            this.board = future.get();
            this.myView0 = (TextView) findViewById(C0459R.C0461id.summation);
            this.myView0.setText("0");
            this.myView0.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            FrameLayout.LayoutParams myParams2 = new FrameLayout.LayoutParams(-2, -2);
            int height = 0;
            int x = 0;
            while (x < i) {
                this.tiles[x] = (ImageView) findViewById(ids[x]);
                Callable<Board> call = r3;
                this.tiles[x].setImageResource(drawables[this.board.board[x / 4][x % 4].value - 1]);
                params[x] = new FrameLayout.LayoutParams(-2, -2);
                FutureTask<Board> future2 = future;
                double d = (double) swidth;
                Double.isNaN(d);
                int[] drawables2 = drawables;
                int height2 = height;
                Thread thread3 = thread2;
                double d2 = (double) height2;
                Double.isNaN(d2);
                int size = (int) Math.round(Math.min(d * 0.2d, d2 * 0.15d));
                this.tiles[x].getLayoutParams().height = size;
                this.tiles[x].getLayoutParams().width = size;
                this.tiles[x].measure(-2, -2);
                int height3 = this.tiles[x].getMeasuredHeight() + 7;
                int width = this.tiles[x].getMeasuredWidth() + 7;
                if (x < 12) {
                    this.toFind[x] = (TextView) findViewById(tids[x]);
                    int i2 = size;
                    this.toFind[x].setText(String.format(Locale.getDefault(), "%d", new Object[]{this.board.nums.get(x)}));
                    this.toFind[x].setBackgroundColor(-1);
                    this.toFind[x].setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    this.toFind[x].setGravity(17);
                    this.toFind[x].setWidth(width / 2);
                    this.toFind[x].setHeight(height3 / 2);
                    tparams[x] = new FrameLayout.LayoutParams(-2, -2);
                    int start = (swidth / 2) - (((width / 2) + 10) * 3);
                    int otherstart = ((sheight / 13) - (height3 / 2)) + 10;
                    int i3 = start;
                    int i4 = otherstart;
                    tparams[x].setMargins((((width / 2) + 10) * (x % 6)) + start, otherstart + (((height3 / 2) + 10) * (x / 6)), 0, 0);
                    this.toFind[x].setLayoutParams(tparams[x]);
                    this.toFind[x].setVisibility(0);
                }
                params[x].setMargins((swidth / 2) + (((x % 4) - 2) * width), (sheight / 2) + (((x / 4) - 2) * height3), 0, 0);
                this.tilepoints[x] = new Rect(params[x].leftMargin + 10, params[x].topMargin + 10, (params[x].leftMargin + width) - 10, (params[x].topMargin + height3) - 10);
                this.tiles[x].setLayoutParams(params[x]);
                this.tiles[x].setVisibility(0);
                x++;
                thread2 = thread3;
                future = future2;
                tparams = tparams;
                drawables = drawables2;
                i = 16;
                height = height3;
                r3 = call;
            }
            Callable<Board> call2 = r3;
            FutureTask<Board> futureTask = future;
            int[] iArr = drawables;
            FrameLayout.LayoutParams[] layoutParamsArr = tparams;
            int height4 = height;
            Thread thread4 = thread2;
            this.myView0.measure(-2, -2);
            this.xpostime = swidth;
            this.toFind[11].measure(-2, -2);
            this.ypostime = ((((FrameLayout.LayoutParams) this.toFind[11].getLayoutParams()).topMargin + this.toFind[11].getMeasuredHeight()) + ((sheight / 2) - (height4 * 2))) / 2;
            myParams2.setMargins(swidth / 2, ((((FrameLayout.LayoutParams) this.toFind[11].getLayoutParams()).topMargin + this.toFind[11].getMeasuredHeight()) + ((sheight / 2) - (height4 * 2))) / 2, 0, 0);
            this.myView0.setLayoutParams(myParams2);
            this.myView0.setVisibility(0);
            this.initColorFilter = this.tiles[0].getColorFilter();
            ImageButton home = (ImageButton) findViewById(C0459R.C0461id.backbutton);
            home.setBackgroundColor(0);
            FrameLayout.LayoutParams newParams = new FrameLayout.LayoutParams(-2, -2);
            home.measure(-2, -2);
            newParams.setMargins(50, myParams2.topMargin - 20, 0, 0);
            home.setLayoutParams(newParams);
            home.setVisibility(0);
            TextView instr = (TextView) findViewById(C0459R.C0461id.instructions);
            instr.setText(C0459R.string.shortinstructions);
            instr.setTextColor(Color.rgb(100, 150, 255));
            instr.measure(-2, -2);
            int inheight = instr.getMeasuredHeight();
            int inwidth = instr.getMeasuredWidth();
            FrameLayout.LayoutParams inparams = new FrameLayout.LayoutParams(-2, -2);
            ImageButton imageButton = home;
            FrameLayout.LayoutParams layoutParams = myParams2;
            inparams.setMargins((swidth / 2) - (inwidth / 2), ((((sheight / 2) + (height4 * 2)) + sheight) / 2) - (inheight / 2), 0, 0);
            instr.setLayoutParams(inparams);
            instr.setVisibility(0);
            return;
        }
        TextView myView02 = (TextView) findViewById(C0459R.C0461id.ToFind0);
        myView02.setBackgroundColor(0);
        myView02.setText(C0459R.string.progress);
        myView02.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        myView02.setVisibility(0);
    }

    public void onTileClick(View view) {
        view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        if (event.getAction() == 0 && this.popupWindow != null) {
            if (this.left == 0) {
                finish();
            }
            this.popupWindow.dismiss();
        }
        if (event.getAction() == 0 || event.getAction() == 2) {
            this.started = true;
            int i = 0;
            for (Rect rectangle : this.tilepoints) {
                if (rectangle.contains(x, y) && !this.board.board[i / 4][i % 4].used && (this.sum == 0 || (Math.abs(this.board.board[i / 4][i % 4].xpos - this.lastTile.xpos) <= 1 && Math.abs(this.board.board[i / 4][i % 4].ypos - this.lastTile.ypos) <= 1))) {
                    this.lastTile = this.board.board[i / 4][i % 4];
                    this.tiles[i].setColorFilter(new LightingColorFilter(-1, 2236962));
                    this.tiles[i].setBackgroundColor(0);
                    this.sum += this.board.board[i / 4][i % 4].value;
                    this.board.board[i / 4][i % 4].used = true;
                }
                i++;
            }
        } else if (event.getAction() == 1) {
            int ind = this.board.nums.indexOf(Integer.valueOf(this.sum));
            if (ind >= 0) {
                Drawable background = this.toFind[ind].getBackground();
                int backcolor = 1;
                if (background instanceof ColorDrawable) {
                    backcolor = ((ColorDrawable) background).getColor();
                }
                if (backcolor != -16711936) {
                    this.left--;
                    this.toFind[ind].setBackgroundColor(-16711936);
                    this.player.start();
                    if (this.left == 0) {
                        Thread thread2 = this.thread;
                        if (thread2 != null && thread2.isAlive()) {
                            this.thread.interrupt();
                        }
                        this.popupWindow = new PopupWindow(((LayoutInflater) getSystemService("layout_inflater")).inflate(C0459R.layout.winpopup, (ViewGroup) null), -2, -2, true);
                        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, C0459R.C0460drawable.white));
                        this.popupWindow.showAtLocation(findViewById(C0459R.C0461id.backbutton), 17, 0, 0);
                        Drawable drawable = background;
                        ((TextView) this.popupWindow.getContentView().findViewById(C0459R.C0461id.textView3)).setText(getString(C0459R.string.congratulations_you_win_d_your_time_was_100_1_seconds, new Object[]{Double.valueOf(this.safetimer)}));
                    }
                } else {
                    if (this.sum != 0) {
                        this.bad.start();
                    }
                }
            } else if (this.sum != 0) {
                this.bad.start();
            }
            this.sum = 0;
            for (int j = 0; j < 16; j++) {
                this.board.board[j / 4][j % 4].used = false;
                this.tiles[j].setColorFilter(this.initColorFilter);
            }
        }
        this.myView0.setText(String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(this.sum)}));
        return true;
    }

    public void onBackClick(View view) {
        this.popupWindow = new PopupWindow(((LayoutInflater) getSystemService("layout_inflater")).inflate(C0459R.layout.popup, (ViewGroup) null), -2, -2, true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, C0459R.C0460drawable.white));
        this.popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void onQuitClick(View view) {
        finish();
    }

    public void onResClick(View view) {
        this.popupWindow.dismiss();
    }

    public void onOkayClick(View view) {
        finish();
    }
}
