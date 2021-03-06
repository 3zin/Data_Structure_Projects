import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	

	GenreList DBList; //가장 상위에 있는 list DB. 장르명대로 소팅되어있는 Sorted list임. 
	
    public MovieDB() {
        // FIXME implement this
    	
    	DBList = new GenreList();
    	
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.
    	
    	String movie_title = item.getTitle();
    	String movie_genre = item.getGenre();
    	
    	
    	
    	DBList.add(item);

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
    	
    	
    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.
    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    	System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);
    	
    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: ITEMS\n");

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
    	return results;
    }
}

//DBItem
/*
class DBInventory implements ListInterface<>{
	
	private Genre head;
	private MovieList movieLists;
	
	
}
*/

//장르 node. 필요한가?
/*
class Genre extends Node<String> implements Comparable<Genre> {
	
	public Genre(String name) {
		super(name);
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem()); // 내부 item string compareTo
	}

	@Override
	public int hashCode() {
		return this.getItem().hashCode(); // ?? 일단 반환 
	}

	@Override
	public boolean equals(Object obj) {
		return this.getItem().equals(obj); // String equals method 불러오기 
	}
}
*/


//GenreList(sorted linked list). MovieItem을 node로 가짐. 

class GenreList implements ListInterface<MovieItem>{
	
	//dummy head
	Node<MovieItem> head;
	int numItems;
		
	public GenreList() {
		head = new Node<MovieItem>(null);
	}
	
	
	
	@Override
	public Iterator<MovieItem> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return this.numItems;
	}

	public int locateIndex(MovieItem item) {

		Node<MovieItem> curr = head;
		
		for (int i = 0;; i++) {
			if (curr.getNext() == null) {
				return i; // Reached to the end of the list. 제일 큰 값이 들어왔을 경우. return null이 아닌 마지막 node의 index. 즉 삽입될 위치. 
			}
			
			if (item.compareTo(curr.getNext().getItem()) <= 0) { // String 사전순 오름차순 정렬. 다음 노드에서 자기보다 큰 문자를 만났을 때. 멈춤. 또한 동일한 문자를 만났을 때도 멈춤.  
				return i; // return 삽입될 node의 index 
			}
			curr = curr.getNext();
		}
	}
	
	// my implement
	public Node<MovieItem> sortedGet(int index){
		// index만큼 이동한 노드를 반환함.(삽입 위치)  
		
		if(index >= 0 && index <= this.size()){
			Node<MovieItem> curr = head;
			for(int i=0;i<index;i++){
				curr = curr.getNext();
			}
			return curr;//삽입될 위치의 node(뒤에 삽입하면 됨)
		}else{
			//wrong index
			return null;
		}
	}

	
	@Override
	public void add(MovieItem item) {
		// 이름 순서대로 배열해야 함... sorted add!
		
				int pos = locateIndex(item); //index 반환 
				
				// 같을 경우 (단, nullpointexcpetion을 고려해서 리스트가 비어있을 때, 리스트의 끝일 때를 고려해야 함)
				if ( !isEmpty() && pos<size() && sortedGet(pos).getNext().getItem().compareTo(item) == 0) {																										
					
				}

				Node<MovieItem> curr = head;
				
				for(int i=0 ; i<pos ; i++){
					curr = curr.getNext();
				}
				curr.insertNext(item);
				numItems++;
		
	}

	@Override
	public MovieItem first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
		
	}	
}





// MovieItem: 장르명과 장르에 속한 영화들로 구성되어 있음. 
class MovieItem implements Comparable<MovieItem>{
	
	String genre;
	MovieList movies;

	@Override
	public int compareTo(MovieItem o) {
		return this.genre.compareTo(o.genre);
	}
	
	public MovieItem(String a, MovieList b){
		this.genre = a;
	}
}









// MovieList(sorted linked list).영화의 이름(String)을 item으로 가짐.

class MovieList implements ListInterface<String> {	
	
	//dummy head
	Node<String> head;
	int numItems;
	
	public MovieList() {
		head = new Node<String>(null);
	}

	@Override
	public Iterator<String> iterator() {
		return new MovieListIterator<String>(this);
		//구색 맞추기 위함..
		//return null;
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return this.numItems;
	}
	
	// my implement
	public int locateIndex(String item) {

		Node<String> curr = head;
		
		for (int i = 0;; i++) {
			if (curr.getNext() == null) {
				return i; // Reached to the end of the list. 제일 큰 값이 들어왔을 경우. return null이 아닌 마지막 node의 index. 즉 삽입될 위치. 
			}
			
			if (item.compareTo(curr.getNext().getItem()) <= 0) { // String 사전순 오름차순 정렬. 다음 노드에서 자기보다 큰 문자를 만났을 때. 멈춤. 또한 동일한 문자를 만났을 때도 멈춤.  
				return i; // return 삽입될 node의 index 
			}
			curr = curr.getNext();
		}
	}
	
	// my implement
	public Node<String> sortedGet(int index){
		// index만큼 이동한 노드를 반환함.(삽입 위치)  
		
		if(index >= 0 && index <= this.size()){
			Node<String> curr = head;
			for(int i=0;i<index;i++){
				curr = curr.getNext();
			}
			return curr;//삽입될 위치의 node(뒤에 삽입하면 됨)
		}else{
			//wrong index
			return null;
		}
	}

	@Override
	public void add(String item) {
		// 이름 순서대로 배열해야 함... sorted add!
		
		int pos = locateIndex(item); //index 반환 
		
		// 같을 경우 (단, nullpointexcpetion을 고려해서 리스트가 비어있을 때, 리스트의 끝일 때를 고려해야 함)
		if ( !isEmpty() && pos<size() && sortedGet(pos).getNext().getItem().compareTo(item) == 0) {																										
			return; //same item. ignore
		}

		Node<String> curr = head;
		
		for(int i=0 ; i<pos ; i++){
			curr = curr.getNext();
		}
		curr.insertNext(item);
		numItems++;
	}

	@Override
	public String first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}

}







class MovieListIterator<String> implements Iterator<String> {
	// FIXME implement this
	// Implement the iterator for MyLinkedList.
	// You have to maintain the current position of the iterator.
	private MovieList list;
	private Node<String> curr;
	private Node<String> prev;

	public MovieListIterator(MovieList list) {
		this.list = list;
		this.curr = (Node<String>) list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}