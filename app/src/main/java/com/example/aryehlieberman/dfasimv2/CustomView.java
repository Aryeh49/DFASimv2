package com.example.aryehlieberman.dfasimv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;



public class CustomView extends View implements Observer {
    private HashMap<String,DFASim> dfas;
    private String currentDFA;
    private float radius;
    private State selectedState1;
    private State selectedState2;
    TextView inputString;

    public void setInputString(TextView inputString) {
        this.inputString = inputString;
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct();
    }
    public CustomView(Context context) {
        super(context);
        construct();
    }
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        construct();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
         construct();
    }
    private void construct(){
        HashSet<Character> alphabet = new HashSet<>();
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        DFASim dfa = new DFASim(alphabet);
        dfa.addObserver(this);
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
        for(Transition t: dfa.getTransitions()){


            drawArrow(canvas, t.getSource().getX(), t.getSource().getY(), t.getDestination().getX(), t.getDestination().getY());

            Paint paint = new Paint();
            paint.setTextSize(30f);
            paint.setTextAlign(Paint.Align.LEFT);
            float x = (t.getDestination().getX() - t.getSource().getX()) / 2 + t.getSource().getX();
            float y = (t.getDestination().getY() - t.getSource().getY()) / 2 + t.getSource().getY();
            canvas.drawText("" + t.getCh(), x,y,paint);


        }
        for(State x: dfa.getStates()){
            if(x == selectedState1 || x == selectedState2 || x == dfa.getCurrentState()) {
                Paint p = new Paint();
                p.setColor(Color.GREEN);
                canvas.drawCircle(x.getX(), x.getY(), radius, p);

            }
            else {
                Paint p = new Paint();
                p.setColor(Color.BLUE);
                canvas.drawCircle(x.getX(), x.getY(), radius, p);
            }
            if(dfa.getAcceptStates().contains(x)) {
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                canvas.drawCircle(x.getX(), x.getY(), radius - 10, p);
            }
            if(x == dfa.getStartState()){
                drawArrow(canvas, x.getX() + radius * 2, x.getY() + radius * 2, (float) (x.getX() + radius*Math.cos(Math.PI / 2)), (float) (x.getY() + radius*Math.cos(Math.PI / 2)));
            }
            Paint paint = new Paint();
            paint.setTextSize(40f);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(x.getIdentifier(), x.getX(), x.getY(), paint);

        }
        AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        if(dfas.get(currentDFA).getRunState() == RunState.REJECTED){
            d.setTitle("Rejected");

            selectedState2 = null;
            selectedState1 = null;
            d.show();
        }
        else if(dfas.get(currentDFA).getRunState() == RunState.ACCEPTED){
            d.setTitle("Accepted");
            selectedState1 = null;
            selectedState2 = null;
            d.show();
        }
        dfa.setRunState(RunState.READY);

    }
    private void drawArrow(Canvas canvas, float srcX, float srcY, float destX, float destY){
        Paint p = new Paint();

        if (srcX == destX && srcY == destY){
            RectF r = new RectF();
            r.set(srcX - radius * 2, srcY + radius, srcX, srcY - radius);
            canvas.drawArc(r, 0, (float) (Math.PI * 2), true, p);
        }
        else {
            double theta = Math.atan2((destY - srcY), (destX - srcX));
            //double theta = destX - srcX > 0 ? Math.atan((destY - srcY) / (destX - srcX)) : Math.PI + Math.atan((destY - srcY) / (destX - srcX));
            float  x2, y2;


            x2 = destX - (float) (radius * Math.cos(theta));
            y2 = destY - (float) (radius * Math.sin(theta));


            canvas.drawLine(srcX, srcY, x2, y2, p);
            double theta1 = Math.PI / 4 + theta;
            double dx = radius * Math.cos(theta1);
            double dy = radius * Math.sin(theta1);
            canvas.drawLine(x2, y2, (float) (x2 - dx), (float) (y2 - dy), p);
            theta1 = -Math.PI / 4 + theta;
            dx = radius * Math.cos(theta1);
            dy = radius * Math.sin(theta1);
            canvas.drawLine(x2, y2, (float) (x2 - dx), (float) (y2 - dy), p);
        }

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final DFASim dfa = dfas.get(currentDFA);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //TODO This isn't working
                final Transition transition = touchingTransition(dfa, event.getX(), event.getY());
                if(transition != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                    builder.setTitle("What do you want to do?");
                    CharSequence options[] = new CharSequence[] {"delete", "set char", "reverse direction"};
                    builder.setItems(options, new AlertDialog.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    dfa.getTransitions().remove(transition);
                                    invalidate();
                                    break;
                                case 1:
                                    dialog.cancel();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    builder1.setTitle("Enter Character");
                                    final EditText input = new EditText(getContext());
                                    builder1.setView(input);
                                    builder1.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            transition.setCh(input.getText().toString().charAt(0));
                                        }
                                    });
                                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    break;
                                case 2:
                                    State temp = transition.getDestination();
                                    transition.setDestination(transition.getSource());
                                    transition.setSource(temp);
                                    invalidate();
                                    break;

                            }
                        }
                    });
                    builder.show();
                }
                final State state = touchingState(dfa,event.getX(), event.getY());
                if(state == null){
                    if(selectedState1 == null){
                        dfa.getStates().add(new State(event.getX(), event.getY(), "q" + dfa.getStates().size()));
                    }
                    else {
                        selectedState1 = null;
                    }
                }
                else {
                    if(selectedState1 == null) selectedState1 = state;
                    else {
                        if(selectedState1 == state){
                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("What do you want to do?");
                            CharSequence options[] = new CharSequence[] {"delete", "name", "set start", "set accept", "loop", "cancel"};
                            builder.setItems(options, new AlertDialog.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            ArrayList<Transition> tr = new ArrayList<>();
                                            for(Transition t: dfa.getTransitions()){
                                                if(t.getSource() == state || t.getDestination() == state){
                                                    tr.add(t);
                                                }
                                            }
                                            for(Transition t: tr){
                                                dfa.getTransitions().remove(t);
                                            }
                                            dfa.getStates().remove(state);
                                            selectedState1 = null;
                                            invalidate();
                                            break;
                                        case 1:
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                            builder1.setTitle("Enter Name");
                                            final EditText input = new EditText(getContext());
                                            builder1.setView(input);
                                            builder1.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    state.setIdentifier(input.getText().toString());
                                                }
                                            });
                                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder1.show();
                                            break;
                                        case 2:
                                            dfa.setStartState(state);
                                            invalidate();
                                            break;
                                        case 3:
                                            dfa.getAcceptStates().add(state);
                                            invalidate();
                                            break;
                                        case 4:
                                            dfa.getTransitions().add(new Transition(state, state, 'q'));
                                            invalidate();
                                            break;
                                        case 5:
                                            dialog.dismiss();
                                            break;
                                    }
                                    selectedState1 = null;
                                }
                            });
                            builder.show();
                        }
                        else {
                            selectedState2 = state;
                            connectStates(dfa, selectedState1, selectedState2);
                        }

                    }
                }
                invalidate();

        }
        return super.onTouchEvent(event);
    }
    private State touchingState(DFASim dfa, float x, float y){
        for(State state: dfa.getStates()){
            if(distance(state.getX(),state.getY(), x, y) < radius * 2){
                return state;
            }
        }
        return null;
    }
    private Transition touchingTransition(DFASim dfa, float x, float y){
        //TODO
        float slope;
        for(Transition transition: dfa.getTransitions()){
            slope = (transition.getDestination().getY() - transition.getSource().getY())/
                    transition.getDestination().getX() - transition.getSource().getX();
            if(Math.abs((double) (y - transition.getSource().getY()) - slope * (x - transition.getSource().getX())) < radius){
                return transition;
            }
        }
        return null;
    }
    private double distance(float x1, float y1, float x2, float y2){
        float dx = x1 - x2;
        float dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
    private void connectStates(DFASim dfa, State state1, State state2){
        //TODO make it deterministic
        final Transition transition = new Transition(state1, state2,'\0' );
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("Enter Character");
        final EditText input = new EditText(getContext());
        builder1.setView(input);
        builder1.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                transition.setCh(input.getText().toString().charAt(0));
            }
        });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.show();
        dfa.getTransitions().add(transition);
        selectedState1 = null;
        selectedState2 = null;
    }
    public void runDFA() {
        selectedState1 = null;
        selectedState2 = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Input String");
        final EditText input = new EditText(this.getContext());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                String in = input.getText().toString();
                inputString.setText(in);
                dfas.get(currentDFA).setInput(in);
                dialog.dismiss();
                Thread a = new Thread(dfas.get(currentDFA));
                a.start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();



    }

    @Override
    public void update(Observable o, Object arg)
    {
        this.postInvalidate();
    }
}
