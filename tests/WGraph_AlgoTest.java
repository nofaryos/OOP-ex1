package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test for WGraph_Algo class.
 */

class WGraph_AlgoTest {

    public weighted_graph graph_creator(int v_size) {
        weighted_graph g = new WGraph_DS();

        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        return g;
    }

    @Test
    void init() {
        weighted_graph g=graph_creator(30);
        for (int i=0; i < g.nodeSize(); i++){
            g.connect(i,1,i);
        }
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g);
        assertEquals(g.nodeSize(),ga.getGraph().nodeSize());
        assertSame(g,ga.getGraph());
        assertEquals(g,ga.getGraph());
        g.addNode(40);
        assertNotNull(ga.getGraph().getNode(40));
    }


    @Test
    void copy_RunTime(){
        weighted_graph g=graph_creator(1000000);
        for (int i = 0; i <100000 ; i++)
            for (int k= 0; k <11 ; k++)
                g.connect(i,k,1);
        WGraph_Algo graph = new WGraph_Algo();
        graph.init(g);

        //Check that it possible to copy a graph with a million vertices and a million sides in a reasonable run
        assertTimeoutPreemptively(Duration.ofMillis(100000), () -> {
            weighted_graph graph_copy = graph.copy();
            System.out.println(graph_copy.edgeSize());

        });
    }

    @Test
    void copy() {
        weighted_graph g = graph_creator(15);
        g.connect(4,10,2);
        g.connect(6,7,2);
        g.connect(11,3,3);
        g.connect(1,14,3);
        WGraph_Algo graph = new WGraph_Algo();
        graph.init(g);
        weighted_graph graph_copy = graph.copy();

        assertEquals(graph_copy,graph.getGraph());
        assertEquals(graph_copy,g);
        assertEquals(g.getMC(),graph_copy.getMC());
        assertEquals(g.edgeSize(), graph_copy.edgeSize());
        assertEquals(g.nodeSize(), graph_copy.nodeSize());

        assertEquals(g.getV(5).size(), graph_copy.getV(5).size());
        g.addNode(20);
        graph_copy.addNode(40);
        assertNull(graph_copy.getNode(20));
        assertNotNull(graph_copy.getNode(40));
        assertNotNull(g.getNode(20));
        assertNull(g.getNode(40));
    }



    @Test
    void isConnected1() {
        weighted_graph g=graph_creator(40);
        weighted_graph_algorithms ga= new WGraph_Algo(g);
        for (int i=0; i< ga.getGraph().nodeSize();i++){
            ga.getGraph().connect(0,i,0);
        }
        assertTrue(ga.isConnected());
        ga.getGraph().removeNode(0);
        //Check that after removing a vertex to which edges were attached the graph is no longer connected.
        assertFalse(ga.isConnected());
        for (int i = 1 ; i < 40 ; i++){
            ga.getGraph().removeNode(i);
        }
        //A test that graphed without vertices and without edges is connected
        assertTrue(ga.isConnected());
    }

    @Test
    void isConnected2() {
        weighted_graph g=graph_creator(100);
        weighted_graph_algorithms ga= new WGraph_Algo(g);
        for (int i=0; i< ga.getGraph().nodeSize();i++){
            ga.getGraph().connect(0,i,0);
        }
        assertTrue(ga.isConnected());
        g.removeEdge(0,2);
        assertFalse(ga.isConnected());
        g.connect(0,2,0);
        //A test that connected graph, after removing a edge and adding the same edge that we remove, returned to being connected
        assertTrue(ga.isConnected());

    }

    @Test
    void shortestPathDist() {
        weighted_graph g=graph_creator(6);
        weighted_graph_algorithms ga= new WGraph_Algo(g);
        g.connect(0,1,0.5);
        g.connect(1,2,0.5);
        g.connect(0,2,1.5);
        g.connect(0,3,4);
        g.connect(3,4,6.5);
        assertEquals(1,ga.shortestPathDist(0,2));
        assertEquals(0,ga.shortestPathDist(0,0));
        assertEquals(4.5,ga.shortestPathDist(1,3));
        assertEquals(-1,ga.shortestPathDist(0,5));
        assertEquals(-1,ga.shortestPathDist(1,7));
        assertEquals(-1,ga.shortestPathDist(8,9));
    }

    @Test
    void shortestPath1() {
        weighted_graph g=graph_creator(4);
        weighted_graph_algorithms ga= new WGraph_Algo(g);
        g.connect(0,1,0.5);
        g.connect(1,2,0.5);
        g.connect(0,2,1.5);
        List<node_info> pathResult= ga.shortestPath(0,2);
        List<node_info> pathCheck= new LinkedList<node_info>();
        pathCheck.add(g.getNode(0));
        pathCheck.add(g.getNode(1));
        pathCheck.add(g.getNode(2));
        assertEquals(pathCheck.size(),pathResult.size());
        for (int i = 0; i < pathCheck.size() ; i++) {
            assertEquals(pathCheck.get(i),pathResult.get(i));
        }
        //Check that we gets null when we trying to get a path that is not in the graph
        assertNull(ga.shortestPath(8,9));
        assertNull(ga.shortestPath(0,10));
    }
    @Test
    void shortestPath2() {
        weighted_graph g = graph_creator(6);
        weighted_graph_algorithms ga = new WGraph_Algo(g);
        for (int i = 0; i < g.nodeSize()-1; i++) {
            g.connect(i,i+1,0);
        }
        assertEquals(1,ga.shortestPath(0,0).size());
        List<node_info> R= ga.shortestPath(0,5);
        List<node_info> C= new LinkedList<node_info>();
        C.add(g.getNode(0));
        C.add(g.getNode(1));
        C.add(g.getNode(2));
        C.add(g.getNode(3));
        C.add(g.getNode(4));
        C.add(g.getNode(5));
        assertEquals(C,R);
        assertEquals(0,ga.shortestPathDist(0,5));
        g.removeNode(5);
        assertNull(ga.shortestPath(0,5));

    }

    @Test
    void save_load() throws IOException {
        weighted_graph g = graph_creator(50);
        weighted_graph_algorithms graph1 = new WGraph_Algo(g);
        g.connect(10,20,0);
        g.connect(1,3,5);
        g.connect(4,5,6);
        g.connect(7,8,9);
        assertTrue(graph1.save("graph"));

        weighted_graph_algorithms graph2 = new WGraph_Algo(g);
        assertTrue(graph2.load("graph"));
        assertEquals(graph1.getGraph(), graph2.getGraph());
        g.removeNode(0);
        assertNotEquals(graph1.getGraph(), graph2.getGraph());
    }
}