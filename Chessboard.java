package Assignment1_0;

public class Chessboard {
    static int occupiedState[][]= new int[8][8]; //0 for not occupied, 1 for occupied by contestor1, 2 for occupied by contestor2
    static int currentTurn;  //this will keep switching during the entire game, 1 for white player, 2 for black player
    static Chesspiece occupiedBy[][]= new Chesspiece[8][8]; //determine which square is occupied by which chess piece
    
    Contestor contestor1;  //this indicates the white player
    Contestor contestor2;  //black player
    
    public Chessboard(){  //setup the occupiedState of each square
    	currentTurn = 1;
    	for(int i=0; i<8; i++)
    		for(int j=0; j<8; j++){
    			if( i>1 && i<6 ) 
    				occupiedState[i][j]=0;
    			else if(i<=1)
    				occupiedState[i][j]=1;
    			else
    				occupiedState[i][j]=2;
    		}
    	contestor1= new Contestor(1);
    	contestor2= new Contestor(2);
    }
    
    public static void main(String args[]){
    	
    }
    
}
