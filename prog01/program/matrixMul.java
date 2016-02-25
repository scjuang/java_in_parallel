/*==============================================================================
  程式名稱：matrixMul.java
  程式說明：使用N個Java threads計算兩個N*N矩陣的乘法運算。
  執行環境：Compiler: Sun Java SDK 1.4.0
            Usage: java matrixMul N
            N是一個正整數值，代表矩陣的行列大小。
  說 明 檔：prog1.doc
  撰寫維護：莊勝超（688410037）
==============================================================================*/

import java.lang.*;
import java.util.*;


/*------------------------------------------------------------------------------
  類別名稱：computeRow，繼承Thread類別，負責矩陣的一列乘法運算。
  成員說明：row - 存放thread所要負責的「列編號」。
            A[][] - multiplicand，被乘數矩陣。
            B[][] - multiplier，乘數矩陣。
            C[][] - resultant，運算結果矩陣，C[][] = A[][] * B[][]。
            size - 矩陣的行列大小，等於使用者傳入的N值。
            computeRow - 此類別的建構函式，對上述的data member進行初始化設定。
            run - Thread被啟動後所執行的程式碼，針對某一列進行乘法運算。
------------------------------------------------------------------------------*/
class computeRow extends Thread {
  int row;
  int A[][], B[][], C[][];
  int size;

  //除了透過super()呼叫父親類別的建構函式外，並對所有的data member以
  //call by reference的方式寫入初始值。
  public computeRow(int num, int s, int a[][], int b[][], int c[][]) {
    super();
    row = num;
    A = a;
    B = b;
    C = c;
    size = s;
  }
  
  //繼承Thread類別而來的interface，並重新改寫成「矩陣列」的乘法運算，
  //運算結果直接寫回C矩陣（結果矩陣）。
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


/*------------------------------------------------------------------------------
  類別名稱：matrixMul，本程式的主類別。
  成員說明：N - 存放矩陣的行列大小，由使用者自行輸入。
            prettyPrint - 提供一個可以對矩陣列印產生整齊美觀的輸出格式。
            main - 整個程式的進入點，負責接收參數（N）、建立和管理threads、
                   並彙整運算結果輸出。
------------------------------------------------------------------------------*/
class matrixMul {
  static int N;
  
  //將矩陣內容由整數轉成字串，再依據長度填入適當空白，達到向右縮排的效果。
  public static void prettyPrint(int M[][]) {
    //紀錄矩陣內「最長」的數字長度。
    int largeLength = String.valueOf(M[N-1][N-1]).length();
    String valueOut = "";
    
    for (int i = 0; i < N; i ++) {
      for (int j = 0; j < N; j ++) {
      	valueOut = String.valueOf(M[i][j]);
      	//在所有數值字串前填入「空白」使它和最長的數字一樣長。
      	for (int k = 0; k < (largeLength - valueOut.length()); k ++) {
      	  valueOut = " " + valueOut;
      	}
      	System.out.print("[" + valueOut + "] ");
      }
      System.out.println();
    }
    System.out.println();
  }
    
  
  /*---------- 本程式的進入點！----------*/	
  public static void main(String args[]) {
    int exeTime = 0;
  	
    //儲存使用者傳入的矩陣行列大小值到N，並保證它是一個大於0的正整數。
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
    
    //初始化A、B、C三個矩陣（分別為multiplicand、multiplier、resultant），
    //其中A和B兩個矩陣的初始值為A[i][j]=B[i][j]=(i*N)+j，而C矩陣初始值則為0。
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
    
    //使用一個大小為N的陣列存放N個threads，並透過建構函式指定
    //該thread所負責的「列」。
    computeRow worker[] = new computeRow[N];
    for (int i = 0; i < N; i ++) {
      //第i個thread就負責計算C矩陣的第i列。
      worker[i] = new computeRow(i, N, A, B, C); 
    }
    
    //紀錄計算啟始時間。
    Calendar sTime = Calendar.getInstance();
    
    //依序啟動每一個thread。
    for (int i = 0; i < N; i ++) {
      worker[i].start();
    }
    
    //main thread依序join每一個thread。
    for (int i = 0; i < N; i ++) {
      try {
        worker[i].join();
      } catch(InterruptedException e) { 
      }
    }
    
    //紀錄計算結束時間。
    Calendar eTime = Calendar.getInstance();
    
    System.out.println(N + "x" + N + " matrix multiplication => \n");
    //輸出被乘數矩陣（multiplicand）到standard output。
    System.out.println("Multiplicand ->");
    prettyPrint(A);
    
    //輸出被乘數矩陣（multiplier）到standard output。
    System.out.println("Multiplier ->");
    prettyPrint(B);
   
    //輸出結果矩陣（resultant）到standard output。
    System.out.println("Resultant ->");   
    prettyPrint(C);
    
    //列印計算時間。
    System.out.print("Execution Time -> ");
    System.out.println(eTime.getTimeInMillis()-sTime.getTimeInMillis() + " ms");
  }
}