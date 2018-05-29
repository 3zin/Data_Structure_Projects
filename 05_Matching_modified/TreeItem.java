
/**********************************************************************************************************
 * Class TreeNode
 * 
 * AVLTreeNode 내부 LinkedList가 가지게 될 가장 말단의 노드이다. 
 * Key 값에 대한 정보는 상위 노드(AVLTreeNode)에 저장되어 있으며, 이 클래스는 오직 위치정보 i, j 값만 저장하고 있다. 
 *
 */

public class TreeItem implements Comparable<TreeItem> {

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

	// 패턴 검색을 위해
	@Override
	public int compareTo(TreeItem o) {
		if(this.i > o.i){
			return 1;
		}else if(this.i < o.i){
			return -1;
		}else{ // 줄 번호가 같을 경우, 시작 글자의 위치로 판단
			if(this.j > o.j){
				return 1;
			}else if(this.j < o.j){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
}
