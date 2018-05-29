
import java.util.LinkedList;

/**********************************************************************************************************
 * Class AVLTreeNode<K, V>
 * 
 * AVLTree를 구성하는 노드이다. 
 * 본 과제가 사용하는 자료구조 내에서 AVLTreeNode는 같은 'Key'를 가진 객체들의 모임이다. 
 * 따라서 AVLTreeNode는 하나의 공통적인 'Key'값을 가지고 있어야 하고, 동일한 key 값을 갖는 'Value'들을 내부 LinkedList에 순차적으로 저장해 놓아야 한다. 
 * 
 * 본 구현에서는 제너릭스를 사용해 재사용성을 높였다. 
 * key의 타입은 K, value의 타입은 V로 입력된다. 
 * 
 */
public class AVLTreeNode<K, V> {

	private AVLTreeNode<K, V> leftChild;
	private AVLTreeNode<K, V> rightChild;
	private K key;
	private int height; // AVLTree의 특성상 현재 노드의 height 값을 저장해야 한다. 이는 AVLTree에 새로운 노드가 삽입될 때마다 매번 갱신된다.
	private LinkedList<V> item; // LinkedList는 따로 구현하지 않고 API를 사용했다. 
	
	
	AVLTreeNode(K key, V input){
		leftChild = null;
		rightChild = null;
		this.key = key;

		this.item = new LinkedList<V>();
		this.item.add(input);
		this.height = 0; // 생성 시점에서 노드는 leaf이므로 height는 null이다. 
	}
	
	
	/**
	 * addItem method
	 * 
	 * 만일 insert 시점에서 동일한 key값이 들어왔을 경우, 내부 LinkedList에 value를 append한다. 
	 */
	public void addItem(V input){
		
		if(this.item.isEmpty()){ // 사실상 초기화 시점에서 LinkedList가 생성되기 때문에 이 경우는 없을 것이다. 
			item = new LinkedList<V>();
			this.item.add(input);
			
		}else{
			this.item.add(input);
		}
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public AVLTreeNode<K, V> getLeft(){
		return this.leftChild;
	}
	
	public AVLTreeNode<K, V> getRight(){
		return this.rightChild;
	}
	
	public LinkedList<V> getItem(){
		return this.item;
	}
	
	public K getKey(){
		return this.key;
	}
	
	public void setLeft(AVLTreeNode<K, V> left){
		this.leftChild = left;
	}
	
	public void setRight(AVLTreeNode<K, V> right){
		this.rightChild = right;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
}
