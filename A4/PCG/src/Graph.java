import java.io.*;
import java.util.Arrays;
import java.util.Random;

import static java.util.Arrays.fill;

/**
 * Created by jinuj on 6/2/2016.
 */
public class Graph {

    public int V, E;

    public Edge[] edges;

    public Graph(int v, int e){
        V = v;
        E = e;

        edges = new Edge[E];
        for (int i = 0; i < E; i++){
            edges[i] = new Edge();
        }
    }

    public Graph(int v, int e, Edge[] _edges){
        V = v;
        E = e;
        edges = _edges;
    }

    // A class to represent a subset for union-find
    // source: http://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/
    public class subset
    {
        int parent, rank;
    }

    // A utility function to find set of an element i
    // source: http://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/
    public int find(subset subsets[], int i)
    {
        // find root and make root as parent of i (path compression)
        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    // A function that does union of two sets of x and y
    // source: http://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/
    public void Union(subset subsets[], int x, int y)
    {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (subsets[xroot].rank < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

            // If ranks are same, then make one as root and increment
            // its rank by one
        else
        {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    // The main function to construct MST using Kruskal's algorithm
    // source: http://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/
    public Edge[] KruskalMST()
    {
        Edge result[] = new Edge[V];  // Tnis will store the resultant MST
        int e = 0;  // An index variable, used for result[]
        int i = 0;  // An index variable, used for sorted edges
        for (i=0; i<V; ++i)
            result[i] = new Edge();

        Arrays.sort(edges);

        // Allocate memory for creating V subsets
        subset subsets[] = new subset[V];
        for(i=0; i<V; ++i)
            subsets[i]=new subset();

        // Create V subsets with single elements
        for (int v = 0; v < V; ++v)
        {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0;  // Index used to pick next edge

        // Number of edges to be taken is equal to V-1
        while (e < V - 1)
        {
            // Step 2: Pick the smallest edge. And increment the index
            // for next iteration
            Edge next_edge = new Edge();
            next_edge = edges[i++];

            int x = find(subsets, next_edge.src);
            int y = find(subsets, next_edge.dest);

            // If including this edge does't cause cycle, include it
            // in result and increment the index of result for next edge
            if (x != y)
            {
                result[e++] = next_edge;
                Union(subsets, x, y);
            }
            // Else discard the next_edge
        }

        // print the contents of result[] to display the built MST
        System.out.println("Following are the edges in the constructed MST");
        for (i = 0; i < e; ++i)
            System.out.println(result[i].src+" -- "+result[i].dest+" == "+
                    result[i].weight);

        System.out.println(result.length);
        return result;
    }



}
