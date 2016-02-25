/*==============================================================================
  �{���W�١Grwmo.java
  �{�������G�ץ���l��rwmo.java�A�ϥ��i�H�b�Y���u������A�Ȫ����ΤU����
            Reader/Writer���D�C
  �������ҡGCompiler: javac rwmo.java rwdr.java
            Usage: java ReadersWriters -E5 -W3 -e2 -w2 -R5
            �W�z�ѼƷN�q�̧Ǭ�numReaders=5, numWriters=3, rNap=2, wNap=2, 
            runTime=5�C
  �� �� �ɡGprog05Report.doc
  ���g���@�G���ӶW�]688410037�^
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
      //-----------�O��Reader��F���ɶ�------------//
      long readerArrivalTime = 0;
      readerArrivalTime = age();
      waitingReadersTime[countR] = readerArrivalTime;
      countR ++;
      //-------------------------------------------//
      if (numWaitingWriters > 0 || numWriters > 0) {
         numWaitingReaders++;
         //----1.�O��Writer���|�j��-----------------------------//
         //----2.�O��Reader���|�������F��Writer���i�J---------// 
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
      //---------���s�վ��C(���e����)----------------//
      for (int r = 1; r < countR; r ++) 
         waitingReadersTime[r-1] = waitingReadersTime[r];
      countR --;
      //-----------------------------------------------//
      if (okToWrite) notifyAll();
   }
  
   public synchronized void startWrite(int i) {
      //-----------����Reader��F���ɶ�------------//
      long writerArrivalTime = 0;
      writerArrivalTime = age();
      waitingWritersTime[countW] = writerArrivalTime;
      countW ++;
      //-------------------------------------------//
      if (numReaders > 0 || numWriters > 0) {
         numWaitingWriters++;
         okToWrite = false;
         //---1. �O�Ҹ�Ʈw��Writer��Reader�ɡA���|��Writer�i�J-------------//
         //---2. �O�ҳs��Writer���|�̷Ӷ��Ƕi�J---------------------------//
         //---3. �O��Writer�]���|�m���񦭨�F��Reader���i�J-----------------//
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
      //-------------���s�վ��C(���e����)-------------//
      for (int w = 1; w < countW; w ++) 
         waitingWritersTime[w-1] = waitingWritersTime[w];
      countW --;
      //------------------------------------------------//
      notifyAll();
   }
}
