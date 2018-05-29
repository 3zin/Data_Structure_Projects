
/**********************************************************************************************************
 * Class AVLTree<K extends Comparable<K>, V>
 * 
 * MyHashTable의 각 Slot을 구성할 AVLTree이다. 
 * 본 과제가 사용하는 자료구조 내에서 AVLTree는 같은 'hash'를 가진 객체들의 모임이다. 
 * 이는 같은 해쉬 값을 갖지만 key값은 서로 다른 객체들을 한데 묶어 관리하는 컨테이너로서 기능하며, collision된 객체들을 처리하며 insert, search등의 작업을 수행한다. 
 * 
 * 본 구현에서는 제너릭스를 사용해 재사용성을 높였다. 
 * key의 타입은 K, value의 타입은 V로 입력된다. 
 * AVLTree는 기본적으로 Search Tree이므로 Key값인 K는 Comparable해야 한다.
 * 
 * AVLTree는 내부적으로 balanced search Tree의 성질을 유지하며, 삽입 이후 균형이 깨졌을 경우(양옆 subtree 의 height가 2 이상 차이날 경우) rotation을 통해 이를 조절한다. 
 * 외부 public domain을 참고하지 않고 직접 구현했으며, AVLTree의 작동 과정을 이해하는 과정에서 수도코드 몇 개를 참고했다.
 * 참고 목록 : https://www.geeksforgeeks.org/avl-tree-set-1-insertion/, https://ratsgo.github.io/data%20structure&algorithm/2017/10/27/avltree/, 수업 PPT, 교재
 *  
 */
public class AVLTree<K extends Comparable<K>, V> {

	private AVLTreeNode<K, V> root;
	
	AVLTree(){
		root = null;
	}
	
	public boolean isEmpty(){
		return root == null;
	}
	
	public void makeEmpty(){
		root = null;
	}
	
	
	/**
	 * preOrder function
	 * 
	 * 재귀적인 preOrder의 방식으로 트리를 순회하며 key 값을 출력한다.
	 * 출력을 위하여 key(type:K)는 toString() function을 적당히 override 해야 한다. 
	 * 
	 */
	public void preOrder(){
		
		StringBuffer sb = new StringBuffer();
		preOrderPrint(this.root, sb);
		String str = sb.toString();
		
		if(str == null){ // 실제 본 구현 환경에서는 MyHashTable.put method에 의하여 AVLTree의 root가 null이 아님이 보장되기 때문에 이 경우는 등장하지 않는다. 
			System.out.println("");
		}else{
			System.out.println(str.substring(0, str.length()-1)); // 마지막 공백문자 하나 삭제
		}
	}
	
	void preOrderPrint(AVLTreeNode<K,V> root, StringBuffer sb){
		
		if (root != null){
			
			sb.append(root.getKey().toString() + " ");
			preOrderPrint(root.getLeft(), sb);
			preOrderPrint(root.getRight(), sb);
		}
	}
	
	
	/**
	 * balance method
	 * 
	 * leftNode, rightNode의 height 정보를 바탕으로. 현재 노드를 root로 둔 subTree의 balanced 여부를 판단한다. 
	 * balance 값이 n이라는 것은 왼쪽 서브트리의 높이가 오른쪽 서브트리보다 n만큼 높다는 뜻이고 
	 * balance 값이 -n이라는 것은 오른쪽 서브트리의 높이가 왼쪽 서브트리보다 n만큼 높다는 뜻이다
	 * AVLTree에서 n의 절댓값이 2 이상이라는 것은 해당 subTree의 균형이 깨졌다는 사실을 의미한다. 이 경우에는 트리의 균형을 맞추기 위해 추가적인 처리가 필요하다. 
	 * 
	 */
	public int balance(AVLTreeNode<K, V> leftNode, AVLTreeNode<K, V> rightNode){
		return nodeHeight(leftNode) - nodeHeight(rightNode);
	}
	
	/**
	 * nodeHeight method
	 * 
	 * 현재 노드의 높이를 반환한다. 
	 * 노드가 null일 경우 -1을 반환한다. (노드가 null일 경우를 처리해주기 위해 AVLTreeNode가 아닌 AVLTree의 method로 구현했다)
	 * 
	 */
	public int nodeHeight(AVLTreeNode<K, V> node){
		if (node == null){
			return -1;
		}else{
			return node.getHeight();
		}
	}
	
	
	/**
	 * search function
	 * 
	 * key(type:K)를 input으로 받아 AVLTree에서 해당 key를 가지고 있는 AVLTreeNode를 검색한다.  
	 * 
	 * 노드를 찾았을 경우 그 노드의 레퍼런스를 리턴하고. 
	 * 해당 key를 갖고 있는 노드를 찾지 못했을 경우에는 null을 리턴한다. 
	 * 전체적인 알고리즘은 일반적인 binary Search Tree의 recursive search function과 동일하다.
	 *
	 */
	public AVLTreeNode<K,V> search(K searchKey){
		return searchItem(this.root, searchKey);
	}
	
	AVLTreeNode<K,V> searchItem(AVLTreeNode<K,V> root, K searchKey){
		
		if(root == null){
			return null; // 찾지 못했을 경우 
		}else if(searchKey.compareTo(root.getKey()) == 0){
			return root; // 찾았을 경우
		}else if(searchKey.compareTo(root.getKey()) < 0){
			return searchItem(root.getLeft(), searchKey);
		}else{
			return searchItem(root.getRight(), searchKey);
		}
	}
	
	
	/**
	 * insert Function
	 * 
	 * AVLTree에 새로운 노드를 삽입한다. 
	 * key 값은 모두 unique하다고 가정한다. 만약 중복되는 key를 삽입하고자 하는 경우에는 MyHashTable.put() function에서 별도로 처리되게 된다.
	 * 
	 * 삽입 이후, 삽입한 시점부터 재귀적으로 올라오며 각 노드의 height 정보를 갱신해 주어야 한다.  
	 * 만약 마주친 노드의 balance 값이 2 이상이거나 -2 이하일 경우, 이는 삽입으로 인해 AVLTree의 균형이 깨졌음을 의미한다. 따라서 적절한 rotation 처리가 필요하다. 
	 * 
	 * 왼쪽 서브트리의 왼쪽에 삽입해서 균형이 깨졌을 경우 -> single right rotation
	 * 왼쪽 서브트리의 오른쪽에 삽입해서 균형이 깨졌을 경우 -> double right rotation
	 * 오른쪽 서브트리의 오른쪽에 삽입해서 균형이 깨졌을 경우 -> single left rotation 
	 * 오른쪽 서브트리의 왼쪽에 삽입해서 균형이 깨졌을 경우 -> double left rotation
	 * 
	 */
	public void insert(K key, V item){
		
		root = insertItem(root, key, item);
	}
	
	
	AVLTreeNode<K,V> insertItem(AVLTreeNode<K,V> node, K searchKey, V item){
	
		if(node == null){
			node = new AVLTreeNode<K, V>(searchKey, item);
			
		// 삽입하려는 Key 값이 현재 노드의 Key보다 작을 경우 
		}else if(searchKey.compareTo(node.getKey()) < 0){ 
			node.setLeft(insertItem(node.getLeft(), searchKey, item)); // 왼쪽에 삽입
			
			// 삽입으로 왼쪽 서브트리가 길어져 균형이 깨졌을 경우 
			if(balance(node.getLeft(), node.getRight()) == 2){
				
				// 왼쪽 서브트리의 오른쪽에 삽입할 경우 double right rotation
				if(searchKey.compareTo(node.getLeft().getKey()) > 0){
					node.setLeft(leftRotation(node.getLeft()));
					node = rightRotation(node);
					
				// 왼쪽 서브트리의 왼쪽에 삽입할 경우 single right rotation
				}else{
					node = rightRotation(node);
				}
			}
			
		// 삽입하려는 Key 값이 현재 노드의 Key보다 클 경우 
		}else if(searchKey.compareTo(node.getKey()) > 0){ 
			node.setRight(insertItem(node.getRight(), searchKey, item)); // 오른쪽에 삽입
			
			// 삽입으로 오른쪽 서브트리가 길어져 균형이 깨졌을 경우
			if(balance(node.getLeft(), node.getRight()) == -2){
				
				// 오른쪽 서브트리의 왼쪽에 삽입할 경우 double left rotation
				if(searchKey.compareTo(node.getRight().getKey()) < 0){
					node.setRight(rightRotation(node.getRight()));
					node = leftRotation(node);
					
				// 오른쪽 서브트리의 오른쪽에 삽입할 경우 single left rotation 
				}else{
					node = leftRotation(node);
				}	
			}
		}else{
			// 중복되는 key를 삽입할 경우는 MyHashTable.put function에서 별도로 처리되게 된다. 이 경우는 발생하지 않는다. 
		}
		
		// 삽입한 시점부터 재귀적으로 올라오며 각 노드의 height 정보를 갱신해 주어야 한다.  
		// 만일 현재 노드가 leaf 노드일 경우, height값은 0이 된다. (node가 null일 경우 nodeHeight(node)의 값은 -1이므로) 
		node.setHeight(Math.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())) + 1);
		return node;
	}
	
	/**
	 * leftRotation function
	 * 
	 * 왼쪽 회전을 통해 균형을 맞춘다. 회전 이후 위치값이 변경된 노드들의 높이 정보를 갱신해 주어야 한다. 
	 */
	public AVLTreeNode<K, V> leftRotation(AVLTreeNode<K, V> node){
		
		AVLTreeNode<K, V> secondNode = node.getRight();
		AVLTreeNode<K, V> childNode = node.getRight().getLeft();
		
		secondNode.setLeft(node);
		node.setRight(childNode);
		
		// height값 업데이트. 순서 중요. 
		node.setHeight(Math.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())) + 1);
		secondNode.setHeight(Math.max(nodeHeight(secondNode.getLeft()), nodeHeight(secondNode.getRight())) + 1);
		
		return secondNode;
	}
	
	/**
	 * rightRotation function
	 * 
	 * 오른쪽 회전을 통해 균형을 맞춘다. 회전 이후 위치값이 변경된 노드들의 높이 정보를 갱신해 주어야 한다. 
	 */
	public AVLTreeNode<K, V> rightRotation(AVLTreeNode<K, V> node){
		
		
		AVLTreeNode<K, V> secondNode = node.getLeft();
		AVLTreeNode<K, V> childNode = node.getLeft().getRight();
		
		
		secondNode.setRight(node);
		node.setLeft(childNode);
		
		// height값 업데이트. 순서 중요. 
		node.setHeight(Math.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())) + 1);
		secondNode.setHeight(Math.max(nodeHeight(secondNode.getLeft()), nodeHeight(secondNode.getRight())) + 1);
		
		return secondNode;
	}
}


