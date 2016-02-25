/*==============================================================================
  程式名稱：rwmo.java
  程式說明：修正原始的rwmo.java，使它可以在嚴格遵守先到先服務的情形下模擬
            Reader/Writer問題。
  執行環境：Compiler: javac rwmo.java rwdr.java
            Usage: java ReadersWriters -E5 -W3 -e2 -w2 -R5
            上述參數意義依序為numReaders=5, numWriters=3, rNap=2, wNap=2, 
            runTime=5。
  說 明 檔：prog05Report.doc
  撰寫維護：莊勝超（688410037）
==============================================================================*/
import Utilities.*;

class Database extends MyObject {

   private int numReaders = 0;
   private int numWriters = 0;
   private int numWaitingReaders = 0;
   private int numWaitingWriters = 0;
   private boolean okToWrite = true;
   private long startWaitingReadersTime = 0;
   
   private long waitingReadersTime[];
   private long waitingWritersTime[];
   private int countR = 0;
   private int countW = 0;

   public Database(int numR, int numW) { 
      super("rwDB");
      waitingReadersTime = new long[numR+1];
      for (int r = 0; r < numR+1; r ++) waitingReadersTime[r] = 0;
      waitingWritersTime = new long[numW+1];
      for (int w = 0; w < numW+1; w ++) waitingWritersTime[w] = 0;
   }

   public synchronized void startRead(int i) {
      //-----------記錄Reader到達的時間------------//
      long readerArrivalTime = 0;
      readerArrivalTime = age();
      waitingReadersTime[countR] = readerArrivalTime;
      countR ++;
      //-------------------------------------------//
      if (numWaitingWriters > 0 || numWriters > 0) {
         numWaitingReaders++;
         //----1.保證Writer不會餓死-----------------------------//
         //----2.保證Reader不會比較早到達的Writer先進入---------// 
         while ((readerArrivalTime >= startWaitingReadersTime) ||
                (readerArrivalTime >= waitingWritersTime[0]))
            try {wait();} catch (InterruptedException e) {}
         //-----------------------------------------------------//
         numWaitingReaders--;
      }
      numReaders++;
   }
  
   public synchronized void endRead(int i) {
      numReaders--;
      okToWrite = numReaders == 0;
      //---------重新調整佇列(往前移位)----------------//
      for (int r = 1; r < countR; r ++) 
         waitingReadersTime[r-1] = waitingReadersTime[r];
      countR --;
      //-----------------------------------------------//
      if (okToWrite) notifyAll();
   }
  
   public synchronized void startWrite(int i) {
      //-----------紀錄Reader到達的時間------------//
      long writerArrivalTime = 0;
      writerArrivalTime = age();
      waitingWritersTime[countW] = writerArrivalTime;
      countW ++;
      //-------------------------------------------//
      if (numReaders > 0 || numWriters > 0) {
         numWaitingWriters++;
         okToWrite = false;
         //---1. 保證資料庫有Writer或Reader時，不會有Writer進入-------------//
         //---2. 保證連續的Writer間會依照順序進入---------------------------//
         //---3. 保證Writer也不會搶先比早到達的Reader先進入-----------------//
         while ((!okToWrite) && (writerArrivalTime > waitingWritersTime[0]) &&
                (writerArrivalTime > waitingReadersTime[0]))
            try {wait();} catch (InterruptedException e) {}
         //-----------------------------------------------------------------//
         numWaitingWriters--;
      }
      okToWrite = false;
      numWriters++;
   }
  
   public synchronized void endWrite(int i) {
      numWriters--;              
      okToWrite = numWaitingReaders == 0;
      startWaitingReadersTime = age();
      //-------------重新調整佇列(往前移位)-------------//
      for (int w = 1; w < countW; w ++) 
         waitingWritersTime[w-1] = waitingWritersTime[w];
      countW --;
      //------------------------------------------------//
      notifyAll();
   }
}
