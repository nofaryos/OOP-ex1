package ex1.src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, Serializable {

    /**
     * This class implements the interfaces of weighted_graph and Serializable,
     * It is designed to create an undirectional weighted graph that can be stored on a hard disk.
     *The creation of the graph requires the use of vertex-type objects,
     * this object is realized as an internal private class in this class.
      */


    private class NodeInfo implements node_info, Comparable<node_info>, Serializable{

        /**
         * This class implements the interface of node_info.
         * NodeInfo is a vertex in  an undirectional weighted graph.
         * Each vertex has an identity number(unique key), color(info) and a distance(tag)
         * that represents it in a particular graph
         */

        private int key;
        private String info;
        private double tag;

        /**
         * Constructor to create a new NodeInfo:
         * @param key
         */

        public NodeInfo(int key){
            this.key = key;
            this.info = " ";
            this.tag = 0;
        }

        /**
         * A copy constructor.
         * @param n
         */

        public NodeInfo(node_info n){
            this.key = n.getKey();
            this.info = n.getInfo();
            this.tag = n.getTag();
        }

        /**
         * This function return the unique key (id) associated with this node.
         * @return  key
         */

        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * This function return the info associated with this node.
         * @return info
         */

        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * This function changing the info associated with this node.
         * @param s
         */

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * This function return the tag associated with this node.
         * @return tag
         */

        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * This function changing the info associated with this node.
         * @param t
         */

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * This function compares between the tags of two nodes.
         * @param n
         * @return  true/false
         */

        @Override
        public int compareTo(node_info n) {
            int ans = 0;
            //if the tag of this node info is bigger return 1
            if (this.getTag()-n.getTag() > 0){
                ans = 1;
            }
            //if the tag of this node info is smaller return -1
            else if (this.getTag()-n.getTag() < 0){
                ans = -1;
            }
            //if the tag of this node info equals to the tag of node_info n return 0
            return ans;
        }

        /**
         * This function returns true if two nodes are equal to each other.
         *Two nodes are equal to each other if they have the same key,
         * the same info and the same tag.
         * @param obj object
         * @return true/false
         */

        @Override
        public boolean equals(Object obj) {
            //Check if it is an object of type NodeInfo.
            if(!(obj instanceof NodeInfo)){
                return false;
            }
            node_info i = (NodeInfo)obj;
            if (i==this){
                return true;
            }
            //Comparison of the keys of the nodes
            if (this.key != i.getKey()){
                return false;
            }
            //Comparison of the tags of the nodes
            if (this.tag != i.getTag()){
                return false;
            }
            //Comparison of the info of the nodes
            return this.getInfo().equals(i.getInfo());
        }

        /**
         * Print function
         * @return String
         */

        @Override
        public String toString() {
            return "Key: " +  this.getKey() + ",  Tag: " +this.getTag() + ", Info: " + this.getInfo();
        }
    }

    private int MC;
    private int edgesNum;
    //The table of edges, each node has a table of edges that connect to it.
    private HashMap<Integer, HashMap<Integer, Double>>edges;
    //The table of the nodes, the keys in the table are the key of each node.
    private HashMap<Integer, node_info> nodes;

    /**
     * Constructor to create a new WGraph_DS:
     */

    public WGraph_DS(){
        edges = new HashMap<Integer, HashMap<Integer, Double>>();
        nodes = new HashMap<Integer, node_info>();
        this.MC = 0;
        this.edgesNum = 0;
    }

    /**
     * A copy constructor.
     * @param  g- weighted_graph
     */

    public WGraph_DS(weighted_graph g){
        this.edges = new HashMap<Integer, HashMap<Integer, Double>>();
        this.nodes = new HashMap<Integer, node_info>();

        //Copy the nodes of the graph.
        for (node_info node: g.getV()) {
            this.nodes.put(node.getKey(),new NodeInfo(node));
            this.edges.put(node.getKey(),new HashMap<Integer, Double>());
            //Connecting each node to all its neighbors.
            for (node_info N:g.getV(node.getKey())){
                this.edges.get(node.getKey()).put(N.getKey(),g.getEdge(node.getKey(),N.getKey()));
            }
        }
        this.MC = g.getMC();
        this.edgesNum = g.edgeSize();
    }

    /**
     * This function return the node_data by getting the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */

    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    /**
     * This function return true if there is an edge between node1 and node2.
     * @param node1
     * @param node2
     * @return true or false
     */

    @Override
    public boolean hasEdge(int node1, int node2) {
        if ((getNode(node1) != null) && node1 != node2){
            return  (edges.get(node1).containsKey(node2));
        }
        return false;
    }

    /**
     *This function return the weight of the edge between node1 and node2(If there is an edge between them).
     * if there is no such edge - it returns -1.
     * @param node1
     * @param node2
     * @return weight.
     */

    @Override
    public double getEdge(int node1, int node2) {
        //Check if there is an edge between the two nodes.
        if (hasEdge(node1,node2)){
            //If there is an edge, return the weight of the edge.
            return edges.get(node1).get(node2);
        }
        //There is no edge between the nodes.
        return -1;
    }

    /**
     *This function add a new node to the graph with the given key.
     * if there is already a node with such a key, no action would be performed.
     * @param key
     */

    @Override
    public void addNode(int key) {
        if (!(nodes.containsKey(key))) {
            nodes.put(key, new NodeInfo(key));
            edges.put(key,new HashMap<Integer, Double>());
            MC++;
        }
    }

    /**
     * This function connect an edge between node1 and node2, with an edge with weight >=0.
     * if the edge node1-node2 already exists - the method updates the weight of the edge.
     * @param node1
     * @param node2
     * @param w- weight
     */

    @Override
    public void connect(int node1, int node2, double w) {
        if (0 <= w) {
            //Check that the two nodes are contained in the graph and that they are different from each other.
            if (getNode(node1) != null && getNode(node2) != null && node1 != node2) {
                //If there is already a edge connected between them with weight w there is no need for any action.
                if (hasEdge(node1,node2) && getEdge(node1,node2) == w){
                    return;
                }
                //If there is no edge between them we will increase the number of edges
                // in the graph by one.
                if (!(hasEdge(node1, node2))) {
                    edgesNum++;
                }
                //Creating a edge and updating the weight/just updating the weight(In cases where there is already an edge
                // between the nodes).
                edges.get(node1).put(node2, w);
                edges.get(node2).put(node1, w);
                MC++;
            }
        }
    }

    /**
     * This function return a Collection that
     * representing all the nodes in the graph.
     * @return Collection<node_data>
     */

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    /**
     * This function returns a Collection containing all the
     * nodes connected to node_id
     * @param  node_id
     * @return Collection<node_data>
     */

    @Override
    public Collection<node_info> getV(int node_id) {
        //Create an array for all the node's neighbors.
        ArrayList<node_info> array = new ArrayList<node_info>();
        for (Integer key: edges.get(node_id).keySet()) {
            array.add(getNode(key));
        }
        return array;
    }

    /**
     * This function delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @param key
     * @return the data of the removed node (null if none).
     */

    @Override
    public node_info removeNode(int key) {
        //Check if the node is in the graph.
        if (nodes.containsKey(key)){
            //Delete all the edges of the graph that are connected to the node.
                for (node_info node : this.getV(key)){
                    removeEdge(node.getKey(),key);
                }
             //Remove the node from the edges collection
            edges.remove(key);
            MC++;
            //Remove the node from the nodes collection and return it.
            return nodes.remove(key);
        }
        return null;
    }

    /**
     * This function delete an edge from the graph.
     * @param node1
     * @param node2
     */

    @Override
    public void removeEdge(int node1, int node2) {
        //Check if there is an edge between the nodes.
        if (hasEdge(node1,node2)){
            edges.get(node1).remove(node2);
            edges.get(node2).remove(node1);
            MC++;
            edgesNum--;
        }
    }

    /**
     * This function return the number of vertices (nodes) in the graph.
     * @return number of nodes.
     */

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * This function return the number of edges in the graph.
     * @return number of edges.
     */

    @Override
    public int edgeSize() {
        return edgesNum;
    }

    /**
     * This function return the Mode Count.
     * Any change in the inner state of the graph is cause an increment in the ModeCount.
     * @return MC
     */

    @Override
    public int getMC() {
        return this.MC;
    }

    /**
     * This function returns true if two graphs are equal to each other.
     *Two undirectional weighted graphs are equal to each other if they have the same number of edges,
     * the same weight for each edge, the same number of nodes and
     * if they contain the same nodes.
     * @param obj
     * @return true/false
     */

    @Override
    public boolean equals(Object obj) {
        //Check if it is an object of type WGraph_DS.
        if(!(obj instanceof WGraph_DS)){
            return false;
        }
        if (obj==this){
            return true;
        }

        WGraph_DS g = (WGraph_DS) obj;

        //Check that both graphs have the same number of nodes and edges.
        if ((g.nodeSize() != this.nodeSize() || (g.edgeSize() != this.edgesNum))){
            return false;
        }
        boolean ans = false;
        for (node_info n:this.getV()) {
            if (!(g.getNode(n.getKey()).equals(n))) {
                return false;
            }
            for (node_info nodeT:this.getV(n.getKey())) {
                //Check that the edges weights in the two graphs are the same
                if (this.getEdge(nodeT.getKey(),n.getKey())!=g.getEdge(nodeT.getKey(),n.getKey())){
                    return false;
                }
                //Check that each graph has the same neighbors.
                for (node_info nodeO: g.getV(n.getKey())){
                    if (nodeT.equals(nodeO)){
                        ans = true;
                        break;
                    }
                }
                if (!ans){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Print function
     * @return String
     */

    @Override
    public String toString() {
        StringBuilder S = new StringBuilder();
        S.append("Num of nodes: " + this.nodeSize() + "\n");
        S.append("Number of edegs: " + this.edgeSize()+ "\n");
        S.append("Nodes: \n");
        Collection<node_info> nodes = this.getV();
        for (node_info n: nodes) {
            S.append(n.toString());
            S.append("\n");
        }
        S.append("\nEdges: \n");
        for (Integer K: edges.keySet()) {
            S.append("key of the node:" + K+"\n");
            for (node_info n:this.getV(K)) {
                S.append("the neighbor:" + n.getKey());
                S.append(", The weight of the edge between them: " + this.getEdge(K, n.getKey())+"\n");
            }
            S.append("\n");
        }
        return S.toString();
    }
}
