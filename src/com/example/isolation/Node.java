package com.example.isolation;

import java.util.ArrayList;

public class Node {
	int[][] board;
	int playerInd;
	int[][] playerPos;
	int playerTurn;
	int numPlayers;
	ArrayList<Node> children;
	
	public Node(int[][] board, int playerInd, int[][] playerPos, int playerTurn){
		this.board = board;
		this.playerInd = playerInd;
		this.playerPos = playerPos;
		this.playerTurn = playerTurn;
		numPlayers = playerPos.length;
	}
	
	public boolean isMax(){
		return playerTurn == playerInd;
	}
	
	public int getPlayerTurn(){
		return playerTurn;
	}
	
	public void generateChildren(){
		int addX = 1;
		int addY = 1;
		int addYd = 1;
		int addXd = 1;
		int [] curPlayerPos = playerPos[playerTurn];
		int curX = curPlayerPos[0];
		int curY = curPlayerPos[1];
		boolean xRowPos = true;
		boolean yRowPos = true;
		boolean diagPos = true;
		boolean diagNeg = true;
		while (diagPos){
			while(diagNeg){
				while(yRowPos){
					while(xRowPos){
						if(board[curY][curX+addX] == 0){
							int[][] board2 = board;
							board2[curY][curX+addX]=1;
							
							children.add(new Node(board2, playerInd, playerPos, (playerTurn+1)%numPlayers));
							addX += addX>0? 1:-1;
						}
						else xRowPos = false;
					}
					if (addX>0){
						addX=-1;
						xRowPos=true;
					}
					else{
						if(board[curY+addY][curX] == 0){
							int[][] board2 = board;
							board2[curY+addY][curX]=1;
							
							children.add(new Node(board2, playerInd, playerPos, (playerTurn+1)%numPlayers));
							addY += addY>0? 1:-1;
						}
						else yRowPos = false;
					}
				}
				if (addY>0){
					addY=-1;
					yRowPos=true;
				}
				else{
					
				}
			}
		}
		
	}

}
