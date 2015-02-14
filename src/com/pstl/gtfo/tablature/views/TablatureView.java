package com.pstl.gtfo.tablature.views;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import com.pstl.gtfo.tablature.generation.Note;
import com.pstl.gtfo.tablature.generation.NoteLoader;
import com.pstl.gtfo.tablature.generation.TablatureGenerator;
import com.pstl.gtfo.tablature.interfaces.ITablatureGenerator;
import com.pstl.gtfo.tablature.interfaces.ITablatureView;
import com.pstl.gtfo.tablature.tablature.Position;
import com.pstl.gtfo.tablature.tablature.Tablature;
import com.pstl.gtfo.tablature.tablaturePlay.NotePlayer;

public class TablatureView extends View 
	implements ITablatureView {
	
	NotePlayer notePlayer;
	ITablatureGenerator generator;
	Tablature tablature; //Tablature de la vue
	private int dCase = 50;
	private int dCorde;
	private int x0 = 40;
	private int y0 = 40;
	private int margeBas = 30;
	private int margeWidth = 100;
	private int currentNumNote;
	
	//canvas
	private Paint cordePaint;
	private Paint readerPaint;
	private Paint cordeSupportPaint;
	private TextPaint caseNumPaint;
	private int width;
	private int height;
	
	
	
	
	public TablatureView(Context context) {
		super(context);
        this.setWillNotDraw(false);
		initCanvas();
		tablature = new Tablature();
		initParams();
		setOnClickListener();
		notePlayer = new NotePlayer();

	}
	
	private void initParams(){
		currentNumNote = 0;
		initWidth();
	}
	
	private void initWidth(){
		this.width = x0 + dCase * tablature.getNbPos();
	}
	
	public void setHeight(int h){
		this.height = h;
		this.invalidate();
	}
	
	private void initCanvas(){
		cordePaint = new Paint();
		readerPaint = new Paint();
		readerPaint.setColor(Color.BLACK);
		cordeSupportPaint = new Paint();
		caseNumPaint = new TextPaint();
		caseNumPaint.setTextSize(30);
		caseNumPaint.setColor(Color.BLACK);

	}
	
	public void setGenerator(ITablatureGenerator gen){
		this.generator = gen;
	}

	@Override
	public void updateTablature(Tablature tablature) {
		this.tablature = tablature;
		initParams();
        this.setWillNotDraw(false);
		this.invalidate();

	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas){
		if(tablature != null && tablature.getNbPos() > 0){
			dCorde = (height - y0 - margeBas)/(Position.MAXCORDE - 1);
			//dessin de la barre support des cordes
			canvas.drawLine(x0, y0, x0, height - margeBas, cordeSupportPaint);	
			//dessin des cordes
			int yi = 0;
			for(int i=0; i<Position.MAXCORDE; i++){
				yi = y0 + i * dCorde;
				canvas.drawLine(x0, yi, width+30, yi, cordePaint);
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
			//System.out.println("chords in TablatureView: " + chords);
			int xCour = 70; 
			for(int i=0;i<chords.size();i++){
				canvas.drawText(chords.get(i), xCour, y0 - 10, caseNumPaint);
				if(i!=0)
					canvas.drawLine(xCour + 8, y0, xCour + 8, yi, cordePaint);
				xCour = xCour + lengths.get(i)*dCase;
				System.out.println("xCour = " + xCour);
			}
			
			/*
			 * 
			 * */
			
			//dessin des positions (numero des cases)
			int x=x0,y;
			Position p ;
			for(int i=1; i<=tablature.getNbPos(); i++){
				p = tablature.getPosition(i-1);
				x = x + dCase;
				if(p.getNumCorde() == -1){
					for(int s=0; s<Position.MAXCORDE; s++){
						y = y0 + dCorde * s;
						canvas.drawText("S", x, y+8, caseNumPaint);
					}
				}
				else {
					y = y0 + dCorde * (p.getNumCorde() - 1);
					canvas.drawText(""+p.getNumCase(), x, y+8, caseNumPaint);
				}
				
			}
			//dessin de la barre de lecture
			int xL = x0+currentNumNote*dCase;
			int yL = y0 + (Position.MAXCORDE - 1)*dCorde;
			canvas.drawLine(xL, y0, xL, yL, readerPaint);
			//if(currentNumNote == 0) this.setScrollX(0);
			//else this.setScrollX(xL-x0);
		}
		
	}
	
	private void setOnClickListener() {
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentNumNote = (currentNumNote + 1 ) % (tablature.getNbPos() + 1);
				v.invalidate();
				if(currentNumNote > 0){
					Note note = ((TablatureGenerator) generator).getNote(currentNumNote - 1);
					if(note!=null) {
						Log.e("notePlayer : ", note.getValue());
						notePlayer.playNote(note);
					}
				}
			}
		});
		
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
    }
	
	
	

}
