package com.pstl.gtfo.tablature.generation;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.pstl.gtfo.tablature.generation.Note;
import com.pstl.gtfo.tablature.sandbox.ChordsFinder2;


public class NoteLoader {
	private ArrayList<Note> notes;
	private ArrayList<Integer> lengths;
	private ArrayList<Integer> codes;
	private BufferedReader buf;
	private File pathFile;
	private List<String> chords;
	private static final String LOG_TAG = "NoteLoader: ";
	
	public NoteLoader(File path){
		
		notes=new ArrayList<Note>();
		this.pathFile=path;
		
	}
	
	public NoteLoader(){
		notes = new ArrayList<Note>();
	}
	
	public void setPath(File path){
		this.pathFile = path;
	}
	
	public void addNote(Note note){
		notes.add(note);
	}
	
	public void addNote(String v,double deb,double dur){
		notes.add(new Note(v,deb,dur));
	}
	
	public Note getNote(int pos){
		return notes.get(pos);
	}
	
	public ArrayList<Note> getNotes(){
		return notes;
	}
	
	public List<String> getChords(){
		return chords;
	}
	
	public int size(){
		return notes.size();
	}
	
	public void parserFichier(String name){
		notes = new ArrayList<Note>();
		lengths=new ArrayList<Integer>();
		chords=new ArrayList<String>();
		codes = new ArrayList<Integer>();
		System.out.println("NoteLoader, fichier: " + name);
		int nbErreur = 0;
		try{
			String tab[]=new String[3];
			buf = new BufferedReader(new FileReader(new File(pathFile, name)));
			String strLine;
		
			//int i = 0;
		   
			while((strLine=buf.readLine())!=null){
				System.out.println("fichier " + name +"StrLine = " + strLine);
				String note;
				double deb;
				double dur;
				strLine.replace(',' ,'.');
				tab=strLine.split("/");
				if(tab[0]!=null &&tab[1]!=null && tab[2]!=null){
					System.out.println("fichier " + name + "StrLine premier if");
					try{
						
						
						note = tab[0];
						deb  = Double.valueOf(tab[1]);//Double.parseDouble(tab[1]);
						dur = Double.valueOf(tab[2]);//Double.parseDouble(tab[2]);
						//System.out.println("note = " + note + " deb = " + deb + " dur = " + deb);
						
						/********PROBLEME AVEC LA PREMIERE NOTE: ELLE N'EST PAS VALABLE********/
						if(note!=null && noteValide(note)){//&& noteValide(note) ??
							System.out.println("fichier " + name + "StrLine deuxième if");
							Log.e("parserFichier note : ", note+" "+deb+" "+dur);
							this.addNote(note,deb,dur);
							String namenote = note.substring(0,note.length()-1);
							System.out.println("namenote = " + namenote);
							//Log.i(LOG_TAG,"notenumber: " + ChordsFinder2.nameToCode(namenote));
							codes.add(ChordsFinder2.nameToCode(namenote));
							System.out.println("codes = " + codes);
							//codes.add(Integer.valueOf(1));
						}
					}
					catch (NumberFormatException e) {
						nbErreur++;
					}
				}
			}
			buf.close();
			System.out.println("après boucle: ");
			List<List<Integer>> tmp = new ArrayList<List<Integer>>();
			tmp = ChordsFinder2.processDnC(codes);
			System.out.println("list of lists: " + tmp);
			System.out.println("codes = " + codes);
			
			for(int i=0;i<tmp.size();i++){
				lengths.add(tmp.get(i).size());
				chords.add(ChordsFinder2.belongSameChord(tmp.get(i)));
			}
			System.out.println("chords" + chords);
			System.out.println("lenghts: " + lengths);
		}
		
		catch(FileNotFoundException e){}
		catch (IOException io){}
		catch (ArrayIndexOutOfBoundsException io){}
		Log.e("parserFichier nb erreurs : ", ""+nbErreur);
		/*notes = new ArrayList<Note>();
		for(int i=0; i<100; i++)
		this.addNote("C5", 0, 0);*/
	}
	
	private boolean noteValide(String note){
		if(note.length()==2 && !note.equals("-1")){
			if(TablatureGenerator.getTableGen().containsKey(note)) return true;
		}
		else if(note.length()==3){
			if(TablatureGenerator.getTableGen().containsKey(""+note.charAt(0)+note.charAt(2)) && (note.charAt(1)=='#' || note.charAt(1)=='b'))
				return true;
		}
		return false;
	}

	public List<Integer> getLengths() {
		// TODO Auto-generated method stub
		return lengths;
	}
	
	

}
