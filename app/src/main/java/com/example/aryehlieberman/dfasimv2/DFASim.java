package com.example.aryehlieberman.dfasimv2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class DFASim extends Observable implements Runnable {
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private State startState;
    private ArrayList<State> acceptStates;
    private RunState runState;

    public void setInput(String input) {
        this.input = input;
    }

    private String input;

    public State getCurrentState() {
        return currentState;
    }

    private State currentState;

    public boolean isFailed() {
        return failed;
    }

    private boolean failed;

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
        currentState = getStartState();
        failed = false;
        input = "q";

    }

    @Override
    public void run() {
        currentState = startState;
        for(int i = 0; i < input.length(); i++){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Transition t: transitions){
                if(t.getSource() == currentState && t.getCh() == input.charAt(i)){
                    currentState = t.getDestination();
                notifyObservers();


                }
            }

        }
        if(acceptStates.contains(currentState)){
            failed = false;
            notifyObservers();
        }
        else {
            failed = true;
            notifyObservers();
        }
    }
}
