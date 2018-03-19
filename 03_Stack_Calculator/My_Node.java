
public class My_Node {

	char item;
	My_Node next;
	
	public My_Node(char item){
		this.item = item;
		this.next = null;
	}
	
	public My_Node(char item, My_Node next){
		this.item = item;
		this.next = next;
	}
	
}
