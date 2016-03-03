import java.lang.*;
import java.util.*;

class computeRow extends Thread {
  int row;
  int A[][], B[][], C[][];
  int size;

  public computeRow(int num, int s, int a[][], int b[][], int c[][]) {
    super();
    row = num;
    A = a;
    B = b;
    C = c;
    size = s;
  }

  public void run() {
    int result, column, index;

    for (result = 0, column = 0; column < size; column ++) {
      for (index = 0; index < size; index ++) {
        result = A[row][index] * B[index][column] + result;
      }
      C[row][column] = result;
      result = 0;
    }
  }
}

class matrixMul {
  static int N;

  public static void prettyPrint(int M[][]) {
    int largeLength = String.valueOf(M[N-1][N-1]).length();
    String valueOut = "";

    for (int i = 0; i < N; i ++) {
      for (int j = 0; j < N; j ++) {
      	valueOut = String.valueOf(M[i][j]);
      	for (int k = 0; k < (largeLength - valueOut.length()); k ++) {
      	  valueOut = " " + valueOut;
      	}
      	System.out.print("[" + valueOut + "] ");
      }
      System.out.println();
    }
    System.out.println();
  }

  public static void main(String args[]) {
    int exeTime = 0;

    N = 1;
    try {
      N = Integer.parseInt(args[0]);
      if (N <= 0) throw new NumberFormatException();
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.print("Usage: java matrixMul N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.print("Usage: java matrixMul N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    }

    int A[][] = new int[N][N];
    int B[][] = new int[N][N];
    int C[][] = new int[N][N];
    for (int i = 0; i < N; i ++) {
      for (int j = 0; j < N; j ++) {
      	A[i][j] = (i * N) + j;
      	B[i][j] = (i * N) + j;
      	C[i][j] = 0;
      }
    }

    computeRow worker[] = new computeRow[N];
    for (int i = 0; i < N; i ++) {
      worker[i] = new computeRow(i, N, A, B, C);
    }

    Calendar sTime = Calendar.getInstance();

    for (int i = 0; i < N; i ++) {
      worker[i].start();
    }

    for (int i = 0; i < N; i ++) {
      try {
        worker[i].join();
      } catch(InterruptedException e) {
      }
    }

    Calendar eTime = Calendar.getInstance();

    System.out.println(N + "x" + N + " matrix multiplication => \n");

    System.out.println("Multiplicand ->");
    prettyPrint(A);

    System.out.println("Multiplier ->");
    prettyPrint(B);

    System.out.println("Resultant ->");
    prettyPrint(C);

    System.out.print("Execution Time -> ");
    System.out.println(eTime.getTimeInMillis()-sTime.getTimeInMillis() + " ms");
  }
}
