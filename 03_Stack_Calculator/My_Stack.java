
public class My_Stack {
	private My_Node top;

	public My_Stack() {
		top = null;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public void push(char item) {
		top = new My_Node(item, top);
	}

	public char pop() throws Exception {
		if (!this.isEmpty()) {
			My_Node tmp = top.next;
			top = top.next;
			return tmp.item;
		} else {
			throw new Exception("empty");
		}
	}
}
