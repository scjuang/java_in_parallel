/*==============================================================================
  程式名稱：cigaretteSmokers.java
  程式說明：使用4個Java threads(一個Agent、三個Smoker)模擬Cigarette Smoker的同步
            問題。
  執行環境：Compiler: Sun Java SDK 1.4.X
            Usage: java cigaretteSmokers N
            N代表要模擬的時間長度，單位是milli-second（毫秒）。
  說 明 檔：prog03Report.doc
  撰寫維護：莊勝超（688410037）
==============================================================================*/

import Utilities.*;
import Synchronization.*;

/*------------------------------------------------------------------------------
  類別名稱：Smoker1，繼承cigaretteSmokers類別，並重新改寫Runnable類別的run()介面
            ，它本身擁有matches，所以在run()中必須用繼承而來的semaphore等待agent
            產生tobacco、paper，並且在somking完成後通知agent繼續產生原料。
  成員說明：run - thread被啟動後所執行的程式碼，重複的流程如下：
                  1)等待tobacco和paper
                  2)抽煙
                  3)通知agent繼續生產原料
------------------------------------------------------------------------------*/
class Smoker1 extends cigaretteSmokers implements Runnable {  
	
  public void run () {
    while (running) {
      P(TobaccoPaper);  //等待tobacco和paper。
      System.out.println("Smoker1(Matches): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //通知agent繼續生產原料。
    }
  }
}

/*------------------------------------------------------------------------------
  類別名稱：Smoker2，繼承cigaretteSmokers類別，並重新改寫Runnable類別的run()介面
            ，它本身擁有tobacco，所以在run()中必須用繼承而來的semaphore等待agent
            產生paper、matches，並且在somking完成後通知agent繼續產生原料。
  成員說明：run - thread被啟動後所執行的程式碼，重複的流程如下：
                  1)等待paper和matches
                  2)抽煙
                  3)通知agent繼續生產原料
------------------------------------------------------------------------------*/
class Smoker2 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) { 
      P(PaperMatches);  //等待paper和matches。
      System.out.println("Smoker2(Tobacco): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //通知agent繼續生產原料。
    }
  }
}

/*------------------------------------------------------------------------------
  類別名稱：Smoker3，繼承cigaretteSmokers類別，並重新改寫Runnable類別的run()介面
            ，它本身擁有paper，所以在run()中必須用繼承而來的semaphore等待agent
            產生tobacco、matches，並且在somking完成後通知agent繼續產生原料。
  成員說明：run - thread被啟動後所執行的程式碼，重複的流程如下：
                  1)等待tobacco和matches
                  2)抽煙
                  3)通知agent繼續生產原料
------------------------------------------------------------------------------*/
class Smoker3 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) { 
      P(TobaccoMatches);  //等待tobacco和matches。
      System.out.println("Smoker3(Paper): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //通知agent繼續生產原料。
    }
  }
}

/*------------------------------------------------------------------------------
  類別名稱：Agent，繼承cigaretteSmokers類別，並重新改寫Runnable類別的run()介面
            ，它隨機產生兩種不同的原料，並利用繼承而來的semaphore通知相對應的
            smoker起來抽煙，並會等待smoker抽煙後再繼續產生新原料。
  成員說明：run - thread被啟動後所執行的程式碼，重複的流程如下：
                  1)隨機產生兩種原料
                  2)通知缺少這兩種原料的smoker
                  3)等待smoker開始取走原料並開始抽煙後繼續製作原料
------------------------------------------------------------------------------*/
class Agent extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      switch((int)random(3)) {
      	case 0: //產生tobacco和paper。
                System.out.println("Agent: Produce Tobacco & Paper"); 
                System.out.flush();
                V(TobaccoPaper);  //通知Smoker1。           
                break;
      	case 1: //產生paper和Matches。
                System.out.println("Agent: Produce Paper & Matches"); 
                System.out.flush();
                V(PaperMatches);  //通知Smoker2。
                break;
      	case 2: //產生tobacco和Matches。
                System.out.println("Agent: Produce Tobacco & Matches"); 
                System.out.flush();
                V(TobaccoMatches);  //通知Smoker3。
                break;
      }
      P(SmokingOK);  //等待smoker叫醒。
    }
  }
}


/*------------------------------------------------------------------------------
  類別名稱：cigaretteSmokers，本程式的主類別，繼承MyObject類別，提供semaphore
            讓threads同步執行，並可以繼承使用MyObject的時間控制函式。
  成員說明：TobaccoPaper - Binary semaphore，在agent產生tobacco和paper時，
                           用來通知Smoker1(擁有matches)。
            PaperMatches - Binary semaphore，在agent產生paper和matches時，
                          用來通知Smoker2(擁有tobacco)。
            TobaccoMatches - Binary semaphore，在agent產生tobacco和matches時，
                            用來通知Smoker3(擁有paper)。
            SmokingOK - Binary semaphore，當三個smoker開始抽煙後，用來通知agent
                        繼續產生原料。
            running - 控制4個threads無窮迴圈的旗標，當它被設為false時所有threads
                      將跳離迴圈，自動結束執行。
            N - 存放要模擬cigarette smokers的時間，由使用者自行輸入，
                單位是milli-second(預設是20ms)。
            main - 整個程式的進入點，負責建立並啟動4個threads，並讓它們執行
                   N個ms的時間後結束。
------------------------------------------------------------------------------*/
class cigaretteSmokers extends MyObject {

  protected static final BinarySemaphore TobaccoPaper = new BinarySemaphore(0);                   
  protected static final BinarySemaphore PaperMatches = new BinarySemaphore(0);
  protected static final BinarySemaphore TobaccoMatches = new BinarySemaphore(0);  
  protected static final BinarySemaphore SmokingOK = new BinarySemaphore(0);
  static boolean running = true;
  static int N = 20;  //預設執行時間20ms。

  public static void main(String[] args) {
    System.out.println();
    
    //初始化4個threads。
    Thread Smoker1 = new Thread(new Smoker1());
    Thread Smoker2 = new Thread(new Smoker2());
    Thread Smoker3 = new Thread(new Smoker3());
    Thread Agent = new Thread(new Agent());
    
    //檢查使用者是否輸入執行時間，及輸入值格式是否正確。
    try {
      if (args.length > 0) {
        N = Integer.parseInt(args[0]);
        if (N <= 0) throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      System.out.print("Usage: java cigaretteSmokers N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    }
    
    //啟動4個threads開始模擬。
    Smoker1.start();
    Smoker2.start();
    Smoker3.start();
    Agent.start();
    
    //時間控制函式：當main thread執行完nap()函式，就代表整個系統執行時間終止，
    //所以running旗標就設為false，使得4個threads自動結束。
    nap(N);
    running = false;
    
    System.exit(0);
  }
}