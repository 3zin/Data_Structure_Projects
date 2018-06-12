import java.util.LinkedList;

/**********************************************************************************************************
 * Class Path
 * 
 * 경로 정보를 담고 있는 class
 * 경로에 포함된 환승 횟수(transferNum), 경로의 총 비용(cost), 경로에 속한 역 이름(String)이 순차적으로 저장되어 있는 LinkedList를 담고 있다. 
 *  
 */

public class Path implements Comparable<Path>{

	public int transferNum;  // 환승 횟수
	public long cost;		 // 경로의 총 cost
	LinkedList<String> path; // 경로의 역 이름이 순서대로 LinkedList 형태로 저장되어 있음. 
	
	
	Path(){
		this.transferNum = 0;
		this.cost = 0;
		this.path = new LinkedList<String>();
	}
	
	
	// 경로를 프린트하는 메소드. path가 null일 경우는 없다고 가정한다. (문제 조건에서 경로가 1이거나, 이외의 잘못된 입력은 들어오지 않음이 보장된다)
	public void printPath(){
		
		StringBuffer br = new StringBuffer();
		
		for(String name:path){
			br.append(name+" ");
		}
		
		String str = br.toString();
		System.out.println(str.substring(0, str.length()-1));
	}


	// cost를 기준으로 비교하도록 Comparable을 구현한다
	@Override
	public int compareTo(Path o) {
		if(this.cost > o.cost){
			return 1;
		}else if(this.cost < o.cost){
			return -1;
		}else{
			return 0;
		}
	}
}
