import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 2018.3.19. 수정 
 * 각주 처리 완료. 
 * 검토 완료. 
 * test case  "big.1.in.txt", "big.2.in.txt", "datain", "input.txt" 통과
 * 
 */
 
// 큰 정수에 대해서도 제한없이 연산을 할 수 있도록 임의의 크기를 갖는 수를 int 배열로 받아 +, -, * 연산을 수행하는 프로그램
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
 
    // 정규식을 사용해서 공백을 제거하고 부호(optional), 연산자, 숫자를 파싱한다  
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\s*([\\+\\-\\*]?)\\s*([0-9]*)\\s*([\\+\\-\\*]?)\\s*([\\+\\-\\*]?)\\s*([0-9]*)\\s*");
 
    public int[] number; //int 배열로 저장
    public int sign = 1; //양수일 경우 1, 음수일 경우 0
    
    
 
    public BigInteger(int[] num1)
    {
    	// 배열을 복사 (shallow copy)
    	
    	this.number = new int[num1.length];
    	System.arraycopy(num1, 0, this.number, 0, num1.length);
    }
    
    
 
    public BigInteger(String s)
    {
    	// String을 int 배열로 변환한다. 계산을 용이하게 하기 위해 순서는 낮은 자릿수부터 역순으로 저장한다. 
    	// 단, 00000001 와 같이 의미 없는 0은 삭제한다. 
    	// ex. "123" -> [3,2,1]
    	
    	s = s.replaceFirst("0*", "");
    	
    	if(s.equals("")){ // ex. 0000000일 경우 
    		s = "0";
    	}
    	
    	this.number = new int[s.length()];
    	for(int i=0;i<s.length();i++){
    		this.number[i] = s.charAt(s.length()-i-1)-'0';
    	}
    }    
    
    
 
    public BigInteger add(BigInteger small)
    {
    	//Precondition : this와 small은 모두 양의 정수 && this의 정수값 >= small의 정수값
    	//Complexity : O(n)
    	//배열을 순회하며 한 자릿수씩 덧셈과 필요할 경우 자리올림을 실행한다
    	
    	int[] result_arr = new int[110];
    	
    	int size_big = this.number.length;
    	int size_small = small.number.length; // size_big >= size_small (precondition)
    	
    	int carry = 0; //자리올림 표시 
    	int i = 0;
    	 
    	for(i=0; i<size_small ;i++){
    		result_arr[i] = (this.number[i] + small.number[i] + carry) % 10;
    		carry = (this.number[i] + small.number[i] + carry) / 10;
    	}
    	
    	// 나머지 처리 
    	for(; i<size_big ; i++){
    		result_arr[i] = (this.number[i] + carry) % 10;
    		carry = (this.number[i] + carry) / 10;
    	}
    	result_arr[i] = carry;
    	return new BigInteger(result_arr);
    }
    
    
    
    
 
    public BigInteger subtract(BigInteger small)
    {
    	//Precondition : this와 small은 모두 양의 정수 && this의 정수값 >= small의 정수값 
    	//Complexity : O(n)
    	//배열을 순회하며 한 자릿수씩 뺄셈과 필요할 경우 자리내림을 실행한다
    	
    	int[] result_arr = new int[110];
    	
    	int size_big = this.number.length;
    	int size_small = small.number.length;
    	
    	int carry = 0; //자리내림 표시 
    	int i = 0;
    	
    	for(i=0; i<size_small ;i++){
    		
    		if(this.number[i] < (small.number[i]+carry)){
    			result_arr[i] = (10 + this.number[i] - small.number[i] - carry);
    			carry = 1;
    		}else{
    			result_arr[i] = (this.number[i] - small.number[i] - carry);
    			carry = 0;
    		}
    	}
    	
    	// 나머지 처리 
    	for(; i<size_big ; i++){
    		if(this.number[i] < carry){
    			result_arr[i] = (10 + this.number[i] - carry);
    			carry = 1;
    		}else{
    			result_arr[i] = (this.number[i] - carry);
    			carry = 0;
    		}
    	}
    	result_arr[i] -= carry;
    	return new BigInteger(result_arr);
    }
    
    
    
    
    
 
    public BigInteger multiply(BigInteger small)
    {
    	//Precondition : this와 small은 모두 양의 정수
    	//Complexity : O(n^2)
    	/*
    	 
    	  간단하게 n^2 이중루프로 구현함. input 자체가 커봤자 100이기 때문에 overhead를 고려해 카라추바 divide&conquer 사용하지 않음. 
    	  덧셈으로 구현하는 방법도 존재함. 하지만 최악의 경우 배열 shifting을 무시한다고 해도 덧셈 1000번으로 더 비효율적(덧셈 자체가 O(n)이므로..)
    	
    	*/
    	
    	int[] result_arr = new int[210];
    	
    	int size_big = this.number.length;
    	int size_small = small.number.length;
    	
    	int i = 0, j = 0;
 
    	for(i=0 ; i<size_small ; i++){
    		int carry = 0;//자리올림
    		for(j=0 ; j<size_big ; j++){
    			int tmp = result_arr[i+j] + (this.number[j] * small.number[i]) + carry;
    			result_arr[i+j] = tmp%10;
    			carry = tmp/10;
    		}
    		result_arr[i+j] += carry;
    	}
    	
    	return new BigInteger(result_arr);
    }
    
    
    
 
    @Override
    public String toString()
    {
    	// int 배열을 String으로 변환한다 
    	// ex. [3,2,1] -> "123"
    	
    	StringBuffer sb = new StringBuffer();
    	
    	if(this.sign == 0) // 부호 판별. + 일 경우 생략  
    		sb.append('-');
    	
    	int i = this.number.length-1; // last index
    	
    	// 배열의 끝 원소에 필요 없는 0이 있으면 지워줌
    	// 단, 배열의 모든 원소가 0일 가능성(정수 0)을 고려해야 함
    	while(this.number[i] == 0 ){
    		i--;
    		if(i==-1){// 정수 0 
        		return "0";
    		}
    	}
    	
    	for(; i>=0 ; i--){
    		sb.append(this.number[i]);
    	}
    	return sb.toString();
    }
    
    
    

    public static int my_compareTo(String a, String b)
    {
    	// String 형태로 입력된 두 양수 a, b의 크기를 비교하는 메소드. 
    	// 리턴값 : a>b -> 1 | a<b -> -1 | a==b -> 0 
    	
    	if(a.length() > b.length()){
    		return 1;
    	}else if(a.length() < b.length()){
    		return -1;
    	}else{//same size
    		return a.compareTo(b);
    	}
    }
    
    
    
 
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
    	
    	/*  
    	    계산의 편의를 위해 본 프로그램은 evaluate 함수 내에서 매개변수로 넘어갈 두 숫자의 부호 처리를 전담하는 방식을 택함.  
    	    즉, add/subtract/multiply 함수에 넘어가는 두 매개변수는 (1) 모두 양수이고, (2) 첫 번째 매개변수의 값이 두 번째 
    	    매개변수의 값보다 크거나 같다는 것이 보장됨. 이로 인해 연산 메소드 내부 계산이 간편해짐. 
    	
    	    ex1. 입력값으로 (3) - (5) 이 주어졌을 경우, evaluate 함수는 (5).subtract(3) 을 호출하고 - 부호를 첨가함.
    	    ex2. 입력값으로 (-3) + (-4) 가 주어졌을 경우, evaluate 함수는 (4).add(3) 을 호출하고 - 부호를 첨가함.     
    	*/
    	
    	//정규식을 사용해서 공백을 제거하고 부호(optional), 연산자, 숫자를 파싱한다  
    	//잘못된 입력이 없다고 가정한다면 sign_1, sign_2 만 optional. 나머지는 empty string이 아닌 임의의 값이 보장됨. 
    	Matcher m = EXPRESSION_PATTERN.matcher(input);
    	m.matches();
    	
    	String sign_1 = m.group(1);
    	String num_1 = m.group(2);
    	char operator = m.group(3).charAt(0);//switch 문 사용을 위해 char로 변환 
    	String sign_2 = m.group(4);
    	String num_2 = m.group(5);
    	
    	
    	//sign_1 과 sign_2가 empty string일 경우 계산의 편의를 위해 부호를 "+"으로 explicit하게 설정.
    	if(sign_1.equals("")){
    		sign_1 = "+";
    	}
    	if(sign_2.equals("")){
    		sign_2 = "+";
    	}
    	
    	//이 과정에서 0000001 +00000 와 같은 이상한 입력이 깔끔하게 정리됨. 
    	BigInteger num1 = new BigInteger(num_1);
    	BigInteger num2 = new BigInteger(num_2);
    	
    	//정리된 값으로 reset
    	num_1 = num1.toString();
    	num_2 = num2.toString();
    	
    	BigInteger result = null;
    	
    	switch(operator){
    	
    	//precondition에 주어진 조건대로 num_1과 num_2를 비교하여 적절한 매개변수 위치에 넘겨주고 부호를 첨가함. 이하 동일.
    	case '+':
    		if(sign_1.equals("+") && sign_2.equals("+")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.add(num2) : num2.add(num1) ;
    			result.sign = 1;		
    			
    		}else if(sign_1.equals("+") && sign_2.equals("-")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.subtract(num2) : num2.subtract(num1) ;
    			result.sign = (my_compareTo(num_1, num_2) > 0) ? 1 : 0 ;
    			
    		}else if(sign_1.equals("-") && sign_2.equals("+")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.subtract(num2) : num2.subtract(num1) ;
    			result.sign = (my_compareTo(num_1, num_2) > 0) ? 0 : 1 ;
    			
    		}else if(sign_1.equals("-") && sign_2.equals("-")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.add(num2) : num2.add(num1) ;
    			result.sign = 0;
    			
    		}else{// wrong input
    			result = null;
    		}
    		break;
    		
    	case '-':
    		if(sign_1.equals("+") && sign_2.equals("+")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.subtract(num2) : num2.subtract(num1) ;
    			result.sign = (my_compareTo(num_1, num_2) > 0) ? 1 : 0 ;
    			
    		}else if(sign_1.equals("+") && sign_2.equals("-")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.add(num2) : num2.add(num1) ;
    			result.sign = 1;
    			
    		}else if(sign_1.equals("-") && sign_2.equals("+")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.add(num2) : num2.add(num1) ;
    			result.sign = 0;    			
    			
    		}else if(sign_1.equals("-") && sign_2.equals("-")){
    			result = (my_compareTo(num_1, num_2) > 0) ? num1.subtract(num2) : num2.subtract(num1) ;
    			result.sign = (my_compareTo(num_1, num_2) > 0) ? 0 : 1 ;
    			
    		}else{// wrong input
    			result = null;
    		}
    		break;
    	
    	case '*':
    		//곱셈의 경우 첫 번째 매개변수가 두 번째 매개변수보다 크거나 같을 필요는 없음. 
    		result = num1.multiply(num2);
    		if(!(sign_1.equals(sign_2))){ //부호가 서로 같을 경우 +, 다를 경우 -
    			result.sign = 0;
    		}else{
    			result.sign = 1;
    		}
    		break;
    		
    	default : // wrong operator
    		result = null;
    	}
    	
    	if(result == null)
    		throw new IllegalArgumentException("Wrong Input");
    	
    	return result;
    }
    
    
    
    
    
    
    
    //이하는 수정하지 않음 
     
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
 
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
 
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
 
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
 
            return false;
        }
    }
 
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}

