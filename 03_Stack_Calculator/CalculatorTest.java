
import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 2018.4.29. 
 * 각주 처리 완료. 
 * test case 통과 
 */


/**
 * Stack을 이용한 간단한 계산기
 * infix expression을 입력받고 변환한 postfix expression과 계산 결과를 출력한다
 */


public class CalculatorTest
{
	
	// main 함수. 뼈대코드에서 별 다른 수정하지 않음
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR"); // 예외처리는 모두 main에서 담당하게 되는 구조로 이루어져 있다.
				//System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	
	
	/**
	 * String type으로 들어오는 infix expression를 받아 알맞은 순서대로 함수를 호출하고 
	 * 마지막으로 변환한 postfix expression과 계산값을 print해주는 control function이다. 
	 * 
	 * 본 프로그램은 크게 세 단계로 이루어진다 
	 * (1) preProcess 전처리를 진행한다
	 * (2) parsing	  전처리를 마친 infix expression을 postfix expression으로 변환한다
	 * (3) evaluate   postfix expression의 값을 계산한다
	 */
	
	private static void command(String input) throws Exception
	{		
		String tmp = preProcess(input);
		String post = parsing(tmp);
		//System.out.println(post);
		long res = evaluate(post);
		
		System.out.println(post);
		System.out.println(res);
	}
	
	
	/**
	 * 연산자의 우선순위를 반환하는 메소드. parsing 과정에서 call된다.
	 * 우선순위는 일반 수식 연산과 같으며, unary operation '-' 은 미리 '~' 로 변환되었다고 가정한다. 
	 */
	
	private static int op_prec(char op){
		switch(op){
		case '^' :
			return 4;
		case '~' :
			return 3;
		case '*' :
		case '/' :
		case '%' :
			return 2;
		case '+' :
		case '-' :
			return 1;
		default :
			return 0;
		}
	}
	
	
	/**
	 * 주어진 char가 숫자인지 판단하는 메소드.
	 * 숫자일 경우 true를 반환한다. 
	 */
	
	public static boolean isNumber(char a){
		return (a>='0' && a<='9');
	}
	
	
	/**
	 * 주어진 char가 연산자인지 판단하는 메소드. 
	 * 유효한 연산자일 경우 true를 반환한다. 
	 */
	
	public static boolean isOperator(char a){
		return (a=='^' || a=='~' || a=='*' || a=='/' || a=='%' || a=='+' || a=='-');
	}
	
	
	/**
	 * 전처리 함수. unary operation '-'를 '~'로 변환하고 연산자와 숫자 사이의 공백을 삭제한다. 
	 * 단, 공백을 삭제하는 과정에서 잘못된 입력이 정상적인 입력으로 탈바꿈하는 경우를 고려해야 한다.
	 * input으로 들어온 숫자 사이에 공백이 있을 경우 이와 같은 일이 발생한다. 정규표현식을 사용해 숫자 사이에 공백이 있는 경우 먼저 error를 발생시키도록 한다. 
	 * 
	 */
	
	public static String preProcess(String input) throws Exception{
		
		// 숫자 사이에 공백이 있는 경우 error를 발생시킨다. 
		Pattern EXPRESSION_PATTERN = Pattern.compile("(?<=[0-9])\\s+(?=[0-9]+(?:\\s|$))");
		Matcher m = EXPRESSION_PATTERN.matcher(input);
		m.matches();
		
		if(m.find()){
			throw new Exception("wrong input");
		}
		
		input = input.replaceAll("\\s", ""); // 공백을 삭제한다
		
		StringBuffer sb = new StringBuffer(input);
		
		// '-' 이 unary인지 binary인지 판단한 후, unary일 경우 '~' 로 변환한다
		// '-' 이 숫자나 ')' 뒤에 나올 경우 binary, 그 이외의 경우는 unary로 판단할 수 있다.
		if(sb.charAt(0) == '-'){
			sb.setCharAt(0, '~');
		}
		
		for(int i=1;i<sb.length();i++){
			if(sb.charAt(i) == '-' && !(sb.charAt(i-1) == ')' || isNumber(sb.charAt(i-1)))){
				sb.setCharAt(i, '~');
			}
		}	
		return sb.toString();
	}
	
	
	/**
	 * infix expression -> postfix expression 변환 함수. 
	 * 전처리가 끝난 infix expression을 input으로 받는다.
	 * 
	 * input expression의 처음부터 while문을 돌며 character를 하나씩 순차적으로 검사하며, 
	 * 마주치는 character의 종류에 따라 각기 알맞은 기능을 실행한다. 
	 */
	
	
	public static String parsing(String input) throws Exception{
		
		StringBuffer result = new StringBuffer(); // 변환된 최종 postfix expression이 저장될 StringBuffer
		StringBuffer tmp_number = new StringBuffer(); // 중간에 숫자가 저장될 StringBuffer
		Stack<Character> stack = new Stack<Character>();
		
		int i = 0;
		int size = input.length();
		
		while(i < size){
			
			char tmp = input.charAt(i);
			
			
			// (1) 숫자일 경우 연속된 숫자가 끝날 때 까지 입력받아서 한꺼번에 result에 append한다.
			//     그렇지 않으면 postfix expression 내부 숫자와 숫자 사이에 공백을 넣기 모호해진다.
			if(isNumber(tmp)){
				
				while(i< size  && isNumber(input.charAt(i))){
					tmp_number.append(input.charAt(i));
					i++;
				}
				result.append(tmp_number.toString());
				result.append(" ");
				tmp_number = new StringBuffer(); //초기화 
				continue;//while문의 처음으로 돌아간다 
				
				
			// (2) 여는 괄호일 경우 괄호를 스택에 집어넣고 진행한다.  
			}else if(tmp=='('){ 
				stack.push(tmp);
				
			// (3) 닫는 괄호일 경우 여는 괄호가 나올 때까지 스택에서 pop해서 result에 append한다. 
			//     단, 바로 전에 나온 문자가 여는 괄호이거나(=빈 괄호) 바로 전에 나온 문자가 숫자가 아닌 연산자일 경우 ERROR를 발생시킨다.
			}else if(tmp==')'){ 
				
				if(input.charAt(i-1) == '('){
					throw new Exception("wrong input");
				}
				
				if(isOperator(input.charAt(i-1))){
					throw new Exception("wrong input");
				}
				
				char b_tmp = stack.pop();
				while(b_tmp !='('){ 
					result.append(b_tmp);
					result.append(" ");
					b_tmp = stack.pop();
				}
				
				
			/* (4) 연산자일 경우, 자기보다 높은(혹은 같거나 높은) 우선순위의 연산자를 만날 때 까지 stack에서 pop해서 result에 append한다.
				   단, 스택에 남은 원소가 없거나 여는 괄호를 만났을 경우 종료한다. 
				   right-associative, left-associative를 고려해서 종료 시점을 결정해야 한다. */	
			}else if(isOperator(tmp)){
				
				// right-associative
				if(tmp == '^' || tmp == '~'){
					
					//stack이 비어있지 않고, 여는 괄호를 만나지 않았으며, 자신의 우선순위가 높다면
					while( !stack.isEmpty() && stack.peek()!='(' && op_prec(stack.peek()) > op_prec(tmp)){ 
						result.append(stack.pop());
						result.append(" ");
					}
					stack.push(tmp);
					
				// left-associative
				}else{ 
					
					//stack이 비어있지 않고, 여는 괄호를 만나지 않았으며, 자신의 우선순위가 같거나 높다면
					while( !stack.isEmpty() && stack.peek()!='(' && op_prec(stack.peek()) >= op_prec(tmp)){ 
						result.append(stack.pop());
						result.append(" ");
					}
					stack.push(tmp);	
				}
				
			}else{// wrong input
				throw new Exception("wrong input");
				
			}
			i++;
		}
		
		//스택에 남은 연산자를 result에 append한다.
		while(!stack.isEmpty()){
			result.append(stack.pop());
			result.append(" ");
		}
		
		//뒤에 붙은 공백을 삭제한다.
		return result.toString().trim();
	}
	
	
	/**
	 * postfix expression 계산 함수.
	 * 공백을 기준으로 숫자와 연산자가 구분된 postfix expression을 input으로 받는다.
	 * 
	 * input expression의 처음부터 while문을 돌며 character를 하나씩 순차적으로 검사하며, 
	 * 마주치는 character의 종류에 따라 각기 알맞은 기능을 실행한다. 
	 */
	
	
	public static long evaluate(String input) throws Exception{
		
		Stack<Long> stack = new Stack<Long>();
		StringBuffer tmp_number = new StringBuffer();
		
		int sum = 0;

		int i = 0;
		int size = input.length();
		
		while(i < size){
			
			char tmp = input.charAt(i);
			
			// (1) 숫자일 경우 stack에 집어넣는다. 단, 연속적인 숫자일 경우 한꺼번에 처리해야 한다.  
			if(isNumber(tmp)){
				while(i < size  && isNumber(input.charAt(i))){
					tmp_number.append(input.charAt(i));
					i++;
				}
				// 숫자가 아닌 문자를 만나거나, input string의 끝을 만나면 입력받은 일련의 숫자를 long type으로 변환하여 스택에 push한다. 
				stack.push(Long.parseLong(tmp_number.toString()));
				tmp_number = new StringBuffer(); // 초기화 
				continue;// while문의 처음으로 돌아간다 
				
				
			// (2) 연산자일 경우, 스택에 저장되어 있는 숫자들을 하나 혹은 두 개 pop한 뒤 연산자에 알맞은 계산을 처리한 후 다시 스택에 push한다. 	
			}else if(isOperator(tmp)){
				
				if(tmp == '~'){
					long k = stack.pop();
					stack.push(-k);//가장 상위의 스택의 부호 변경 
					
				}else{
					long first = stack.pop();
					long second = stack.pop();
					
					switch(tmp){
					
					case '^':
						// (y<0일 때) 0^y는 연산이 성립하지 않는다. 나머지는 Math.pow의 기능을 따른다. 
						if(second == 0 && first < 0){
							throw new Exception("wrong operation");
						}
						stack.push((long)Math.pow(second, first));
						break;
					case '*': 
						stack.push(second*first);
						break;
					case '/': 
						// 0으로 나눌 경우 연산 과정에서 알아서 error가 발생하므로 따로 처리해주지 않아도 된다. 
						stack.push(second/first);
						break;
					case '%': 
						// 0으로 mod할 경우 연산 과정에서 알아서 error가 발생하므로 따로 처리해주지 않아도 된다. 
						stack.push(second%first);
						break;
					case '+': 
						stack.push(second+first);
						break;
					case '-': 
						stack.push(second-first);
						break;
					default:
						//wrong input
						throw new Exception("wrong input");
					}	
				}
			
			// (3) 공백일 경우 아무것도 하지 않는다  
			}else if(tmp == ' '){
				//do nothing	
				
			}else{ // wrong input
				throw new Exception("wrong input");
			}
			i++;
		}
		
		long result = stack.pop();
		
		if(!stack.isEmpty()){// 스택이 비어있지 않을 경우 중간에 문제가 생긴 것이다. 
			throw new Exception("wrong input");
		}
		
		return result;
	}
	
}




