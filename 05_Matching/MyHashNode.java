
// 삭제할 것. 

public class MyHashNode<K, V> {
	
	K key;
	V value;
	int hashNum;
	
	MyHashNode(K key, V value, int hash){
		this.key = key;
		this.value = value;
		this.hashNum = hash;
	}

}
