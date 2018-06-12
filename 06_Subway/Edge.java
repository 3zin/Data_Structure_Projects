
/**********************************************************************************************************
 * Class Edge
 * 
 * 간선 정보를 담고 있는 class.
 * Station에 대한 정보와 해당 Station까지 가는 weight 정보를 담고 있다. 
 * Edge는 Station class 내부 프로퍼티인 StationList(ArrayList<Edge>)의 원소로 저장되어, 해당 Station 객체에서 다른 역까지 가는 간선의 정보를 나타낸다. 
 *  
 */


public class Edge {
	
	private Station station;
	private int weight; // 간선에 올려진 weight
	
	Edge(Station station, int weight){
		this.station = station;
		this.weight = weight;
	}
	
	public Station getStation(){
		return this.station;
	}
	
	public int getWeight(){
		return this.weight;
	}
}
