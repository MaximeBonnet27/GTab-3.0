package com.pstl.gtfo.tablature.ChordsFinder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Test {

	public static ArrayList<Note>  fichierMIDIVersListeNotes(String nomFichier){
		ArrayList<Note> notes = new ArrayList<Note>();
		String ligne;
		String[] codesMidi;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			while ((ligne=input.readLine())!=null ) {
				codesMidi=ligne.split("\\s+");
				for (int i=0;i<codesMidi.length;i++){
					notes.add(new Note(Integer.parseInt(codesMidi[i])));
				}
			}
			input.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notes;
	}
	
	public static ArrayList<Note>  fichierNotationVersListeNotes(String nomFichier){
		ArrayList<Note> notes = new ArrayList<Note>();
		String ligne;
		String[] notations;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			while ((ligne=input.readLine())!=null ) {
				notations=ligne.split("\\s+");
				for (int i=0;i<notations.length;i++){
					notes.add(new Note(notations[i]));
				}
			}
			input.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notes;
	}
	
	public static ArrayList<Note> genererListeAleatoire(int taille){
		ArrayList<Note> res = new ArrayList<Note>();
		Random rand = new Random();
		for(int i = 0; i < taille; ++i){
			res.add(new Note(rand.nextInt(12)));
		}
		return res;
	}

	public static void main(String[] args) {
		ArrayList<Note> notes = null;
//		notes = fichierNotationVersListeNotes("toto.txt");
		notes =genererListeAleatoire(100000);
		Algorithm algo = new Algorithm();
		long time = System.currentTimeMillis();
		ArrayList<Accord> accords = algo.compute(notes);
		time = System.currentTimeMillis() - time;

		System.out.println(accords.size() + " accords trouvés en " + time +" ms");
		for (Accord accord : accords) {
			if(accord != null)
			System.out.println(accord);
		}
	}

}
