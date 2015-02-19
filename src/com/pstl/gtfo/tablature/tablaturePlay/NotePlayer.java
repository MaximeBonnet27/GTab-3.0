package com.pstl.gtfo.tablature.tablaturePlay;

import com.pstl.gtfo.sound.NoteDetection;
import com.pstl.gtfo.sound.SyntheSound;
import com.pstl.gtfo.tablature.generation.Note;
import com.pstl.gtfo.tablature.interfaces.INotePlayer;

public class NotePlayer implements INotePlayer {
	NoteDetection noteDetection;
	public NotePlayer(){
		noteDetection = new NoteDetection();
	}

	@Override
	public void playNote(Note note) {
		double freq = noteDetection.noteToFrequency(note.getValue());
		double dur = 1;//note.getDuree();
		SyntheSound synthSound = new SyntheSound(freq, dur);
		synthSound.genTone();
		synthSound.playSound();
	}

}
