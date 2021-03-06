import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 2018.3.19. 수정 
 * 각주 처리 미완료.
 * 검토 미완료. 
 * test case  "testcase_Ultimate.txt" 통과, "HW3_TESTSET" float 예제 제외 통과 
 * unary, exponential 관련 수정한 demo version
 * 수정 과정에서 문제가 많이 생긴 듯... 
 * 
 */


public class CalculatorTest
{
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
				System.out.println("ERROR"); // 예외처리는 모두 main에게 넘김
				//System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	
	
	private static void command(String input) throws Exception
	{		
		String tmp = preProcess(input);
		String post = parsing(tmp);
		//System.out.println(post);
		long res = evaluate(post);
		
		System.out.println(post);
		System.out.println(res);
		
	}
	
	
	
	private static int op_prec(char op){
		// 연산자의 우선순위를 반환하는 메소드. unary - 은 미리 ~ 로 변환되었다고 가정. 
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
	
	
	
	public static boolean isNumber(char a){
		// 숫자일 경우 true 반환
		return (a>='0' && a<='9');
	}
	
	public static boolean isOperator(char a){
		// 연산자일 경우 true 반환
		return (a=='^' || a=='~' || a=='*' || a=='/' || a=='%' || a=='+' || a=='-');
	}
	
	
	
	public static String preProcess(String input) throws Exception{
		 
		// 전처리 메소드. unary '-'를 '~'로 변환하고 공백을 삭제한다. 
		// 단, 공백을 삭제하는 과정 속에서 잘못된 입력이 정상적인 입력으로 탈바꿈하는 경우를 고려해야 한다. 
		// 숫자 사이에 공백이 있을 경우 이와 같은 일이 발생한다. 정규표현을 사용해 숫자 사이에 공백이 있는 경우 error을 발생시키도록 한다. 
		
		Pattern EXPRESSION_PATTERN = Pattern.compile("(?<=[0-9])\\s+(?=[0-9]+(?:\\s|$))");
		Matcher m = EXPRESSION_PATTERN.matcher(input);
		m.matches();
		
		if(m.find()){
			throw new Exception("wrong input");
		}
		
		input = input.replaceAll("\\s", ""); // 공백 삭제
		
		StringBuffer sb = new StringBuffer(input);
		
		// '-' 이 unary인지 binary인지 판단한 후, unary일 경우 '~' 로 변환한다
		// '-' 이 숫자나 ')' 뒤에 나올 경우 binary, 그 이외의 경우는 unary로 판단 가능.
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
	
	
	
	
	public static String parsing(String input) throws Exception{
		// infix -> postfix
		
		/* 
		   예외 처리 전혀 되어있지 않음... !!
		   다양한 경우의 예외가 존재할 것. 대표적인 예시는 책에 나와있음. 
		   한꺼번에 Exception으로 받아서 ERROR 로 통일하는 게 일단 편할 듯?
		
		*/
		
		StringBuffer result = new StringBuffer();
		StringBuffer tmp_number = new StringBuffer();
		Stack<Character> stack = new Stack<Character>();
		
		int i = 0;
		int size = input.length();
		
		while(i < size){
			
			char tmp = input.charAt(i);
			
			if(isNumber(tmp)){
				// (1) 숫자일 경우 숫자가 끝날 때 까지 입력받아서 한꺼번에 더한다 
				
				while(i< size  && isNumber(input.charAt(i))){
					tmp_number.append(input.charAt(i));
					i++;
				}
				result.append(tmp_number.toString());
				result.append(" ");
				tmp_number = new StringBuffer(); //초기화 
				continue;//다시 처음으로 
				
			}else if(tmp=='('){ // (2) 여는 괄호일 경우 괄호를 스택에 집어넣고 진행  
				stack.push(tmp);
				
			}else if(tmp==')'){ // (3) 닫는 괄호일 경우 여는 괄호가 나올 때까지 pop (단, 괄호 안이 비어있을 경우, 즉 바로 전 문자가 '(' 일 경우 ERROR 처리)
				
				if(input.charAt(i-1) == '('){
					throw new Exception("wrong input");
				}
				
				char b_tmp = stack.pop();
				while(b_tmp !='('){ 
					result.append(b_tmp);
					result.append(" ");
					b_tmp = stack.pop();
				}
				
			}else if(isOperator(tmp)){// 연산자일 경우 
				
				if (tmp == '^'){	
					
					// right-associative
					while( !stack.isEmpty() && stack.peek()!='(' && op_prec(stack.peek()) > op_prec(tmp)){ //stack이 비어있지 않고, 괄호를 만나지 않았으며, 우선순위가 높다면 (높은 우선순위를 만나기 전까지)
						result.append(stack.pop());
						result.append(" ");
					}
					stack.push(tmp);
					
				}else if (tmp == '~'){
					
					// tricky case : 3^-5 와 같은 경우는 따로 처리해줘야 한다. (unary -가 먼저 처리되어야 함..)
					
					if(!stack.isEmpty() && stack.peek()=='^'){
						char ex = stack.pop();
						stack.push(ex);
						stack.push(tmp);
					}else{
						// right-associative
						while( !stack.isEmpty() && stack.peek()!='(' && op_prec(stack.peek()) > op_prec(tmp)){ //stack이 비어있지 않고, 괄호를 만나지 않았으며, 우선순위가 높다면 (높은 우선순위를 만나기 전까지)
							result.append(stack.pop());
							result.append(" ");
							stack.push(tmp);
						}
					}
					
					
				}else{ 
					// left-associative
					while( !stack.isEmpty() && stack.peek()!='(' && op_prec(stack.peek()) >= op_prec(tmp)){ //stack이 비어있지 않고, 괄호를 만나지 않았으며, 우선순위가 같거나 높다면 (높은 우선순위를 만나기 전까지)
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
		
		
		while(!stack.isEmpty()){
			result.append(stack.pop());
			result.append(" ");
		}
		
		return result.toString().trim();//뒤에 붙은 공백 삭제 
	}
	
	
	
	
	public static long evaluate(String input) throws Exception{
		// postfix 계산 
		// 웬만한 오류는 postfix 계산 과정에서 발생하지 않을까? 
		
		Stack<Long> stack = new Stack<Long>();
		StringBuffer tmp_number = new StringBuffer();
		
		int sum = 0;

		int i = 0;
		int size = input.length();
		
		while(i < size){
			
			char tmp = input.charAt(i);
			
			if(isNumber(tmp)){//숫자일 경우
				while(i < size  && isNumber(input.charAt(i))){
					tmp_number.append(input.charAt(i));
					i++;
				}
				//숫자가 아닌 문자를 만나면 그동안 입력받은 숫자를 스택에 push
				stack.push(Long.parseLong(tmp_number.toString()));
				tmp_number = new StringBuffer(); //초기화 
				continue;//다시 처음으로 
				
			}else if(isOperator(tmp)){//operator일 경우. unary('~')일 경우 따로 처리해야 한다. 
				if(tmp == '~'){
					long k = stack.pop();
					stack.push(-k);//가장 상위의 스택의 부호 변경 
					
				}else{
					long first = stack.pop();
					long second = stack.pop();
					
					switch(tmp){
					case '^':
						//0^(음수)의 경우 Error 표시해야 하나? 
						if(second == 0 && first < 0){
							throw new Exception("divide by zero");
						}
						stack.push((long)Math.pow(second, first));
						break;
					case '*': 
						stack.push(second*first);
						break;
					case '/': 
						stack.push(second/first);//divide by zero?
						break;
					case '%': 
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
			}else if(tmp == ' '){
				//공백 : do nothing	
			}else{
				
				throw new Exception("wrong input");
			}
			i++;
		}
		
		long result = stack.pop();
		
		if(!stack.isEmpty()){// 비어있지 않을 경우 문제 있음... 
			throw new Exception("wrong input");
		}
		
		return result;
	}
	
}
