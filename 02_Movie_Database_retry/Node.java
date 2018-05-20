public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
    	Node<T> tmp = new Node<T>(obj);
    	tmp.next = this.next;
    	this.next = tmp;
		//throw new UnsupportedOperationException("not implemented yet");
    	//4.5. 수정. 수업시간에 배운 방식을 사용으므로 설명 생략 
    }
    
    public final void removeNext() {
    	Node<T> tmp = this.next;
    	this.next = this.next.next;
    	tmp.next = null;
		//throw new UnsupportedOperationException("not implemented yet");
    	//4.5. 수정. 수업시간에 배운 방식을 사용으므로 설명 생략 
    }
}