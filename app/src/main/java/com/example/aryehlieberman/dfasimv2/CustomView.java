package com.example.aryehlieberman.dfasimv2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aryehlieberman on 4/23/17.
 */

public class CustomView extends View {
    private HashMap<String,DFASim> dfas;
    private String currentDFA;
    private float radius;
    private State selectedState1;
    private State selectedState2;


    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DFASim dfa = new DFASim();
        dfas = new HashMap<>();
        dfas.put("first",dfa);
        currentDFA = "first";
        radius = 60;
    }



    public CustomView(Context context) {
        super(context);
        DFASim dfa = new DFASim();
        dfas = new HashMap<>();
        dfas.put("first",dfa);
        currentDFA = "first";
        radius = 60;
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DFASim dfa = new DFASim();
        dfas = new HashMap<>();
        dfas.put("first",dfa);
        currentDFA = "first";
        radius = 60;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DFASim dfa = new DFASim();
        dfas = new HashMap<>();
        dfas.put("first",dfa);
        currentDFA = "first";
        radius = 60;
    }
//
    @Override
    protected void onDraw(Canvas canvas) {
       DFASim dfa = dfas.get(currentDFA);
        if (dfa == null) return;

        canvas.drawColor(Color.WHITE);
        for(State x: dfa.getStates()){
            if(x == selectedState1 || x == selectedState2) {
                Paint p = new Paint();
                p.setColor(Color.YELLOW);
                canvas.drawCircle(x.getX(), x.getY(), radius, p);

            }
            else {
                canvas.drawCircle(x.getX(), x.getY(), radius, new Paint(Color.BLUE));
            }

        }
        for(Transition t: dfa.getTransitions()){


            canvas.drawLine(t.source.getX(), t.source.getY(), t.destination.getX(), t.destination.getY(), new Paint(Color.BLACK));

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DFASim dfa = dfas.get(currentDFA);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                State state = touchingState(dfa,event.getX(), event.getY());
                if(state == null){
                    if(selectedState1 == null){
                        dfa.getStates().add(new State(event.getX(), event.getY(), "q"));
                    }
                    else {
                        selectedState1 = null;
                        //TODO add a pop up to name the states
                    }
                }
                else {
                    if(selectedState1 == null) selectedState1 = state;
                    else {
                        selectedState2 = state;
                        connectStates(dfa,selectedState1, selectedState2, 'q');

                    }
                }
                invalidate();

        }
        return super.onTouchEvent(event);
    }
    private State touchingState(DFASim dfa, float x, float y){
        for(State state: dfa.getStates()){
            if(distance(state.getX(),state.getY(), x, y) < radius){
                return state;
            }
        }
        return null;
    }
    private double distance(float x1, float y1, float x2, float y2){
        float dx = x1 - x2;
        float dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
    private void connectStates(DFASim dfa, State state1, State state2, char ch){
        dfa.getTransitions().add(new Transition(state1, state2,ch ));
        selectedState1 = null;
        selectedState2 = null;
    }
}
