package com.example.isolation;

public class HumanPlayer extends Player {

	int boardSize;
	int[] click = {0,0};
	int[] position = {-1,-1};
	int[] newClick;
	boolean clicked = false;
	
	public HumanPlayer(int boardSize) {
		this.boardSize = boardSize;
	}

	@Override
	public int[] getMove(int[][] board) {
		boolean legalMove = false;
		
			
			synchronized(click){
				try {
					click.wait(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				click = newClick;
				int x = click[0];
				int y = click[1];
				if (position[0] == x && position[1] == y)
					legalMove = false;
				else if (position[0] == -1 && board[y][x] == 0)
					legalMove = true;
				else if (position[0] == x){
					legalMove = true;
					int addY = y-position[1]<0? -1:1;
					for (int a=1; a<=Math.abs(y-position[1]); a++){
						if( board[position[1]+addY*a][position[0]]==1)
								legalMove = false;
					}
				}
				else if (position[1] == y){
					legalMove = true;
					int addX = x-position[0]<0? -1:1;
					for (int a=1; a<=Math.abs(x-position[0]); a++){
						if( board[position[1]][position[0]+addX*a]==1)
								legalMove = false;
					}
				}
				else if (Math.abs(position[0]-x) == Math.abs(position[1]-y)){
					legalMove = true;
					int addX = x-position[0]<0? -1:1;
					int addY = y-position[1]<0? -1:1;
					for (int a = 1; a<=Math.abs(x-position[0]); a++){
						if (board[position[1]+addY*a][position[0]+addX*a] == 1)
							legalMove = false;
					}
				}
				//if (!legalMove)
					//MainActivity.notifyInvalidMove();
			}
			
		clicked = legalMove;
		if (legalMove)
			position = click;
		return click;
	}

	@Override
	public void setClick(int[] click) {
		this.newClick=click;
		synchronized(this.click){
			clicked = true;
			this.click.notify();
		}
		
	}

	@Override
	public void setPosition(int[] position) {
		this.position = position;	
	}
	
	@Override
	public int[] getPosition(){
		return position;
	}

	@Override
	public boolean hasClicked() {
		// TODO Auto-generated method stub
		return clicked;
	}

}
