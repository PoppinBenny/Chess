package Assignment1_0;

import static org.junit.Assert.*;

import org.junit.Test;
public class ChessTest {
		static Chessboard game = new Chessboard();
		//This test checks all the preconditions to start the game
		@Test
		public void validGamePreconditions() throws Exception{

		    for(int i=0; i<8; i++)	//check occupiedState
				for(int j=0; j<8; j++){
			    	if( i>1 && i<6 ) 
			    		assertEquals(Chessboard.occupiedState[i][j],0);
			    	else if(i<=1)
			    		assertEquals(Chessboard.occupiedState[i][j],1);
			    	else
			    		assertEquals(Chessboard.occupiedState[i][j],2);
			    }
		    
		    int originalPosition1[][] = { {0,0}, {0,7}, {0,2}, {0,5}, {0,1}, {0,6}, {1,0}, {1,1}, {1,2}, {1,3}, {1,4}, {1,5}, {1,6}, {1,7} };
		    Contestor contestor1 = game.contestor1;
		    assertEquals(contestor1.king.current_x,0);
		    assertEquals(contestor1.king.current_y,4);
		    assertEquals(contestor1.queen.current_x,0);
		    assertEquals(contestor1.queen.current_y,3);
		    trueOriginalPositions(contestor1,originalPosition1);
		    
		    
		    int originalPosition2[][] = { {7,0}, {7,7}, {7,2}, {7,5}, {7,1}, {7,6}, {6,0}, {6,1}, {6,2}, {6,3}, {6,4}, {6,5}, {6,6}, {6,7} };
		    Contestor contestor2 = game.contestor2;
		    assertEquals(contestor2.king.current_x,7);
		    assertEquals(contestor2.king.current_y,4);
		    assertEquals(contestor2.queen.current_x,7);
		    assertEquals(contestor2.queen.current_y,3);
		    trueOriginalPositions(contestor2,originalPosition2);	//check all original Positions
		    
		    /*check occupiedBy[][], we do not need to go through every chess piece, because before the game starts, the constructor of Chesspiece class
		     * is the only place where occupiedBy[][] can be changed. Then if these two cases are correct, others must be all correct.*/
		    assertEquals(contestor1.king,Chessboard.occupiedBy[0][4]);
		    assertEquals(contestor2.queen,Chessboard.occupiedBy[7][3]);	

		    
		}
				
		public void trueOriginalPositions(Contestor contestor, int[][] originalPosition){
			int goThroughPos=0;
		    int position[][] = new int[14][2];
		    for(int i=0; i<14; i++){
		    	if(i>=0&&i<=1){
		    		position[i][0]=contestor.rook[i].current_x; position[i][1]=contestor.rook[i].current_y; }
		    	else if(i>=2&&i<=3){
		    		position[i][0]=contestor.bishop[i-2].current_x; position[i][1]=contestor.bishop[i-2].current_y; }
		    	else if(i>=4&&i<=5){
		    		position[i][0]=contestor.knight[i-4].current_x; position[i][1]=contestor.knight[i-4].current_y; }
		    	else if(i>=6){
		    		position[i][0]=contestor.pawn[i-6].current_x; position[i][1]=contestor.pawn[i-6].current_y; }
		    	goThroughPos++;
		    }
		    assertArrayEquals(position,originalPosition);
		}
		
		
		@Test
	    public void moveAndKill(){
	        Contestor player1 = game.contestor1;
	        Contestor player2 = game.contestor2;
	        int occupiedState[][] = Chessboard.occupiedState;
	        Chesspiece occupiedBy[][] = Chessboard.occupiedBy;
	        
	        Chesspiece piece1 = player1.pawn[3];    //the piece to move first is the fouth pawn on white side
	        piece1.move(3,3);
	        assertEquals(piece1.current_x,3);
	        assertEquals(piece1.current_y,3);    //move to 3,3 to see if its property has changed
	        assertEquals(occupiedState[1][3],0);    
	        assertNull(occupiedBy[1][3]);    //check if its previous position has nothing
	        assertEquals(occupiedState[3][3],1);    
	        assertEquals(occupiedBy[3][3],piece1);    //check if its target position has the right state
	        
	        Chessboard.currentTurn=2;    //second piece to move is the second rook on black side
	        Chesspiece piece2 = player2.rook[1];
	        piece2.move(3,7);
	        assertEquals(piece2.current_x,3);
	        assertEquals(piece2.current_y,7);    //move to 3,7 to see if its property has changed
	        assertEquals(occupiedState[7][7],0);
	        assertNull(occupiedBy[7][7]);    //same as above
	        assertEquals(occupiedState[3][7],2);
	        assertEquals(occupiedBy[3][7],piece2);    //check if the target position state is 2
	        
	        piece2.move(3,3);    //move piece2 to 3,3 to kill piece1
	        assertEquals(occupiedState[3][3],2);    
	        assertEquals(occupiedBy[3][3],piece2);    //now the state of square 3,3 should be 2 and occupiedBy piece2
	        assertEquals(piece1.current_x,-1);
	        assertEquals(piece1.current_y,-1);    //and piece1 should no longer be on the chess board
	        for(int i=0; i<8; i++)
	            for(int j=0; j<8; j++)
	                assertTrue(occupiedBy[i][j]!=piece1);    //make sure we cannot find piece1 on the board
	        
	        Chessboard.currentTurn=1;
	        piece1 = player1.knight[0];
	        piece1.move(3,3);    //change turn, one more kill
	        assertEquals(occupiedState[3][3],1);
	        assertEquals(occupiedBy[3][3],piece1);
	        assertEquals(piece2.current_x,-1);
	        assertEquals(piece2.current_y,-1);
	        for(int i=0; i<8; i++)
	            for(int j=0; j<8; j++)
	                assertTrue(occupiedBy[i][j]!=piece2);
	        
	        game = new Chessboard();    //reset Chess board for subsequent test
	    }
		
		
		@Test
		public void pawnMovement() throws Exception {	//test for pawn movement
			Contestor contestor1 = game.contestor1;
			Contestor contestor2 = game.contestor2;
			
			Chessboard.currentTurn = 1;
			contestor1.pawn[1].move(4, 1);
			contestor1.pawn[3].move(3, 3);
			if(Chessboard.currentTurn == 1)	
			pawnMovement(contestor1);	//check white pawns
				//check pawn NO.1 to NO.8
			
			Chessboard.currentTurn = 2;
			contestor2.pawn[4].move(4, 4);
			contestor2.pawn[6].move(2, 6);	
			pawnMovement(contestor2);	//check black pawns
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		
		public void pawnMovement(Contestor contestor){
			int oneSquareBeforeIt_x = -1, twoSquareBeforeIt_x = -1, original_x = -1;
			for(int j=0; j<8; j++){
				Chesspiece currPawn = contestor.pawn[j];
				oneSquareBeforeIt_x = Chessboard.currentTurn==1 ? currPawn.current_x+1 : currPawn.current_x-1;
				twoSquareBeforeIt_x = Chessboard.currentTurn==1 ? currPawn.current_x+2 : currPawn.current_x-2;
				original_x = Chessboard.currentTurn==1 ? 1 : 6;
				//check initial reachable square when pawns are in original squares, it can move 2 square
				if(currPawn.current_x==original_x && Chessboard.occupiedState[oneSquareBeforeIt_x][j]==0 && Chessboard.occupiedState[twoSquareBeforeIt_x][j]==0){
					currPawn.pawnSearch();
					assertTrue(currPawn.squareReachable[oneSquareBeforeIt_x][j]);
					assertTrue(currPawn.squareReachable[twoSquareBeforeIt_x][j]);
				}
				//check if this pawn can move 1 square forward
				else if(Chessboard.occupiedState[oneSquareBeforeIt_x][currPawn.current_y]==0){
					currPawn.pawnSearch();
					assertTrue(currPawn.squareReachable[oneSquareBeforeIt_x][currPawn.current_y]);
				}//check if the upRight adjacent square is reachable if there's an opponent on it
				else if(Chessboard.occupiedState[oneSquareBeforeIt_x][currPawn.current_y+1]==2){
					currPawn.pawnSearch();
					assertTrue(currPawn.squareReachable[oneSquareBeforeIt_x][currPawn.current_y+1]);
				}//check if the upLeft adjacent square is reachable if there's an opponent on it
				else if(Chessboard.occupiedState[oneSquareBeforeIt_x][currPawn.current_y-1]==2){
					currPawn.pawnSearch();
					assertTrue(currPawn.squareReachable[oneSquareBeforeIt_x][currPawn.current_y-1]);
				}
			}
		}
		
		@Test
		public void rowAndColMovement() throws Exception{
			Chesspiece rook = game.contestor1.rook[0];
			Chesspiece queen = game.contestor1.queen;
			Chesspiece pawn[] = game.contestor2.pawn;
			rook.searchReachablePos();

			for(int i=0; i<8; i++)	//At the beginning of the game, there's nowhere it can move
				for(int j=0; j<8; j++)
					assertFalse(rook.squareReachable[i][j]);
			
			int trueCoords1[][]={{3,0},{3,1},{3,2},{3,4},{3,5},{3,6},{3,7},{2,3},{4,3},{5,3},{6,3}};	//the squares at ONLY these coordinates should be true after the rook moves
			rook.move(3,3);
			rook.searchReachablePos();
			assertTrue(matchResult(rook,trueCoords1)&&countNumOfTrue(rook)==trueCoords1.length);
			
			Chessboard.currentTurn=2;	//let the opponent pieces move, and see if rook's reachable squares are correct
			pawn[0].move(3,0);
			pawn[3].move(5,3);
			pawn[4].move(3,4);
			int trueCoords2[][]={{3,0},{3,1},{3,2},{3,4},{2,3},{4,3},{5,3}};
			Chessboard.currentTurn=1;
			rook.searchReachablePos();
			rook.move(0,0);		//move back the rook
			
			queen.move(3,3);	//also try queen
			queen.searchOnRowAndCol();
			assertTrue(matchResult(rook,trueCoords2)&&countNumOfTrue(rook)==trueCoords2.length);
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		
		@Test
		public void diagonalMovement() throws Exception{
			Chesspiece bishop = game.contestor1.bishop[0];
			Chesspiece queen = game.contestor1.queen;
			Chesspiece pawn[] = game.contestor2.pawn;

			for(int i=0; i<8; i++)	//At the beginning of the game, there's nowhere it can move
				for(int j=0; j<8; j++)
					assertFalse(bishop.squareReachable[i][j]);
			
			int trueCoords1[][]={{2,2},{2,4},{4,2},{5,1},{6,0},{4,4},{5,5},{6,6}};	//the squares at ONLY these coordinates should be true after the bishop moves
			bishop.move(3,3);
			bishop.searchReachablePos();
			assertTrue(matchResult(bishop,trueCoords1)&&countNumOfTrue(bishop)==trueCoords1.length);
			
			Chessboard.currentTurn=2;	//let the opponent pieces move, and see if bishop's reachable squares are correct
			pawn[1].move(5,1);
			pawn[2].move(2,2);
			pawn[5].move(5,5);
			int trueCoords2[][]={{4,2},{5,1},{2,2},{4,4},{5,5},{2,4}};
			Chessboard.currentTurn=1;
			bishop.searchReachablePos();
			assertTrue(matchResult(bishop,trueCoords2)&&countNumOfTrue(bishop)==trueCoords2.length);
			bishop.move(0,0);		//move back the bishop
			
			queen.move(3,3);	//also try queen
			queen.searchOnRowAndCol();
			assertTrue(matchResult(bishop,trueCoords2)&&countNumOfTrue(bishop)==trueCoords2.length);
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		/*rowAndColMovement() and diagonalMovement() together checks the behavior of rook, bishop and queen*/
		
		
		@Test
		public void knightMovement() throws Exception{
			Chesspiece knight = game.contestor1.knight[0];
			Chesspiece pawn[] = game.contestor2.pawn;

			for(int i=0; i<8; i++)	//At the beginning of the game, there's nowhere it can move
				for(int j=0; j<8; j++)
					assertFalse(knight.squareReachable[i][j]);
			
			int trueCoords1[][]={{4,1},{5,2},{5,4},{4,5},{2,1},{2,5}};	//the squares at ONLY these coordinates should be true after the knight moves
			knight.move(3,3);
			knight.searchReachablePos();
			assertTrue(matchResult(knight,trueCoords1)&&countNumOfTrue(knight)==trueCoords1.length);
			
			Chessboard.currentTurn=2;	//let the opponent pieces move, and see if knight's reachable squares are correct
			pawn[2].move(4,2);
			pawn[4].move(4,4);
			int trueCoords2[][]={{5,1},{6,2},{3,1},{2,2},{2,4},{3,5},{6,4},{5,5}};
			Chessboard.currentTurn=1;
			knight.move(4,3);
			knight.searchReachablePos();
			assertTrue(matchResult(knight,trueCoords2)&&countNumOfTrue(knight)==trueCoords2.length);
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		
		
		@Test
		public void inCheck() throws Exception{
			Contestor player1 = game.contestor1;
			Contestor player2 = game.contestor2;
			
			Chessboard.currentTurn=2;
			player2.pawn[4].move(4,4);
			
			Chessboard.currentTurn=1;
			assertTrue(player1.inCheck(3,3));	//check if player1 can be checked by player2's pawn
			assertFalse(player1.inCheck(3,2));	//for player1 square 3,2 should not be inCheck()
			assertTrue(player1.inCheck(5,2));	//check if player1 can be checked by player2's knight
			player1.queen.move(5,5);
			
			Chessboard.currentTurn=2;
			assertTrue(player2.inCheck(7,3));	//player2's queen is now checked by player1's queen
			player2.rook[0].move(0,3);
			Chessboard.currentTurn=1;
			assertTrue(player1.inCheck(0,4));	//player1's king is now inCheck() because player2's rook is on its left side without barriers
			player1.king.move(2,3);
			assertFalse(player1.inCheck(2,3));	//now the king should not be inCheck() because there's a barrier between them
			player1.pawn[3].move(3,0);	//now we move the barrier(pawn)
			assertTrue(player1.inCheck(2,3));	//the king should be inCheck() again
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		
		
		@Test
		public void checkMate() throws Exception{
			Contestor player1 = game.contestor1;
			Contestor player2 = game.contestor2;
			
			player1.king.move(3,3);
			player1.pawn[0].move(2,3);
			player1.pawn[1].move(2,4);
			player1.pawn[2].move(3,4);
			
			Chessboard.currentTurn = 2;
			player2.pawn[2].move(5,1);
			player2.bishop[1].move(5,5);
			
			Chessboard.currentTurn = 1;
			assertFalse(player1.testCheckMate());	//now for player1's king, there are still 3 ways out: up, left, and downLeft
			
			Chessboard.currentTurn = 2;
			player2.rook[0].move(3,0);		//block left way
			player2.queen.move(1,1);	//block downLeft, now there's should be only 1 way out: up
			Chessboard.currentTurn = 1;
			assertFalse(player1.testCheckMate());	//still one step away from checkMate
			
			Chessboard.currentTurn = 2;
			player2.pawn[4].move(5,4);	//block the final way out
			Chessboard.currentTurn = 1;
			assertTrue(player1.testCheckMate());	//now player1's king is in checkMate, GAME OVER
			
			game = new Chessboard();	//reset Chess board for subsequent test
		}
		
		
		/*Printing the current state of the chess board, used for debugging.*/
		private void printChessboard(){
			for(int x=7; x>=0;x--){
				for(int y=0;y<8;y++){
					System.out.print(Chessboard.occupiedState[x][y]+" ");
				}
				System.out.println();
			}
		}
		
		private void printSquareReachable(Chesspiece piece){
			for(int x=7; x>=0;x--){
				for(int y=0;y<8;y++){
					System.out.print(piece.squareReachable[x][y]+" ");
				}
				System.out.println();
			}
		}
		
		private int countNumOfTrue(Chesspiece piece){	//called after ifMatchResult() to see if the number of truth is correct
			int count = 0;
			for(int i=0; i<8; i++)
				for(int j=0; j<8; j++)
					if(piece.squareReachable[i][j])
						count++;
			return count;
		}
		
		private boolean matchResult(Chesspiece piece, int[][] coordinates){
			for(int i=0; i<coordinates.length; i++)
				if(!piece.squareReachable[coordinates[i][0]][coordinates[i][1]])
					return false;
			return true;
		}
}
