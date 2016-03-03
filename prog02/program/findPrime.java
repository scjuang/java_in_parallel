// Find Prime

import java.lang.*;

class primeChecker extends Thread {
  numKeeper keeper;

  public primeChecker(numKeeper k) {
    super();
    keeper = k;
  }

  public void run() {
    int Num = 1;

    int squareRoot = 1;
    boolean isPrime = true;
    Num = keeper.getNum();

    while (Num > 0) {
      Double sqrt = new Double(Math.sqrt(Num));
      squareRoot = sqrt.intValue();
      for (int n = 2; n <= squareRoot; n ++) {
        if ((Num % n) == 0) {
          isPrime = false;
          break;
        }
      }

      if (isPrime) {
      	System.out.println(this.getName() + ": " + Num + " is prime!");
      	keeper.resultTable[Num] = Integer.parseInt(this.getName().substring(7, this.getName().length()));
      } else {
      	System.out.println(this.getName() + ": " + Num + " is not prime!");
      }

      Num = keeper.getNum();
      isPrime = true;
    }
  }
}


class numKeeper {
  static int targetNum = 1;
  static int currentNum = 1;

  static int resultTable[];

  public numKeeper(int tNum, int cNum) {
    targetNum = tNum;
    currentNum = cNum;
    resultTable = new int[targetNum+1];
    for (int i = 0; i <= targetNum; i ++) {
      resultTable[i] = 0;
    }
  }

  synchronized int getNum() {
    int num;

    num = currentNum;

    if (num <= targetNum) {
      currentNum ++;
      return(num);
    } else {
      return(0);
    }
  }
}


class findPrime {
  static int threadNum = 4;
  static int N;
  static numKeeper keeper = null;

  public static void main(String args[]) {

    N = 1;
    try {
      N = Integer.parseInt(args[0]);
      if (N <= 0) throw new NumberFormatException();
      keeper = new numKeeper(N, 2);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    }

    primeChecker checker[] = new primeChecker[4];
    for (int i = 0; i < threadNum; i ++) {
      checker[i] = new primeChecker(keeper);
    }

    for (int i = 0; i < threadNum; i ++) {
      checker[i].start();
    }

    for (int i = 0; i < threadNum; i ++) {
      try {
        checker[i].join();
      } catch(InterruptedException e) {
      }
    }

    System.out.println("\n[Result]");

    System.out.print("Prime List: ");
    int totalPrime = 0;
    int threadFind[] = new int[threadNum+1];
    for (int j = 0; j <= threadNum; j ++) threadFind[j] = 0;
    for (int i = 2; i <= N; i ++) {
      if (keeper.resultTable[i] > 0) {
      	System.out.print(i + " ");
      	totalPrime ++;
      	threadFind[keeper.resultTable[i]] ++;
      }
    }

    System.out.print("\nTotal Prime: " + totalPrime + " = ");
    for (int i = 1; i <= threadNum; i ++) {
      System.out.print(threadFind[i] + "(Thread-" + i + ")");
      if (i != threadNum) System.out.print(" + ");
    }
    System.out.println();


  }
}
