import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a 3x3 tile game.
 *
 * Internally the state is represented as a flat array with 9 slots
 * storing the numbers 0 through 8. 0 is considered to be the blank. 
 * 
 * So, for example, the game state:
 * 
 * 0 3 2
 * 1 6 5
 * 4 7 8
 *
 * is internally stored as {0, 3, 2, 1, 6, 5, 4, 7, 8}. 
 * 
 * @author John C. Bowers and Joseph Hicks
 */
public class TileState {

    
    // The flattened tile stored in row major order. 
    private final int[] state; 

    public TileState(int[] state) {
        this.state = state;
    }

    /**
     * @return a list of all possible tile states. 
     */
    public static ArrayList<TileState> allStates() {
    	ArrayList<TileState> states = new ArrayList<TileState>();
        int index = 0;
        int[] cur = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        permutations(cur, states, index);
        return states;
    }
    
    static void permutations(int[] current ,ArrayList<TileState> states  ,int idx) {
    	 if (idx == 8) {
    		 int[] nextPerm = current.clone();
    		 states.add(new TileState(nextPerm));
    	 } else {
    	     for (int j = idx; j < 9; j++) {
    	         int curr = current[idx];
                 current[idx] = current[j];
                 current[j] = curr;
    	         permutations(current, states, idx+1);
    	         curr = current[idx];
                 current[idx] = current[j];
                 current[j] = curr;
    	     }
    	 }
    }
    
    /** 
     * Returns the tile at row i and column j in the tile game.
     * @param i The row index. 
     * @param j The column index.
     * @return The tile number between 1 and 8, or 0 for blank at row i column j. 
     */
    public int tileAt(int i, int j) {
        return state[3*i + j];
    }


    /**
     * @param c The move to make, either 'A', 'B', 'L', or 'R'. 
     * @return The TileState for the move that slides the appropriate tile into the blank, or null if the move is not valid.
     */
    public TileState neighborByMove(char c) {
    ArrayList<TileState> neighbors = (ArrayList<TileState>) neighboringStates();
    TileState neighbor = null;
    
    int zero = findZero();
    boolean above = false;
    boolean below = false;
    boolean left = false;
    boolean right = false;
    
    if(zero - 3 >= 0) {
    	above = true;
    }
    
    if(zero + 3 < 9) {
    	below = true;
    }
    
    if(zero % 3 - 1 >= 0) {
    	left = true;
    }
    
    if(zero % 3 + 1 < 3) {
    	right = true;
    }
    
    	switch (c) {
    	  case 'A':
    		  if (above) {
    		     neighbor = neighbors.get(0);
    		  }
    	    break;
    	  case 'B':
    		  if (below) {
    			 if (!above) {
    				 neighbor = neighbors.get(0);
    			 } else {
    		     neighbor = neighbors.get(1);
    			 }
    		  }
    		break;
    	  case 'L':
    		  if (left) {
    			 if (!above || !below) {
    				 neighbor = neighbors.get(1);
    			 } else {
    		     neighbor = neighbors.get(2);
    			 }
    		  }
    		break;
    	  case 'R' :
    		  if (right) {
    			  if ((!above || !below) && !left) {
     				 neighbor = neighbors.get(1);
     			 } else if (!above || !below || !left) {
     				 neighbor = neighbors.get(2);
     			 } else {
    		         neighbor = neighbors.get(3);
    	         }
    		  }
    	    break;
    	  default:
    		  return null;
    	}
        return neighbor;
    }

    /**
     * Generates all states reachable by a single move. 
     * @return a List object containing all the states reachable by one move from this one.
     */
    public List<TileState> neighboringStates() {
    	ArrayList<TileState> neighbors = new ArrayList<TileState>();
    	
    	int zero = findZero();
    	
    	
    	int i = zero / 3;
    	int j = zero % 3;
    	
    	
    	
    	if (i - 1 != -1) {    		
    	   int above = tileAt(i - 1, j);
    	   int[] neighbor = this.state.clone();
    	   neighbor[zero] = above;
    	   neighbor[zero - 3] = 0;
    	   neighbors.add(new TileState(neighbor));
    	}
    	
    	if (i + 1 != 3) {
    	   int below = tileAt(i + 1, j);
    	   int[] neighbor = this.state.clone();
    	   neighbor[zero] = below;
    	   neighbor[zero + 3] = 0;
    	   neighbors.add(new TileState(neighbor));
    	}
    	
    	if (j - 1 != -1) {
    	   int left = tileAt(i, j - 1);
    	   int[] neighbor = this.state.clone();
    	   neighbor[zero] = left;
    	   neighbor[zero - 1] = 0;
    	   neighbors.add(new TileState(neighbor));
    	}
    	
    	if (j + 1 != 3) {
    	   int right = tileAt(i, j + 1);
    	   int[] neighbor = this.state.clone();
    	   neighbor[zero] = right;
    	   neighbor[zero + 1] = 0;
    	   neighbors.add(new TileState(neighbor));
    	}
    	
        return neighbors;
    }
    
    public int findZero() {
    	for(int i = 0; i < 8; i++) {
    		if(state[i] == 0) {
    			return i;
    		}
    	}
    	return 8;
    }

    /**
     * Returns a hashCode so if two state objects represent the same state, 
     * they will hash to the same value. 
     * @return A hash cde for this state. 
     */
    public int hashCode() {
        return Arrays.toString(state).hashCode();
    }

    /**
     * Determines whether two state objects represent the same state. 
     * @param o The other object to test. 
     * @return true if the other object represents the same state. 
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        } else if (!(o instanceof TileState)) {
            return false;
        } else {
            TileState other = (TileState) o;
            for (int i = 0; i < 9; i++) {
                if (this.state[i] != other.state[i]) return false;
            }
            return true;
        }
    }

    /**
     * @return the state of the game as a String that prints each board position on a line.
     */
    public String toString() {
        String acc = "";
        for (int i = 0; i < 9; i++) {
            acc += state[i] == 0 ? " " : state[i];
            acc += i % 3 == 2 ? "\n" : " ";
        }
        return acc;
    }

}