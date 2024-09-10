import java.util.LinkedList;
import java.util.Scanner;




/**
 *
 * The main program should generate the entire state graph of the 
 * 3x3 tile game. It should present the user with a prompt that 
 * allows the user to play the tile game, and/or get a one move hint.
 * 
 * @author Joseph Hicks
 */
public class TileMain {
	
	public static void main(String[] args) {
		// Start of game.
		TileGraph tiles = new TileGraph();
		
		
		
		int curTile = StartGame(tiles);
		System.out.println("================================================");
		GameState(curTile, tiles);
		
		//User input
		Scanner in = new Scanner(System.in);
		String next = "";
		while(!next.equals("Q")) {
			
			next = in.next();
			System.out.println("\n================================================");
			char r = next.charAt(0);
			TileState neighbor = null;
			switch (r) {
	    	  case 'A':
	    		  neighbor = tiles.vertexData(curTile).neighborByMove('A');
	    		  if (neighbor == null) {
	    			  System.out.println("\nInvalid move. Try again\n");
	    			  
	    		  } else {
	    			  curTile = tiles.indexOf(neighbor);
	    		  }
	    	    break;
	    	  case 'B':
	    		  neighbor = tiles.vertexData(curTile).neighborByMove('B');
	    		  if (neighbor == null) {
	    			  System.out.println("\nInvalid move. Try again\n");
	    		  } else {
	    			  curTile = tiles.indexOf(neighbor);
	    		  }
	    		break;
	    	  case 'L':
	    		  neighbor = tiles.vertexData(curTile).neighborByMove('L');
	    		  if (neighbor == null) {
	    			  System.out.println("\nInvalid move. Try again\n");
	    			  
	    		  } else {
	    			  curTile = tiles.indexOf(neighbor);
	    		  }
	    		break;
	    	  case 'R' :
	    		  neighbor = tiles.vertexData(curTile).neighborByMove('R');
	    		  if (neighbor == null) {
	    			  System.out.println("\nInvalid move. Try again.\n");
	    			  
	    		  } else {
	    			  curTile = tiles.indexOf(neighbor);
	    		  }
	    	    break;
	    	  case 'H' :
	    		  LinkedList<Integer> list = (LinkedList<Integer>) tiles.shortestPath(0, curTile);
	    		  String move = "";
	    		  if (tiles.vertexData(curTile).neighborByMove('A') != null && tiles.vertexData(curTile).neighborByMove('A').equals(tiles.vertexData(list.get(1)))) {
	    				move = "(A)bove";
	    			}
	    			if (tiles.vertexData(curTile).neighborByMove('B') != null && tiles.vertexData(curTile).neighborByMove('B').equals(tiles.vertexData(list.get(1)))) {
	    				move = "(B)elow";
	    			}
	    			if (tiles.vertexData(curTile).neighborByMove('L') != null && tiles.vertexData(curTile).neighborByMove('L').equals(tiles.vertexData(list.get(1)))) {
                        move = "(L)eft";
	    			}
	    			if (tiles.vertexData(curTile).neighborByMove('R') != null && tiles.vertexData(curTile).neighborByMove('R').equals(tiles.vertexData(list.get(1)))) {
	    				move = "(R)ight";
	    			}
	    		  
	    		  System.out.printf("\nYou should move the tile %s the blank.\n\n", move);
	    		  
	    		  
	    		  
		    	break;
	    	  case 'C' :
	    		  System.out.println("\nConnected components: " + tiles.connectedComponents() + "\n");
		    	break;
		      case 'N' :
		    	  curTile = StartGame(tiles);
		    	break;
		      case 'Q' :
		    	  System.out.println("Goodbye!");
		    	break;
	    	  default:
	    		  System.out.println("\nInvalid response. Try again.\n");
	    	}
			
			if(curTile == 0) {
				System.out.println("Congraturation! \nThe story is happy end. \nthank you. \nGenerating new game!\n");
				curTile = StartGame(tiles);
			}
			
			if (r != 'Q') {
			GameState(curTile, tiles);
			}
			
		}
		
		
		
		
		
		
	}

	private static void GameState(int curTile, TileGraph tile) {
		
		
		TileState tileState = tile.vertexData(curTile);
		
        System.out.println(tileState);
		
		System.out.print("Available moves:");
		
		if (tileState.neighborByMove('A') != null) {
			System.out.print(" (A)bove");
		}
		if (tileState.neighborByMove('B') != null) {
			System.out.print(" (B)elow");
		}
		if (tileState.neighborByMove('L') != null) {
			System.out.print(" (L)eft");
		}
		if (tileState.neighborByMove('R') != null) {
			System.out.print(" (R)ight");
		}
		
		
		
	    System.out.print("\nYou can also ask for a (H)int, or the number of connected\n" + 
				"(C)omponents, a (N)ew game, or to (Q)uit.\n" +
				"What do you want to do? ");
		
		
		
	}

	private static int StartGame(TileGraph tile) {
		
		int start = 0;
		boolean goodStart = false;
		while (!goodStart) {
		   start = (int)(Math.random() * (((tile.vertexCount() - 1) - 1) + 1)) + 1;
		   
		   if (tile.statesReachableFrom(tile.vertexData(start)).contains(tile.vertexData(0))) {
			   goodStart = true;
		   }
		}
		return start;
		
	}
}