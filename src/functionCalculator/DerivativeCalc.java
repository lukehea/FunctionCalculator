package functionCalculator;

import java.util.Scanner;
import array.Array;
import md_array.MDArray;

public class DerivativeCalc {

	public static void main (String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("Input a function in the form f(x)=y");

		System.out.print("f(x)=");

		String function = sc.nextLine();

		char[] brokenFunction = function.toCharArray();
		char[] returnFunction = new char[0];

		char[] allowed = {'x','(',')','+','-','*','/','^','r','s','c','t'};

		returnFunction = derive(brokenFunction, allowed);

		System.out.println("f'(x)=" + new String(returnFunction));

	}

	/** 
	 * sorts elements of an index in order of size, low to high
	 * @param originalArray - the array to be sorted
	 * @return the sorted array
	 */
	public static int [][] sortLowToHigh(int [][] originalArray) {

		//create 2 copies of the original array
		int [][] newArray = new int[2][originalArray[0].length];
		int [][] sortArray = MDArray.copyArray(originalArray);

		//for all indices in the new array, replace them with the smallest element currently in the sort array, then remove that value from the sort array
		for (int j = 0;j<originalArray[0].length;j++) {

			int pos = Array.findMin(sortArray[0]);

			//replace index j of new array with current min from sort array
			newArray[0][j] = sortArray[0][pos];
			newArray[1][j] = sortArray[1][pos];

			//find and delete min from sort array
			sortArray[0] = Array.deleteElement(sortArray[0], pos);
			sortArray[1] = Array.deleteElement(sortArray[1], pos);

		}	

		//return the new array
		return newArray;

	}//end sortLowToHigh

	/** 
	 * sorts elements of an index in order of size, low to high
	 * @param originalArray - the array to be sorted
	 * @return the sorted array
	 */
	public static int [][] sortHighToLow(int [][] originalArray) {

		//create 2 copies of the original array
		int [][] newArray = new int[2][originalArray[0].length];
		int [][] sortArray = MDArray.copyArray(originalArray);

		//for all indices in the new array, replace them with the smallest element currently in the sort array, then remove that value from the sort array
		for (int j = 0;j<originalArray[0].length;j++) {

			int pos = Array.findMax(sortArray[0]);

			//replace index j of new array with current min from sort array
			newArray[0][j] = sortArray[0][pos];
			newArray[1][j] = sortArray[1][pos];

			//find and delete min from sort array
			sortArray[0] = Array.deleteElement(sortArray[0], pos);
			sortArray[1] = Array.deleteElement(sortArray[1], pos);

		}	

		//return the new array
		return newArray;

	}//end sortLowToHigh

	public static int[][] findDefiners(char[] brokenFunction, char[] allowed){

		int[][] definers = new int[0][2];

		for (int j=0;j<brokenFunction.length;j++) {

			int charType = Array.findElement(brokenFunction[j], allowed);
			int[] brackets = new int[2];
			int[] subFunction = new int[3];

			switch (charType) {
			default: 
			case 0: 
			case 1: 
			case 2: break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8: 
			case 9: 
			case 10: 
			case 11: brackets = findBrackets(brokenFunction, j); subFunction[0] = brackets[0]; subFunction[1]=j; subFunction[2]=brackets[1]; definers = MDArray.insertRow(definers, subFunction, definers.length); break;
			}

		}

		return definers;

	}

	public static int[][] sizeFinder(int[][] definers){

		int[][] functionSizes = new int[2][0];

		for(int j = 0;j<definers.length;j++) {

			int[] functionLength = {(definers[j][2]-definers[j][0]), j};

			functionSizes[0] = Array.insertElement(functionSizes[0], functionLength[0], functionSizes[0].length);
			functionSizes[1] = Array.insertElement(functionSizes[1], functionLength[1], functionSizes[1].length);

		}

		return functionSizes;

	}

	public static int[] findBrackets(char[] function, int position) {

		int[] bracketPosition = {-1,function.length};

		int jumpCounter = 0;

		for (int j = position;j>-1;j--) {

			if (function[j]=='(') {

				if (jumpCounter==0) {
					bracketPosition[0] = j;
					break;
				}else {
					jumpCounter--;
				}

			}else if (function[j]==')') {

				jumpCounter++;

			}

		}

		jumpCounter = 0;

		for (int j = position;j<function.length;j++) {

			if (function[j]==')') {

				if (jumpCounter==0) {
					bracketPosition[1] = j;
					break;
				}else {
					jumpCounter--;
				}

			}else if (function[j]=='(') {

				jumpCounter++;

			}

		}

		return bracketPosition;

	}

	private static char[] derive (char[] function, char[] allowed) {

		char[] returnFunction = new char[0];

		int[][] definers = findDefiners(function, allowed);

		int[][] functionSizes = sizeFinder(definers);

		functionSizes = sortHighToLow(functionSizes);

		int length = definers.length;

		int[] subDefiners= new int[3];

		if (definers.length==0) {

			int pos = Array.findElement('x', function);

			if (pos>-1) {
				function = Array.deleteElement(function, pos);
			}
			if (function.length==0) {

				function = Array.insertElement(function, '1', 0);

			}

			return function;

		}else if(definers.length>1) {

			subDefiners = Array.copyArray(definers[functionSizes[1][1]]);

		}else {

			subDefiners[0] = definers[functionSizes[1][0]][1];
			subDefiners[1] = definers[functionSizes[1][0]][1];
			subDefiners[2] = definers[functionSizes[1][0]][1];

			if (function[definers[functionSizes[1][0]][1]]=='^') {

				subDefiners[0] = definers[functionSizes[1][0]][0] + 1;
				subDefiners[2] = definers[functionSizes[1][0]][1] - 1;
				definers = MDArray.insertRow(definers, subDefiners, definers.length);

			}

		}

		char[] derivative = new char[0];

		if (function[definers[functionSizes[1][0]][1]]=='*') {
			derivative = product(function, definers[functionSizes[1][0]], subDefiners, allowed);
			char[][] updated = specialUpdateFunctions(definers, derivative, function, returnFunction, functionSizes);
			function = Array.copyArray(updated[0]);
			returnFunction = Array.copyArray(updated[1]);
		}else if (function[definers[functionSizes[1][0]][1]]=='/') {
			derivative = quotient(function, definers[functionSizes[1][0]], subDefiners, allowed);
			char[][] updated = specialUpdateFunctions(definers, derivative, function, returnFunction, functionSizes);
			function = Array.copyArray(updated[0]);
			returnFunction = Array.copyArray(updated[1]);
		}else {
			derivative = stndDerive(function, definers[functionSizes[1][0]], subDefiners);
			char[][] updated = updateFunctions(definers, subDefiners, derivative, function, returnFunction, functionSizes);
			function = Array.copyArray(updated[0]);
			returnFunction = Array.copyArray(updated[1]);
		}

		definers = findDefiners(function, allowed);

		length = definers.length;

		if (length==0&&function.length>1) {
			int[][] specialDefiner = {{0,0,function.length}};
			int[][] specialSize = {{0,0},{0,0}};

			int r = 0;

			derivative = new char[0];

			while (function[r]!='x') {

				derivative = Array.insertElement(derivative, function[r], derivative.length);
				r++;

			}

			returnFunction = Array.insertElement(returnFunction, '*', returnFunction.length);
			char[][] updated = specialUpdateFunctions(specialDefiner, derivative, function, returnFunction, specialSize);
			function = Array.copyArray(updated[0]);
			returnFunction = Array.copyArray(updated[1]);

		}

		for (int j = 0;j<length;j++) {

			returnFunction = Array.insertElement(returnFunction, ')', returnFunction.length);
			returnFunction = Array.insertElement(returnFunction, '*', returnFunction.length);
			returnFunction = Array.insertElement(returnFunction, '(', 0);

			definers = findDefiners(function, allowed);

			functionSizes = sizeFinder(definers);

			functionSizes = sortHighToLow(functionSizes);

			if(definers.length>1) {

				subDefiners = Array.copyArray(definers[functionSizes[1][1]]);

			}else {

				subDefiners[0] = definers[functionSizes[1][0]][1];
				subDefiners[1] = definers[functionSizes[1][0]][1];
				subDefiners[2] = definers[functionSizes[1][0]][1];

			}

			if (function[definers[functionSizes[1][0]][1]]=='*') {
				derivative = product(function, definers[functionSizes[1][0]], subDefiners, allowed);
				char[][] updated = specialUpdateFunctions(definers, derivative, function, returnFunction, functionSizes);
				function = Array.copyArray(updated[0]);
				returnFunction = Array.copyArray(updated[1]);
			}else if (function[definers[functionSizes[1][0]][1]]=='/') {
				derivative = quotient(function, definers[functionSizes[1][0]], subDefiners, allowed);
				char[][] updated = specialUpdateFunctions(definers, derivative, function, returnFunction, functionSizes);
				function = Array.copyArray(updated[0]);
				returnFunction = Array.copyArray(updated[1]);
			}else {
				derivative = stndDerive(function, definers[functionSizes[1][0]], subDefiners);
				char[][] updated = updateFunctions(definers, subDefiners, derivative, function, returnFunction, functionSizes);
				function = Array.copyArray(updated[0]);
				returnFunction = Array.copyArray(updated[1]);
			}

		}

		if (returnFunction[returnFunction.length-1]=='*') {
			returnFunction = Array.deleteElement(returnFunction, returnFunction.length-1);
		}

		return returnFunction;

	}

	public static char[][] updateFunctions(int[][] definers, int[] subDefiners, char[] derivative, char[] function, char[] returnFunction, int[][] functionSizes) {

		char[][] returnFunctions = new char[2][];

		int start = Math.max(definers[functionSizes[1][0]][0], 0);

		int count = 0;

		for (int i = start;i<=subDefiners[0];i++) {

			if (definers[functionSizes[1][0]][0]>=0) {
				function = Array.deleteElement(function, definers[functionSizes[1][0]][0]);
			}else if (i<(definers[functionSizes[1][0]][2]-1)){
				function = Array.deleteElement(function, (definers[functionSizes[1][0]][0]+1));
			}

			count++;

		}

		subDefiners[2]-=count;
		definers[functionSizes[1][0]][2]-=count;

		for (int i = subDefiners[2]-1;i<definers[functionSizes[1][0]][2];i++) {

			if (definers[functionSizes[1][0]][0]>=0) {
				function = Array.deleteElement(function, subDefiners[2]);
			}else if (i<(definers[functionSizes[1][0]][2]-1)&&function.length>0){
				function = Array.deleteElement(function, Math.max(subDefiners[2], 0));
			}

		}

		for (int i = 0;i<derivative.length;i++) {

			if (definers[functionSizes[1][0]][0]>=0) {
				returnFunction = Array.insertElement(returnFunction, derivative[i], returnFunction.length);
			}else {
				returnFunction = Array.insertElement(returnFunction, derivative[i], returnFunction.length);
			}

		}

		returnFunctions[0] = function;
		returnFunctions[1] = returnFunction;

		return returnFunctions;

	}

	public static char[][] specialUpdateFunctions(int[][] definers, char[] derivative, char[] function, char[] returnFunction, int[][] functionSizes) {

		char[][] returnFunctions = new char[2][];

		int start = 0;

		if (functionSizes.length>1) {
			start = Math.max(definers[functionSizes[1][0]][0], 0);
		}

		for (int i = start;i<=definers[functionSizes[1][0]][2];i++) {

			if (definers[functionSizes[1][0]][0]>0) {
				function = Array.deleteElement(function, definers[functionSizes[1][0]][0]);
			}else if (i<(definers[functionSizes[1][0]][2]-1)){
				function = Array.deleteElement(function, (definers[functionSizes[1][0]][0]+1));
			}

		}

		for (int i = 0;i<derivative.length;i++) {

			if (definers[functionSizes[1][0]][0]>=0) {
				returnFunction = Array.insertElement(returnFunction, derivative[i], returnFunction.length);
			}else {
				returnFunction = Array.insertElement(returnFunction, derivative[i], returnFunction.length);
			}

		}

		returnFunctions[0] = function;
		returnFunctions[1] = returnFunction;

		return returnFunctions;

	}

	public static char[] stndDerive(char[] function, int[] definer, int[] subDefiner) {

		String resultString = "";
		char[] result;

		double value1 = 0;
		double value2 = 0;

		boolean swap = false;
		double swapVal = 0;

		boolean f1N = false;
		boolean f2N = false;

		for (int j = definer[0]+1;j<subDefiner[0];j++) {

			if (function[j]=='x') {

				if (j == definer[0]+1) {
					value1 = 1;
				}

				break;

			}
			if (function[j]=='.') {
				swap = true;
				swapVal+=j;
			}else if (function[j]=='_') {

				value1 = 0;

			}else if (swap) {
				value1+=((double)(function[j]-'0')/(10*(j-swapVal)));
			}else if (!swap){
				value1*=10;
				value1+=(function[j]-'0');
			}
		}

		swap = false;
		swapVal = 0;

		int start = Math.max(subDefiner[2]+1, definer[1]+1);

		for (int j = start;j<definer[2];j++) {

			if (function[j]=='x') {

				if (j == definer[1]+1) {
					value2 = 1;
				}
				break;

			}
			if (function[j]=='.') {
				swap = true;
				swapVal+=j;
			}else if (function[j]=='_') {

				f2N = true;

			}else if (swap) {
				value2+=((double)(function[j]-'0')/(Math.pow(10, (j-swapVal))));
			}else if (!swap){
				value2*=10;
				value2+=(function[j]-'0');
			}
		}

		if (f1N) {

			value1*=-1;

		}
		if (f2N) {

			value2*=-1;

		}

		if (subDefiner[0]==definer[0]+1) {

			value1 = 1;

		}

		if (subDefiner[2]==definer[2]) {

			value2 = 1;

		}

		char[] x = new char[0];

		if (subDefiner[0]!=subDefiner[1]) {
			for (int j = subDefiner[0];j<=subDefiner[2];j++) {

				x = Array.insertElement(x, function[j], x.length);

			}
		}else {
			x = new char[0];
			x=Array.insertElement(x, 'x', 0);
		}
		switch (function[definer[1]]){
		case '+': 
			if (function[definer[1]-1]!='x') {
				resultString = Double.toString(value2);
			}else if (function[definer[2]-1]!='x') {
				resultString = Double.toString(value1);
			}else if (function[definer[1]-1]!='x'&&function[definer[2]-1]!='x') {
				resultString = "";
			}else {
				resultString = Double.toString(value1 + value2);
			}break;
		case '-': 
			if (definer[1]-1>=0&&function[definer[1]-1]!='x') {
				resultString = Double.toString(-value2);
			}else if (function[definer[2]-1]!='x') {
				resultString = Double.toString(value1);
			}else if (definer[1]-1>=0&&function[definer[1]-1]!='x'&&function[definer[2]-1]!='x') {
				resultString = "";
			}else {
				resultString = Double.toString(value1 - value2);
			}break;
		case '^': 
			switch ((int) value2) {
			case 1: resultString = Double.toString(value1);break;
			case 2: resultString = value1*2 + "*" + new String (x);break;
			default: resultString = Double.toString(value2*value1) + "*(" + new String (x) + "^" + Double.toString(value2-1) + ")";break;
			}break;
			//case 'r': resultString = Double.toString(Math.sqrt(value2));break;
		case 's': resultString = "(c" + new String (x) + ")";break;
		case 'c': resultString = "(-(s" + new String (x) + "))";break;
		case 't': resultString = "(1/((c" + new String(x) + ")^2)";break;
		}

		result = resultString.toCharArray();

		return result;

	}

	public static char[] quotient(char[] function, int[] definer, int[] subDefiner, char[] allowed) {

		String resultString = "";
		char[] result;

		char[] fx = new char[0];
		char[] gx = new char[0];

		for (int j = definer[0]+2;j<definer[1]-1;j++) {

			fx = Array.insertElement(fx, function[j], fx.length);

		}

		for (int j = definer[1]+2;j<definer[2]-1;j++) {

			gx = Array.insertElement(gx, function[j], gx.length);

		}

		char[] fpx = derive(fx, allowed);
		char[] gpx = derive(gx, allowed);

		resultString = "(((" + new String (gx) + ")*(" + new String (fpx) + "))-((" + new String (fx) + ")*(" + new String (gpx) + ")))/((" + new String (gx) + ")^2)";

		result = resultString.toCharArray();

		return result;

	}

	public static char[] product(char[] function, int[] definer, int[] subDefiner, char[] allowed) {

		String resultString = "";
		char[] result;

		char[] fx = new char[0];
		char[] gx = new char[0];

		for (int j = definer[0]+2;j<definer[1]-1;j++) {

			fx = Array.insertElement(fx, function[j], fx.length);

		}

		for (int j = definer[1]+2;j<definer[2]-1;j++) {

			gx = Array.insertElement(gx, function[j], gx.length);

		}

		char[] fpx = derive(fx, allowed);
		char[] gpx = derive(gx, allowed);

		resultString = "((" + new String (gx) + ")*(" + new String (fpx) + "))+((" + new String (fx) + ")*(" + new String (gpx) + "))";

		result = resultString.toCharArray();

		return result;

	}

}
