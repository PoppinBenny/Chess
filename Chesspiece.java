package Assignment1_0;

public class Chesspiece{
	int current_x, current_y;	//current position of each chesspiece. When killed, both become -1
	boolean squareReachable[][] = new boolean[8][8];	//every piece has a 2-D array indicating where it can and cannot go, this will be a prerequisite for move()
	String type;	//indicates which type of piece it is
	
	public Chesspiece(int x_position, int y_position, String type){
		current_x=x_position;
		current_y=y_position;
		this.type=type;
		Chessboard.occupiedBy[x_position][y_position]=this;		//store the reference of a chess piece into occupiedBy[][] upon creation
	}
	
    void move(int x, int y){	//movement function for each piece, assuming squareReachable[][] is already checked
    	assert(x>=0 && x<8 && y>=0 && y<8);
    	Chessboard.occupiedState[current_x][current_y]=0;	//leaves the current square, state=0
    	Chessboard.occupiedBy[current_x][current_y]=null;	//no Chesspiece object on the current square
    	current_x=x;	
    	current_y=y;	//move to the target
    	if(Chessboard.occupiedState[x][y]!=Chessboard.currentTurn && Chessboard.occupiedState[x][y]!=0) //XXXXXXXXXXXXX
    		if(Chessboard.occupiedBy[x][y]!=null){	//these two ifs together indicate the target square has an opponent piece on it, which means we want to kill it
    			Chessboard.occupiedBy[x][y].current_x=-1;
    			Chessboard.occupiedBy[x][y].current_y=-1;
    		}
    	Chessboard.occupiedState[current_x][current_y]=Chessboard.currentTurn;	//change the state of the target square
    	Chessboard.occupiedBy[x][y]=this;	//set the piece object reference on the target
    }
    
    void searchReachablePos(){	//Called by a chess piece. Search and set all the reachable squares according to its type
    		if(type.equals("king"))
    			kingSearch();
    		else if(type.equals("queen")){
    	    	searchOnRowAndCol();
    	    	searchOnDiagonal(); }
    	    else if(type.equals("rook"))
    	    	searchOnRowAndCol();
    	    else if(type.equals("bishop"))
    	    	searchOnDiagonal();
    	    else if(type.equals("knight"))
    			knightSearch();
    	    else if(type.equals("pawn"))
    			pawnSearch();
    }
    
    void kingSearch(){
    	for(int i=0; i<8; i++)
			for(int j=0; j<8; j++){
				boolean inRange = i>=current_x-1 && i<=current_x+1 && j>=current_y-1 && j<=current_y+1		//king can only move 1 square each time
									&& !(i==current_x && j==current_y);
				if(inRange && Chessboard.occupiedState[i][j]!=Chessboard.currentTurn)		//and it can only move when the target square does not has any piece of its own side
					squareReachable[i][j]=true;
				else squareReachable[i][j]=false;	//otherwise the square is not reachable
			}
    }
    
    void knightSearch(){
    	for(int i=0; i<8; i++)
			for(int j=0; j<8; j++){
				boolean inRange = (i==current_x+1&&j==current_y+2) || (i==current_x+1&&j==current_y-2) || (i==current_x-1&&j==current_y+2) ||
						(i==current_x-1&&j==current_y-2) || (i==current_x+2&&j==current_y+1) || (i==current_x+2&&j==current_y-1)||
						(i==current_x-2&&j==current_y+1) || (i==current_x-2&&j==current_y-1);						//knight has 8 choices to move to
				if(inRange && Chessboard.occupiedState[i][j]!=Chessboard.currentTurn)		//and it can only move when the target square does not has any ally piece
					squareReachable[i][j]=true;
				else squareReachable[i][j]=false;	//otherwise the square is not reachable
			}
    }
    
    void pawnSearch(){
    	int opponent = Chessboard.currentTurn==1 ? 2 : 1;	//check its opponent, 2 for black and 1 for white
    	for(int i=0; i<8; i++)
			for(int j=0; j<8; j++){
				int squareBeforeIt1_x = opponent==2 ? current_x+1 : current_x-1;	//since the pawns on each side move in opposite direction, we need two cases
				int squareBeforeIt2_x = opponent==2 ? current_x+2 : current_x-2;
				int originalX = opponent==2 ? 1 : 6;	//the start x-position for two players are 1 and 6 respectively
				
				boolean inRange = current_x==originalX ? //if current x-position is its original x-pos then it has not moved yet, so it's able to move 2 squares
						((i==squareBeforeIt1_x && j==current_y)||
						(i==squareBeforeIt2_x && j==current_y))&&Chessboard.occupiedState[squareBeforeIt1_x][j]==0	//for pawns, no matter it wants to move 1 or 2 squares, there should be no piece in front of it
						:(i==squareBeforeIt1_x && j==current_y)&&Chessboard.occupiedState[i][j]==0;
				if(i==squareBeforeIt1_x && j==current_y+1)				//if there are opponent chess pieces in front of it on an adjacent square, that square is also reachable
					inRange |= Chessboard.occupiedState[i][j]==opponent;
				else if(i==squareBeforeIt1_x && j==current_y-1)
					inRange |= Chessboard.occupiedState[i][j]==opponent;

				if(inRange&&Chessboard.occupiedState[i][j]!=Chessboard.currentTurn)	//if the square is in range there should no ally piece on it
					squareReachable[i][j]=true;
				else squareReachable[i][j]=false;
			}
    }
    
    void searchOnRowAndCol(){	//search for the rows of current x-pos and columns of current y-pos
    	for(int i=0; i<8; i++){
				if(i==current_x){
					searchOnRowAndCol(i, "up");
					searchOnRowAndCol(i, "down");
				}
				if(i==current_y){
					searchOnRowAndCol(i, "left");
					searchOnRowAndCol(i, "right");
				}
    	}
    }
    
    void searchOnRowAndCol(int i, String subPart){	//XorY indicates looping in terms of x-coordinates or y-coor, subPart indicates left/down or right/up
    	int opponent = Chessboard.currentTurn==1 ? 2 : 1;
    	boolean cannotLeapOver=false; //this is an important flag, for that once we find the first piece in the linear search, any squares after it will not be reachable
    	
    	boolean onCol = subPart.equals("up")||subPart.equals("down"),	//if we search up or down then it's column search
    			firstPart = subPart.equals("left")||subPart.equals("down");		//if we search left or down then loop index will be decreasing
    	int onRowOrCol = onCol ? current_x : current_y;		
    	int j = firstPart ? onRowOrCol-1 : onRowOrCol+1;	//j is the loop index

		while(firstPart ? (j>=0) : (j<=7)){	//searching part
    	    int row = onCol ? i : j, 	//if the search is in terms of y-coordinates, then the current row is to be searched, otherwise the current column
    	    	col = onCol ? j : i;
			//System.out.println(row+" "+col+" "+subPart);
			if(cannotLeapOver)	//once the flag becomes true, it means we've already found something in the line, so any latter squares will not be reachable
				squareReachable[row][col]=false;
			else if(Chessboard.occupiedState[row][col]==0){	//if flag is false and the current square is empty, then reachable
				squareReachable[row][col]=true;
				//
			}
			else if(Chessboard.occupiedState[row][col]==Chessboard.currentTurn){	//if we find an ally piece first, that square is not reachable, flag becomes true
				squareReachable[row][col]=false;
				cannotLeapOver=true;
			}
			else if(Chessboard.occupiedState[row][col]==opponent){	//if we find an opponent piece, that square is reachable, flag becomes true
				squareReachable[row][col]=true;
				cannotLeapOver=true;
			}
			if(firstPart) j--; else j++;
		}
		
    }
    
    void searchOnDiagonal(){	//wrapper function for diagonal search in four directions
    	searchOnDiagonal("upLeft");
    	searchOnDiagonal("upRight");
    	searchOnDiagonal("downLeft");
    	searchOnDiagonal("downRight");
    }
    
    void searchOnDiagonal(String subPart){
    	boolean cannotLeapOver=false, loopCondition=false;	//the "cannotLeapOver" flag functions identically as the one in the Row and Column search function, "loopCondition" is to make sure the loop indices are within the chess board
    	int occupiedState=-1,x=-1,y=-1;	//occupiedState is to shorten latter expression, x and y are loop indices
    	
    	for(int i=0; i<8;i++){
    		if(subPart.equals("upLeft")){
    			x=current_x+i; 
    			y=current_y+i;
    			loopCondition= x<=7 && y<=7; }
    		else if(subPart.equals("upRight")){
    			x=current_x+i; 
    			y=current_y-i;
    			loopCondition= x<=7 && y>=0; }
    		else if(subPart.equals("downLeft")){
    			x=current_x-i; 
    			y=current_y+i;
    			loopCondition= x>=0 && y<=7; }
    		else if(subPart.equals("downRight")){
    			x=current_x-i; 
    			y=current_y-i;
    			loopCondition= x>=0 && y>=0;
    		}
    		
    		loopCondition &= !(x==current_x&&y==current_y);
    		if(loopCondition){
        		occupiedState=Chessboard.occupiedState[x][y];
        		//System.out.println(x+" "+y+" "+occupiedState+" "+cannotLeapOver);
    			if(cannotLeapOver)	//this part is the same in Row and Column search function
    				squareReachable[x][y]=false;
    			else if(occupiedState==0){
    				squareReachable[x][y]=true;    }
    			else if(occupiedState==Chessboard.currentTurn){
    				squareReachable[x][y]=false;
    			    cannotLeapOver=true;
    			}
    			else{
    				squareReachable[x][y]=true;
    				cannotLeapOver=true;
    			}
    		}
    		
    	}
    }
    
}
