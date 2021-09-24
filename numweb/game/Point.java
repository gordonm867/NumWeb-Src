package com.miloappdev.numweb.game;

public class Point {
	
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        if ((obj instanceof Point) && ((Point) obj).x == this.x && ((Point) obj).y == this.y) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
