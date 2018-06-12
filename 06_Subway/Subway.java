import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**********************************************************************************************************
 * Class Subway [This Class is The Starting Point of The Program]
 * 
 * 프로그램의 최상단에 위치한 class로, 전체적인 프로그램의 흐름을 총괄한다. 
 * (1) System argument로 들어오는 데이터 파일을 처리하여 역 정보와 간선 정보를 파싱해 그래프의 형태로 저장하고
 * (2) 이를 바탕으로 표준 입력으로 들어오는 출발역과 도착역 사이의 최단경로/최소환승경로를 구하여 출력한다 
 *  
 */

public class Subway {
	
	final static int TRANSFER_TIME = 5; // 기본 환승 시간
	final static long TRANSFER_DOMINANT = 21000000000L; // 최소환승 경로를 구할 때 사용될 dummy weight

	
	public static void main(String[] args) {
		
		TreeMap<String, Station> subwayGraph = new TreeMap<String, Station>(); // 고유번호를 key로 해서 관리될 balanced binary tree
		TreeMap<String, Station> nameGraph = new TreeMap<String, Station>();   // 이름을 key로 해서 관리될 balanced binary tree
		
		
		try {
	
			// (1). System argument를 통해 데이터 파일을 입력받는다	
			File f = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			
			// (1.1). 데이터 파일의 역 정보 파싱
			while(true){ 
				String tmp = br.readLine();
				
				if(tmp.isEmpty()){ // 빈 줄을 만나기 전까지
					break;
				}
				
				String[] txtArr = tmp.split("\\s");
				Station newst = new Station(txtArr[0], txtArr[1], txtArr[2]);
				
				Station transfer = nameGraph.get(newst.getName()); 
				
				// 여기서 transfer 값이 null이 아니라는 것은 해당 이름으로 들어온 역이 이미 존재한다는 걸 의미함. 즉, 해당 역이 '환승역'이라는 의미임. 
				if(transfer != null){ 
					
					// 여기서 반환되는 Station 객체는, 해당 이름으로 가장 먼저 들어온 노선의 역을 의미한다. 이 역을 시발점으로 다른 호선의 환승역에 접근할 수 있음  
					// 이 과정에서 상당히 세밀한 접근이 사용되었음. 기존에 연결되어 있었던 모든 환승역에 새로운 환승역을 연결해주고, 반대로 새로운 환승역에 기존의 모든 환승역을 연결해준다.  
					for(Edge item : transfer.getStationList()){
						item.getStation().getStationList().add(new Edge(newst, TRANSFER_TIME));
						newst.getStationList().add(new Edge(item.getStation(), TRANSFER_TIME));
					}
					
					// 순서 중요함. 이 것을 먼저 쓸 경우 값이 중복되게 됨. 
					transfer.getStationList().add(new Edge(newst, TRANSFER_TIME));
					newst.getStationList().add(new Edge(transfer, TRANSFER_TIME));
				
					subwayGraph.put(newst.getKey(), newst);
					
				} else { // 처음 들어오는 역 이름일 경우
					nameGraph.put(newst.getName(), newst);
					subwayGraph.put(newst.getKey(), newst);
				}
			}
			
			
			// (1.2). 데이터 파일의 간선 정보 파싱
			while(true){
				String tmp = br.readLine();
				
				if(tmp == null){ // 빈 줄을 만나기 전까지
					break;
				}	
				
				String[] txtArr = tmp.split("\\s");
				Station st1 = subwayGraph.get(txtArr[0]);
				Station st2 = subwayGraph.get(txtArr[1]);

				// debug
				if(st1 == null){
					System.out.println("Wrong1");
				}
				if(st2 == null){
					System.out.println("Wrong2");
				}
				st1.getStationList().add(new Edge(st2, Integer.parseInt(txtArr[2]))); // 간선 정보(역 정보 + weight)를 넣어준다
			}
			br.close();
			
			
			
			// (2). 표준 입력으로 들어오는 출발역과 도착역 사이의 최단경로/최소환승경로 출력 
			
			br = new BufferedReader(new InputStreamReader(System.in));
			
			while(true){
				
				String input = br.readLine();
				
				if(input.isEmpty()){
					continue;
				}
				
				if (input.compareTo("QUIT") == 0){
					break;
				}
				
				// 출발역과 도착 역의 이름이 공백을 기준으로 들어온다
				String[] startEnd = input.split("\\s");
				
				String startName = startEnd[0];
				String desName = startEnd[1];
				
				// 최소환승경로 검색을 할 것인가의 여부 판단
				boolean token = false;
				if(startEnd.length == 3 && startEnd[2].equals("!")){
					token = true; 
				}
				
				/** 
				 * [주의] 인코딩 문제로 인해 출발역/도착역 이름으로 깨진 글자가 입력되어 이 부분에서 error가 발생할 수 있음. (엉뚱한 검색을 진행하는..)
				 * 이는 프로그램의 결함이 아니므로 과제 채점시에는 해결될 것으로 사료됨.
				 **/
				Station startStation = nameGraph.get(startName); // debug
				Station desStation = nameGraph.get(desName); // debug
				if(startStation == null || desStation == null){
					System.out.println("Wrong" );
					continue;
				}
				
				// 출발역이 여러 노선이 통과하는 '환승역'인 경우, 연결되어 있는 각각의 노선마다 '그 노선의 환승역'을 시작 역으로 하는 최단경로를 구해서 서로 비교해 주어야 한다.
				// 따라서 환승역 정보를 미리 전부 저장해 놓고 각각의 환승 정보에 대해 다익스트라 알고리즘을 따로 실행하고 마지막에 한꺼번에 비교한다. 
				LinkedList<Station> transferStations = new LinkedList<Station>(); // 환승역 리스트
				transferStations.add(startStation);
				
				for(Edge edge:startStation.getStationList()){
					if(edge.getStation().getName().equals(startName)){ // 이름이 같을 경우 환승역이므로 환승역 리스트에 추가
						transferStations.add(edge.getStation());
					}
				}
				
				ArrayList<Path> paths = new ArrayList<Path>();
				
				for(Station st : transferStations){
				
					paths.add(Dijkstra.ShortestPath(st, desName, token)); // 환승역 리스트의 원소를 시작점으로 하는 경로를 각각 구해서 저장해 놓는다
					
					// 한 번 검색한 다음에는 초기화해야함 
					for(Map.Entry<String, Station> entry : subwayGraph.entrySet()){
						entry.getValue().reset();
					}
				}
				
				// 저장해 놓은 경로들 중 최단경로를 출력한다. 이는 Path Class에 구현된 CompareTo를 통해 cost를 기준으로 sorting된다. 
				Path minPath = Collections.min(paths);
				minPath.printPath();
				
				if(token == false){
					System.out.println(minPath.cost);
				}else{ // 최소환승경로를 구했을 경우, 전체 cost에서 dummy weight 값을 다시 빼 주어야 한다. 
					System.out.println(minPath.cost - (minPath.transferNum)*TRANSFER_DOMINANT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
