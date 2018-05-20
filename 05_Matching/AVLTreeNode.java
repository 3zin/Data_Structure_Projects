import java.util.LinkedList;

public class AVLTreeNode<T> {

	AVLTreeNode<T> leftChild;
	AVLTreeNode<T> rightChild;
	
	LinkedList<T> item;
	
	
	AVLTreeNode(T input){
		leftChild = null;
		rightChild = null;
		
		item = new LinkedList<T>();
		this.item.add(input);
	}
	
	AVLTreeNode(AVLTreeNode left, AVLTreeNode right, T input){
		leftChild = left;
		rightChild = right;
		
		item = new LinkedList<T>();
		this.item.add(input);
	}
	
	public AVLTreeNode<T> getLeft(){
		return this.leftChild;
	}
	
	public AVLTreeNode<T> getRight(){
		return this.rightChild;
	}
	
	public LinkedList<T> getItem(){
		return this.item;
	}
	
	public void addItem(T input){
		
		if(this.item.isEmpty()){
			item = new LinkedList<T>();
			this.item.add(input);
			//이럴 경우가 존재하나?
		}else{
			this.item.add(input);
		}
	}
	
}
