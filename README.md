Readme: ex1

NAME: NOFAR YOSEF
ID:20858376


This project is designed to model data structures and algorithms on undirectional weighted graphs.
the project consists of three different classes:

1. NodeInfo class: This class is an internal class in WGraph_DS class and is designed to create a vertex in the graph.
The class implements the interface of nide_info.
Each node in the graph has a unique key, tag and info,
the last two used to define properties of the node through which it will be possible to check whether the graph is connected and in addition to calculate path weights in the graph.

2.  WGraph_DS class: this class implements the interface of weighted graph.
each graph has a 2 collections in the form of hash map- one for keeping the vertices graph and the other for keeping the edges and their weights.
changes in the graph such as: add/remove a vertex/edge, checking if there is a edge between two vertices and more can be made in this class.
Hash Map:
In the hash map data structure, each member in the collection has a unique key, in this way i can access the member, add an member or delete an member with an O(1).
Hence, I chose this data structure so that graph changes would be made quickly, even when it comes to a graph with lots of vertices.

3. WGraph_Algo class: this class implements the interface of weighted_graph_algorithms,
this class implements algorithms that can be run on the graph, among others, the class includes the following algorithms : 
checking whether the graph is connected, finding the shortest path between two vertices , deep copying of a graph, saving and loading the graph.
In this class i used Dijkstra's algorithm to check whether the graph is connected and in addition, to calculate path weights in the graph .
This algorithm scans the graph using the properties of each vertex, at each stage of the algorithm each vertex is marked by which it is possible to know whether the algorithm has already visited 
a particular node in the graph and in addition, to know the minimum weight of the path between that vertex and the start vertex.




