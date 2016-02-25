/*==============================================================================
  程式名稱：findPrime.java
  程式說明：使用4個Java threads計算兩個N*N矩陣的乘法運算。
  執行環境：Compiler: Sun Java SDK 1.4.0
            Usage: java findPrime N
            N是一個正整數值，代表所要檢測的質數範圍。
  說 明 檔：prog02Report.doc
  撰寫維護：莊勝超（688410037）
==============================================================================*/
import java.lang.*;


/*------------------------------------------------------------------------------
  類別名稱：primeChecker，繼承Thread類別，負責以同步的方式取得一個整數值，並檢測
            它是否為質數。
  成員說明：keeper - 存放4個threads所要共同存取的檢測整數資料。（詳參numKeeper的
                     類別說明）
            primeChecker - 本類別的建構函式，主要會接收一個numKeeper類別的物件，
                           因為在Java中若參數是「物件」，預設是以call by reference
                           的方式傳遞，所以再指定給資料成員keeper之後，會保證4個
                           threads是使用同一份資料。
            run - Thread被啟動後所執行的程式碼，針對透過keeper取得的數字分析它是
                  否為質數。
------------------------------------------------------------------------------*/
class primeChecker extends Thread {
  numKeeper keeper;

  //建構函式（Constructor）：除了透過super()呼叫父親類別的建構函式外，並對keeper
  //以call by reference的方式寫入初始值。
  public primeChecker(numKeeper k) {
    super();
    keeper = k;
  }
  
  //繼承Thread類別而來的interface，並重新改寫成質數的檢測函式，
  //檢測結果直接輸出至standard output（螢幕）。
  public void run() {
    int Num = 1;  //存放所要檢查的數值。
                  //等於1，代表初始值。
                  //等於0，代表已經沒有測試資料。
                  //等於其他值，代表真正的測試資料。
                  
    int squareRoot = 1;        //儲存檢測值的平方根。
    boolean isPrime = true;    //若檢測值為質數，為真；反之，為假。
    Num = keeper.getNum();     //以「同步」方式取得檢測值。
    
    //每進入迴圈一次，代表取得一個整數，並判斷是否為質數。
    while (Num > 0) {
      //檢測是否為質數的方法：
      //對檢測值開平方根（假設得到R），把檢測值「除以」從2到R之間的所有數字，
      //若有其中一個除法是可以整除，代表這個檢測值不是質數。
      Double sqrt = new Double(Math.sqrt(Num));
      squareRoot = sqrt.intValue();
      for (int n = 2; n <= squareRoot; n ++) {
        if ((Num % n) == 0) {
          isPrime = false;
          break;
        } 
      }
      
      //輸出檢測結果；若是質數，並會寫到到keeper內的記錄表，以便最後可以統計，
      //有哪些質數、總共幾個、每一個thread分別找到幾個？
      if (isPrime) {
      	System.out.println(this.getName() + ": " + Num + " is prime!");
      	keeper.resultTable[Num] = Integer.parseInt(this.getName().substring(7, this.getName().length()));
      } else {
      	System.out.println(this.getName() + ": " + Num + " is not prime!");
      }
      
      //重新取得檢測值，並將旗標變數預設為真。
      Num = keeper.getNum();
      isPrime = true;
    }
  }
}


/*------------------------------------------------------------------------------
  類別名稱：numKeeper，存放檢測資料與檢測結果，並提供「同步存取」的介面。
  成員說明：targetNum - 代表所要檢測的質數目標範圍（最大值）。
            currentNum - 代表目前已經判斷到哪一個數值，每個thread都會根據它取得
                         檢測資料，所以必須以「同步方式」存取它！！
            resultTable - 使用一個大小為(targetNum+1)的一維矩陣，存放檢測結果。
            numKeeper - 建構函式，對上述的data member進行初始化設定。
            getNum - 同步函式，透過關鍵字synchronized保證，這個函式被呼叫時，
                     不會被中斷會連續執行，以保證每個thread不會拿到相同的檢測
                     數值。
------------------------------------------------------------------------------*/
class numKeeper {
  static int targetNum = 1;    
  static int currentNum = 1;   
  
  //檢測結果儲存規則：
  //(1) resultTable[i] = 0，代表i這個數字「不是質數」。
  //(2) resultTable[i] > 0，a - 代表i這個數字「是質數」
  //                        b - resultTable[i]的值就是檢測出i是質數的thread ID。
  static int resultTable[];  
	
  public numKeeper(int tNum, int cNum) {
    targetNum = tNum;
    currentNum = cNum;
    resultTable = new int[targetNum+1];
    for (int i = 0; i <= targetNum; i ++) {
      resultTable[i] = 0;
    }
  }
  
  /*------------- 同步函式 -------------*/
  synchronized int getNum() {
    int num;
    
    num = currentNum;
    
    if (num <= targetNum) {
      currentNum ++;
      return(num);
    } else {
      return(0);  //傳回0，代表已經到達檢測的最大值！
    }
  }
}


/*------------------------------------------------------------------------------
  類別名稱：findPrime，本程式的主類別。
  成員說明：threadNum - 存放要共同檢測質數的threads個數（作業規定為4）。
            N - 存放質數檢測的範圍（最大值），由使用者自行輸入。
            keeper - 根據N值，並提供同步存取N值範圍內數字的介面。
            main - 整個程式的進入點，負責接收參數（N）、建立和管理threads、
                   並彙整運算結果輸出。
------------------------------------------------------------------------------*/
class findPrime {
  static int threadNum = 4;
  static int N;
  static numKeeper keeper = null;
  
  /*---------- 本程式的進入點！----------*/	
  public static void main(String args[]) {
  	
    //儲存使用者傳入的檢測最大值（N），並保證它是一個大於0的正整數。
    N = 1;
    try { 
      N = Integer.parseInt(args[0]);
      if (N <= 0) throw new NumberFormatException();
      keeper = new numKeeper(N, 2);   //將N值透過建構函式寫入到keeper。
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    }
    
    //使用一個大小為4的陣列存放4個threads。
    //並依序透過建構函式將keeper傳入thread。
    primeChecker checker[] = new primeChecker[4];
    for (int i = 0; i < threadNum; i ++) {
      checker[i] = new primeChecker(keeper); 
    }
    
    //依序啟動每一個thread。
    for (int i = 0; i < threadNum; i ++) {
      checker[i].start();
    }
    
    //main thread依序join每一個thread。
    for (int i = 0; i < threadNum; i ++) {
      try {
        checker[i].join();
      } catch(InterruptedException e) { 
      }
    }
    
    //輸出統計資料到standard output（螢幕）。
    System.out.println("\n[Result]");
    //列出檢測範圍內所有的質數（從2開始）。
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
    //計算檢測範圍內所有的質數個數，並統計每個thread分別找到幾個。
    System.out.print("\nTotal Prime: " + totalPrime + " = ");
    for (int i = 1; i <= threadNum; i ++) {
      System.out.print(threadFind[i] + "(Thread-" + i + ")");
      if (i != threadNum) System.out.print(" + ");
    }
    System.out.println();      	
      
    
  }
}