/**
 * Created by jinuj on 6/2/2016.
 */
public class Edge implements  Comparable<Edge>{

    public int src, dest, weight;

    public Edge(){}

    public Edge(int _src, int _dest, int _weight){
        src = _src;
        dest = _dest;
        weight = _weight;
    }

    public int compareTo(Edge e){
        return this.weight - e.weight;
    }


}
