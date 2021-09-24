package com.miloappdev.numweb.game;

import com.miloappdev.numweb.BuildConfig;

public class Tile {
    public boolean used = false;
    public int value;
    public int xpos;
    public int ypos;

    public Tile(int value2, int x, int y) {
        this.value = value2;
        this.xpos = x;
        this.ypos = y;
    }

    public String toString() {
        return this.value + BuildConfig.FLAVOR;
    }
}
