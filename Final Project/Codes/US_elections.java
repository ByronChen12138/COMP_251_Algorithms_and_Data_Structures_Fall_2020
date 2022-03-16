import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;

public class US_elections {
	
	private static int numOfDelegates;
	private static int comfirmedDelegatesForBiden;
	private static int comfirmedDelegatesForTrump;
	private static int[] uncomfirmedStates;	
	private static int numOfUncomfirmedStates;
	
	/**Do the first check for all states
	 * 
	 * All the states with 0 undecided vote is comfirmed and the delegates can be recorded
	 * All the states, which don't change the winner after all the undecided votes goes to Biden/Trump, are confirmed
	 * 
	 * All comfirmed delegates are added to the related field and the confirmed states are recorded
	 * 
	 * Update to_win for the unconfirmed states
	 * 
	 * @param num_states 
	 * @param delegate 
	 * @param vote_Biden 
	 * @param vote_Trump 
	 * @param vote_Undecided 
	 * @param to_win
	 * @param index
	 * 
	 * @return updated to_win
	 */
	
	private static int statesFirstCheck(int num_states, int delegate, int vote_Biden, int vote_Trump, int vote_Undecided, int to_win, int index) {
		int winNum = (vote_Biden + vote_Trump + vote_Undecided)/2 + 1 - vote_Biden;		// The number of votes needed to win for Biden
		int[] temp1 = new int[num_states];	// create a temp array to store the Uncomfirmed States
		int k = 0;							// the index of the temp array
		
		// initialize the temp1 array
		for (int i = 0; i < num_states; i++) {
			temp1[i] = -1;
		}
		
		
		if (vote_Undecided == 0) {									// no undecided vote
			
			if (vote_Biden > vote_Trump) {							// win
				comfirmedDelegatesForBiden += delegate;
				to_win = 0;											// CONFIRMED

			}else {													// lose and tie
				comfirmedDelegatesForTrump += delegate;
				to_win = -1;										// CONFIRMED
			}
			
		}else {														// there are undecided votes
			if (vote_Biden > vote_Trump) {							// IF Biden still wins without undecided votes
				if (vote_Biden > vote_Trump + vote_Undecided) {		// IF Biden wins if all undecided votes goes to Trump
					to_win = 0;										// CONFIRMED
					comfirmedDelegatesForBiden += delegate;	
				
				}else {
					if (winNum <= vote_Undecided) {					// IF have chance to win
						to_win = winNum;
						temp1[k] = index;
						k++;
						numOfUncomfirmedStates++;
						
					}else {
						to_win = -1;								// CONFIRMED
						comfirmedDelegatesForTrump += delegate;	
					}
				}
			}else if(vote_Biden == vote_Trump) {					// IF tie without undecided votes
				to_win = winNum;
				temp1[k] = index;
				k++;
				numOfUncomfirmedStates++;
				
			}else {													// IF Biden loses without undecided votes
				if (vote_Biden + vote_Undecided < vote_Trump) {		// IF Biden still loses if all undecided votes goes to Biden
					to_win = -1;									// CONFIRMED
					comfirmedDelegatesForTrump += delegate;	
				
				}else {
					if (winNum <= vote_Undecided) {					// IF have chance to win
						to_win = winNum;
						temp1[k] = index;
						k++;
						numOfUncomfirmedStates++;
						
					}else {
						to_win = -1;								// CONFIRMED
						comfirmedDelegatesForTrump += delegate;	
					}
				}
			}
		}
		
		return to_win;												// 0 means win; -1 means lose(or tie)
	}
	
	/**Do the comparision based on to_win value
	 * 
	 * if w1 < w2, returns -1
	 * if w1 = w2, returns 0
	 * if w1 > W2, returns 1
	 * 
	 * @param num1
	 * @param num2
	 * @param to_win
	 * @return int
	 */
	
	/*
    private static int compare_toWin(int num1, int num2, int[] to_win) {
		int w1 = to_win[num1];
		int w2 = to_win[num2];
		
		if (w1 < w2) {
			return -1;
			
		}else if(w1 == w2){
			return 0;
			
		}else {
			return 1;
		}
	}
	*/
	
    /**MERGE
     * Input: Array containing sorted subarrays arr[p...q] and arr[q+1...r]
     * 
     * Output: Merged sorted subarray in arr[p...r]
     * 
     * Sort them in increasing order by the to_win value
     * 
     * @param arr
     * @param p
     * @param q
     * @param r
	 * @param to_win
     */
	
    /*
    private static void merge(int[] arr, int p, int q, int r, int[] to_win) {
        // Get the lengths of the two subarray
        int n1 = q - p + 1;
        int n2 = r - q;
        
        //create the subarrays 
        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[p + i];        	
        }
        
        for (int j = 0; j < n2; j++) {
            R[j] = arr[q + j + 1];
        }
        
        // Initialize indices of the subarrays
        int i = 0;
        int j = 0;
        
        // Merge and sort them at the same time
        for (int k = p; k <= r; k++) {
        	if(compare_toWin(L[i], R[j], to_win) == 1) {		// If the to_win number of L[i] > R[i]
        		arr[k] = R[j];
        		j++;
        		
        	}else {										// If the to_win number of L[i] <= R[i]
        		arr[k] = L[i];
        		i++;
        	}
        }
    }
 	*/
 	
	/**Merge Sort function
     * With T(n) = O(nlgn)
     * 
     * @param arr
     * @param p
     * @param r
	 * @param to_win
     */
    
	/*
	private static void mergeSort(int[] arr, int p, int r, int[] to_win) {
        if (p < r) {						//if any element isn't checked in this array
            int q = (p + r) / 2;	//get the middle of the array
            
            //Sort two parts of the array by dividing them at middle
            mergeSort(arr, p, q, to_win);
            mergeSort(arr, q + 1, r, to_win);
 
            // Do Merge
            merge(arr, p, q, r, to_win);
        }
    }
 	*/
    
    /**To print an array in a readable way
     * 
     * @param arr
     */
    
	/*
    private static void printArray(int[] arr) {
        int k = arr.length;
        System.out.print("[");
        
        for (int i = 0; i < k; i++) {
        	if(i != k - 1) {
        		System.out.print(arr[i] + " ");
        	}else {
        		System.out.print(arr[i]);
        	}
        }
        
        System.out.println("]");
    }
	*/
    
	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){
		int min = -1;
		numOfDelegates = 0;
		comfirmedDelegatesForBiden = 0;
		comfirmedDelegatesForTrump = 0;
		numOfUncomfirmedStates = 0;
		uncomfirmedStates = new int[num_states];
		int k = 0;
		
		for(int i = 0; i < num_states; i++) {
			uncomfirmedStates[i] = -1;
		}
		
		
		//get the total number of the delegates
		for (int i = 0; i < num_states; i++) {
			numOfDelegates += delegates[i];
		}
		
		int numOfDelegatesForWin = numOfDelegates / 2 + 1;	//when the num of delegate for one of them greater than this, END
		
		//Get the to_win value
		int[] to_win = new int[num_states];				// Number of votes for Biden to win the corresponding state; 0 means win; -1 means lose(or tie)
		for (int i = 0; i < num_states; i++) {
			to_win[i] = statesFirstCheck(num_states, delegates[i], votes_Biden[i], votes_Trump[i], votes_Undecided[i], to_win[i], i);
			if (to_win[i] > 0) {
				uncomfirmedStates[k] = i;
				k++;
			}
		}
		
		// Do the sort
		//mergeSort(uncomfirmedStates, 0, numOfUncomfirmedStates - 1, to_win);
		
		int numOfDelegatesNeeded_Biden = numOfDelegatesForWin - comfirmedDelegatesForBiden;
		
		// Dynamic Part
		int[][] M = new int[numOfUncomfirmedStates + 1][numOfDelegatesNeeded_Biden + 1];
		
		for(int i = 0; i < numOfUncomfirmedStates + 1; i++) {		
			for(int j = 0; j < numOfDelegatesNeeded_Biden + 1; j++) {
				M[i][j] = Integer.MAX_VALUE;
			}
		}
		
		int maxDelegate = 0;
		
		for(int i = 1; i < numOfUncomfirmedStates + 1; i++) {
			int vi = uncomfirmedStates[i - 1];
			maxDelegate += delegates[vi];							// Stores the maximum delegates Biden could get
					
			for(int j = 1; j < numOfDelegatesNeeded_Biden + 1; j++) {
				if(maxDelegate < j) {								// If impossible to win
					
				}else {
					if(j < delegates[vi]) {
						M[i][j] = Math.min(M[i - 1][j], to_win[vi]);
						
					}else if(M[i - 1][j - delegates[vi]] == Integer.MAX_VALUE){
						M[i][j] =  Math.min(M[i - 1][j], to_win[vi]);
								
					}else {
						M[i][j] = Math.min(M[i - 1][j], to_win[vi] + M[i - 1][j - delegates[vi]]);
					}
				}
			}
		}
		
		if (M[numOfUncomfirmedStates][numOfDelegatesNeeded_Biden] != Integer.MAX_VALUE) {
			min = M[numOfUncomfirmedStates][numOfDelegatesNeeded_Biden];
		}
		
		return min;
	}

	public static void main(String[] args) {		
//		int num_states = 3;
//		int[] delegates = {32, 32, 64};
//		int[] votes_Biden = {0, 0, 0};
//		int[] votes_Trump = {0, 0, 0};
//		int[] votes_Undecided = {20, 20, 41};
//		
//		int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
//	    System.out.println(answer);
		
		try {
		  String path = args[0];
	      File myFile = new File(path);
	      Scanner sc = new Scanner(myFile);
	      int num_states = sc.nextInt();
	      int[] delegates = new int[num_states];
	      int[] votes_Biden = new int[num_states];
	      int[] votes_Trump = new int[num_states];
	      int[] votes_Undecided = new int[num_states];	
	      
	      for (int state = 0; state<num_states; state++){
	    	  delegates[state] =sc.nextInt();
	    	  votes_Biden[state] = sc.nextInt();
	    	  votes_Trump[state] = sc.nextInt();
	    	  votes_Undecided[state] = sc.nextInt();
	      }
	      
	      sc.close();
	      int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
	      System.out.println(answer);
    	
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
    	}
  	}

}