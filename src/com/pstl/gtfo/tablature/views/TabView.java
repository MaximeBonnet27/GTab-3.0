package com.pstl.gtfo.tablature.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.pstl.gtfo.R;
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
public class TabView extends View implements ITablatureView {


	Tablature tab;
	NotePlayer notePlayer;
	ITablatureGenerator generator;
	private int width;
	private int height;
	private int dCase;
	private int dCorde;
	private int x0 = 40;
	private int y0 = 40;
	private int margeBas = 30;
	private int margeWidth = 100;
	private int currentNumNote;
	private Paint cordePaint;
	private Paint readerPaint;
	private Paint cordeSupportPaint;
	private TextPaint caseNumPaint;

	private int delta_Y;
	private int notesPerScreen = 12;
	DisplayMetrics dM;

    //Son tablature
    private ArrayList<Position> notes = new ArrayList<Position>();
    private MediaPlayer mp= new MediaPlayer();

	private HorizontalScrollView scrollView;


	public TabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setWillNotDraw(false);
		tab = new Tablature();
		notePlayer = new NotePlayer();
		dM = new DisplayMetrics();
		initCanvas();
		initParams();
		setFocusable(true);


	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
		tab = new Tablature();
		notePlayer = new NotePlayer();
		dM = new DisplayMetrics();
		initCanvas();
		initParams();
		setFocusable(true);

	}

	public TabView(Context context) {
		super(context);
		this.setWillNotDraw(false);
		tab = new Tablature();
		notePlayer = new NotePlayer();
		dM = new DisplayMetrics();
		initCanvas();
		initParams();
		setFocusable(true);

	}

	private void initCanvas() {
		cordePaint = new Paint();
		cordePaint.setStrokeWidth((float) 3.0);
		readerPaint = new Paint();
		readerPaint.setColor(Color.BLUE);
		readerPaint.setStrokeWidth((float) 3.0);
		cordeSupportPaint = new Paint();
		caseNumPaint = new TextPaint();
		caseNumPaint.setTextSize(70);
		caseNumPaint.setColor(Color.RED);

	}
	/* Never called ? */
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
		this.setBackgroundColor(Color.WHITE);
		// Y = 0
		//canvas.drawLine(x0, 0, x0 + 3000, 0, cordeSupportPaint);
		if (tab != null && tab.getNbPos() > 0) {
			initParams();
			dCorde = (height - y0 - margeBas) / (Position.MAXCORDE - 1);
			//dessin de la barre support des cordes
			canvas.drawLine(x0, y0, x0, height - margeBas, cordeSupportPaint);
			//dessin des cordes

			for(int yi = 2 * delta_Y; yi <= 7 * delta_Y; yi+= delta_Y){
				canvas.drawLine(x0, yi, width + dCase, yi, cordePaint);
			}

			/*int yi = 0;
			for (int i = 0; i < Position.MAXCORDE; i++) {
				yi = y0 + i * dCorde;
				System.out.println("Corde " + yi);
				canvas.drawLine(x0, yi, width + dCase, yi, cordePaint);
			}
			 */
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

			for(int i = 0; i < chords.size(); i++){
				canvas.drawText(chords.get(i), xCour, delta_Y, caseNumPaint);
				if(i != 0)
					canvas.drawLine(xCour, delta_Y, xCour,  7 * delta_Y, cordePaint);
				xCour += lengths.get(i) * dCase;
			}

			/*			for (int i = 0; i < chords.size(); i++) {
				canvas.drawText(chords.get(i), xCour, y0 - 10, caseNumPaint);
				if (i != 0)
					canvas.drawLine(xCour + 8, y0, xCour + 8, yi, cordePaint);
				xCour = xCour + lengths.get(i) * dCase;
				System.out.println("xCour = " + xCour);
			}
			 */
			/*
			 *
			 * */

			//dessin des positions (numero des cases)
			int x = x0, y;
			Position p;
			for (int i = 1; i <= tab.getNbPos(); i++) {

				p = tab.getPosition(i - 1);
                notes.add(p);
				x = x + dCase;
				if (p.getNumCorde() == -1) {

					for (int s = 0; s < Position.MAXCORDE; s++) {
						y = y0 + delta_Y * (1 + s);
						canvas.drawText("S", x, y + 8, caseNumPaint);
					}
				} else {
					y = y0 + delta_Y * (p.getNumCorde() + 1) - margeBas;
					canvas.drawText("" + p.getNumCase(), x, y, caseNumPaint);
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
		delta_Y = height / 8;

		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dM);
		width = (int) (dM.widthPixels / dM.density);
		dCase = width / notesPerScreen;
		this.setMeasuredDimension(dCase * tab.getNbPos(), height);
	}

	public void updateTablature(Tablature tab) {
		System.out.println("TabView.updateTablature()");
		this.tab = tab;
		System.out.println("NbPos de la tab : " + tab.getNbPos());
		postInvalidate();
		this.requestLayout();
		System.out.println("TabView.updateTablature() : après invalidate");
	}

	public void nextNote(HorizontalScrollView hsv){
		this.scrollView = hsv;
		currentNumNote = (currentNumNote + 1) % (tab.getNbPos() + 1);
		System.out.println("CURRENT NOTE " + currentNumNote);
		if (currentNumNote > 0) {
			Note note = ((TablatureGenerator) generator).getNote(currentNumNote - 1);
            Position p = notes.get(currentNumNote);
			if (note != null) {
                System.err.println(note.getValue()+" "+p.getNumCorde()+" "+p.getNumCase());
                String song = ""+p.getNumCorde()+p.getNumCase()+".mp3";
                int sound_id = getContext().getResources().getIdentifier(song, "raw",
                        getContext().getPackageName());
                System.out.println(song +" "+sound_id);
                try {
                    mp = MediaPlayer.create(this, sound_id);
                    if(mp != null) {
                        mp.stop();
                        mp.release();
                    }

                    mp.setDataSource(song);
                    mp.prepare();
                   mp.start();

                }
                catch(IllegalStateException e ) {System.err.println("erreur lecture son1");  }
                catch(IllegalArgumentException e) {System.err.println("erreur lecture son2");}
                catch(IOException e){System.err.println("erreur lecture son3");}
                //Log.e("notePlayer : ", note.getValue());
				//notePlayer.playNote(note);
			}
		}		
		if(currentNumNote == 0){ hsv.scrollTo(0, 0); }
		else{
			hsv.scrollBy(dCase, 0);
		}
		invalidate();
	}
	private void initParams() {
		//currentNumNote = 0;
		initWidth();
	}

	private void initWidth() {
		System.out.println("TabView.initWidth()");
		System.out.println("nbpos -> " + tab.getNbPos());
		this.width = x0 + dCase * tab.getNbPos();
	}

}