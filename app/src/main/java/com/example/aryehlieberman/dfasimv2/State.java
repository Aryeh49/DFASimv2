package com.example.aryehlieberman.dfasimv2;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class State {
    private float x;
    private float y;
    private String identifier;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public State(float x, float y, String identifier) {

        this.x = x;
        this.y = y;
        this.identifier = identifier;
    }
    @Override
    public String toString(){
        return identifier;
    }
}
