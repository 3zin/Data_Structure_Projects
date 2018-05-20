import java.io.*;
import java.util.Hashtable;

public class Matching
{
	
	// API 사용해도 무방한가?

	
	public static void main(String args[])
	{
		
		// 프로그램에서 사용할 HashTable 객체를 생성한다 (?) HashTable 객체를 어떤 식으로 어디에 위치시켜야 하는가? 
		// moviedatabase처럼, command 클래스를 따로 만들어서 객체를 계속 전달하며 처리를 위임해야 하나?
		Hashtable<Integer, AVLTree<TreeItem>> hashTable = new Hashtable<Integer, AVLTree<TreeItem>>(100);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException
	{
		// TODO : 아래 문장을 삭제하고 구현해라.
		System.out.println("<< command 함수에서 " + input + " 명령을 처리할 예정입니다 >>");
		
		String subString = input.substring(2);
		
		switch (input.charAt(0)){
			case '<':   // 데이터 입력
				inputData(subString);
				break;
			case '@':   // 저장된 데이터 출력
				
				break;
			case '?':   // 패턴 검색
				
				break;
			default:
				throw new IOException("잘못된 정렬 방법을 입력했습니다.");
		}
		
	}
	
	// 데이터 입력
	public static void inputData(String path){
		hashTable = new Hashtable<Integer, AVLTree<TreeItem>>(100);
		
	}
	
	// 저장된 데이터 출력
	public void printData(){
		
	}
	
	// 패턴 검색
	public void searchPattern(){
		
	}
	
	
}
