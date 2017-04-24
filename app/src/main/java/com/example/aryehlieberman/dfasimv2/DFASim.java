package com.example.aryehlieberman.dfasimv2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class DFASim {
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private State startState;
    private ArrayList<State> acceptStates;

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public ArrayList<State> getAcceptStates() {
        return acceptStates;
    }

    public void setAcceptStates(ArrayList<State> acceptStates) {
        this.acceptStates = acceptStates;
    }

    private HashSet<Character> alphabet;

    public HashSet<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(HashSet<Character> alphabet) {
        this.alphabet = alphabet;
    }

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

    public DFASim(HashSet<Character> alphabet){
        this.alphabet = alphabet;
        states = new ArrayList<>();
        acceptStates = new ArrayList<>();
        transitions = new ArrayList<>();

    }
}
