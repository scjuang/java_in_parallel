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

      long readerArrivalTime = 0;
      readerArrivalTime = age();
      waitingReadersTime[countR] = readerArrivalTime;
      countR ++;

      if (numWaitingWriters > 0 || numWriters > 0) {
         numWaitingReaders++;

         while ((readerArrivalTime >= startWaitingReadersTime) ||
                (readerArrivalTime >= waitingWritersTime[0]))
            try {wait();} catch (InterruptedException e) {}

         numWaitingReaders--;
      }
      numReaders++;
   }

   public synchronized void endRead(int i) {
      numReaders--;
      okToWrite = numReaders == 0;

      for (int r = 1; r < countR; r ++)
         waitingReadersTime[r-1] = waitingReadersTime[r];
      countR --;

      if (okToWrite) notifyAll();
   }

   public synchronized void startWrite(int i) {

      long writerArrivalTime = 0;
      writerArrivalTime = age();
      waitingWritersTime[countW] = writerArrivalTime;
      countW ++;

      if (numReaders > 0 || numWriters > 0) {
         numWaitingWriters++;
         okToWrite = false;

         while ((!okToWrite) && (writerArrivalTime > waitingWritersTime[0]) &&
                (writerArrivalTime > waitingReadersTime[0]))
            try {wait();} catch (InterruptedException e) {}

         numWaitingWriters--;
      }
      okToWrite = false;
      numWriters++;
   }

   public synchronized void endWrite(int i) {
      numWriters--;
      okToWrite = numWaitingReaders == 0;
      startWaitingReadersTime = age();

      for (int w = 1; w < countW; w ++)
         waitingWritersTime[w-1] = waitingWritersTime[w];
      countW --;

      notifyAll();
   }
}
