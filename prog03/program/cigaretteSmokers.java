/*==============================================================================
  �{���W�١GcigaretteSmokers.java
  �{�������G�ϥ�4��Java threads(�@��Agent�B�T��Smoker)����Cigarette Smoker���P�B
            ���D�C
  �������ҡGCompiler: Sun Java SDK 1.4.X
            Usage: java cigaretteSmokers N
            N�N��n�������ɶ����סA���Omilli-second�]�@��^�C
  �� �� �ɡGprog03Report.doc
  ���g���@�G���ӶW�]688410037�^
==============================================================================*/

import Utilities.*;
import Synchronization.*;

/*------------------------------------------------------------------------------
  ���O�W�١GSmoker1�A�~��cigaretteSmokers���O�A�í��s��gRunnable���O��run()����
            �A�������֦�matches�A�ҥH�brun()���������~�ӦӨӪ�semaphore����agent
            ����tobacco�Bpaper�A�åB�bsomking������q��agent�~�򲣥ͭ�ơC
  ���������Grun - thread�Q�Ұʫ�Ұ��檺�{���X�A���ƪ��y�{�p�U�G
                  1)����tobacco�Mpaper
                  2)���
                  3)�q��agent�~��Ͳ����
------------------------------------------------------------------------------*/
class Smoker1 extends cigaretteSmokers implements Runnable {  
	
  public void run () {
    while (running) {
      P(TobaccoPaper);  //����tobacco�Mpaper�C
      System.out.println("Smoker1(Matches): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //�q��agent�~��Ͳ���ơC
    }
  }
}

/*------------------------------------------------------------------------------
  ���O�W�١GSmoker2�A�~��cigaretteSmokers���O�A�í��s��gRunnable���O��run()����
            �A�������֦�tobacco�A�ҥH�brun()���������~�ӦӨӪ�semaphore����agent
            ����paper�Bmatches�A�åB�bsomking������q��agent�~�򲣥ͭ�ơC
  ���������Grun - thread�Q�Ұʫ�Ұ��檺�{���X�A���ƪ��y�{�p�U�G
                  1)����paper�Mmatches
                  2)���
                  3)�q��agent�~��Ͳ����
------------------------------------------------------------------------------*/
class Smoker2 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) { 
      P(PaperMatches);  //����paper�Mmatches�C
      System.out.println("Smoker2(Tobacco): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //�q��agent�~��Ͳ���ơC
    }
  }
}

/*------------------------------------------------------------------------------
  ���O�W�١GSmoker3�A�~��cigaretteSmokers���O�A�í��s��gRunnable���O��run()����
            �A�������֦�paper�A�ҥH�brun()���������~�ӦӨӪ�semaphore����agent
            ����tobacco�Bmatches�A�åB�bsomking������q��agent�~�򲣥ͭ�ơC
  ���������Grun - thread�Q�Ұʫ�Ұ��檺�{���X�A���ƪ��y�{�p�U�G
                  1)����tobacco�Mmatches
                  2)���
                  3)�q��agent�~��Ͳ����
------------------------------------------------------------------------------*/
class Smoker3 extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) { 
      P(TobaccoMatches);  //����tobacco�Mmatches�C
      System.out.println("Smoker3(Paper): Somking!"); 
      System.out.flush();
      V(SmokingOK);  //�q��agent�~��Ͳ���ơC
    }
  }
}

/*------------------------------------------------------------------------------
  ���O�W�١GAgent�A�~��cigaretteSmokers���O�A�í��s��gRunnable���O��run()����
            �A���H�����ͨ�ؤ��P����ơA�çQ���~�ӦӨӪ�semaphore�q���۹�����
            smoker�_�ө�ϡA�÷|����smoker��ϫ�A�~�򲣥ͷs��ơC
  ���������Grun - thread�Q�Ұʫ�Ұ��檺�{���X�A���ƪ��y�{�p�U�G
                  1)�H�����ͨ�ح��
                  2)�q���ʤֳo��ح�ƪ�smoker
                  3)����smoker�}�l������ƨö}�l��ϫ��~��s�@���
------------------------------------------------------------------------------*/
class Agent extends cigaretteSmokers implements Runnable {

  public void run () {
    while (running) {
      switch((int)random(3)) {
      	case 0: //����tobacco�Mpaper�C
                System.out.println("Agent: Produce Tobacco & Paper"); 
                System.out.flush();
                V(TobaccoPaper);  //�q��Smoker1�C           
                break;
      	case 1: //����paper�MMatches�C
                System.out.println("Agent: Produce Paper & Matches"); 
                System.out.flush();
                V(PaperMatches);  //�q��Smoker2�C
                break;
      	case 2: //����tobacco�MMatches�C
                System.out.println("Agent: Produce Tobacco & Matches"); 
                System.out.flush();
                V(TobaccoMatches);  //�q��Smoker3�C
                break;
      }
      P(SmokingOK);  //����smoker�s���C
    }
  }
}


/*------------------------------------------------------------------------------
  ���O�W�١GcigaretteSmokers�A���{�����D���O�A�~��MyObject���O�A����semaphore
            ��threads�P�B����A�åi�H�~�Өϥ�MyObject���ɶ�����禡�C
  ���������GTobaccoPaper - Binary semaphore�A�bagent����tobacco�Mpaper�ɡA
                           �Ψӳq��Smoker1(�֦�matches)�C
            PaperMatches - Binary semaphore�A�bagent����paper�Mmatches�ɡA
                          �Ψӳq��Smoker2(�֦�tobacco)�C
            TobaccoMatches - Binary semaphore�A�bagent����tobacco�Mmatches�ɡA
                            �Ψӳq��Smoker3(�֦�paper)�C
            SmokingOK - Binary semaphore�A��T��smoker�}�l��ϫ�A�Ψӳq��agent
                        �~�򲣥ͭ�ơC
            running - ����4��threads�L�a�j�骺�X�СA���Q�]��false�ɩҦ�threads
                      �N�����j��A�۰ʵ�������C
            N - �s��n����cigarette smokers���ɶ��A�ѨϥΪ̦ۦ��J�A
                ���Omilli-second(�w�]�O20ms)�C
            main - ��ӵ{�����i�J�I�A�t�d�إߨñҰ�4��threads�A�������̰���
                   N��ms���ɶ��ᵲ���C
------------------------------------------------------------------------------*/
class cigaretteSmokers extends MyObject {

  protected static final BinarySemaphore TobaccoPaper = new BinarySemaphore(0);                   
  protected static final BinarySemaphore PaperMatches = new BinarySemaphore(0);
  protected static final BinarySemaphore TobaccoMatches = new BinarySemaphore(0);  
  protected static final BinarySemaphore SmokingOK = new BinarySemaphore(0);
  static boolean running = true;
  static int N = 20;  //�w�]����ɶ�20ms�C

  public static void main(String[] args) {
    System.out.println();
    
    //��l��4��threads�C
    Thread Smoker1 = new Thread(new Smoker1());
    Thread Smoker2 = new Thread(new Smoker2());
    Thread Smoker3 = new Thread(new Smoker3());
    Thread Agent = new Thread(new Agent());
    
    //�ˬd�ϥΪ̬O�_��J����ɶ��A�ο�J�Ȯ榡�O�_���T�C
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
    
    //�Ұ�4��threads�}�l�����C
    Smoker1.start();
    Smoker2.start();
    Smoker3.start();
    Agent.start();
    
    //�ɶ�����禡�G��main thread���槹nap()�禡�A�N�N���Өt�ΰ���ɶ��פ�A
    //�ҥHrunning�X�дN�]��false�A�ϱo4��threads�۰ʵ����C
    nap(N);
    running = false;
    
    System.exit(0);
  }
}