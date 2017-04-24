package com.example.aryehlieberman.dfasimv2;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class Transition {
    private State source;
    private State destination;
    private char ch;

    public State getSource() {
        return source;
    }

    public void setSource(State source) {
        this.source = source;
    }

    public State getDestination() {
        return destination;
    }

    public void setDestination(State destination) {
        this.destination = destination;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public Transition(State source, State destination, char ch) {

        this.source = source;
        this.destination = destination;
        this.ch = ch;
    }
}
