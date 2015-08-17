package com.rrohit.hakerrank;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.rrohit.hakerrank.GraphGoodNode.Graph.Vertex;

/*
 *  We have a list of N nodes with each node pointing to one of the N nodes. 
It could even be pointing to itself. We call a node ‘good’, 
if it satisfies one of the following properties: 

* It is the tail node (marked as node 1) 
* It is pointing to the tail node (node 1) 
* It is pointing to a good node 

You can change the pointers of some nodes in order to make them all ‘good’. 
You are given the description of the nodes. 
You have to find out what is minimum number of nodes that you have to change in order 
to make all the nodes good. 

Input: 
The first line of input contains an integer number N which is the number of nodes. 
The next N lines contains N numbers, 
all between 1 and N. 
The first number, is the number of the node pointed to by Node 1; 
the second number is the number of the node pointed to by Node 2; 
the third number is the number of the node pointed to by Node 3 and so on. 

N is no larger than 1000. 

Output: 
Print a single integer which is the answer to the problem 

Sample Input 1: 
5 
1 
2 
3 
4 
5 

Sample output 1: 
4 
 * @author rrohit 
 */
public class GraphGoodNode {
	
	private int connectedId[];

	public enum TYPE {
		DIRECTED, UNDIRECTED;
	}
	
	public class Graph {
		
		private List<Vertex> vertices = new ArrayList<Vertex>();
		private List<Edge> edges = new ArrayList<Edge>();
		
		//private final TYPE type = TYPE.DIRECTED;
		
		public Graph(){}
		
		/*public Graph(List<Vertex> vertices, List<Edge> edges){
			this(type, vertices, edges);
		}*/
		
		public Graph(List<Vertex> vertices, List<Edge> edges){
			this.vertices.addAll(vertices);
			this.edges.addAll(edges);
			
			for (Edge edge : edges) {
				Vertex from = edge.from;
				Vertex to = edge.to;
				if ( !(this.vertices.contains(from)) || !(this.vertices.contains(to))) {
					continue;
				}
				
				int index = this.vertices.indexOf(from);
				Vertex fromVertex = this.vertices.get(index);
				index = this.vertices.indexOf(to);
				Vertex toVertex = this.vertices.get(index);
				fromVertex.edges.add(edge);
			}
			
		}
		
		public class Vertex {
			private int value;
			private List<Edge> edges = new ArrayList<Edge>();
			private boolean visited = false;
			
			public Vertex(int value){
				this.value = value;
			}
			
			public Vertex(Vertex vertex){
				this(vertex.value);
				this.edges = new ArrayList<Edge>();
				for (Edge edge : vertex.edges) {
					this.edges.add(new Edge(edge));
				}
			}
			
			public boolean equals(Object obj){
				if ( !(obj instanceof Vertex)) {
					return false;
				}
				Vertex v = (Vertex)obj;
				return ((Integer)this.value).equals((Integer)v.value);
			}
		}
		
		public class Edge{
			private Vertex from;
			private Vertex to;
			
			public Edge(Vertex from, Vertex to){
				this.from = from;
				this.to = to;
			}
			
			public Edge(Edge edge){
				this(edge.from, edge.to);
			}
		}
	}
	
	public Graph getInput(){
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Graph graph = new Graph();
		try{
			int N = Integer.parseInt(input.readLine().trim());
			if ( !(N>0 && N<=1000) ) {
				throw new IllegalArgumentException("No fo nodes must be > 0 and <= 1000");
			}
			
			int i=1;
			int nodeNo;
			//Create N vertex.
			List<Graph.Vertex> vertices = new ArrayList<Graph.Vertex>();
			while (i<=N) {
				Graph.Vertex v = graph.new Vertex(i); 
				vertices.add(v);
				i++;
			}
			
			i=0;
			Graph.Vertex from, to;
			Graph.Edge edge;
			List<Graph.Edge> edges = new ArrayList<Graph.Edge>();
			while (i<N){
				nodeNo = Integer.parseInt(input.readLine().trim());
				if ( !(nodeNo >=1 && nodeNo<=N) ){
					throw new IllegalArgumentException("Node number must be between 1 to N");
				}
				to = vertices.get(nodeNo-1);
				from = vertices.get(i);
				edge = graph.new Edge(from, to);
				edges.add(edge);
				i++;
			}
			graph = new Graph(vertices, edges);
			
		}catch(NumberFormatException nfe){
			System.out.println("Unable to parse the input");
			nfe.getStackTrace();
		}catch(IOException ioe){
			System.out.println("Unable to read input from stdin");
			ioe.getStackTrace();
		}finally{
			try{
				input.close();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		return graph;
	}
	
	public static void main(String args[]){
		GraphGoodNode gg = new GraphGoodNode();
		Graph graph = gg.getInput();
		int minNoOfNodes = gg.getNodetoMakeAllNodeGood(graph);
		System.out.println(minNoOfNodes);
	}

	private int getNodetoMakeAllNodeGood(Graph graph) {
		int count=1;
		connectedId = new int[graph.vertices.size()];
		for (Graph.Vertex v : graph.vertices) {
			if (!v.visited) {
				dfs(graph, v, count);
				count++;
			}
		}
		int nodeCount=0;
		/*
		 * 1.If node points to itself then its connected
		 * 2. If more than one node are connected on same path then they are connected
		 * 3. If A node points to another node but its already visited by other node then it will not be counted
		 * 4. Only in case a single node points to itself, will be counted as connected.
		 * 5. And dont count for tail node which is 1. 
		 */
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int i=connectedId.length-1;
		int same=1;
		while(i>0){ // no need to check 1 node, which is tail node
			if (connectedId[i] == connectedId[i-1]){
				i--;
				same++;
				continue;
			}else {
				
				if (same > 1){
					nodeCount++;
					same=1;
				}else{
					
					// node either points to itself or
					//only have one edge which is already visited.
					Graph.Vertex v = graph.vertices.get(i);
					Graph.Edge edge = v.edges.get(0);
					if (edge.to == v) { // points to itself
						nodeCount++;
					}
				}
				
			}
			i--;
		}
		if (same>1) {
			nodeCount++;
		}
		
		return nodeCount;
	}

	private void dfs(Graph graph, Vertex v, int count) {
		connectedId[graph.vertices.indexOf(v)] = count;
		v.visited = true;
		for (Graph.Edge e: v.edges){
			if (!e.to.visited){
				dfs(graph, e.to, count);
			}
		}
	}

}
