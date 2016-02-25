import Utilities.*;
import Synchronization.*;

class DiningServer extends MyObject {

   private static final int
      THINKING = 0, HUNGRY = 1, EATING = 2, STARVE = 3;  
   private int numPhils = 0;
   private int[] state = null;
   private int[] starve = null;  
   private BinarySemaphore[] self = null;
   private BinarySemaphore mutex = null;

   public DiningServer(int numPhils) {
      super("DiningServer for " + numPhils + " philosophers");
      this.numPhils = numPhils;
      state = new int[numPhils];
      starve = new int[numPhils];
      for (int i = 0; i < numPhils; i++) {
      	 state[i] = THINKING;  
      	 starve[i] = 0;  
      }
      self = new BinarySemaphore[numPhils];
      for (int i = 0; i < numPhils; i++) self[i] = new BinarySemaphore(0);
      mutex = new BinarySemaphore(1);
   }

   private final int left(int i) { return (numPhils + i - 1) % numPhils; }

   private final int right(int i) { return (i + 1) % numPhils; }

   public void takeForks(int i) {
      P(mutex);
      state[i] = HUNGRY;
      test(i);
      V(mutex);
      P(self[i]);
   }

   public void putForks(int i) {
      P(mutex);
      state[i] = THINKING;
      test(left(i));
      test(right(i));
      V(mutex);
   }

   private void test(int k) {
      if (state[k] == STARVE) {
      	 if (state[left(k)] == EATING) state[left(k)] = THINKING;
      	 if (state[right(k)] == EATING) state[right(k)] = THINKING;
      	 starve[k] = 0;
      	 state[k] = EATING;
      	 V(self[k]);
      }
      if (state[left(k)] != EATING && state[right(k)] != EATING
            && state[k] == HUNGRY) {
         state[k] = EATING;
         V(self[k]);
      } else if (state[k] == HUNGRY) {
      	 starve[k] ++;
      	 if (starve[k] > 2) {
      	   if (state[left(k)] != STARVE && state[right(k)] != STARVE) {
      	      state[k] = STARVE;
      	   } else {
      	      starve[k] --;
      	   }
      	 }
      }
   }
}