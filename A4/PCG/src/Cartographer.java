import java.io.*;
import java.util.Random;

import static java.util.Arrays.fill;

/**
 * Created by jinuj on 6/2/2016.
 */
public class Cartographer {

    static int roomSize = 9;


    static int[] generateMap(int height, int width){
        return new int[height*width];
    }

    static Graph createGraph(int[] map){
        int v = map.length/roomSize;    // number of vertices
        int sqrtV = (int)Math.sqrt(v);
        int e = (sqrtV*(sqrtV-1))+(sqrtV*(sqrtV-1));        // number of edges
        System.out.println("edges = " + e);

        Edge[] edges = new Edge[e];
        Random rand = new Random();

        int k = 0;

        // Iterate through vertices; use k to track edges
        for (int i = 0; i < v-1; i++){

            // bottom vertices only need edges to the right
            if (i >= sqrtV*(sqrtV-1)){
                edges[k] = new Edge(i, i+1, rand.nextInt(4)+1);
                k++;
            }
            // far right vertices only need edges below
            else if (i%sqrtV == sqrtV-1) {
                edges[k] = new Edge(i, i + sqrtV, rand.nextInt(4) + 1);
                k++;
            } else {
                // regular vertice need edges to right and below
                edges[k] = new Edge(i, i + 1, rand.nextInt(4) + 1);
                k++;
                edges[k] = new Edge(i, i + sqrtV, rand.nextInt(4) + 1);
                k++;
            }

        }

        Graph g = new Graph(v,e,edges);
        return g;

    }

    // Creates a map using an MST
    static int[] createMap(Edge[] mst, int[] map, int n){

        int numOfRooms = mst.length;
        int numRows = (int)Math.sqrt(numOfRooms);
        int roomsPerRow = numOfRooms/numRows;
        int rowSize = 3 * n;
        int center = n+1;
        int row = 0;

        int[] centers = new int[numOfRooms];
        int room = 0;

        // Find the centers of each room
        int k = 0;
        for (int i = 0; i < n*n; i++){
            if (i == center){
                map[i] = 0;
                centers[room] = i;      // add position of center
                room++;
                k++;
                if (k == roomsPerRow ){ // reached end of row
                    row++;
                    center = (row*rowSize)+n+1;
                    k = 0;
                } else {
                    center = center + 3; // center of next room
                }
            } else {
                map[i] = 1;
            }

        }

        // Create passages between rooms according to MST
        for (int i = 0; i < mst.length; i++){
            Edge e = mst[i];
            int dest = centers[e.dest];
            int src = centers[e.src];
            if (e.dest - e.src == 1){
                map[src+1] = 0;
                map[dest-1] = 0;
            } else {
                map[src+n] = 0;
                map[dest-n] = 0;
            }
        }

        return map;
    }

    static String[] randomizeRoom(){
        Random rn = new Random();
        int roomType = rn.nextInt(5);

        if (roomType == 0){
            return new String[]{ "1", "12"};    // light gray tiles
        } else if (roomType == 1) {
            return new String[] {"31", "21"};   // grass and water tiles
        } else if (roomType == 2) {
            return  new String[] {"41", "11"};  // brown tiles
        } else if (roomType == 3) {
            return new String[] {"31", "45"};   // grass and trees
        } else if (roomType == 4) {
            return  new String[] {"57", "67"};  // dark gray tiles
        } else {
            return  new String[] {"1", "1"};    // gray floor tiles just in case
        }


    }

    // Create a XML file that represents a map
    static void writeMap(int[] map, int n) throws IOException {
        PrintWriter bw;
        File file = new File("../A4Engine2.3-win64/games/example-game/newMap.xml");

        try{
            bw = new PrintWriter(new FileWriter(file, false));

            // Opener
            bw.println("<map version=\"A4\" orientation=\"orthogonal\" width=\""+n+"\" height=\""+n+"\" tilewidth=\"32\" tileheight=\"32\">");
            bw.println("<properties>");
            bw.println("    <property name=\"name\" value=\"Blackrock\"/>");
            bw.println("  </properties>");
            bw.println("  <tileset firstgid=\"1\" name=\"graphics\" tilewidth=\"32\" tileheight=\"32\">");
            bw.println("    <image source=\"graphics.png\" width=\"320\" height=\"1184\"/>");
            bw.println("  </tileset>");
            bw.println("  <layer name=\"Tile Layer 1\" width=\""+n+"\" height=\""+n+"\">");
            bw.println("    <data>");

            String[] tiles = randomizeRoom();
            String tileType1 = tiles[0];
            String tileType2 = tiles[1];

            for (int i = 0; i < map.length; i++){
                String value = tileType1;
                if (map[i] == 1){
                    value = tileType2;       // wall
                }
                bw.println("      <tile gid=\""+value+"\"/>");
            }

            // Closer
            bw.println("    </data>");
            bw.println("  </layer>");
            bw.println("</map>");

            bw.flush();
            bw.close();

        }
        catch(Exception e) {
            System.out.println("Exception caught: "+e.getMessage());
        } finally {

        }
    }


    public static void main(String[] args) throws IOException {

        int n;
        if (args.length == 0){
            n = 9;
        }
        else if (!(Integer.valueOf(args[0])%3 == 0)){
            System.out.println("value must be a multiple of 3");
            return;
        } else {
            n = Integer.valueOf(args[0]);
        }

        int[] map = generateMap(n, n);
        Graph g = createGraph(map);
        Edge[] mst = g.KruskalMST();
        map = createMap(mst, map, n);

        for (int i = 0; i < n*n; i++){
            System.out.print(map[i] + " ");
            if (i%n == n-1){
                System.out.println();
            }
        }

        writeMap(map, n);

    }
}
