package com.rrohit.hakerrank;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
/*
 * K caterpillars are eating their way through N leaves, each caterpillar falls from leaf to leaf in a unique sequence, 
 * all caterpillars start at a twig at position 0 and falls onto the leaves at position between 1 and N. 
 * Each caterpillar j has as associated jump number Aj. 
 * A caterpillar with jump number j eats leaves at positions that are multiple of j. 
 * It will proceed in the order j, 2j, 3j…. till it reaches the end of the leaves and it stops and build its cocoon. 
 * Given a set A of K elements K<-15, N<=10^9, we need to determine the number of uneaten leaves.

Input:

N = No of un-eaten leaves
K = No. of caterpillars
A = Array of integer jump numbers
Output:

The integer number Of uneaten leaves

Sample Input:

10
3
2
4
5
Output:

4
Explanation:

[2, 4, 5] is a j member jump numbers, all leaves which are multiple of 2, 4, and 5 are eaten, 
leaves 1,3,7,9 are left, and thus the no. 4
 * @author rrohit
 */
public class CaterpillarSolution {
	
	
	public class Caterpillar {
		private long leaves;
		private int K;
		private int jumpNo[];
		
		public Caterpillar(){}
		public Caterpillar(long leaves, int K){
			this.leaves = leaves;
			this.K = K;
			this.jumpNo = new int[K];
		}
	}
	
	
	public Caterpillar readInput(){
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Caterpillar caterpillar = new Caterpillar();
		try{
			caterpillar.leaves = Long.parseLong(input.readLine().trim());
			if ( !(caterpillar.leaves >0 && caterpillar.leaves<= Math.pow(10, 9)) ) {
				throw new IllegalArgumentException("Leaves Must be in range N >0 and N<= 10^9");
			}
			caterpillar.K = Integer.parseInt(input.readLine().trim());
			if ( !(caterpillar.K >0 && caterpillar.K<= 15) ) {
				throw new IllegalArgumentException("Caterpillars Must be in range K >0 and K<= 15");
			}
			
			int i = 0;
			caterpillar.jumpNo = new int[caterpillar.K];
			while (i<caterpillar.K) {
				caterpillar.jumpNo[i] = Integer.parseInt(input.readLine().trim());
				//System.out.println("jump no : "+jumpNo[i]);
				i++;
			}
			
		}catch(NumberFormatException nfe){
			System.out.println("Caught NumberFormatException : Unable to parse input");
			nfe.getStackTrace();
		}catch(IOException ioe){
			System.out.println("Caught IOException: Unable to read Input from Stdin");
			ioe.getStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return caterpillar;
	}
	
	public static void main(String args[]){
		CaterpillarSolution cp = new CaterpillarSolution();
		Caterpillar caterpillar = cp.readInput();
		System.out.println(cp.getNoOfUneatenLeaves(caterpillar));
	}

	private int getNoOfUneatenLeaves(Caterpillar caterpillar) {
		
		int leave = caterpillar.jumpNo[0], j=0;
		int unEatenNo = leave-1; // assuming jumpNo are in ascending order.
		long N = caterpillar.leaves;
		int K = caterpillar.K;
		while (leave<=N) {
			j=0;
			while (j<K) {
				if(leave % caterpillar.jumpNo[j] == 0){
					break;
				}
				j++;
			}
			if (j==K) {
				// this leave is not divided by any jumpNo
				unEatenNo++;
			}
			leave++;
		}
		return unEatenNo;
	}

}
