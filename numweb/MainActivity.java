package com.miloappdev.numweb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.miloappdev.numweb.game.GameActivity;

public class MainActivity extends AppCompatActivity {
    boolean global = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0459R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0459R.C0463menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0459R.C0461id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPracClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("global", false);
        startActivity(intent);
    }

    public void onPlayClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("global", true);
        startActivity(intent);
    }
}
