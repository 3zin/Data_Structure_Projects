import java.util.LinkedList;

public class Path implements Comparable<Path>{

	public int transferNum;
	public long cost;
	LinkedList<String> path;
	
	
	Path(){
		this.transferNum = 0;
		this.cost = 0;
		this.path = new LinkedList<String>();
	}
	
	
	
	public void printPath(){
		
		StringBuffer br = new StringBuffer();
		
		for(String name:path){
			br.append(name+" ");
		}
		
		String str = br.toString();
		System.out.println(str.substring(0, str.length()-1));
	}


	// cost를 기준으로 비교 
	
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
