package com.rrohit.hakerrank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 1. There are ‘n’ ticket windows in the railway station, ith window has a[i] tickets available. 
 * Price of a ticket is equal to the number of tickets remaining in that window at that time. 
 * When ‘m’ tickets have been sold, what’s the maximum amount of money the railway station can earn?
	e.g.
	INPUT: n=2, m=4
	a1=2 , a2=5
	OUTPUT: 14(2nd window sold 4 tickets so 5+4+3+2).
 * @author rrohit
 */
public class RailwayTicketSell {
	
	public class TicketData{
		private int windows;
		private long tickets;
		private int[] avail;
		
		public TicketData(){}
		
		public TicketData(int windows, long tickets){
			this.windows = windows;
			this.tickets = tickets;
			this.avail = new int[windows];
		}
	}
	
	public class MaxHeap {
		
		private int[] heap;
		private int count;
		private int capacity;
		
		public MaxHeap(){}
		
		public MaxHeap(int count, int capacity) {
			this.capacity = capacity;
			this.count= count;
			this.heap = new int[capacity];
		}
		
		public int getLeft(int i) {
			int left = 2*i+1;
			if (left >= this.count){
				return -1;
			}
			return left;
		}
		
		public int getParent(int i) {
			int parent = (i-1)/2;
			if (parent >= this.count){
				return -1;
			}
			return parent;
		}
		
		public int getRight(int i) {
			int right = 2*i+2;
			if (right >= this.count){
				return -1;
			}
			return right;
		}
		
		public int getMax() {
			if (this.count ==0){
				return -1;
			}
			return this.heap[0];
		}
		
		public void heapify(int i) {
			int left, right, max, temp;
			left = getLeft(i);
			right = getRight(i);
			
			if ( (left != -1) && this.heap[left]>this.heap[i]){
				max = left;
			}else{
				max = i;
			}
			
			if ( (right != -1) && (this.heap[right]>max)){
				max = right;
			}
			
			if (max != i) { // swap i with max, then heapify on max
				temp = this.heap[i];
				this.heap[i] = this.heap[max];
				this.heap[max] = temp;
				heapify(max); 
			}
		}
		
		/*
		 * 1 Copy top element into temp
		 * 2 Copy last element onto top
		 * 3 decrease heap count
		 * 4 now heapify top
		 * Time : O(lgN)
		 */
		public int deleteMax() {
			if (this.count == 0){
				return -1;
			}
			int data = this.heap[0];
			this.heap[0] = this.heap[this.count-1];
			this.count--;
			heapify(0);
			return data;
		}
		
		public void insert(int data){
			if (this.count == this.capacity){
				//resizeHeap();
			}
			this.count++;
			int i = count-1;
			while (i>=0 && data>this.heap[(i-1)/2]){
				this.heap[i] = this.heap[(i-1)/2];
				i = (i-1)/2;
			}
			this.heap[i] = data;
		}
		
		public void buildheap(int []arr, int n){
			this.capacity = n;
			this.count = n;
			this.heap = arr;
			/*
			 * 1. Consider arr as a heap
			 * 2. And start heapify from last parent
			 */
			for (int i=(n-1)/2; i>=0; i--){
				this.heapify(i);
			}
		}
	}

	public TicketData readInput() {
		TicketData td = new TicketData();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		try{
			String[] inputArr = input.readLine().trim().split(" ");
			td.windows = Integer.parseInt(inputArr[0]);
			td.tickets = Long.parseLong(inputArr[1]);
			td.avail = new int[td.windows];
			
			inputArr = input.readLine().trim().split(" ");
			int len = inputArr.length;
			if (len>td.windows) {
				throw new IllegalArgumentException("Tickets must be given for all windows");
			}
			int index=0;
			while(index<len){
				td.avail[index] = Integer.parseInt(inputArr[index]);
				index++;
			}
		}catch(NumberFormatException nfe){
			System.out.println("Unable to parse input");
			nfe.getStackTrace();
		}catch(IOException ioe){
			System.out.println("Unable to read input");
			ioe.getStackTrace();
		}finally{
			try{
				input.close();
			}catch(IOException ioe){
				System.out.println("Unable to close input");
				ioe.getStackTrace();
			}
		}
		return td;
	}
		
	private long getMaxAmtFromTicketSell(TicketData td) {
		
		int i =0;
		long maxAmount =0;
		MaxHeap heap = new MaxHeap();
		heap.buildheap(td.avail, td.windows);
		int data;
		while (i<td.tickets) {
			data = heap.deleteMax();
			maxAmount += data;
			data--;
			i++; // increase ticket count as well
			while (data>heap.getMax() && i<td.tickets){ // sell ticket till its rate are highest
				maxAmount += data;
				data--;
				i++; // increase the ticket count as well
			}
			heap.insert(data); // since rate are not highest insert back to heap
		}
		
		return maxAmount;
	}
	
	public static void main(String[] args) {
		RailwayTicketSell rt = new RailwayTicketSell();
		TicketData td = rt.readInput();
		long maxAmount = rt.getMaxAmtFromTicketSell(td);
		System.out.println(maxAmount);
	}

}
