package functionCalculatorOBJ;

import array.Array;
import md_array.MDArray;

public class SimplifyCalc {

	public static char[] simplify (char[] function, int[][] definers) {
		
		char[][] functionB = new char[1][0];
		
		functionB[0] = Array.copyArray(function);
		
		int[][] exponents = MDArray.findElement(functionB, '^');
		
		for (int i = 0;i<definers.length;i++) {
			
			
			
		}
		
		return function;
		
	}
	
}
