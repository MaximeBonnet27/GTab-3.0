package com.pstl.gtfo.tablature.testViews;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.pstl.gtfo.R;
import com.pstl.gtfo.tablature.ChordsFinder.Algorithm;
import com.pstl.gtfo.tablature.ChordsFinder.Note;

public class DisplayTabActivity extends Activity {

	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/SoundProject";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_tab);
		Intent intent = getIntent();
		String tabName = intent.getStringExtra(ListTabActivity.EXTRA_MESSAGE);
		TextView textView = (TextView) findViewById(R.id.test_textview);
		textView.setText(getChordsFromFileName(tabName));
	}

	private String getChordsFromFileName(String tabName) {
		Algorithm algo = new Algorithm();
		ArrayList<Note> notes = fichierNotationVersListeNotes(tabName);
		return notes.toString() + "\n" + algo.compute(notes).toString();
	}

	private ArrayList<Note> fichierNotationVersListeNotes(String nomFichier){
		ArrayList<Note> notes = new ArrayList<Note>();
		String ligne;
		String[] notations;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + nomFichier)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			while ((ligne=input.readLine())!=null ) {
				notations=ligne.split("/");
				/*for (int i=0;i<notations.length;i++){
					notes.add(new Note(notations[i]));
				}*/
				System.out.println("NOTATION : " + notations[0]);
				notes.add(new Note(notations[0]));
			}
			input.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("NOTES  : ");
		System.out.println(notes);
		return notes;
	}
	


}
