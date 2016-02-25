/*==============================================================================
  �{���W�١GfindPrime.java
  �{�������G�ϥ�4��Java threads�p����N*N�x�}�����k�B��C
  �������ҡGCompiler: Sun Java SDK 1.4.0
            Usage: java findPrime N
            N�O�@�ӥ���ƭȡA�N��ҭn�˴�����ƽd��C
  �� �� �ɡGprog02Report.doc
  ���g���@�G���ӶW�]688410037�^
==============================================================================*/
import java.lang.*;


/*------------------------------------------------------------------------------
  ���O�W�١GprimeChecker�A�~��Thread���O�A�t�d�H�P�B���覡���o�@�Ӿ�ƭȡA���˴�
            ���O�_����ơC
  ���������Gkeeper - �s��4��threads�ҭn�@�P�s�����˴���Ƹ�ơC�]�԰�numKeeper��
                     ���O�����^
            primeChecker - �����O���غc�禡�A�D�n�|�����@��numKeeper���O������A
                           �]���bJava���Y�ѼƬO�u����v�A�w�]�O�Hcall by reference
                           ���覡�ǻ��A�ҥH�A���w����Ʀ���keeper����A�|�O��4��
                           threads�O�ϥΦP�@����ơC
            run - Thread�Q�Ұʫ�Ұ��檺�{���X�A�w��z�Lkeeper���o���Ʀr���R���O
                  �_����ơC
------------------------------------------------------------------------------*/
class primeChecker extends Thread {
  numKeeper keeper;

  //�غc�禡�]Constructor�^�G���F�z�Lsuper()�I�s�������O���غc�禡�~�A�ù�keeper
  //�Hcall by reference���覡�g�J��l�ȡC
  public primeChecker(numKeeper k) {
    super();
    keeper = k;
  }
  
  //�~��Thread���O�ӨӪ�interface�A�í��s��g����ƪ��˴��禡�A
  //�˴����G������X��standard output�]�ù��^�C
  public void run() {
    int Num = 1;  //�s��ҭn�ˬd���ƭȡC
                  //����1�A�N���l�ȡC
                  //����0�A�N��w�g�S�����ո�ơC
                  //�����L�ȡA�N��u�������ո�ơC
                  
    int squareRoot = 1;        //�x�s�˴��Ȫ�����ڡC
    boolean isPrime = true;    //�Y�˴��Ȭ���ơA���u�F�Ϥ��A�����C
    Num = keeper.getNum();     //�H�u�P�B�v�覡���o�˴��ȡC
    
    //�C�i�J�j��@���A�N����o�@�Ӿ�ơA�çP�_�O�_����ơC
    while (Num > 0) {
      //�˴��O�_����ƪ���k�G
      //���˴��ȶ}����ڡ]���]�o��R�^�A���˴��ȡu���H�v�q2��R�������Ҧ��Ʀr�A
      //�Y���䤤�@�Ӱ��k�O�i�H�㰣�A�N��o���˴��Ȥ��O��ơC
      Double sqrt = new Double(Math.sqrt(Num));
      squareRoot = sqrt.intValue();
      for (int n = 2; n <= squareRoot; n ++) {
        if ((Num % n) == 0) {
          isPrime = false;
          break;
        } 
      }
      
      //��X�˴����G�F�Y�O��ơA�÷|�g���keeper�����O����A�H�K�̫�i�H�έp�A
      //�����ǽ�ơB�`�@�X�ӡB�C�@��thread���O���X�ӡH
      if (isPrime) {
      	System.out.println(this.getName() + ": " + Num + " is prime!");
      	keeper.resultTable[Num] = Integer.parseInt(this.getName().substring(7, this.getName().length()));
      } else {
      	System.out.println(this.getName() + ": " + Num + " is not prime!");
      }
      
      //���s���o�˴��ȡA�ñN�X���ܼƹw�]���u�C
      Num = keeper.getNum();
      isPrime = true;
    }
  }
}


/*------------------------------------------------------------------------------
  ���O�W�١GnumKeeper�A�s���˴���ƻP�˴����G�A�ô��ѡu�P�B�s���v�������C
  ���������GtargetNum - �N��ҭn�˴�����ƥؼнd��]�̤j�ȡ^�C
            currentNum - �N��ثe�w�g�P�_����@�ӼƭȡA�C��thread���|�ھڥ����o
                         �˴���ơA�ҥH�����H�u�P�B�覡�v�s�����I�I
            resultTable - �ϥΤ@�Ӥj�p��(targetNum+1)���@���x�}�A�s���˴����G�C
            numKeeper - �غc�禡�A��W�z��data member�i���l�Ƴ]�w�C
            getNum - �P�B�禡�A�z�L����rsynchronized�O�ҡA�o�Ө禡�Q�I�s�ɡA
                     ���|�Q���_�|�s�����A�H�O�ҨC��thread���|����ۦP���˴�
                     �ƭȡC
------------------------------------------------------------------------------*/
class numKeeper {
  static int targetNum = 1;    
  static int currentNum = 1;   
  
  //�˴����G�x�s�W�h�G
  //(1) resultTable[i] = 0�A�N��i�o�ӼƦr�u���O��ơv�C
  //(2) resultTable[i] > 0�Aa - �N��i�o�ӼƦr�u�O��ơv
  //                        b - resultTable[i]���ȴN�O�˴��Xi�O��ƪ�thread ID�C
  static int resultTable[];  
	
  public numKeeper(int tNum, int cNum) {
    targetNum = tNum;
    currentNum = cNum;
    resultTable = new int[targetNum+1];
    for (int i = 0; i <= targetNum; i ++) {
      resultTable[i] = 0;
    }
  }
  
  /*------------- �P�B�禡 -------------*/
  synchronized int getNum() {
    int num;
    
    num = currentNum;
    
    if (num <= targetNum) {
      currentNum ++;
      return(num);
    } else {
      return(0);  //�Ǧ^0�A�N��w�g��F�˴����̤j�ȡI
    }
  }
}


/*------------------------------------------------------------------------------
  ���O�W�١GfindPrime�A���{�����D���O�C
  ���������GthreadNum - �s��n�@�P�˴���ƪ�threads�Ӽơ]�@�~�W�w��4�^�C
            N - �s�����˴����d��]�̤j�ȡ^�A�ѨϥΪ̦ۦ��J�C
            keeper - �ھ�N�ȡA�ô��ѦP�B�s��N�Ƚd�򤺼Ʀr�������C
            main - ��ӵ{�����i�J�I�A�t�d�����Ѽơ]N�^�B�إߩM�޲zthreads�B
                   �÷J��B�⵲�G��X�C
------------------------------------------------------------------------------*/
class findPrime {
  static int threadNum = 4;
  static int N;
  static numKeeper keeper = null;
  
  /*---------- ���{�����i�J�I�I----------*/	
  public static void main(String args[]) {
  	
    //�x�s�ϥΪ̶ǤJ���˴��̤j�ȡ]N�^�A�ëO�ҥ��O�@�Ӥj��0������ơC
    N = 1;
    try { 
      N = Integer.parseInt(args[0]);
      if (N <= 0) throw new NumberFormatException();
      keeper = new numKeeper(N, 2);   //�NN�ȳz�L�غc�禡�g�J��keeper�C
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.print("Usage: java findPrime N");
      System.out.println("  (N is a positive integer)");
      System.exit(1);
    }
    
    //�ϥΤ@�Ӥj�p��4���}�C�s��4��threads�C
    //�ȩ̀ǳz�L�غc�禡�Nkeeper�ǤJthread�C
    primeChecker checker[] = new primeChecker[4];
    for (int i = 0; i < threadNum; i ++) {
      checker[i] = new primeChecker(keeper); 
    }
    
    //�̧ǱҰʨC�@��thread�C
    for (int i = 0; i < threadNum; i ++) {
      checker[i].start();
    }
    
    //main thread�̧�join�C�@��thread�C
    for (int i = 0; i < threadNum; i ++) {
      try {
        checker[i].join();
      } catch(InterruptedException e) { 
      }
    }
    
    //��X�έp��ƨ�standard output�]�ù��^�C
    System.out.println("\n[Result]");
    //�C�X�˴��d�򤺩Ҧ�����ơ]�q2�}�l�^�C
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
    //�p���˴��d�򤺩Ҧ�����ƭӼơA�òέp�C��thread���O���X�ӡC
    System.out.print("\nTotal Prime: " + totalPrime + " = ");
    for (int i = 1; i <= threadNum; i ++) {
      System.out.print(threadFind[i] + "(Thread-" + i + ")");
      if (i != threadNum) System.out.print(" + ");
    }
    System.out.println();      	
      
    
  }
}