package com.example.aryehlieberman.dfasimv2;

import java.util.ArrayList;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class DFASim {
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    public DFASim(){
        states = new ArrayList<>();
        transitions = new ArrayList<>();

    }
}
