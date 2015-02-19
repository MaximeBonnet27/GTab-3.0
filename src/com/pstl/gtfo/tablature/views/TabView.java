package com.pstl.gtfo.tablature.views;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;

import com.pstl.gtfo.tablature.generation.Note;
import com.pstl.gtfo.tablature.generation.NoteLoader;
import com.pstl.gtfo.tablature.generation.TablatureGenerator;
import com.pstl.gtfo.tablature.interfaces.ITablatureGenerator;
import com.pstl.gtfo.tablature.interfaces.ITablatureView;
import com.pstl.gtfo.tablature.tablature.Position;
import com.pstl.gtfo.tablature.tablature.Tablature;
import com.pstl.gtfo.tablature.tablaturePlay.NotePlayer;

/**
 * Created by Kevin Lorant on 18/02/2015.
 */
public class TabView extends HorizontalScrollView implements ITablatureView {


    Tablature tab;
    NotePlayer notePlayer;
    ITablatureGenerator generator;
    private int width;
    private int height;
    private int dCase = 50;
    private int dCorde;
    private int x0 = 40;
    private int y0 = 400;
    private int margeBas = 30;
    private int margeWidth = 100;
    private int currentNumNote;
    private Paint cordePaint;
    private Paint readerPaint;
    private Paint cordeSupportPaint;
    private TextPaint caseNumPaint;


    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setWillNotDraw(false);
        tab = new Tablature();
        notePlayer = new NotePlayer();
        initCanvas();
        initParams();
        setOnClickListener();
        setFocusable(true);

    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        tab = new Tablature();
        notePlayer = new NotePlayer();
        initCanvas();
        initParams();
        setOnClickListener();
        setFocusable(true);

    }

    public TabView(Context context) {
        super(context);
        this.setWillNotDraw(false);
        tab = new Tablature();
        notePlayer = new NotePlayer();
        initCanvas();
        initParams();
        setOnClickListener();
        setFocusable(true);

    }

    private void initCanvas() {
        cordePaint = new Paint();
        readerPaint = new Paint();
        readerPaint.setColor(Color.BLUE);
        cordeSupportPaint = new Paint();
        caseNumPaint = new TextPaint();
        caseNumPaint.setTextSize(30);
        caseNumPaint.setColor(Color.RED);

    }

    public void setHeight(int h) {
        this.height = h;
        postInvalidate();
        this.requestLayout();
    }

    public void setGenerator(ITablatureGenerator gen) {
        this.generator = gen;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("TablatureView.onDraw()");
        this.setBackgroundColor(Color.WHITE);
        // Y = 0
        canvas.drawLine(x0, 0, x0 + 3000, 0, cordeSupportPaint);
        
        if (tab != null && tab.getNbPos() > 0) {
        	initParams();
            dCorde = (height - y0 - margeBas) / (Position.MAXCORDE - 1);
            System.out.println("DCORDE " + dCorde);
            System.out.println("HEIGHT " + height);
            //dessin de la barre support des cordes
            canvas.drawLine(x0, y0, x0, height - margeBas, cordeSupportPaint);
            //dessin des cordes
            int yi = 0;
            for (int i = 0; i < Position.MAXCORDE; i++) {
                yi = y0 + i * dCorde;
                System.out.println("Corde " + yi);
                canvas.drawLine(x0, yi, width + dCase, yi, cordePaint);
            }

			/*AFFICHAGE DES ACCORDS
			 *
			 **/

            NoteLoader n = generator.getNoteLoader();
            List<String> chords = new ArrayList<String>();
            chords = n.getChords();

            List<Integer> lengths = new ArrayList<Integer>();
            lengths = n.getLengths();
            //System.out.println("lengths in TablatureView: " + lengths);
            // System.out.println("chords in TablatureView: " + chords);
            int xCour = 70;
            for (int i = 0; i < chords.size(); i++) {
                canvas.drawText(chords.get(i), xCour, y0 - 10, caseNumPaint);
                if (i != 0)
                    canvas.drawLine(xCour + 8, y0, xCour + 8, yi, cordePaint);
                xCour = xCour + lengths.get(i) * dCase;
                System.out.println("xCour = " + xCour);
            }

			/*
			 *
			 * */

            //dessin des positions (numero des cases)
            int x = x0, y;
            Position p;
            for (int i = 1; i <= tab.getNbPos(); i++) {

                p = tab.getPosition(i - 1);
                x = x + dCase;
                if (p.getNumCorde() == -1) {

                    for (int s = 0; s < Position.MAXCORDE; s++) {
                        y = y0 + dCorde * s;
                        canvas.drawText("S", x, y + 8, caseNumPaint);
                    }
                } else {
                    System.out.println(p.getNumCase() + " " + p.getNumCorde());
                    y = y0 + dCorde * (p.getNumCorde() - 1);
                    System.out.println("Corde + " + p.getNumCorde() + " -> " + y);
                    canvas.drawText("" + p.getNumCase(), x, y + 8, caseNumPaint);
                }

            }
            //dessin de la barre de lecture
            int xL = x0 + currentNumNote * dCase;
            int yL = y0 + (Position.MAXCORDE - 1) * dCorde;
            canvas.drawLine(xL, y0, xL, yL, readerPaint);
            //if(currentNumNote == 0) this.setScrollX(0);
            //else this.setScrollX(xL-x0);
            super.onDraw(canvas);
        }
        //    postInvalidate();
        //  requestLayout();
    }

    
    
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	height = MeasureSpec.getSize(heightMeasureSpec);
    	this.setMeasuredDimension(getWidth(), height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void updateTablature(Tablature tab) {
        System.out.println("TabView.updateTablature()");
        this.tab = tab;
        System.out.println("NbPos de la tab : " + tab.getNbPos());
        postInvalidate();
        this.requestLayout();
        System.out.println("TabView.updateTablature() : après invalidate");
    }

    private void setOnClickListener() {
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentNumNote = (currentNumNote + 1) % (tab.getNbPos() + 1);
                v.invalidate();
                if (currentNumNote > 0) {
                    Note note = ((TablatureGenerator) generator).getNote(currentNumNote - 1);
                    if (note != null) {
                        Log.e("notePlayer : ", note.getValue());
                        notePlayer.playNote(note);
                    }
                }
            }
        });

    }

    private void initParams() {
        currentNumNote = 0;
        initWidth();
    }

    private void initWidth() {
    	System.out.println("TabView.initWidth()");
    	System.out.println("nbpos -> " + tab.getNbPos());
        this.width = x0 + dCase * tab.getNbPos();
    }

    

  /*
   *  @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        System.out.println("TablatureView.onMeasure()"+ widthMeasureSpec + " | " + heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        measureSpec = width + margeWidth;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }


    private int measureHeight(int measureSpec) {
        int result = 0;
        measureSpec = height;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = specSize;
        }
        return result;
    }*/
}