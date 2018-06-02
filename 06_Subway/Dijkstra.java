import java.util.PriorityQueue;
import java.util.TreeMap;

// 모든 노드의 정보가 초기화되어 있다고 가정한다. (mindistance와 before 정보)

public class Dijkstra {
	
	// 가정에 의해, 여기서 start는 임의의 역이고, end는 마지막 역의 '이름'이다
	// 맨 처음! 탐색할 경우 환승역을 탐색하면 안된다. 왜냐하면 그럴 경우 처음 여러 번 환승하는 일이 발생하기 때문에 때문에..
	// 하지만 이 역시 처리 가능하지 않을까? 
	public static Path ShortestPath(Station start, String end, boolean token){ // token은 최소환승 처리를 위해
		
		//start로부터 특정 노드 사이의 최단 경로와 cost를 구한다. 
		
		
		Path shortestPath = new Path(); // 최단 경로 정보가 저장되어 있을 path
		
		PriorityQueue<Station> distanceList = new PriorityQueue<Station>(); // default : min_heap
		TreeMap<String, Station> finished = new TreeMap<String, Station>(); // 최단거리 처리가 완료된 스테이션들
		
		start.setMinDistance(0);
		//start.before = null;
		
		
		distanceList.add(start);
		
		
		while(!distanceList.isEmpty()){
			
			Station top = distanceList.poll(); // 현재 distanceList에 있는 가장 작은 경로. 이 경로는 이 시점에서 처리가 완료됨. finished.
			
			
			
			
			if(top.getName().equals(end)){ // 환승 역 관계 없이, 이름만 같으면 도착한 것으로 간주
				
				// 경로 출력 
				shortestPath.cost = top.getMinDistance();
					
				while(true){ // 첫 역에 도착할 때까지
					
					if(top.getKey().equals(start.getKey())){ // 시작 지점으로 돌아왔을 경우. 모든 경로를 다 돈 것. equals?
						break;
					}
					
					Station topBefore = top.before; // 가정에 의해, 시작부터 null이 될 수는 없음이 보장된다
					
					if(topBefore.getName().equals(top.getName())){ // 환승할 경우. 환승이 두 번 연속으로 이루어질수는 없음!
						
						shortestPath.path.addFirst("["+top.getName()+"]");
						//System.out.print("["+top.getName()+"] "); // debug
						shortestPath.transferNum++;
						topBefore = topBefore.before;
						
						if(topBefore == null){ // 맨 처음에 환승을 했을 경우. 
							break; 
						}
						
					}else{
						//System.out.print(top.getName()+" "); // debug 
						shortestPath.path.addFirst(top.getName()); 
					}
					top = topBefore; 
				}
				
				//System.out.println(start.getName()+" "); // debug
				shortestPath.path.addFirst(start.getName());
				return shortestPath;
			}
			
			finished.put(top.getKey(), top);
			
			
			// 연결된 path 각각에 대해 
			for(Edge edge:top.getStationList()){
				
				//System.out.println(edge.getStation().getName());
				
				long newDistance;
				
				if(token == false){
					newDistance = top.getMinDistance() + edge.getWeight();
				}else{// 환승 처리 필요
					if(edge.getStation().getName().equals(top.getName())){
						newDistance = top.getMinDistance() + edge.getWeight() + Subway.TRANSFER_DOMINANT; // 선택하지 않도록
					}else{
						newDistance = top.getMinDistance() + edge.getWeight();
					}
				}
				
				if(!finished.containsKey(edge.getStation().getKey()) && newDistance < edge.getStation().getMinDistance()){ // Relaxation
					
					//distanceList.remove(edge.getStation()); // 있을 경우 제거
					
					edge.getStation().before = top; // 바로 이전 경로를 갱신한다 
					edge.getStation().setMinDistance(newDistance); // 갱신하고 새롭게 넣어준다
					
					distanceList.add(edge.getStation());
				}
				
			}
			
		}
		
		return null;
		
	}
}
