import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**********************************************************************************************************
 * Interface MatchingCommand 
 * 
 * 프로그램에서 사용되는 명령들의 공통 인터페이스.
 * 공통적으로 들어오는 인자(argument)를 해석(parse)하고, 이를 바탕으로 데이터베이스를 수정하거나 내용을 출력하는 작업(apply)을 진행한다.  
 *
 */
public interface MatchingCommand {

	/**
	 * parse function
	 * 
	 * input 을 해석하는 공통 메소드.
	 */
	void parse(String input) throws Exception;

	/**
	 * apply function
	 * 
	 * 명령을 hashTable 에 적용하고 결과를 출력하는 공통 메소드.
	 */
	void apply(MyHashTable<MyString, TreeItem> hashTable) throws Exception;
	
}


/**********************************************************************************************************
 * Class MyFileIO
 * 
 * 명령 : "파일 입력" 
 * 텍스트 파일을 입력받는다.
 * 새로운 텍스트 파일이 입력된 경우 이전에 입력된 데이터는 지워진다.
 * 
 */
class MyFileIO implements MatchingCommand{

	// 경로가 저장될 String (절대경로, 상대경로는 알아서 처리될 것이다) 
	private String path;
	
	
	/**
	 * 인자로 들어오는 경로 정보를 인스턴스 변수에 저장한다
	 */
	@Override
	public void parse(String input) throws Exception {
		path = input.substring(2);
	}
	
	
	/** 
	 * 텍스트 파일을 한 줄씩 입력받으며. 각 줄을 길이가 6인 substring으로 쪼개어 hashTable에 저장한다. 
	 * 저장 시 Key 값은 MyString(substring)이고, Value 값은 (줄 번호, 시작 글자의 위치) 정보를 담고 있는 TreeItem이다. 
	 * 각 줄의 길이는 6 이상임이 보장된다. 
	 */
	@Override
	public void apply(MyHashTable<MyString, TreeItem> hashTable) throws Exception {
		
		
		File f = new File(path);
		
		// 파일이 존재하지 않을 경우
		if(!f.exists()){
			throw new Exception("해당 파일이 없습니다");
			
		}else{    
			// 새로운 값이 들어온 경우, 기존 값은 삭제되어야 한다. 
			hashTable.reset();
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String tmp1;
			int StringNum = 0; // 줄 번호 
			
			// 한 줄씩 읽어들인다.
			while((tmp1 = br.readLine())!= null){
				
				StringNum++;
				String tmp2;
				
				// 각 줄을 길이가 6인 substring으로 쪼개어 저장한다. 
				for(int i = 0 ; i+6 <= tmp1.length(); i++){
					
					tmp2 = tmp1.substring(i, i+6);
					MyString tmp3 = new MyString(tmp2);
					
					// 자리 정보 저장 (줄 번호, 시작 글자의 위치[1부터 시작])
					TreeItem myItem = new TreeItem(StringNum, i+1);
					
					// 해쉬테이블에 삽입  (key:MyString, value:TreeItem)
					hashTable.put(tmp3, myItem);
				}
			}
			br.close();
		}
	}
}


/**********************************************************************************************************
 * Class MyPrint
 * 
 * 명령 : "출력" 
 * 입력한 번호에 해당하는 hash table의 slot에 담긴 문자열을 출력한다. 
 * 전위 순회(preorder traversal) 방식으로 트리의 모든 노드를 출력한다. 
 * slot이 비어 있을 경우 EMPTY를 출력한다.
 * 들어오는 인덱스의 범위는 0~99임이 보장된다
 *
 */
class MyPrint implements MatchingCommand{
	
	private int index;

	/**
	 * 인자로 들어오는 인덱스 번호를 인스턴스 변수에 저장한다. 
	 */
	@Override
	public void parse(String input) throws Exception {
		index = Integer.parseInt(input.substring(2));
	}

	/**
	 * 인덱스 번호에 알맞은 hash table의 slot(type : AVLTree)을 찾아간다.
	 * slot 자체가 null일 경우(즉, 해당 해쉬 슬롯이 아예 생성되지 않았을 경우), EMPTY를 출력하고
	 * 이외의 경우 preOrder()로 트리의 모든 노드를 출력한다.   
	 */
	@Override
	public void apply(MyHashTable<MyString, TreeItem> hashTable) throws Exception {
		AVLTree<MyString, TreeItem> tmp = hashTable.getHash(index);
		
		if(tmp == null){
			System.out.println("EMPTY");
		}else{
			tmp.preOrder();	
		}
	}
}


/**********************************************************************************************************
 * Class MyPattern
 * 
 * 명령 : "패턴 검색 " 
 * 데이터 파일에서 등장하는 패턴의 위치를 모두 출력한다. 
 * 패턴의 위치는 (줄 번호, 시작 글자의 위치)의 형식으로 출력한다. 첫 번째 줄의 첫 번째 글자는 (1, 1)이다.
 * 패턴이 여러 번 등장하는 경우 각 좌표는 공백으로 구분한다. 
 * 패턴이 검색되지 않는 경우 (0, 0)을 출력한다. 
 * 패턴 문자열의 길이는 6 이상임이 보장된다. 
 *
 */
class MyPattern implements MatchingCommand{

	private String pattern;
	
	/**
	 * 인자로 들어오는 패턴을 인스턴스 변수에 저장한다. 
	 */
	@Override
	public void parse(String input) throws Exception {
		pattern = input.substring(2);
		
	}
	
	
	/**
	 * 데이터 파일에서 등장하는 패턴의 위치를 출력한다.
	 * 패턴을 길이 6의 substring으로 쪼개어 배열에 저장하고, 패턴의 첫 6글자를 Key로 하여 검색을 진행한다. 
	 * 검색 결과 LinkedList<TreeItem>의 형태로 데이터 파일에서 '패턴의 첫 6글자'가 등장하는 위치정보들이 반환될 것이다.
	 * 
	 * 첫 위치정보를 기억해 둔 채, 패턴의 나머지 substring을 차례로 key로 하여 검색해보며 등장 위치가 연속적으로 나타나는지 판단한다. 
	 * (즉, 패턴이 중간에 끊기지 않고 전부 나타나는지 확인한다)
	 * 패턴이 데이터 파일에 있음이 확인되었을 경우, (줄 번호, 최초 글자)를 출력하고, 다음 위치정보에 대해 검사를 진행한다. 
	 */
	@Override
	public void apply(MyHashTable<MyString, TreeItem> hashTable) throws Exception {
		

		MyString[] splitPatterns = new MyString[pattern.length()-5];
		
		// for문을 돌며 주어진 패턴을 6글자씩 쪼갠다. 이 쪼갠 각각의 substring(type:MyString)들은 순차적으로 배열 splitPatterns 에 저장한다. 
		for(int i = 0 ; i+6 <= pattern.length() ; i++){
			String tmp = pattern.substring(i, i+6);
			splitPatterns[i] = new MyString(tmp);
		}
		
		// 패턴의 첫 6글자(MyString)를 Key로 하여 검색한다. 
		// 입력되는 패턴의 길이가 6 이상임이 보장되기 때문에 splitPatterns[0]은 null이 될 수 없다. 
		AVLTree<MyString, TreeItem> firstTree = hashTable.getKey(splitPatterns[0]);
		
		// (1) 애초부터 패턴의 첫 6글자에 맞는 해쉬 슬롯 자체가 생성되지 않았을 때 (Key는 고사하고, 패턴의 hashCode 자체도 없음) 
		if(firstTree == null){
			System.out.println("(0, 0)");
			return;
		}
		
		AVLTreeNode<MyString, TreeItem> firstNode = firstTree.search(splitPatterns[0]);
		
		// (2) 패턴의 첫 6글자에 맞는 키 자체가 들어온 적이 없을 때 (패턴에 해당하는 hashSlot은 있으나, 동일한 Key가 없음) 
		if(firstNode == null){
			System.out.println("(0, 0)");
			return;
		}
		
		int stringNum; // 줄 번호 
		int wordNum; // 시작 글자의 위치
		
		boolean isEmpty = true;
		boolean isFirst = true;
		
		// 검색 결과 LinkedList<TreeItem>의 형태로 데이터 파일에서 '패턴의 첫 6글자'가 등장하는 위치정보들에 접근할 수 있다(firstNode.item)
		// 각각의 위치정보에 대해 실제로 패턴이 중간에 끊기지 않고 전부 나타나는지 검사를 진행한다.
		for(TreeItem item : firstNode.getItem()){
			
			// true일 경우 패턴이 중간에 끊기지 않고 전부 나타났다는 뜻
			// 기본 값을 true로 설정함으로써, 만약 splitPatterns 배열의 length가 1일 경우에(즉, pattern의 length가 6일 때) 자연스럽게 프린트되도록 한다 
			boolean token = true;
			stringNum = item.getI();
			wordNum = item.getJ();
			
			// 패턴의 나머지 substring을 차례로 key로 하여 검색해보며 등장 위치가 연속적으로 나타나는지 판단한다. 
			for(int i = 1 ; i < splitPatterns.length ; i++){
				
				AVLTree<MyString, TreeItem> tmpTree = hashTable.getKey(splitPatterns[i]);
				
				// (1) 애초부터 패턴의 substring에 맞는 해쉬 슬롯 자체가 생성되지 않았을 때
				if(tmpTree == null){
					token = false; // 못찾았음을 밝히고
					break;// for 문을 빠져나온다
				}
				
				AVLTreeNode<MyString, TreeItem> tmpNode = tmpTree.search(splitPatterns[i]);
				
				// (2) 패턴의 substring에 맞는 키 자체가 들어온 적이 없을 때 
				if(tmpNode == null){
					token = false;
					break;
				}
				
				// (3) 연속적인 인덱스 값이 존재하지 않을 경우  
				if(!find(tmpNode, stringNum, wordNum+i)){
					token = false; 
					break; 
				}
			}
			
			if(token && isFirst){
				System.out.print("(" + stringNum +  ", " + wordNum +  ")");
				isEmpty = false;
				isFirst = false;
			}else if(token && !isFirst){
				System.out.print(" (" + stringNum +  ", " + wordNum +  ")");
				isEmpty = false;
			}
		}
		
		if(isEmpty){ // 결국 끝까지 이어지는 패턴을 하나도 발견하지 못했을 경우
			System.out.println("(0, 0)");
			
		}else{ // 뭐라도 출력했을 경우 줄바꿈문자 추가
			System.out.println();
		}
	}
	
	
	/**
	 * find method
	 * 
	 * AVLTreeNode node를 input으로 받아 노드의 인스턴스 변수 item LinkedList<TreeItem>를 순회하며
	 * 매개변수로 넘어온 줄 번호(stringNum)와 시작 글자의 위치(wordNum)를 갖고 있는 TreeItem 객체가 리스트에 존재하는지 확인한다  
	 */
	public boolean find(AVLTreeNode<MyString, TreeItem> node, int stringNum, int wordNum){
		
		for(TreeItem item : node.getItem()){
			if(item.getI() == stringNum && item.getJ() == wordNum){
				return true;
			}
		}
		return false;
	}
}

