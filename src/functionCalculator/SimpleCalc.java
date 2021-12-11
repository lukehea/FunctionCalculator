package functionCalculator;

public class SimpleCalc {

	public static char[] result(char[] function, int[] definer) {

		String resultString = "";
		char[] result;

		double value1 = 0;
		double value2 = 0;

		boolean swap = false;
		double swapVal = 0;

		for (int j = definer[0]+1;j<definer[1];j++) {
			if (function[j]=='x') {

				break;

			}
			if (function[j]=='.') {
				swap = true;
				swapVal+=j;
			}else if (swap) {
				value1+=((double)(function[j]-'0')/(10*(j-swapVal)));
			}else if (!swap){
				value1*=10;
				value1+=(function[j]-'0');
			}
		}

		swap = false;
		swapVal = 0;

		for (int j = definer[1]+1;j<definer[2];j++) {
			if (function[j]=='x'||function[j]=='(') {

				break;

			}
			if (function[j]=='.') {
				swap = true;
				swapVal+=j;
			}else if (swap) {
				value2+=((double)(function[j]-'0')/(Math.pow(10, (j-swapVal))));
			}else if (!swap){
				value2*=10;
				value2+=(function[j]-'0');
			}
		}

		switch (function[definer[1]]){
		case '+': resultString = Double.toString(value1 + value2);break;
		case '-': resultString = Double.toString(value1 - value2);break;
		case '*': resultString = Double.toString(value1 * value2);break;
		case '/': resultString = Double.toString(value1 / value2);break;
		case '^': resultString = Double.toString(Math.pow(value1, value2));break;
		//case 'r': resultString = Double.toString(Math.sqrt(value2));break;
		case 's': resultString = Double.toString(Math.sin(Math.toRadians(value2)));break;
		case 'c': resultString = Double.toString(Math.cos(Math.toRadians(value2)));break;
		case 't': resultString = Double.toString(Math.tan(Math.toRadians(value2)));break;
		}

		result = resultString.toCharArray();

		return result;

	}
	
}
