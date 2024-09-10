import java.util.HashSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This is your graph implementation. 
 * You need to decide whether it is better to store it 
 * as an adjacency matrix or an adjacency list, or an adjacency set
 * data structure, and why?
 *
 * Then you need to implement the interface IGraph here. 
 */
public class TileGraph implements IGraph<TileState> {
    
    // I suggest you implement the graph using
    // and adjacency set data structure, store vertex
    // data implicitly in a separate array, and
    // store a lookup table that maps a given tile state
    // to its index in a table. 
    private ArrayList<HashSet<Integer>>     neighborsOf;
    private TileState[]                     stateOf; 
    private Hashtable<TileState, Integer>   indexOf;
    
    
    
    
    public TileGraph() {
    	
    	ArrayList<TileState> states = TileState.allStates();
    	
    	this.neighborsOf = new ArrayList<HashSet<Integer>>();
    	this.stateOf = new TileState[states.size()];
    	this.indexOf = new Hashtable<TileState, Integer>();
    	
 
    	for(int i = 0; i < states.size(); i++) {
    		stateOf[i] = states.get(i);
    		indexOf.put(vertexData(i), i);
    	}
    	
    	for(int i = 0; i < stateOf.length; i++) {
    		neighborsOf.add((HashSet<Integer>) neighborsOf(i));
    	}
    	
    	
    }
    
    // TODO. Implement the following method as well
    //       as all methods inherited from the IGraph 
    //       interface and any private helper methods
    //       you want to include. 
    /**
     * @return The list of all states reachable from a given TileState by legal moves.
     *          Note that this should include the state ts.
     */
    public ArrayList<TileState> statesReachableFrom(TileState ts) {
    	ArrayList<TileState> reachable = new ArrayList<TileState>();
    	int size = vertexCount();
    	boolean visited[] = new boolean[size];
		 
        
	    for(int i = 0; i < size; i++) {
	       visited[i] = false;
	    }
	    
		int v = indexOf.get(ts);
		
	    BFSReach(v, visited, reachable);
	    
		 
        return reachable; 
    }

	private void BFSReach(int v, boolean[] visited, List<TileState> reachable) {
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		
		queue.add(v);
	    reachable.add(vertexData(v));
	    visited[v] = true;
	    
		while (!queue.isEmpty()) {
            int k = queue.remove();
            Iterator<Integer> it = neighborsOf.get(k).iterator();
            
            while (it.hasNext()) {
            	int next = it.next();
                if (visited[next] == false) {
                	visited[next] = true;
                    queue.add(next);
                    reachable.add(vertexData(next));
                }
            }
        }
		
	}

	@Override
	public int vertexCount() {
		
		return stateOf.length;
	}

	@Override
	public int edgeCount() {
		
		int size = vertexCount();
    	boolean visited[] = new boolean[size];
    	
		int edgeCount = 0;
		for(int i = 0; i < vertexCount(); i++) {
           Iterator<Integer> it = neighborsOf.get(i).iterator();
            
           while (it.hasNext()) {
            	int next = it.next();
                if (visited[next] == false) {
                	edgeCount++;
                	
                }
           }
           visited[i] = true;
		}
		return edgeCount;
	}

	@Override
	public TileState vertexData(int u) {
		
		return stateOf[u];
	}

	@Override
	public int indexOf(TileState data) {
		// TODO Auto-generated method stub
		return indexOf.get(data);
	}

	@Override
	public boolean hasEdge(int u, int v) {
	
		TileState vState = stateOf[u];
		TileState uState = stateOf[v];
		
		ArrayList<TileState> neighbors = (ArrayList<TileState>) vState.neighboringStates();
		
		for(int i  = 0; i < neighbors.size(); i++) {
			if(uState.equals(neighbors.get(i))) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Iterable<Integer> neighborsOf(int u) {
		HashSet<Integer> list = new HashSet<>();
		
		ArrayList<TileState> n = (ArrayList<TileState>) stateOf[u].neighboringStates();
		for(int i  = 0; i < n.size(); i++) {
			list.add(indexOf.get(n.get(i)));
		}
		
		return list;
	}

	@Override
	public int connectedComponents() {
		int size = vertexCount();
		boolean visited[] = new boolean[size];
		 
	        
	    for(int i = 0; i < size; i++) {
	       visited[i] = false;
	    }
	    
		int componentCount = 0;
	     for (int i = 0; i < stateOf.length; i++) {
	    	 if(visited[i] == false) {
	    		 componentCount++;
	    		 BFSConnected(i, visited);
	    	 }
	     }
		 
		 
		 return componentCount;
	}

	
	
	
	private void BFSConnected(int u, boolean[] visited) {
		LinkedList<Integer> queue = new LinkedList<Integer>(); 
		
        
        queue.add(u);
        while (!queue.isEmpty()) {
        	int k = queue.remove();
		 if(visited[k] == false) {
			 visited[k] = true;
		 }
		 
		 Iterator<Integer> it = neighborsOf.get(k).iterator();
         
         while (it.hasNext()) {
         	int next = it.next();
             if (visited[next] == false) {
             	visited[next] = true;
                queue.add(next);
             }
         }
		 
         }
	}

	@Override
	public List<Integer> shortestPath(int u, int v) {
		LinkedList<Integer> path = new LinkedList<Integer>();
		int size = vertexCount();
		
		int pred[] = new int[size];
        int dist[] = new int[size];
		
		if(hasEdge(u,v)) {
			path.add(v);
			path.add(u);
		} else {
			if (BFSShort( u, v, size, pred, dist) == false) {
	            return null;
	        }
	        int crawl = v;
	        path.add(crawl);
	        while (pred[crawl] != -1) {
	            path.add(pred[crawl]);
	            crawl = pred[crawl];
	        }
		}
		
		
		return path;
	}

	private boolean BFSShort(int u, int v, int size, int[] pred, int[] dist) {
	    LinkedList<Integer> queue = new LinkedList<Integer>();
		 
        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean visited[] = new boolean[size];
 
        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < size; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
 
        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[u] = true;
        dist[u] = 0;
        queue.add(u);
 
        // bfs Algorithm
        while (!queue.isEmpty()) {
            int k = queue.remove();
            Iterator<Integer> it = neighborsOf.get(k).iterator();
            
            while (it.hasNext()) {
            	int next = it.next();
                if (visited[next] == false) {
                	visited[next] = true;
                    dist[next] = dist[k] + 1;
                    pred[next] = k;
                    queue.add(next);
 
                    // stopping condition (when we find
                    // our destination)
                    if (next == v)
                        return true;
                }
            }
        }
        return false;
    }
                
	

	

	
}