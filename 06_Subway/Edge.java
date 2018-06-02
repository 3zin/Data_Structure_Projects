
public class Edge {
	
	private Station station;
	private int weight;
	
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
