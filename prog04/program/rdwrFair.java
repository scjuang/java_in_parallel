import Utilities.*;
import Synchronization.*;

class Database extends MyObject {

   private int numReaders = 0;
   private int numWriters = 0;
   private BinarySemaphore mutexA = new BinarySemaphore(1);
   private BinarySemaphore mutexB = new BinarySemaphore(1);
   private BinarySemaphore mutexC = new BinarySemaphore(1);
   private BinarySemaphore okRead = new BinarySemaphore(1);
   private BinarySemaphore okWrite = new BinarySemaphore(1);

   public Database() { super("Database"); }

   public void startRead(int i) {
      P(mutexC);
      P(okRead);
      P(mutexA);
      numReaders++;
      if (numReaders == 1) {
         System.out.println("age=" + age()  + "," + " Reader" + i
            + " waiting to read, numReaders=" + numReaders);
         P(okWrite);
      }
      System.out.println("age=" + age()  + "," + " Reader" + i
         + " has begun reading, numReaders=" + numReaders);
      V(mutexA);
      V(okRead);
      V(mutexC);
   }

   public void endRead(int i) {
      P(mutexA);
      numReaders--;
      System.out.println("age=" + age()  + "," + " Reader" + i
         + " finished reading, numReaders=" + numReaders);
      if (numReaders == 0) V(okWrite);
      V(mutexA);
   }

   public void startWrite(int i) {
      P(mutexB);
      numWriters ++;
      if (numWriters == 1) P(okRead);
      V(mutexB);
      P(okWrite);
      System.out.println("age=" + age()  + "," + " WRITER" + i
         + " has begun Writing");
   }

   public void endWrite(int i) {
      System.out.println("age=" + age()  + "," + " WRITER" + i
         + " has finished Writing");
      V(okWrite);
      P(mutexB);
      numWriters --;
      if (numWriters == 0) V(okRead);
      V(mutexB);
   }
}


