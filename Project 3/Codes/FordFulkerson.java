import java.util.*;
import java.io.File;

public class FordFulkerson {
	
	private static ArrayList<Integer> visitedNodes = new ArrayList<Integer>();	//used to record the nodes visited
	
	private static ArrayList<Integer> DFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<Integer>();
		visitedNodes.add(source);	//record this node as visited	
		
		if(source.equals(destination)) {				//if reach the destination
			path.add(source);
			return path;								//return the path with destination
			
		}else {
			ArrayList<Edge> edges = graph.getEdges();	//get all the edges in the graph
			ArrayList<Edge> edgesOfs = new ArrayList<Edge>();
		
			//get all the edges with the input "source" as source
			for (Edge e : edges) {
				if(source.equals(e.nodes[0]) && e.weight != 0) {	//if the source is correct and the path is available
					edgesOfs.add(e);	
				}
			}
		
			for(Edge e : edgesOfs) {					//for all the edges start from s
				if(!visitedNodes.contains(e.nodes[1]) && e.weight != 0) {	//if the destination isn't visited and the path is available
					
					path = DFS(e.nodes[1], destination, graph);	//go to this node(do recursion)
					
					if(!path.isEmpty()) {					//if the path returned isn't empty, which means destination is reached
						path.add(0, e.nodes[0]);			//add the source of this edge to the path
						return path;						//return the path with this new node
					}
				}
			}
			return path;								//if no more edges for check, return empty path
		}	
	}
		
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		visitedNodes.clear();			//clear the records of the visited nodes
		path = DFS(source, destination, graph);
		
		return path;								
	}

	/**!!!!ATTENTION!!!!
	 * Graph used for storing original edges and capacity
	 * Gc used for storing capacity of the residual graph
	 * Gf used for storing current flow of the residual graph
	 */
	
	//create a Graph with same forward edges with 0 flow
	private static WGraph createGf(WGraph graph) {
		WGraph Gf = new WGraph();
		
		for (Edge e : graph.getEdges()) {
			Edge edge = new Edge(e.nodes[0], e.nodes[1], 0);
			Gf.addEdge(edge);
		}
		
		return Gf;
	}
	
	//check of the edge is forward
	private static boolean isForward(Edge e, WGraph graph) {
		ArrayList<Edge> forwardEdges = graph.getEdges();
		for(Edge edge : forwardEdges) {
			if (e.nodes[0] == edge.nodes[0] && e.nodes[1] == edge.nodes[1]) {
				return true;
			}
		}
		return false;
	}
	
	//do the augment
	private static int augment(ArrayList<Edge> edgesInPath, WGraph graph, WGraph Gf) {
		int beta = edgesInPath.get(0).weight;
		
		//get the minimum flow of the current path
		for(Edge e : edgesInPath) {	//for all the edges
			beta = Math.min(beta, Math.abs(e.weight));
		}
		
		for(Edge e : edgesInPath) {	//for all the edges
			if(isForward(e, graph)) {
				Gf.getEdge(e.nodes[0], e.nodes[1]).weight += beta;
			}else {
				Gf.getEdge(e.nodes[0], e.nodes[1]).weight -= beta;
			}
		}
		
		return beta;
	}	
	
	//update Gc with the new flows in Gf
	private static WGraph updateGc(WGraph graph, WGraph Gf, WGraph Gc) {
		WGraph newGc = new WGraph();
		Edge e;
		
		for (Edge eInG : graph.getEdges()) {
			
			Edge eInGf = Gf.getEdge(eInG.nodes[0], eInG.nodes[1]);	//get current flow
			
			if(eInGf.weight == 0 && Gf.getEdge(eInG.nodes[1], eInG.nodes[0]) != null) {	//if the flow happens to the inverted edge
				eInGf = Gf.getEdge(eInG.nodes[1], eInG.nodes[0]);	//get current flow with negative value
			}
			
			Edge eInGc = Gc.getEdge(eInG.nodes[0], eInG.nodes[1]);	//get current capacity			
			int totalFlow = eInGf.weight + (eInG.weight - eInGc.weight);	//compute current total flow of this edge
			
			if(totalFlow <= eInG.weight) {							//create forward edge for all cases
				e = new Edge(eInG.nodes[0], eInG.nodes[1], eInG.weight - totalFlow);
				newGc.addEdge(e);
			}
			
			if(totalFlow > 0) {										//create backward edge if neeeded
				e = new Edge(eInG.nodes[1], eInG.nodes[0], totalFlow);
				newGc.addEdge(e);
			}
		}
		return newGc;
	}
	
	
	public static String fordfulkerson(WGraph graph){
		String answer = "";
		int maxFlow = 0;
		
		//============================== Here's my codes ==============================		
		WGraph Gc = new WGraph(graph);	//create a Gc to record the capacity of the residual graph
		WGraph Gf = createGf(graph);	//create a Gf with all edges with 0 flow

		ArrayList<Integer> path = pathDFS(Gc.getSource(), Gc.getDestination(), Gc);
//		System.out.println(path.toString());
		
		while(path.size() != 0) {	//while there is a path from s to t in Gc
			//get all the edges in the path
			ArrayList<Edge> edgesInPath = new ArrayList<Edge>();
			for(int i = 0; i < path.size() - 1; i++) {
				edgesInPath.add(Gc.getEdge(path.get(i), path.get(i + 1)));
			}
			
			maxFlow += augment(edgesInPath, graph, Gf);
			Gc = updateGc(graph, Gf, Gc);
			
			Gf = createGf(Gc);				//create a new Gf with all the new edges in Gc

			path = pathDFS(graph.getSource(), graph.getDestination(), Gc);
//			System.out.println(path.toString());
		}
		
		GcToGraph(graph, Gc);
		
		//============================== Here's my codes ==============================		
		answer += maxFlow + "\n" + graph.toString();
		return answer;
	}

	//change the Gc to final output graph
	private static void GcToGraph(WGraph graph, WGraph Gc) {
		for (Edge e : graph.getEdges()) {
			
				Edge edge = Gc.getEdge(e.nodes[0], e.nodes[1]);
				if(edge != null) {
					e.weight -= edge.weight;	
				}
		}
	}




	public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
	     System.out.println(fordfulkerson(g));
	        
		 
		 
//     	 System.out.println("====================== ff2 ======================");
//         String file1 = "C:/Users/Alienware/eclipse-workspace/COMP 251 Assignment 3/src/ff2.txt";
//         WGraph g1 = new WGraph(file1);
//         System.out.println(fordfulkerson(g1));
//         
//         System.out.println(pathDFS(0, 5, g1));
	 }
}

