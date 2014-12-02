package com.example.isolation;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	
	ImageView boardView;
	Bitmap boardBitmap;
	int boardDim;
	int N = 5;
	int[][] boardArr;
	int numPlayers = 2;
	int playerTurn;
	int[] viewCoords = new int[2];
	int squareSize;
	Player[] players;
	static TextView playerText;
	boolean [] playerMobility;
	Canvas boardCanvas;
	private long mLastClickTime =0;
	int colorToPaint;
	int[] curPosPaint;
	MyRunnable runnable;
	boolean clicked = true;
    int [] curMove = {0,0};
    Button playAgainBtn;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playAgainBtn = (Button) findViewById(R.id.playAgainBtn);
        boardView = (ImageView) findViewById(R.id.imageView1);
        playerText = (TextView) findViewById(R.id.textView1);
        
        playAgainBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Runnable runnable = new Runnable(){
					
					@Override
					public void run(){
						setup();
						play();
					}
				};
				new Thread(runnable).start();
				
			}
        	
        });
        setup();
       
        Runnable runnable = new Runnable(){

			@Override
			public void run() {
				play();
				
			}
        	
        };
        new Thread(runnable).start();
    }
    
    private void setup(){
    	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				playAgainBtn.setVisibility(Button.INVISIBLE);
				playAgainBtn.setClickable(false);
			}
    		
    	});
    	
    	boardArr = new int[N][N];
        playerMobility = new boolean[]  {true, true};
        players =  new Player[numPlayers];
        playerTurn = numPlayers-1;
        for (int i=0; i<numPlayers; i++)
        	players[i] = new HumanPlayer(N);

        
        boardDim = Math.min(getApplicationContext().getResources().getDisplayMetrics().heightPixels, getApplicationContext().getResources().getDisplayMetrics().widthPixels);
        boardView.setMaxHeight(boardDim);
        boardView.setMaxWidth(boardDim);
        squareSize = boardDim/N;
        boardBitmap = createCheckerBoard(boardDim, N);
        runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boardView.setImageBitmap(boardBitmap);
       
				boardView.refreshDrawableState();
			}
        	
        });

    }
    private void play(){
    	boolean won = false;
    	while (!won){
    		playerTurn = (playerTurn+1)%2;
    		
    		
    		if (playerMobility[playerTurn]){
    			runOnUiThread(new Runnable(){

				@Override
				public void run() {
					playerText.setText("Player " + (playerTurn+1) +"'s turn!");
				} });
    			int[] oldPos = players[playerTurn].getPosition();
    			if (oldPos[0]>=0)
    				fillSquare(oldPos, Color.RED);
    			clicked = false;
    			while (!clicked){
    				curMove = players[playerTurn].getMove(boardArr);
    				clicked = players[playerTurn].hasClicked();
    			}
	    		
	    		boardArr[curMove[1]][curMove[0]] = 1;  

	    		fillSquare(curMove, Color.BLACK);
    			if (oldPos[0]>=0)
    				fillSquare(oldPos, Color.BLUE);
    			runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						boardView.refreshDrawableState();
						boardView.invalidate();
					}
    				
    			});
	    		if (isWin()){
	    			won = true;
	    		}	
    		}
    	}
    	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				playerText.setText("Congratulations, Player " + (playerTurn+1) + "\nYou Win!");
				playAgainBtn.setVisibility(Button.VISIBLE);
				playAgainBtn.setClickable(true);
			}
    		
    	});
    	
    }
   
    
    private boolean isWin(){
    	int numMobile = 0;
    	boolean curMob;
    	for (int p = 0; p<numPlayers; p++){
    		curMob = canMove(p);
    		playerMobility[p]=curMob;
    		numMobile += curMob? 1:0;
    	}
    	return numMobile <=1;
    }
    
    private boolean canMove(int playerNum){
    	Player curPlayer = players[playerNum];
    	int [] curPos = curPlayer.getPosition();
    	int lowerX = Math.max(0, curPos[0]-1);
    	int topX = Math.min(N-1,  curPos[0]+1);
    	int lowerY = Math.max(0,  curPos[1]-1);
    	int topY = Math.min(N-1, curPos[1]+1);
    	boolean canMove = false;
    	for (int x = lowerX; x<=topX;x++){
    		for (int y=lowerY; y<=topY;y++){
    			if( boardArr[y][x]==0)
    				canMove = true;
    		}
    	}
    	return canMove;
    }
    
   /* public static void notifyInvalidMove(){
    	runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				playerText.setText("Invalid move. Try again");
			}
    	
    	});
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
        
    }
    
    private void fillSquare(int[] pos, int color){

    	Rect rect = new Rect(0,0,squareSize, squareSize);
    	rect.offset(pos[0]*squareSize, pos[1]*squareSize);
    	runnable = new MyRunnable(rect, color);
		
    	runOnUiThread(runnable);
    }
    
    
    private Bitmap createCheckerBoard(int pixelSize, int numSquares)
    {
        Bitmap bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);
        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.STROKE);
        fill.setColor(Color.BLACK);
        boardCanvas = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, squareSize, squareSize);
        for (int i=0;i<numSquares;i++){
        	for (int j=0;j<numSquares;j++){
	        	boardCanvas.drawRect(rect, fill);
	        	rect.offset(squareSize, 0);
        	}
        	rect.offset(-1*numSquares*squareSize,squareSize);
        }
        
        return bitmap;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
    	if (SystemClock.elapsedRealtime()-mLastClickTime < 500 || clicked){
    		return true;
    	}
    	clicked = true;
    	mLastClickTime = SystemClock.elapsedRealtime();
    	long temp = SystemClock.currentThreadTimeMillis();
    	Drawable d = boardView.getDrawable();
    	Rect bounds = d.getBounds();
        float x = e.getX()-viewCoords[0];
        float y = e.getY()-viewCoords[1];
        int squareX = (int) (x/squareSize);
        int squareY = (int) (y/squareSize)-1;
        if (squareX <N &&squareX>=0 && squareY<5 && squareY>=0){
        	int[]click = new int[] {squareX, squareY};
       // synchronized(click){
        	players[playerTurn].setClick(click);
        }
       // }
        return true;
    }
    
    private class MyRunnable implements Runnable{
    	private Rect rect;
    	private Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    	private int color;
    	int squareSize;
    	
		@Override
		public void run() {
			fill.setStyle(Paint.Style.FILL);
			fill.setColor(color);
			boardCanvas.drawRect(rect,fill);
			fill.setStyle(Paint.Style.STROKE);
			fill.setColor(Color.BLACK);
			boardCanvas.drawRect(rect,fill);
			boardView.setImageBitmap(boardBitmap);
			
		}
		
		public MyRunnable(Rect rect, int color){
			this.rect = rect;
			this.color = color;
		}
    	
    }
}
