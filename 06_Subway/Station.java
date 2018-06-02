import java.util.ArrayList;

public class Station implements Comparable<Station>{

	private String key;
	private String name;
	private String line;
	
	private long minDistance = Long.MAX_VALUE;
	
	public Station before = null; // 경로 출력에 사용할, 바로 전에 거쳤던 역
	
	private ArrayList<Edge> stationList; // ArrayList? LinkedList?
	
	//public station;
	
	Station(String key, String name, String line){
		this.key = key;
		this.name = name;
		this.line = line;
		stationList = new ArrayList<Edge>();
	}

	// mindistance를 기준으로 비교
	@Override
	public int compareTo(Station o) {
		if(this.minDistance > o.minDistance){
			return 1;
		}else if(this.minDistance < o.minDistance){
			return -1;
		}else{
			return 0;
		}
	}
	
	/*@Override
	public boolean equals(Object o){
		return this.name.equals(((Station)o).name);
	}*/
	
	public String getKey(){
		return this.key;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getLine(){
		return this.line;
	}
	
	public ArrayList<Edge> getStationList(){
		return this.stationList;
	}
	
	public long getMinDistance(){
		return this.minDistance;
	}
	
	public void setMinDistance(long x){
		this.minDistance = x;
	}
	
	public void reset(){
		this.minDistance = Long.MAX_VALUE;
		this.before = null;
	}
}
