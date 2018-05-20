import java.util.Iterator;
import java.util.NoSuchElementException;

// 2018.4.5.
// 각주 완료
// 기본 테스트 완료


/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를 유지하는 데이터베이스이다. 
 * 
 * 본 프로그램은 "MyLinkedList" 클래스를 상속하여 Sorted Linked List로 구현한 "MovieList" 클래스를 
 * Genre와 Title을 내부적으로 정렬된 상태로 유지하는 컨테이너 리스트로 활용하였다
 */

public class MovieDB {
	
	MovieList<Genre> genreList;
	
    public MovieDB() {

    	// Genre 클래스를 정렬된 상태로 유지하기 위한 MovieList<Genre> 타입의 멤버 변수를 초기화한다.   	

    	genreList = new MovieList<Genre>();
    }

    
    /**
     * 주어진 item을 MovieDB 내부에 삽입한다.
     */
    
    public void insert(MovieDBItem item) {
        
    	// 매개변수로 주어진 item의 장르정보로 새로운 Genre 객체를 만든다
        Genre newGenre = new Genre(item.getGenre());
        
        for (Genre tmp : genreList){
        	
        	//만약 item의 장르와 동일한 장르가 genreList에 존재할 경우, Genre 내부 titleList에 영화 정보를 추가한다. 
        	//알아서 적당한 위치를 찾아서 삽입되게 된다. 중복되는 영화 삽입의 경우 MovieList.add 메소드 내에서 처리된다. 
        	if(tmp.equals(newGenre)){ 
        		tmp.titleList.add(item);
        		return;
        	}
        }
        
        //item의 장르가 새로운 장르일 경우, 앞에서 만든 Genre 객체 newGenre 내부 titleList에 영화 정보를 추가하고, genreList에 newGenre를 추가한다.
        newGenre.titleList.add(item);
        genreList.add(newGenre); 
        genreList.numItems++;
        
       //System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }
    
    

    /**
     * 주어진 item을 MovieDB에서 삭제한다. 
     */
    
    public void delete(MovieDBItem item) {
       
    	
    	// 매개변수로 주어진 item의 장르정보로 새로운 Genre 객체를 만든다
        Genre newGenre = new Genre(item.getGenre());
        
        for (Genre tmp : genreList){
        	
        	//만약 item의 장르와 동일한 장르가 genreList에 존재할 경우, Genre 내부 titleList에서 해당 영화 삭제를 시도한다
        	if(tmp.equals(newGenre)){ 
       
        		tmp.titleList.delete(item); //알아서 해당 영화의 자리를 찾아 삭제되게 된다. 해당하는 제목의 영화가 없는 경우 MovieList.delete 메소드 내에서 처리된다. 
        		
        		if(tmp.titleList.isEmpty()){//만약 삭제 이후 더이상 그 장르에 남아있는 영화가 없다면 장르를 삭제한다. 
        			genreList.delete(newGenre);
        			
        		}	
        	}
        }
        //System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    
    /**
     * 영화 제목에 주어진 term을 포함하는 모든 영화들의 장르와 제목을 정렬된 순서로 출력한다. 
     * 따로 Print로 출력할 필요 없이, MyLinkedList<MovieDBItem>(Not sorted List) 매개변수에 조건을 만족하는 영화를 차례대로 넣은 후 return하면 된다. 
     */
    
    public MyLinkedList<MovieDBItem> search(String term) {
        	
    	// 새로운 MyLinkedList(not sorted)를 만든 후 for 문을 돌며 순서대로 search 조건을 만족하는 영화를 집어넣는다
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
        for(Genre tmp:genreList){
        	for(MovieDBItem movie:tmp.titleList){
        		if(movie.getTitle().contains(term)){ //String Class에 구현되어 있는 contains 메소드를 활용했다. 
        			results.add(movie);
        		}
        	}
        }
        
       //System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);

        return results;
    }
    
    
    /**
     * MovieDB에 저장된 모든 영화들의 장르와 제목을 정렬된 순서로 출력한다. 
     * 따로 Print로 출력할 필요 없이, MyLinkedList<MovieDBItem>(Not sorted List) 매개변수에 영화를 차례대로 넣은 후 return하면 된다. 
     */
    
    public MyLinkedList<MovieDBItem> items() {
        
    	// 새로운 MyLinkedList(not sorted)를 만든 후 for 문을 돌며 영화를 집어넣는다
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
        for(Genre tmp:genreList){
        	for(MovieDBItem movie:tmp.titleList){
        		results.add(movie);
        	}
        }
        
        //System.err.printf("[trace] MovieDB: ITEMS\n");
        
    	return results;
    }
}



/**
 * Node<String>을 상속해서 만든 Genre node
 * 장르 이름(String)을 item 값으로 받아 저장하며, 그 장르에 속하는 영화들의 목록이 MovieList<MovieDBItem>의 형태로 titleList에 저장되어 있다. 
 */

class Genre extends Node<String> implements Comparable<Genre> {
	
	//장르에 속한 영화들의 목록를 저장하는 리스트 
	MovieList<MovieDBItem> titleList;
	
	/**
	 * 장르 이름 name을 노드의 item 값으로 받아 저장한다. 
	*/
	
	public Genre(String name) {
		super(name); //Node<String>(String name) 호출
		titleList = new MovieList<MovieDBItem>(); //영화 목록 리스트 초기화.  
	}
	
	
	/**
	 * Genre 클래스끼리의 값이 같은지 노드의 item 값(장르 이름)을 기준으로 비교. item 값의 타입인 String type의 compareTo 메소드를 호출한다. 
	*/
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}
	
	
	/**
	 * Genre 클래스끼리의 값을 노드의 item 값(장르 이름)을 기준으로 비교. item 값의 타입인 String type의 equal 메소드를 호출한다. 
	 * 매개변수로 들어오는 obj가 Genre type임을 가정한다. 
	*/
	
	@Override
	public boolean equals(Object obj) {
		
		Genre genreobj = (Genre) obj;
		return this.getItem().equals(genreobj.getItem()); 
	}
	
	/* Genre data set이나 hash table을 사용하지 않을 것이기 때문에 hashcode는 따로 구현하지 않는다
	@Override
	public int hashCode() {}	
	*/
}



/**
 * MyLinkedList을 상속해서 만든 Sorted Linked List
 * 구현상의 차이가 있는 add(T item) 메소드를 override했으며, delete(T item) 메소드를 추가했다.
 * MyLinkedListIterator에 구현되어 있는 remove 메소드는 따로 사용하지 않고 새로 구현한 delete 메소드를 전적으로 사용했다.
 * 노드의 item으로 들어오는 T는 Comparable을 implement해야 한다(즉, 비교 가능해야 한다). 
 * MovieDB class의 매개변수인 genreList, Genre class의 매개변수인 titleList에서 본 클래스를 활용해 각각 장르, 제목에 따른 정렬이 가능하도록 하였다. 
 */

class MovieList<T extends Comparable<T>> extends MyLinkedList<T> {	
	
	public MovieList() {
		super();
	}

	/**
     * item을 리스트 내 적절한 위치에 삽입한다. 
     * 중복된 값이 들어올 경우 무시한다.
     * 부모 class MyLinkedList가 dummy head Linked List이기 때문에 special case에 대한 처리는 불필요하다. 
     */
	
	@Override
	public void add(T item) {
		
		Node<T> tmp = head;
		
		while(tmp.getNext()!=null){//본 루프는 tmp가 끝 노드에 도달할 때(item의 값이 리스트 내부의 모든 값보다 클 때)까지 실행된다. 
			
			T tmpItem = tmp.getNext().getItem();
			if(tmpItem.compareTo(item) > 0){//적당한 위치를 찾은 경우 루프를 바로 빠져 나온다 
				break;
				
			}else if(tmpItem.compareTo(item) == 0){//중복된 값이 들어왔을 경우 메소드를 종료한다
				return;
				
			}else{
				tmp = tmp.getNext();
			}
		}	
		//리스트 내부에서 적당한 위치를 찾았거나, 끝 노드에 도달한 경우 다음 위치에 삽입한다.  
		tmp.insertNext(item);	
		numItems++;
	}

	
	/**
	 * item을 리스트 내에서 삭제한다. 
	 * 리스트에 없는 값이 들어올 경우 무시한다. 
	 * 부모 class MyLinkedList가 dummy head Linked List이기 때문에 special case에 대한 처리는 불필요하다. 
	 */
	
	public void delete(T item) {
		
		Node<T> tmp = head;
		
		while(tmp.getNext()!=null){
			
			T tmpItem = tmp.getNext().getItem();
			if(tmpItem.compareTo(item) == 0){//리스트 내에서 주어진 item과 동일한 값을 찾았을 경우 삭제한다 
				tmp.removeNext();
				numItems--;
				return;
				
			}else if(tmpItem.compareTo(item) > 0){//item이 있어야 할 위치를 지나쳤을 경우 더 이상 찾을 필요가 없으므로 종료한다
				return;
				
			}else{
				tmp = tmp.getNext();
			}
		}
	}	
}


