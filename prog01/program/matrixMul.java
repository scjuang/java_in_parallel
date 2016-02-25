/*==============================================================================
  �{���W�١GmatrixMul.java
  �{�������G�ϥ�N��Java threads�p����N*N�x�}�����k�B��C
  �������ҡGCompiler: Sun Java SDK 1.4.0
            Usage: java matrixMul N
            N�O�@�ӥ���ƭȡA�N��x�}����C�j�p�C
  �� �� �ɡGprog1.doc
  ���g���@�G���ӶW�]688410037�^
==============================================================================*/

import java.lang.*;
import java.util.*;


/*------------------------------------------------------------------------------
  ���O�W�١GcomputeRow�A�~��Thread���O�A�t�d�x�}���@�C���k�B��C
  ���������Grow - �s��thread�ҭn�t�d���u�C�s���v�C
            A[][] - multiplicand�A�Q���Ưx�}�C
            B[][] - multiplier�A���Ưx�}�C
            C[][] - resultant�A�B�⵲�G�x�}�AC[][] = A[][] * B[][]�C
            size - �x�}����C�j�p�A����ϥΪ̶ǤJ��N�ȡC
            computeRow - �����O���غc�禡�A��W�z��data member�i���l�Ƴ]�w�C
            run - Thread�Q�Ұʫ�Ұ��檺�{���X�A�w��Y�@�C�i�歼�k�B��C
------------------------------------------------------------------------------*/
class computeRow extends Thread {
  int row;
  int A[][], B[][], C[][];
  int size;

  //���F�z�Lsuper()�I�s�������O���غc�禡�~�A�ù�Ҧ���data member�H
  //call by reference���覡�g�J��l�ȡC
  public computeRow(int num, int s, int a[][], int b[][], int c[][]) {
    super();
    row = num;
    A = a;
    B = b;
    C = c;
    size = s;
  }
  
  //�~��Thread���O�ӨӪ�interface�A�í��s��g���u�x�}�C�v�����k�B��A
  //�B�⵲�G�����g�^C�x�}�]���G�x�}�^�C
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
  ���O�W�١GmatrixMul�A���{�����D���O�C
  ���������GN - �s��x�}����C�j�p�A�ѨϥΪ̦ۦ��J�C
            prettyPrint - ���Ѥ@�ӥi�H��x�}�C�L���;�����[����X�榡�C
            main - ��ӵ{�����i�J�I�A�t�d�����Ѽơ]N�^�B�إߩM�޲zthreads�B
                   �÷J��B�⵲�G��X�C
------------------------------------------------------------------------------*/
class matrixMul {
  static int N;
  
  //�N�x�}���e�Ѿ���ন�r��A�A�̾ڪ��׶�J�A��ťաA�F��V�k�Y�ƪ��ĪG�C
  public static void prettyPrint(int M[][]) {
    //�����x�}���u�̪��v���Ʀr���סC
    int largeLength = String.valueOf(M[N-1][N-1]).length();
    String valueOut = "";
    
    for (int i = 0; i < N; i ++) {
      for (int j = 0; j < N; j ++) {
      	valueOut = String.valueOf(M[i][j]);
      	//�b�Ҧ��ƭȦr��e��J�u�ťաv�ϥ��M�̪����Ʀr�@�˪��C
      	for (int k = 0; k < (largeLength - valueOut.length()); k ++) {
      	  valueOut = " " + valueOut;
      	}
      	System.out.print("[" + valueOut + "] ");
      }
      System.out.println();
    }
    System.out.println();
  }
    
  
  /*---------- ���{�����i�J�I�I----------*/	
  public static void main(String args[]) {
    int exeTime = 0;
  	
    //�x�s�ϥΪ̶ǤJ���x�}��C�j�p�Ȩ�N�A�ëO�ҥ��O�@�Ӥj��0������ơC
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
    
    //��l��A�BB�BC�T�ӯx�}�]���O��multiplicand�Bmultiplier�Bresultant�^�A
    //�䤤A�MB��ӯx�}����l�Ȭ�A[i][j]=B[i][j]=(i*N)+j�A��C�x�}��l�ȫh��0�C
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
    
    //�ϥΤ@�Ӥj�p��N���}�C�s��N��threads�A�óz�L�غc�禡���w
    //��thread�ҭt�d���u�C�v�C
    computeRow worker[] = new computeRow[N];
    for (int i = 0; i < N; i ++) {
      //��i��thread�N�t�d�p��C�x�}����i�C�C
      worker[i] = new computeRow(i, N, A, B, C); 
    }
    
    //�����p��ҩl�ɶ��C
    Calendar sTime = Calendar.getInstance();
    
    //�̧ǱҰʨC�@��thread�C
    for (int i = 0; i < N; i ++) {
      worker[i].start();
    }
    
    //main thread�̧�join�C�@��thread�C
    for (int i = 0; i < N; i ++) {
      try {
        worker[i].join();
      } catch(InterruptedException e) { 
      }
    }
    
    //�����p�⵲���ɶ��C
    Calendar eTime = Calendar.getInstance();
    
    System.out.println(N + "x" + N + " matrix multiplication => \n");
    //��X�Q���Ưx�}�]multiplicand�^��standard output�C
    System.out.println("Multiplicand ->");
    prettyPrint(A);
    
    //��X�Q���Ưx�}�]multiplier�^��standard output�C
    System.out.println("Multiplier ->");
    prettyPrint(B);
   
    //��X���G�x�}�]resultant�^��standard output�C
    System.out.println("Resultant ->");   
    prettyPrint(C);
    
    //�C�L�p��ɶ��C
    System.out.print("Execution Time -> ");
    System.out.println(eTime.getTimeInMillis()-sTime.getTimeInMillis() + " ms");
  }
}