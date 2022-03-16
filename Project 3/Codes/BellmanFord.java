import java.util.*;

public class BellmanFord{

    private int[] distances;
    private int[] predecessors;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }
    
    /* Constructor, input a graph and a source
     * Computes the Bellman Ford algorithm to populate the
     * attributes 
     *  distances - at position "n" the distance of node "n" to the source is kept
     *  predecessors - at position "n" the predecessor of node "n" on the path
     *                 to the source is kept
     *  source - the source node
     *
     *  If the node is not reachable from the source, the
     *  distance value must be Integer.MAX_VALUE
     */
    BellmanFord(WGraph g, int source) throws NegativeWeightException{
    	
    	distances = new int[g.getNbNodes()];
    	predecessors = new int[ g.getNbNodes()];
    	
    	for(int i = 0; i < g.getNbNodes(); i++) {
    		distances[i] = Integer.MAX_VALUE;
    		predecessors[i] = i;
    	}
    	
    	distances[source] = 0;
    	
    	for(int i = 1; i <= g.getNbNodes() - 1; i++) {
    		for(Edge e : g.getEdges()) {
    			if (isReachable(e, source, g)) {	//if this node could be reached by the source
    				Relax(e);						//do relax
    			}
    		}
    	}
    	
    	for(Edge e : g.getEdges()) {
    		if (distances[e.nodes[1]] > (distances[e.nodes[0]] + e.weight)) {
    			throw new NegativeWeightException("Negative Weight Exception Found!");
    		}
    	}

    }
    
    private static ArrayList<Integer> visitedNodes = new ArrayList<Integer>();	//used to record the nodes visited
    
    //to determine if the destination node of an edge could be reached by the source
    private boolean isReachable(Edge e, int source, WGraph g) {
    	
    	if(e.nodes[1] == source) {			//if this node is source
    		return true;
    		
    	}else if(e.nodes[0] == source) {	//if this node is connected to source
    		predecessors[e.nodes[1]] = source;	//update this node's predecessor
    		return true;
    		
    	}else {
    		boolean f = false;
    		for(Edge edge : g.getEdges()) {			//check each edge
    			if (edge.nodes[1] == e.nodes[0] && !visitedNodes.contains(e.nodes[0])) {	//if the edge is connected to this edge
    				visitedNodes.add(e.nodes[0]);
    				boolean flag = isReachable(edge, source, g);	//do recursion
    				
    				if(flag) {
    					f = true;
    					if(distances[predecessors[e.nodes[1]]] > distances[e.nodes[0]]) {	
    						predecessors[e.nodes[1]] = e.nodes[0];		//update the edge's predecessor if shorter
    						Relax(e);
    					}
    				}
    			}
    		}
    		
    		visitedNodes.clear();
    		return f;
    	}
	}

	//Do relax
    private void Relax(Edge e) {
		int newDistance = distances[e.nodes[0]] + e.weight;
		int oldDistance = distances[e.nodes[1]];
		
    	if(newDistance < oldDistance) {
    		distances[e.nodes[1]] = newDistance;
		}
	}
    
    /*Returns the list of nodes along the shortest path from 
     * the object source to the input destination
     * If not path exists an Error is thrown
     */
	public int[] shortestPath(int destination) throws PathDoesNotExistException{
		
		int[] path; 
		ArrayList<Integer> pathInList = new ArrayList<Integer>();
		
		if(distances[destination] == Integer.MAX_VALUE) {
			throw new PathDoesNotExistException("Path Does Not Exist!!!");
			
		}else {
			pathInList = getPath(pathInList, destination, source);
			
			int index = 0;
			path = new int[pathInList.size()];
			
			for(int n : pathInList) {
				path[index] = n;
				index++;
			}
		}
		
        return path;
    }

    private ArrayList<Integer> getPath(ArrayList<Integer> path, int destination, int source) {
    	if(path.size() == 0) {
    		path.add(destination);
    	}
    	
    	int pre = (predecessors[destination]);
    	
    	if (pre != source) {
    		path.add(0, pre);
    		return getPath(path, pre, source);
    	}
    	
    	path.add(0, source);
    	
		return path;
	}
    
    /*Print the path in the format s->n1->n2->destination
     *if the path exists, else catch the Error and 
     *prints it
     */
	public void printPath(int destination){

        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
    	
//      System.out.println("====================== bf1 ======================");
//      String file1 = "C:/Users/Alienware/eclipse-workspace/COMP 251 Assignment 3/src/bf2.txt";
//      WGraph g1 = new WGraph(file1);
//      
//      try{
//          BellmanFord bf = new BellmanFord(g1, g1.getSource());
//          bf.printPath(g1.getDestination());
//      }
//      catch (Exception e){
//          System.out.println(e);
//      }
//      
//      System.out.println("====================== ff2 ======================");
//      String file2 = "C:/Users/Alienware/eclipse-workspace/COMP 251 Assignment 3/src/ff2.txt";
//      WGraph g2 = new WGraph(file2);
//      
//      try{
//          BellmanFord bf = new BellmanFord(g2, g2.getSource());
//          bf.printPath(g2.getDestination());
//      }
//      catch (Exception e){
//          System.out.println(e);
//      }
      
 	
        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}

