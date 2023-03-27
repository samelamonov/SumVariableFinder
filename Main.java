/* AClab3_MaxSum_TODO.java, A.Pająk 2023
   Classic problem showing the complexity gap between naive approach
   of O(n^3) cost through O(n^2), O(n*log(n)) to optimal O(n) solution. 
   The problem statement
   ---------------------
   Given an array X of numbers (we will use integers) find a subarray 
   whose elements give maximum non negative total value. If all elements
   of the array are negative or zero empty subarray should be choosen,
   which by convention "sums up" to 0. For instance the array
   [3,-2,-4,5*,-2*,4*,-1] should give maximum value of 7 (elements with *).
*/
// TODO-1: write below your name and student ID
// my name:       my student ID:  

// TODO-2: before submitting rename the source .java file and the public class
// to:     AClab3_<YourLastName>.
// Note: I will not accept any other file formats, only *.java
// ===========================================================

import java.util.Random;
import java.util.Scanner;

public class AClab3_Sanjar_Elamonov{

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    Random rg = new Random();
    System.out.println("My name: Sanjar Elamonov"+" My student ID: 44467");
    System.out.println("/nThe program finds a subarray with maximum >=0 sum of elements."); 
    System.out.println("If all elements are <= 0, the choosen subarray is empty.");
    System.out.println("Enter n r: size of array and range -r..r to generate from.");
    System.out.println("If n<=0: end of program; n>0 && r<=0: data from the user.");
    System.out.println("Convention: empty subarray is denoted by index range [-1..-1].");
    
    while(true ){ // Conversation
      int n, r;           // Input data
      int beg, end, sum;  // For index range [beg..end] and subarray max sum 
      int[] X, result;    // Arrays for generated/entered data and result capture

      System.out.print("\nSize, range: n r = ");
      n = scn.nextInt(); 
      r = scn.nextInt(); 
      if(n <= 0) break; // Terminate program
      
      X = new int[n];
      
      if(r>0) { // Random generation
        for(int i=0; i<n; ++i){
          X[i] = rg.nextInt(2*r+1) -r;          // Values from -r..r
          System.out.print(X[i] + "  ");
          if((i+1)%10==0) System.out.println(); // Show 10 numbers in a line
        }
        if(n%10!=0) System.out.println();
      } // Data in X generated and shown
      else { // User data input
        System.out.println("Enter " + n + " ints");
        for(int i=0; i<n; ++i)
          X[i] = scn.nextInt();
      } // Data from the user ready to process
      
      // Computing and printing the maximum sums
      // maxSumOn2S(X), maxSumDaCS(X, 0, n), maxSumOptS(X)
      // =================================================
      System.out.println("Maximum sum by maxSumOn2S(): " + maxSumOn2S(X));
      System.out.println("Maximum sum by maxSumDaCS(): " + maxSumDaCS(X, 0, n));
      System.out.println("Maximum sum by maxSumOptS(): " + maxSumOptS(X));

      // Computing and printing the max sums and ranges
      // maxSumOn2R(X), maxSumOptR(X)
      // ============================
      
      // maxSumOn2R(X)
      result = maxSumOn2R(X); // returns: beg, end, maxSum      
      beg=result[0]; end=result[1]; sum=result[2];
      System.out.println("Maximum sum by maxSumOn2R(): " + sum +
                         ", indices: [" + beg + ".." + end + "]");
      printMarkedRange(X, beg, end);
      
      // maxSumOptR(X)
      result = maxSumOptR(X); // returns: beg, end, maxSum      
      beg=result[0]; end=result[1]; sum=result[2];
      System.out.println("Maximum sum by maxSumOptR(): " + sum +
                         ", indices: [" + beg + ".." + end + "]");
      printMarkedRange(X, beg, end);

    }
    scn.close();
    System.out.println("\nEnd of program");
  } // main()
  
  // Methods returning only max sums (suffix 'S')
  // ===============================
  
  // O(n^2) method. Returns only max sum of a subarray.
  static int maxSumOn2S(int X[]){
    int n = X.length, sum = 0;

    for(int d=0; d<n; ++d){
      int s = 0;
      for(int g=d; g<n; ++g){
        s += X[g];
        sum = Math.max(sum, s);
      }
    }
    return sum;
  }

  // O(nlog(n)) divide & conquer recursive method; returns only max sum.
  // Time complexity equation: T(n) = 2T(n/2) + O(n)
  static int maxSumDaCS(int X[], int d, int g) { // index range [d..g)
    int maxL, maxR, maxA, maxB, s;
  
    if(d>=g) return 0; // Empty subarray
    if(d == g-1)        // 1 element only
      return Math.max(X[d], 0);
  
    int m = (d+g)/2;    // Middle index

    //Compute max sum to the left of m
    s = maxL= 0;
    for(int i=m-1; i>=d; --i) {
      s += X[i];
      maxL = Math.max(s, maxL);
    }
  
    //Compute max sum to the right of m
    s = maxR = 0;
    for(int i=m; i<g; i++) {
      s += X[i];
      maxR = Integer.max(s, maxR);
    }
    
    maxA = maxSumDaCS(X, d, m);  // Recursion on left subarray
    maxB = maxSumDaCS(X, m, g);  // Recursion on right subarray
    return Math.max(Math.max(maxA, maxB), maxL + maxR);
  }

  // O(n) optimal method; returns only max sum 
  static int maxSumOptS(int X[]) {
    int n = X.length;
    int maxG = 0;  // Max global sum 
    int maxL = 0;  // Max left-side sum

    for(int i=0; i<n; ++i) { // Update maxG, maxL in X[0..i]
      maxL = Math.max(0, maxL+X[i]);
      maxG = Math.max(maxL, maxG);
    }
    return maxG;
  }

  // Methods returning index ranges and max sums (suffix 'R')
  // ========================================================
  
  // O(n^2) complexity method. Returns index range beg..end and max sum.
  static int[] maxSumOn2R(int X[]){
    int n = X.length;
    int beg=-1, end=-1, sum = 0; // Empty subarray, zero sum 

    for(int d=0; d<n; ++d){
      if(X[d]<=0) continue; // Ignore elems <=0 at range start
      int s = 0;
      for(int g=d; g<n; ++g){
        s += X[g];
        if(sum < s){ 
          sum = s; beg = d; end = g;
        }
      }
    }
    return new int[]{beg, end, sum};
  }

  // O(n) optimal method; returns range [beg..end] and max sum 
  // TODO3 (4p): Define this linear complexity method that returns an array
  // with 3 values: beg, end, sum: index range beg..end and max sum.
  static int[] maxSumOptR(int X[]) {
    int beg=-1, end=-1, sum = 0;   // Subarray params (now empty)


    
    int n = X.length;
    int maxG = 0;  // Max global sum 
    int maxL = 0;  // Max left-side sum
    int tempBeg = 0;  // Temporary start index for current subarray
    int tempSum = 0;  // Temporary sum for current subarray
    int tempEnd = -1;  // Temporary end index for current subarray

    for(int i=0; i<n; ++i) { // Update maxG, maxL, tempBeg, tempSum, and tempEnd in X[0..i]
        if(X[i] > (tempSum + X[i])) {  // If current element is greater than the sum of current subarray
            tempBeg = i;  // Start new subarray from this element
            tempSum = X[i];
        } else {
            tempSum += X[i];  // Extend current subarray
        }

        if(tempSum > maxG) {  // If current subarray sum is greater than global max sum
            maxG = tempSum;  // Update global max sum
            beg = tempBeg;  // Update start index of max subarray
            end = i;  // Update end index of max subarray
        }
    }
    return new int[]{beg, end, maxG};
}
    // Put your code here


  // TODO4 (4p): define printMarkedRange() method that prints array X
  // at most 10 elements in a line and for each element from index range
  // beg..end appends an asterisk as shown in examples.
  
static void printMarkedRange(int[] X, int beg, int end) {
    for (int i = 0; i < X.length; i++) {
        if (i % 10 == 0 && i > 0) {
            System.out.println();
        }
        if (i >= beg && i <= end) {
            System.out.print(X[i] + "* ");
        } else {
            System.out.print(X[i] + "  ");
        }
    }
    System.out.println();
}
}

  

/* Example run of application with missing methods defined.

The program finds a subarray with maximum >=0 sum of elements.
If all elements are <= 0, the choosen subarray is empty.
Enter n r: size of array and range -r..r to generate from.
If n<=0: end of program; n>0 && r<=0: data from the user.
Convention: empty subarray is denoted by index range [-1..-1].

Size, range: n r = 5 0
Enter 5 ints
0 -1 4 -2 3
Maximum sum by maxSumOn2S(): 5
Maximum sum by maxSumDaCS(): 5
Maximum sum by maxSumOptS(): 5
Maximum sum by maxSumOn2R(): 5, indices: [2..4]
0  -1  4* -2* 3* 
Maximum sum by maxSumOptR(): 5, indices: [2..4]
0  -1  4* -2* 3* 

Size, range: n r = 10 9
-2  3  2  7  6  -4  -7  7  7  -9  
Maximum sum by maxSumOn2S(): 21
Maximum sum by maxSumDaCS(): 21
Maximum sum by maxSumOptS(): 21
Maximum sum by maxSumOn2R(): 21, indices: [1..8]
-2  3* 2* 7* 6* -4* -7* 7* 7* -9  
Maximum sum by maxSumOptR(): 21, indices: [1..8]
-2  3* 2* 7* 6* -4* -7* 7* 7* -9  

Size, range: n r = 5 0
Enter 5 ints
0 0 -1 0 0
Maximum sum by maxSumOn2S(): 0
Maximum sum by maxSumDaCS(): 0
Maximum sum by maxSumOptS(): 0
Maximum sum by maxSumOn2R(): 0, indices: [-1..-1]
0  0  -1  0  0  
Maximum sum by maxSumOptR(): 0, indices: [-1..-1]
0  0  -1  0  0  

Size, range: n r = 20 20
-14  -17  -3  -11  -3  12  -3  4  -11  -2  
-8  -15  -12  -2  13  -19  -10  1  2  -14  
Maximum sum by maxSumOn2S(): 13
Maximum sum by maxSumDaCS(): 13
Maximum sum by maxSumOptS(): 13
Maximum sum by maxSumOn2R(): 13, indices: [5..7]
-14  -17  -3  -11  -3  12* -3* 4* -11  -2  
-8  -15  -12  -2  13  -19  -10  1  2  -14  
Maximum sum by maxSumOptR(): 13, indices: [5..7]
-14  -17  -3  -11  -3  12* -3* 4* -11  -2  
-8  -15  -12  -2  13  -19  -10  1  2  -14  

Size, range: n r = 0 0

End of program

*/