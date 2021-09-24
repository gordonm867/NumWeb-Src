package com.miloappdev.numweb.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board {
    public Tile[][] board = ((Tile[][]) Array.newInstance(Tile.class, new int[]{4, 4}));
    public ArrayList<Integer> nums;

    public Board(ArrayList<Integer> nums2, String board2) {
        int pos = 0;
        for (int y = 0; y < this.board.length; y++) {
            int x = 0;
            while (true) {
                Tile[][] tileArr = this.board;
                if (x >= tileArr[y].length) {
                    break;
                }
                tileArr[y][x] = new Tile(Integer.parseInt(board2.substring(pos, pos + 1)), x, y);
                pos++;
                x++;
            }
        }
        this.nums = nums2;
        Iterator<Integer> it = nums2.iterator();
        while (it.hasNext()) {
            if (!isValid(0, it.next().intValue())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public boolean isValid(int sum, int check) {
        int y = 0;
        while (y < this.board.length) {
            int x = 0;
            while (true) {
                Tile[][] tileArr = this.board;
                if (x >= tileArr[y].length) {
                    break;
                } else if (recur(sum + tileArr[y][x].value, x, y, check, new ArrayList<>())) {
                    return true;
                } else {
                    x++;
                }
            }
        }
        return false;
    }

    public boolean recur(int sum, int x, int y, int check, ArrayList<Point> used) {
        int i = sum;
        int i2 = x;
        int i3 = y;
        int i4 = check;
        ArrayList<Point> arrayList = used;
        arrayList.add(new Point(i2, i3));
        ArrayList arrayList2 = new ArrayList();
        if (i2 > 0) {
            if (!contains(arrayList, new Point(i2 - 1, i3))) {
                arrayList2.add(this.board[i3][i2 - 1]);
            }
            if (i3 > 0) {
                if (!contains(arrayList, new Point(i2, i3 - 1))) {
                    arrayList2.add(this.board[i3 - 1][i2]);
                }
                if (!contains(arrayList, new Point(i2 - 1, i3 - 1))) {
                    arrayList2.add(this.board[i3 - 1][i2 - 1]);
                }
            }
            if (i3 < 3) {
                if (!contains(arrayList, new Point(i2, i3 + 1))) {
                    arrayList2.add(this.board[i3 + 1][i2]);
                }
                if (!contains(arrayList, new Point(i2 - 1, i3 + 1))) {
                    arrayList2.add(this.board[i3 + 1][i2 - 1]);
                }
            }
        }
        if (i2 < 3) {
            if (!contains(arrayList, new Point(i2 + 1, i3))) {
                arrayList2.add(this.board[i3][i2 + 1]);
            }
            if (i3 > 0) {
                if (i2 == 0 && !contains(arrayList, new Point(i2, i3 - 1))) {
                    arrayList2.add(this.board[i3 - 1][i2]);
                }
                if (!contains(arrayList, new Point(i2 + 1, i3 - 1))) {
                    arrayList2.add(this.board[i3 - 1][i2 + 1]);
                }
            }
            if (i3 < 3) {
                if (i2 == 0 && !contains(arrayList, new Point(i2, i3 + 1))) {
                    arrayList2.add(this.board[i3 + 1][i2]);
                }
                if (!contains(arrayList, new Point(i2 + 1, i3 + 1))) {
                    arrayList2.add(this.board[i3 + 1][i2 + 1]);
                }
            }
        }
        if (arrayList2.isEmpty() || i > i4) {
            arrayList.remove(used.size() - 1);
            return false;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            Tile tile = (Tile) it.next();
            if (tile.value + i == i4) {
                arrayList.add(new Point(tile.xpos, tile.ypos));
                return true;
            }
            Tile tile2 = tile;
            if (recur(i + tile.value, tile.xpos, tile.ypos, check, used)) {
                return true;
            }
        }
        arrayList.remove(used.size() - 1);
        return false;
    }

    public boolean contains(ArrayList<Point> points, Point point) {
        Iterator<Point> it = points.iterator();
        while (it.hasNext()) {
            if (it.next().equals(point)) {
                return true;
            }
        }
        return false;
    }

    public static Board generateBoard() {
        ArrayList<Integer> find = new ArrayList<>();
        StringBuilder build = new StringBuilder();
        int sum = 0;
        int min = 9;
        Random rand = new Random();
        for (int x = 0; x < 16; x++) {
            int random = rand.nextInt(9);
            build.append(random);
            sum += random;
            min = Math.min(random, min);
        }
        for (int x2 = 0; x2 < 12; x2++) {
            int random2 = 0;
            while (true) {
                if (random2 <= sum && random2 >= min && !find.contains(Integer.valueOf(random2))) {
                    break;
                }
                random2 = rand.nextInt(sum);
            }
            find.add(Integer.valueOf(random2));
        }
        int x3 = 0;
        while (x3 < build.toString().length()) {
            try {
                if (find.contains(Integer.valueOf(Integer.parseInt(build.toString().substring(x3, x3 + 1))))) {
                    return generateBoard();
                }
                x3++;
            } catch (Exception e) {
                return generateBoard();
            }
        }
        for (int x4 = 0; x4 < find.size(); x4++) {
            if (sum - find.get(x4).intValue() <= 9 && !find.contains(Integer.valueOf(sum - find.get(x4).intValue()))) {
                return generateBoard();
            }
        }
        return new Board(find, build.toString());
    }
}
