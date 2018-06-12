import java.util.ArrayList;

/**********************************************************************************************************
 * Class Station
 * 
 * 역 정보를 담고 있는 Class
 * 고유번호(key), 이름(name), 호선(line) 정보를 담고 있다.
 * 
 * 노선에서 연결된 역들의 '간선' 목록을 ArrayList<Edge>의 형태로 보관하며, 현재까지 Dijkstra algorithm을 통해 갱신된 최단경로 길이를 minDistance 변수로 담고 있음. 
 * 경로 출력을 위해 최단경로 상에서 바로 전 역에 대한 정보(before)도 담고 있다.
 * 
 */


public class Station implements Comparable<Station>{

	private String key;   // 고유번호
	private String name;  // 이름
	private String line;  // 호선 
	
	private long minDistance = Long.MAX_VALUE; // 현재까지 갱신된 최단경로 길이. infinite 값으로 초기화.
	public Station before = null; // 최단경로 출력에 사용할, 경로상 바로 전 역에 대한 정보
	private ArrayList<Edge> stationList; // 연결된 역들의 '간선' 목록. 
	
	
	Station(String key, String name, String line){
		this.key = key;
		this.name = name;
		this.line = line;
		stationList = new ArrayList<Edge>();
	}

	
	// mindistance를 기준으로 비교하도록 Comparable을 구현한다.
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
	
	// 초기화를 위한 메소드
	public void reset(){
		this.minDistance = Long.MAX_VALUE;
		this.before = null;
	}
}
