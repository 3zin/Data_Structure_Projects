
/**
 * 2018.spring
 * [자료구조 과제 4] : Matching
 * 
 * ******<프로그램에 대한 설명>********************************************************************************
 * 
 * 본 프로그램은 Hash-table, AVL tree, LinkedList와 같은 다양한 자료 구조를 혼합해서 사용해 텍스트 파일을 length=6인 substring으로 나누어 관리하며, 검색 및 출력 등의 메소드를 지원한다.  
 * 프로그램의 작동 흐름에 있어서 자료구조 두 번째 과제 "MovieDatabase" 뼈대코드의 객체지향적 디자인을 참조했다. (modified)
 * 
 * 본 프로그램은 크게 입력 - 해석 - 처리 과정을 반복하는 것으로 구성되어 있다. 
 * 프로그램은 콘솔(System.in)로부터 커맨드를 입력받고 이를 해석해 각각의 작업을 지시하는 단계로부터 시작된다. 
 * 프로그램은 총 세 개의 명령(파일 입력, 출력, 패턴 검색)을 지원하며, 이는 각자 식별자 (<, @, ?) 에 의해 구별된다. 
 * 
 * 식별자를 기준으로 구분되는 각각의 '명령'은 들어오는 인자(argument)를 알맞은 형식으로 해석(parse) 해야 하고, 이를 바탕으로 데이터베이스를 수정하거나 내용을 출력하는 등의 작업(apply)을 진행해야 한다 
 * 즉 '파일 입력', '출력', '패턴 검색'이라는 세개의 '명령'을 각각의 객체라고 생각한다면, 이 세 객채는 모두 해석(parse)과 작업(apply)을 수행하는 메소드를 가지고 있어야 한다. 
 * 따라서 본 프로그램은 parse(), apply() 메소드를 구현할 것을 지시하는 "MatchingCommand" interface를 만들고, 
 * 각각의 명령이 "MatchingCommand"를 implement하여 각자의 parse(), apply() 메소드를 구현하도록 하였다 (MyFileIO, MyPrint, MyPattern)
 * 
 * main 함수는 command() 메소드를 통해 입력에 맞는 MatchingCommand 타입의 객체를 생성하며(MatchingCommand를 implement하는 객체). 
 * 생성과 동시에 객체의 parse() 함수를 호출한다. 이 과정에서 객체는 인자를 해석해 인스턴스 변수를 알맞게 초기화하게 된다. 
 * 마지막으로 main 함수는 객체의 apply()메소드를 호출하며, 공통적으로 사용할 프로그램의 데이터베이스 hashTable을 인자로 전달한다. 
 * 
 * 이로 인해 입력과 해석의 단계가 끝나고. 객체의 apply() 메소드 내에서 구체적인 처리가 이루어지게 된다.  
 * 
 */


/*********************************************************************************************************
 * Class Matching [This Class is The Starting Point of The Program]
 * 
 * 프로그램의 최상단에 위치한 class로, 전체적인 프로그램의 흐름을 총괄한다. 
 * 
 */
import java.io.*;

public class Matching
{
	
	/**
	 * main function
	 * 
	 * 입력 - 해석 - 처리 작업을 수행한다. 
	 */
	public static void main(String args[])
	{
		
		// 프로그램에서 지속적으로 사용할 HashTable 객체를 생성한다 (key는 MyString, value는 TreeItem)
		MyHashTable<MyString, TreeItem> hashTable = new MyHashTable<MyString, TreeItem>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				// System.in으로부터 커맨드를 입력받는다.
				String input = br.readLine();
				
				if (input.isEmpty())
					continue;
				
				if (input.compareTo("QUIT") == 0)
					break;

				// 입력된 커맨드를 해석해서 알맞은 객체를 생성한다. 
				MatchingCommand command = command(input);
				
				// 입력을 처리하는 과제의 복잡한 스펙을 아래의 한 줄로 묘사함으로써 자세한 내용을 생략하고 다른 클래스에 위임한다. 
				command.apply(hashTable);

				// 위와 같은 코드를 통해, 과제 프로그램의 큰 흐름이 입력 - 해석 - 처리 과정을 반복하는 것으로 구성되어 있음을 명확하게 드러낼 수 있다.
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * MatchingCommand function
	 * 
	 * 들어온 input을 해석해 식별자별로 알맞은 MatchingCommand 객체를 생성해주고
	 * 인자를 parsing하여 객체 내부에 알맞은 정보로 저장한다. (구체적인 작업은 객체의 parse() 메소드 내에서 처리된다)
	 * 
	 */
	private static MatchingCommand command(String input) throws Exception
	{
		
		MatchingCommand command = null;
		
		char operator = input.charAt(0);
		
		switch(operator){
			case '<':
				command = new MyFileIO();
				break;
			case '@':
				command = new MyPrint();
				break;
			case '?':
				command = new MyPattern();
				break;
			default:
				throw new IOException("잘못된 입력입니다");
			
		}
		command.parse(input);
		return command;
	}
}
