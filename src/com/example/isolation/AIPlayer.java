package com.example.isolation;

public class AIPlayer extends Player {

	private int[] position;
	boolean clicked = false;
	private int[] click;
	
	@Override
	public int[] getMove(int[][] board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(int[] position) {
		this.position = position;
		
	}

	@Override
	public void setClick(int[] click) {
		this.click = click;
		
	}

	@Override
	public int[] getPosition() {
		return position;
	}

	@Override
	public boolean hasClicked() {
		return clicked;
	}

}
