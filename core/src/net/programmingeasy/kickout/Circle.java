package net.programmingeasy.kickout;

import com.badlogic.gdx.graphics.Color;

public class Circle {
    float x, y, rad, w, rot;
    boolean visible = true;
    Color[] c;
    public Circle(float x, float y, float rad, float w, float rot, Color[] c) {
        this.x = x;
        this.y = y;
        this.rad = rad;
        this.w = w;
        this.c = c;
        this.rot = rot;
    }
    public Circle(Circle old) {
        this.x = old.x;
        this.y = old.y;
        this.w = old.w;
        this.c = old.c;
        this.rot = old.rot;
    }
}
