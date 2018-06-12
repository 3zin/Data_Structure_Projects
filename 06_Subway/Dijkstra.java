import java.util.PriorityQueue;
import java.util.TreeMap;


/**********************************************************************************************************
 * Class Dijkstra
 * 
 * 다익스트라 알고리즘 메소드가 구현되어 있는 class. 
 * 
 */

public class Dijkstra {
	
	/** 
	 * ShortestPath Method
	 * 
	 * 시작 Station의 '객체(type: Station)'와, 목적지 Station의 '이름(type: String)'을 받아서 최단경로를 Path 형태로 리턴한다. 
	 * token으로 true가 들어온다면 최단경로가 아닌 최소환승경로를 구한다. 
	 * 모든 노드의 정보가 mindistance = infinite , before = null 로 초기화되어 있다고 가정한다.
	 * 
	 */
	public static Path ShortestPath(Station start, String end, boolean token){
		
		Path shortestPath = new Path(); // 최종적으로 리턴하게 될 최단경로
		
		PriorityQueue<Station> distanceList = new PriorityQueue<Station>(); // 현재까지 구한 최단경로 목록. Java API를 사용해 min_heap으로 구현한다. 
		TreeMap<String, Station> finished = new TreeMap<String, Station>(); // 최단거리 처리가 완료된 Vertex들의 목록.
		
		start.setMinDistance(0); // 시작 vertex의 연결 비용을 0으로 초기화한다.
		
		distanceList.add(start); // 첫 vertex를 우선 최단경로 목록으로 포함시킨다 
		
		
		while(!distanceList.isEmpty()){
			
			Station top = distanceList.poll(); // 현재 distanceList에 있는 가장 작은 경로.
			
			// 목적지의 호선 정보와는 관계 없이, 목적지와 이름만 같으면 도착한 것으로 간주한다.
			if(top.getName().equals(end)){
				
				// 최단경로의 cost를 갱신한다. 
				shortestPath.cost = top.getMinDistance();
					
				// 재귀적으로 경로를 print한다. 
				while(true){ 
					// 시작 지점으로 돌아왔을 경우. 모든 경로를 다 돈 것.
					if(top.getKey().equals(start.getKey())){ 
						break;
					}
					
					Station topBefore = top.before; // 바로 이전 역에 대한 정보 (시작부터 null이 될 수는 없음이 보장된다(경로가 0인 경우는 없으므로))
					
					if(topBefore.getName().equals(top.getName())){ // 환승할 경우 (환승이 두 번 연속으로 이루어질수는 없음)
						
						shortestPath.path.addFirst("["+top.getName()+"]");
						shortestPath.transferNum++;
						topBefore = topBefore.before;
						
						if(topBefore == null){ // 맨 처음에 환승을 했을 경우. 이 경우는 사실상 Subway class에서 적당히 걸러질 것이다. 
							break; 
						}
						
					}else{ // 환승하지 않았을 경우
						shortestPath.path.addFirst(top.getName()); 
					}
					
					top = topBefore; 
				}
				
				shortestPath.path.addFirst(start.getName());
				return shortestPath;
			}
			
			// 이 vertex에 대한 처리는 완료되었다.
			finished.put(top.getKey(), top);
			
			
			// 연결된 station 각각에 대해 필요에 따라 최단경로 값을 갱신한다
			for(Edge edge:top.getStationList()){
				
				long newDistance;
				
				if(token == false){ // 최단경로
					newDistance = top.getMinDistance() + edge.getWeight();
					
				}else{// 최소 환승 경로
					if(edge.getStation().getName().equals(top.getName())){ // 환승역일 경우
						// 다른 경로가 있을 때 환승을 선택하지 않도록, 큰 dummy weight 값을 의도적으로 더해준다. 이 값은 모든 처리가 완료된 이후 다시 빼 주어야 한다.  
						newDistance = top.getMinDistance() + edge.getWeight() + Subway.TRANSFER_DOMINANT; 
					}else{
						newDistance = top.getMinDistance() + edge.getWeight();
					}
				}
				
				// 해당 vertex에 대한 처리가 완료되지 않았고, 갱신된 최단경로의 값이 기존의 값보다 더 작을 때 Relaxation이 일어난다.
				if(!finished.containsKey(edge.getStation().getKey()) && newDistance < edge.getStation().getMinDistance()){
				
					distanceList.remove(edge.getStation()); // 있을 경우 제거한다 
					// 전 역 정보, 최단경로 값을 갱신하고 새롭게 넣어준다
					edge.getStation().before = top; 
					edge.getStation().setMinDistance(newDistance); 
					distanceList.add(edge.getStation());
				}
			}
		}
		return null;
	}
}
