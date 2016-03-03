import Utilities.*;
import Synchronization.*;

class Smoker1 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      P(TobaccoPaper);
      System.out.println("Smoker1(Matches): Somking!");
      System.out.flush();
      V(SmokingOK);
    }
  }
}

class Smoker2 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      P(PaperMatches);
      System.out.println("Smoker2(Tobacco): Somking!");
      System.out.flush();
      V(SmokingOK);
    }
  }
}


class Smoker3 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      P(TobaccoMatches);
      System.out.println("Smoker3(Paper): Somking!");
      System.out.flush();
      V(SmokingOK);
    }
  }
}


class Agent extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      switch((int)random(3)) {
      	case 0:
                System.out.println("Agent: Produce Tobacco & Paper");
                System.out.flush();
                V(TobaccoPaper);
                break;
      	case 1:
                System.out.println("Agent: Produce Paper & Matches");
                System.out.flush();
                V(PaperMatches);
                break;
      	case 2:
                System.out.println("Agent: Produce Tobacco & Matches");
                System.out.flush();
                V(TobaccoMatches);
                break;
      }
      P(SmokingOK);
    }
  }
}


class cigaretteSmokers extends MyObject {

  protected static final BinarySemaphore TobaccoPaper = new BinarySemaphore(0);
  protected static final BinarySemaphore PaperMatches = new BinarySemaphore(0);
  protected static final BinarySemaphore TobaccoMatches = new BinarySemaphore(0);
  protected static final BinarySemaphore SmokingOK = new BinarySemaphore(0);
  static boolean running = true;
  static int N = 20;

  public static void main(String[] args) {
    System.out.println();


    Thread Smoker1 = new Thread(new Smoker1());
    Thread Smoker2 = new Thread(new Smoker2());
    Thread Smoker3 = new Thread(new Smoker3());
    Thread Agent = new Thread(new Agent());


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


    Smoker1.start();
    Smoker2.start();
    Smoker3.start();
    Agent.start();


    nap(N);
    running = false;

    System.exit(0);
  }
}
