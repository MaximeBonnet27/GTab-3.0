package com.pstl.gtfo.tablature.generation;

import java.util.ArrayList;

import com.pstl.gtfo.tablature.tablature.Position;


public class LPosition {
	ArrayList<Position> positions;
	
	public LPosition(){
		this.positions = new ArrayList<Position>();
	}
	
	
	public void add(Position p){
		positions.add(p);
	}
	
	public Position getPos(int i){
		return positions.get(i);
	}

	public int getNbPos(){
		return positions.size();
	}

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("LISTE POSITION : ");
        for(Position p : positions){
            sb.append(p + " ");
        }
        return sb.toString();
    }
}
