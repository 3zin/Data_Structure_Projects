import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


public class Subway {
	
	// 기본 환승 시간
	final static int TRANSFER_TIME = 5;
	
	final static long TRANSFER_DOMINANT = 21000000000L;

	public static void main(String[] args) {

		
		TreeMap<String, Station> subwayGraph = new TreeMap<String, Station>(); // 고유번호를 key로 관리될 balanced binary tree
		TreeMap<String, Station> nameGraph = new TreeMap<String, Station>(); // 이름을 key로 관리될 balanced binary tree
		
		
		try {

			// 파일을 입력받는다	
			File f = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			// (1) 역 정보 처리
			while(true){ 
				String tmp = br.readLine();
				
				if(tmp.isEmpty()){ // 빈 줄을 만나기 전까지
					break;
				}
				
				String[] txtArr = tmp.split(" ");
				//System.out.println(txtArr[0]);
				Station newst = new Station(txtArr[0], txtArr[1], txtArr[2]);
				
				Station transfer = nameGraph.get(newst.getName()); 
				
				if(transfer != null){ // 중복된 키가 있을 경우. 환승이라는 표시임
					// 여기서 반환되는 역은, 그 이름으로 가장 먼저 들어온 노선의 역을 의미한다. 이 역을 시발점으로 다른 환승 호선에 접근할 수 있음  
					
					// 기존에 연결된 모든 경로를 새로 삽입될 노드에 연결해 갱신해준다
					// 이 과정에서 상당히 세밀한 접근이 사용되었음. 기존에 연결된 모든 환승역에 새로운 역을 추가해주고, 반대로 새로운 역에 연결된 호선에 모든 환승역을 넣어줘야 함.  
					for(Edge item : transfer.getStationList()){
						
						item.getStation().getStationList().add(new Edge(newst, TRANSFER_TIME));
						newst.getStationList().add(new Edge(item.getStation(), TRANSFER_TIME));
					}
					
					// 순서 중요함. 이 것을 먼저 쓸 경우 스테이션 리스트가 늘어나 값이 중복되게 됨. 
					transfer.getStationList().add(new Edge(newst, TRANSFER_TIME));
					newst.getStationList().add(new Edge(transfer, TRANSFER_TIME));
					
					subwayGraph.put(newst.getKey(), newst);
					
				} else {
					nameGraph.put(newst.getName(), newst);
					subwayGraph.put(newst.getKey(), newst);
				}
			}
			
			/*for(Map.Entry<String, Station> entry : subwayGraph.entrySet()){
				System.out.print("["+entry.getKey()+",");
				System.out.print(entry.getValue().getName()+","+entry.getValue().getLine() + "] ->");
				
				
				System.out.println();
			}*/
			
			
			
			// (2) 역 사이 경로 처리
			while(true){
				String tmp = br.readLine();
				
				if(tmp == null){ // 빈 줄을 만나기 전까지
					break;
				}	
				
				String[] txtArr = tmp.split(" ");
				//System.out.println(txtArr[0]+ " " + txtArr[1] + " " + txtArr[2]);
				
				Station st1 = subwayGraph.get(txtArr[0]);
				Station st2 = subwayGraph.get(txtArr[1]);

				if(st1 == null){
					System.out.println("Wrong1");
				}
				if(st2 == null){
					System.out.println("Wrong2");
				}
				
				st1.getStationList().add(new Edge(st2, Integer.parseInt(txtArr[2]))); // Station 정보 weight 정보와 함께 더해준다
			}
			
			br.close();
			
			
			/*
			// 디버깅용 경로 출력 프로그램
			System.out.println("--------그래프 출력--------");
			
			for(Map.Entry<String, Station> entry : subwayGraph.entrySet()){
				System.out.print("["+entry.getKey()+",");
				System.out.print(entry.getValue().getName()+","+entry.getValue().getLine() + "] ->");
				
				ArrayList<Edge> lst = entry.getValue().getStationList();
				
				for(int i = 0 ; i < lst.size(); i++){
					System.out.print("[" + lst.get(i).getStation().getName() + ":" + lst.get(i).getStation().getKey() + ":" + lst.get(i).getStation().getLine() + "|" + lst.get(i).getWeight() + "] ");
				}
				System.out.println();
			}
			*/
			
			
			
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
				String[] startEnd = input.split(" ");
				
				String startName = startEnd[0];
				String desName = startEnd[1];
				
				// 환승 검색을 할 것인가의 여부
				boolean token = false;
				
				if(startEnd.length == 3 && startEnd[2].equals("!")){ // 환승 최단거리를 요구했을 경우
					token = true; 
				}
				
				
				Station startStation = nameGraph.get(startName);
				if(startStation == null){
					System.out.println("Wrong" );
				}
				
				// 환승역 정보를 몽땅 저장해 놓는다. 
				LinkedList<Station> transferStations = new LinkedList<Station>();
				
				transferStations.add(startStation);
				
				for(Edge edge:startStation.getStationList()){
					if(edge.getStation().getName().equals(startName)){ // 이름이 같을 경우 환승역
						transferStations.add(edge.getStation());
					}
				}
				
				ArrayList<Path> paths = new ArrayList<Path>();
				
				for(Station st : transferStations){
					// System.out.println(st.getName() + ":" + st.getLine());
					paths.add(Dijkstra.ShortestPath(st, desName, token)); // 각각의 경로 집어넣고 최솟값 찾음
					// 한 번 검색한 다음에는 초기화해야함 
					for(Map.Entry<String, Station> entry : subwayGraph.entrySet()){
						entry.getValue().reset();
					}
				}
				
				
				Path minPath = Collections.min(paths);
				// 출력
				minPath.printPath();
				
				if(token == false){
					System.out.println(minPath.cost);
				}else{
					System.out.println(minPath.cost - (minPath.transferNum)*TRANSFER_DOMINANT);
				}
				//System.out.println(minPath.transferNum);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
