package Assignment1_0;

public class Contestor {
	
    Chesspiece king;  
    Chesspiece queen;
    Chesspiece rook[] = new Chesspiece[2];
    Chesspiece bishop[] = new Chesspiece[2];
    Chesspiece knight[] = new Chesspiece[2];
    Chesspiece pawn[] = new Chesspiece[8];   //each contestor has its own group of chess pieces
    
    static int goThroughPos = 0;   //the iterator for originalPosition arrays
    static int originalPosition1[][] = { {0,0}, {0,7}, {0,2}, {0,5}, {0,1}, {0,6}, {1,0} };   //the originalPosition data for the white player
    static int originalPosition2[][] = { {7,0}, {7,7}, {7,2}, {7,5}, {7,1}, {7,6}, {6,0} };   //the originalPosition data for the black player
    
    boolean kingInCheck = false;   //each contester has a boolean to determine whether its king is in check or not
    
    public Contestor(int oneOrTwo){	//oneOrTwo is to determine which player to setup
    	
    	if(oneOrTwo==1){
    		king = new Chesspiece(0,4,"king");
    		queen = new Chesspiece(0,3,"queen");
    		setupChesspiece(1);
    	}
    	else{
    	    king = new Chesspiece(7,4,"king");
    	    queen = new Chesspiece(7,3,"queen");
    	    setupChesspiece(2);
    	}
    	
    }
    
    void setupChesspiece(int oneOrTwo){	//wrapper function for setting up chess pieces, oneOrTwo is the index
    	setupChesspiece(rook, oneOrTwo);
		setupChesspiece(bishop, oneOrTwo);
		setupChesspiece(knight, oneOrTwo);
		setupChesspiece(pawn, oneOrTwo);
    }
    
    void setupChesspiece(Chesspiece piece[], int oneOrTwo){
    	
    	int pos[][] = oneOrTwo==1 ? originalPosition1 : originalPosition2;    //select original position data for contesters
    	
    	String name=null;	//determine which type to set up
    	if(goThroughPos>=0&&goThroughPos<=1) 
    		name = "rook"; 
    	else if(goThroughPos>=2&&goThroughPos<=3) 
    		name = "bishop";
    	else if(goThroughPos>=4&&goThroughPos<=5) 
    		name = "knight";
    	else if(goThroughPos>=6) 
    		name = "pawn";

    	for(int i=0; i<piece.length; i++){
    		int y_pos = goThroughPos>=6 ? pos[goThroughPos][1]+i : pos[goThroughPos][1];
    		piece[i]=new Chesspiece(pos[goThroughPos][0],y_pos, name);
    		if(goThroughPos<6) goThroughPos++;
    	}
    	if(name.equals("pawn")&&goThroughPos>=6) 
    		goThroughPos=0;			//when the setup for a contester is over, goThroughPos becomes 0 again for next contester setup
    }
    
    boolean inCheck(int test_x, int test_y){	
    	/*This function will be called at the end of every turn (in the game loop). For this function we need a temp boolean variable,
    	 * even though we have a global variable kingInCheck, because the king's move might make itself out of check state.
    	 * So we need this temp Check variable to update that global variable. This function will check for all possible ways from which
    	 * the king can be attacked, including rows, columns, diagonals and knights.*/
    	int opponent = Chessboard.currentTurn==1 ? 2 : 1;
    	boolean Check = false;
    	
    	String directions[] = {"up","down","right","left","upLeft","upRight","downLeft","downRight"};
    	for(int i=0; i<8; i++)	//this part is for rows, columns, and diagonals
    		if(testOnEightDirections(test_x, test_y, directions[i]))
    			Check=true;
    	
    	for(int i=0; i<8; i++)	//this is for knight
    		for(int j=0; j<8; j++){
    			boolean inRange = (i==test_x+1&&j==test_y+2) || (i==test_x+1&&j==test_y-2) || (i==test_x-1&&j==test_y+2) ||
    					(i==test_x-1&&j==test_y-2) || (i==test_x+2&&j==test_y+1) || (i==test_x+2&&j==test_y-1)||
    					(i==test_x-2&&j==test_y+1) || (i==test_x-2&&j==test_y-1);
    			if(inRange&&Chessboard.occupiedState[i][j]==opponent&&Chessboard.occupiedBy[i][j].type.equals("knight"))
    				Check=true;
    		}
    	
    	return Check;
    }
    
    boolean testOnEightDirections(int king_x, int king_y, String direction){
    	int opponent = Chessboard.currentTurn==1 ? 2 : 1;	//determine the opponent
    	Chesspiece occupiedBy[][] = Chessboard.occupiedBy;	//shorten latter expressions
    	int occupiedState[][] = Chessboard.occupiedState;
    	int x=-1,y=-1;		//loop indices
    	boolean inBound=false, tempCheck=false;		//inBound is to make sure loop indices are within the chess board. tempCheck is the return value,
    												//which becomes true once the king can be captured from the current direction
    	
    	for(int i=0; i<8; i++){//set up indices and bounding boolean based on current direction
    		if(direction.equals("up")){ x=king_x+i; y=king_y; inBound=x<=7; }
    		else if(direction.equals("down")){ x=king_x-i; y=king_y; inBound=x>=0; }
    		else if(direction.equals("left")){ x=king_x; y=king_y-i; inBound=y>=0; }
    		else if(direction.equals("right")){ x=king_x; y=king_y+i; inBound=y<=7; }
    		else if(direction.equals("upLeft")){ x=king_x+i; y=king_y-i; inBound=x<=7&&y>=0; }
    		else if(direction.equals("upRight")){ x=king_x+i; y=king_y+i; inBound=x<=7&&y<=7; }
    		else if(direction.equals("downLeft")){ x=king_x-i; y=king_y-i; inBound=x>=0&&y>=0; }
    		else if(direction.equals("downRight")){ x=king_x-i; y=king_y+i; inBound=x>=0&&y<=7; }

    	
    		inBound &= !(x==king_x&&y==king_y);
    		if(inBound){
    			if(occupiedState[x][y]!=0){	//if we actually find a piece
        			Chesspiece target= occupiedBy[x][y];
        			boolean possibleCheck=false;	//this boolean is for: if what we find is an opponent, determine whether it can attack our king from that position
        			if(direction.equals("up")||direction.equals("down")||direction.equals("left")||direction.equals("right"))	//if the search is on rows or columns
        				possibleCheck=occupiedState[x][y]==opponent&&(target.type.equals("queen")||target.type.equals("rook"));		//then check if what we find is an opponent queen or opponent rook
        			else{																			//if the search is on diagonals
        				possibleCheck=(target.type.equals("queen")||target.type.equals("bishop"));	//then check if what we find is an opponent queen or opponent bishop, or opponent pawn
        				if(opponent==1)
        					possibleCheck|=target.type.equals("pawn")&&((x==king_x-1&&y==king_y-1)||(x==king_x-1&&y==king_y+1));
        				else if(opponent==2)
        					possibleCheck|=target.type.equals("pawn")&&((x==king_x+1&&y==king_y-1)||(x==king_x+1&&y==king_y+1));
        			}
    				if(occupiedState[x][y]==opponent&&possibleCheck)	//if we find an opponent and it can attack our king, then our king is in Check, break the loop and then return
    					tempCheck=true;
    				
    				break;	//either way we will break the loop, the only difference is the value of tempCheck
    			}
    		}
    	}
        return tempCheck;
    }
    	

    
    boolean testCheckMate(){
    	/*This function will be called only when the king is actually inCheck(). To determine a checkmate, 
    	 * we need to search all those 8 squares around the king to see if that square is reachable, and if
    	 * opponent can attack on that square. If all the squares around the king can be attacked and the 
    	 * king cannot move anywhere else, the king is in checkmate.*/
    	int x=king.current_x, y=king.current_y;
    	int surroundPos[][] = { {x+1,y},{x-1,y},{x,y+1},{x,y-1},{x+1,y+1},{x+1,y-1},{x-1,y+1},{x-1,y-1} };	//first we need to have those 8 squares around the king
    	boolean inCheckMate=true;
    	for(int i=0; i<8; i++){
    		int xx=surroundPos[i][0], yy=surroundPos[i][1];	//temp variables to shorten expressions
    		if(Chessboard.occupiedState[xx][yy]==Chessboard.currentTurn)	//if we find an ally on one of them, then this square cannot be moved to
    			continue;
    		if(!inCheck(xx,yy))	//if that square is not inCheck(), that square can be a way out
    			inCheckMate=false;
    	}
    	//if we didn't find a way out then "inCheckMate" remains the same as true, the king is in checkmate
    	return inCheckMate;
    }

}
