package com.divproject.divAutomation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        BankAccount user = new BankAccount("5034592253", 22, "Ashok", "sampe@gmio.com", "7904523217");
//        user.depostfunds();
//        user.setAccountBalance(-5);
//        user.withdrawfunds();
//        user.setAccountBalance(5);
//        user.withdrawfunds();
//        System.out.println(user.getCustomerName());
    	
    	for(int i=0; i<=5; i++) {
    		
    		RecordClass stu = new RecordClass("S92300" + i, 
    				switch(i) {
    				case 1 -> "billa";
    				case 2 -> "bfgjla";
    				case 3 -> "billfhfa";
    				case 4 -> "billdfa";
    				case 5 -> "bidga";
    				default -> "dontknow";
    		}, 
    				"javacourse");
    		System.out.println(stu);
    	}
    	
    }
}
