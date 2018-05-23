import java.lang.reflect.Array;

/**********************************************************************************************************
 * Class MyHashTable <K extends Comparable<K>>
 * 
 * AVLTree 배열을 자료구조로 갖는 Custom Hash Table  
 * key 값에 따라 hash를 생성하고, 해당되는 hash의 위치에 자료를 집어넣는다.  
 * Collision이 발생할 경우 AVLTree에서 적당히 처리되게 된다 (일종의 Separate Chaining)
 * 본 HashTable은 Key값의 중복을 허용하며, Key값이 중복되었을 경우 value는 AVLTreeNode 내부 LinkedList에 순차적으로 저장되게 된다. 
 *
 * 본 구현에서는 제너릭스를 사용해 재사용성을 높였다. 
 * key의 타입은 K, value의 타입은 V로 입력된다. 
 * AVLTree에 삽입해야 하므로 Key값인 K는 Comparable해야 한다.
 *
 */
public class MyHashTable<K extends Comparable<K>, V> {
	
	private final static int HASH_SIZE = 100;
	private int numItems;	
	private AVLTree<K, V>[] hashSlots;
	
	@SuppressWarnings("unchecked")
	MyHashTable(){
		hashSlots = new AVLTree[HASH_SIZE];
		numItems = 0;
	}
	
	public int size(){
		return this.numItems;
	}
	
	public boolean isEmpty(){
		return this.numItems == 0;
	}
	
	
	/**
	 * reset method
	 * 
	 * 기존 해쉬테이블에 저장된 값을 리셋한다.
	 */
	@SuppressWarnings("unchecked")
	public void reset(){
		this.hashSlots = new AVLTree[HASH_SIZE];
		numItems = 0;
	}
	
	
	/**
	 * put function
	 * 
	 * key 값을 바탕으로 hash를 생성하고, hashSlots[hash]에 value를 삽입한다.
	 * 해당하는 인덱스의 hashSlot(AVLTree)이 비어있을 경우 AVLTree를 새롭게 만들어 준다.
	 * 
	 * 본 hashTable은 중복되는 key 값의 삽입을 허용하기 때문에. 삽입에 앞서 미리 해당하는 key 값이 hashSlot(AVLTree)에 이미 들어 있는지 검사해 주어야 한다.
	 * 만약 해당 key값이 아직 없을 경우, AVLTree에 일반적인 삽입을 진행한다. (이 결과 해당 key 값을 갖는 unique한 AVLTreeNode가 AVLTree 내부에 새로 생기게 된다)
	 * 해당 key값이 있을 경우, AVLTreeNode 내부 LinkedList에 직접 삽입한다. 
	 * 
	 */
	public void put(K key, V item){
		
		int hash = key.hashCode(); 
		
		if(hashSlots[hash]==null){ // 비어있을 경우 초기화  
			hashSlots[hash] = new AVLTree<K, V>(); 
		}
		
		// 미리 해당하는 key의 값이 hashSlot에 이미 들어 있는지 검사한다. 
		AVLTreeNode<K, V> tmp = hashSlots[hash].search(key);

		if(tmp == null){ // AVLTree에 삽입	
			hashSlots[hash].insert(key, item);
		}else{ // AVLTreeNode에 삽입
			tmp.addItem(item);
		}
		numItems++;
	}
	
	/**
	 * getKey method
	 * 
	 * 입력되는 'Key'값을 바탕으로 hash값을 생성하고, hash값에 해당하는 hashSlot(AVLTree)을 리턴한다. 
	 * 
	 */
	public AVLTree<K, V> getKey(K key){
		
		int index = key.hashCode();
		return getHash(index);
	}
	
	/**
	 * getHash method
	 * 
	 * 입력되는 'hash(int)'값에 해당하는 hashSlot(AVLTree)을 리턴한다. 
	 * 
	 */
	public AVLTree<K, V> getHash(int index){
		
		return hashSlots[index];
	}
}
