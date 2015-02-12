package com.pstl.gtfo.tablature.interfaces;

import com.pstl.gtfo.tablature.generation.NoteLoader;

public interface ITablatureGenerator {
	public void notifyTablature();
	public void randomConvert();
	public void optDistConvert();
	public void optDistBorneConvert(int bmin, int bmax);
	public NoteLoader getNoteLoader();
}
