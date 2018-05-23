
/**********************************************************************************************************
 * Class TreeNode
 * 
 * AVLTreeNode 내부 LinkedList가 가지게 될 가장 말단의 노드이다. 
 * Key 값에 대한 정보는 상위 노드(AVLTreeNode)에 저장되어 있으며, 이 클래스는 오직 위치정보 i, j 값만 저장하고 있다. 
 *
 */

public class TreeItem {

	private int i; // 줄 번호 
	private int j; // 시작 글자의 위치
	
	TreeItem(int i, int j){
		this.i = i;
		this.j = j;
	}
	
	public int getI(){
		return this.i;
	}
	
	public int getJ(){
		return this.j;
	}
	
}
