package java;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class LangTestRunner{
  public static void main(String args[]){
    Result result = JUnitCore.runClasses(LangTest.class);
	int errorCtr = 0;
    for (Failure failure : result.getFailures()) {
	  errorCtr++;
	  System.out.println("Error #:"+ errorCtr);
      System.out.println(failure.toString());
	  System.out.println();
    }
	
	if (errorCtr == 0)
	  System.out.println("Congratulations!  There are no errors.");
  }
} 

