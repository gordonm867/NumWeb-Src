package com.miloappdev.numweb.popups;

import android.os.Bundle;
import android.widget.PopupWindow;
import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {
    PopupWindow popUp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.popUp = new PopupWindow(this);
    }
}
