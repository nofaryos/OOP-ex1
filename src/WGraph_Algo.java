package ex1.src;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {

    /**
     * This class implements the interface of weighted_graph_algorithms.
     *The class includes a number of algorithms that run on undirectional weighted graphs.
     */


    private weighted_graph gr;
    //This field is intended to keep parent nodes in the lowest weight path between two nodes in the graph.
    private HashMap<Integer,Integer> pred = new HashMap<Integer, Integer>();

    /**
     * Constructor to create a new WGraph_Algo:
     */

    public WGraph_Algo(){
        this.gr = new WGraph_DS();
    }

    /**
     * A copy constructor(Shallow copy).
     * @param ga-weighted_graph.
     */

    public WGraph_Algo(weighted_graph ga){
        this.gr = ga;
    }

    /**
     * This function init the graph on which this set of algorithms operates on.
     * @param g- weighted_graph.
     */

    @Override
    public void init(weighted_graph g) {
        this.gr = g;
    }

    /**
     * This function return the underlying graph of which WGraph_Algo works.
     * @return this weighted_graph gr.
     */

    @Override
    public weighted_graph getGraph() {
        return this.gr;
    }

    /**
     * This function compute a deep copy of this weighted graph.
     * @return this weighted_graph gr.
     */

    @Override
    public weighted_graph copy() {
        weighted_graph gr = new WGraph_DS(this.gr);
        return gr;
    }

    /**
     * Dijkstra algorithm return true if this weighted_graph is connected,
     * it is scanning this graph using the tag and info of each node in the graph,
     * start from the vertex identified with some key.
     *Using this algorithm it is possible to determine whether the graph is connected -If the graph has a vertex whose tag
     * is infinite or whose info is white, it means that the graph is not a connected.
     * In addition, path weight between nodes in the graph can be found using this algorithm.
     * @param key
     * @return true/false
     */

    public boolean Dijkstra(int key) {
        PriorityQueue<node_info> PQ = new PriorityQueue<node_info>();
        //Update the weight(tag) of each vertex to infinity and update the color(info) of all the nodes to white.
        //White node - means we have not visited it yet.
        for (node_info n : gr.getV()) {
                    n.setTag(Integer.MAX_VALUE);
                    n.setInfo("White");
        }

        //Update the weight of the node from which we will start scanning the graph to 0.
        gr.getNode(key).setTag(0);
        PQ.add(gr.getNode(key));

        //A loop that goes through all the vertices in the graph.
        while (PQ.size() != 0){
            node_info u = PQ.poll();
            //Black node - means we have updated the minimum weight of the node
            if (!(u.getInfo().equals("Black"))) {
                for (node_info ni : gr.getV(u.getKey())) {
                    if (!(ni.getInfo().equals("Black"))) {
                        double t = gr.getEdge(ni.getKey(), u.getKey()) + u.getTag();
                        //Update the min weight of the neighbors of node u.
                        if (ni.getTag() > t) {
                            ni.setTag(Math.min(t, ni.getTag()));
                            //Update the parent node of node ni.
                            pred.put(ni.getKey(), u.getKey());
                        }
                        PQ.add(ni);
                    }
                }
            }
            u.setInfo("Black");
        }
        //Check if the graph is connected-
        //check if there are any nodes that we did not reach (white nodes)
            for (node_info node : gr.getV()) {
                if (node.getInfo().equals("White")) {
                    return false;
                }
            }
            return true;
    }

    /**
     *This function returns true if there is a valid path from EVREY node to each other node.
     * @return true/false.
     */

    @Override
    public boolean isConnected() {
        //An empty graph or a graph that contains a single node is connected
        if (gr.nodeSize() == 0 || gr.nodeSize() == 1) {
            return true;
        }
        int key = 0;
        //Finding a node in the graph- when we know the graph is not empty
        for (node_info node:gr.getV()) {
            key = node.getKey();
            break;
        }
        //Run the Dijkstra algorithm to check if the graph is connected
        return Dijkstra(key);
    }

    /**
     *This function finds the path with the lowest weight between src to dest.
     * @param src- start node
     * @param  dest- end node
     * @return the weight of the path, returns -1 if no such path.
     */

    @Override
    public double shortestPathDist(int src, int dest) {
        if (gr.getNode(src) != null && gr.getNode(dest) != null){
            //The weight of the path of a node to itself is 0.
            if (src == dest){
                return 0;
            }
            Dijkstra(src);
            //If the weight of dest different from infinitely,
            // we will return the weight of the path from the node from which we scanned the graph(src) to dest
            if (gr.getNode(dest).getTag() != Integer.MAX_VALUE){
                return gr.getNode(dest).getTag();
            }
        }
        //If there is no path between the two vertices we will return -1
        return -1;
    }

    /**
     *This function returns the path with the lowest weight between src to dest - as a list of nodes:
     * @param src- start node
     * @param dest- end node
     * @return List<node_data>
     */

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        ArrayList<node_info> array = new ArrayList<node_info>();
        //The weight of the path between src to dest.
        double dis = shortestPathDist(dest, src);
        //There is no path between the two nodes.
        if (dis == -1) {
            return null;
        }
       // A path between a node node itself
        if (dis == 0 && src == dest){
            array.add(gr.getNode(src));
            return array;
        }
        node_info begin = gr.getNode(src);
        array.add(gr.getNode(src));
        //A loop that finds the nodes in the path according to the parent nodes.
        while (!(begin.equals(gr.getNode(dest)))){
            int key = this.pred.get(begin.getKey());
            array.add(gr.getNode(key));
            begin = gr.getNode(key);
        }
        return array;
    }

    /**
     * This function saves this gr weighted undirected graph to the given
     * file name
     * @param file - the file name.
     * @return true - if the file was successfully saved
     */

    @Override
    public boolean save(String file)  {
        boolean ans = false;
        //Creating output stream
        ObjectOutputStream save;
        try {
            FileOutputStream saveFile = new FileOutputStream(file);
            save = new ObjectOutputStream(saveFile);
            //Writing the object
            save.writeObject(this.getGraph());
            save.close();
            saveFile.close();
            //We were able to save the file
            ans = true;
        }
        //Error exception in cases where we could not save the file
        catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * This function load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph would remain "as is".
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     */

    @Override
    public boolean load(String file)  {
        boolean ans = false;
        //Creating input stream
        ObjectInputStream load;

        try {
            FileInputStream loadFile = new FileInputStream(file);
            load = new ObjectInputStream(loadFile);
            //reading the new object
            this.gr = (weighted_graph) load.readObject();
            load.close();
            loadFile.close();
            //We were able to load the file
            ans = true;
        }
        //Error exception in cases where we could not load the file
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * Print function
     * @return String
     */

    public String toString(){
        return this.gr.toString();
    }
}

