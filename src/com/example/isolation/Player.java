package com.example.isolation;

public abstract class Player {

	
	abstract public int[] getMove(int[][] board);
	
	abstract public void setPosition(int[] position);
	
	abstract public void setClick(int[] click);
	abstract public int[] getPosition();
	abstract public boolean hasClicked();
}
