package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test for WGraph_DS class.
 */


class WGraph_DSTest {


    //Function for creating a graph with v_size vertices
    public weighted_graph graph_creator(int v_size) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        return g;
    }

    @Test
     void getNode() {
        weighted_graph g = graph_creator(0);
        //Check that if you want to get a vertex in the graph without vertices you get null back
        assertNull(g.getNode(5));
        weighted_graph r = graph_creator(40);
        //Check that if you want to get a vertex that is not in the graph you get null back
        assertNull(g.getNode(50));
        //Checking for a vertex that exists in the graph
        assertNotNull(r.getNode(30));
    }


    @Test
    void hasEdge() {
        weighted_graph g = graph_creator(20);
        g.connect(4,5,1);
        g.connect(5,6,8);
        g.connect(1,0,6);
        g.connect(12,11,7);
        g.connect(8,14,5);
        g.connect(7,18,9);
        g.connect(15,0,9);
        g.connect(3,3,8);

        //Check that has no edge between a vertex and itself
        assertFalse(g.hasEdge(3,3));
        assertFalse(g.hasEdge(4,19));
        assertTrue(g.hasEdge(8,14));
        //Check that there is no edge between two vertices that are not in the graph
        assertFalse(g.hasEdge(30,40));
        assertFalse(g.hasEdge(4,60));

        g.removeEdge(4,5);
        g.removeEdge(5,6);

        //Checking for edge removal in the graph
        assertFalse(g.hasEdge(4,5));

        g.removeNode(15);
        //Check that after erasing a vertex, all the edges that were attached to it were deleted
        assertFalse(g.hasEdge(15,0));

    }

    @Test
    void getEdge() {
        weighted_graph g=graph_creator(50);
        g.connect(4,5,1);
        g.connect(5,6,8);
        g.connect(1,0,6);
        g.connect(12,11,7);
        g.connect(8,14,5);
        g.connect(7,18,9);
        g.connect(15,0,9);
        g.connect(20,30,1);
        g.connect(12,22,8);
        g.connect(45,33,6);
        g.connect(8,49,7);
        g.connect(25,35,5);
        g.connect(6,30,9);
        g.connect(26,1,9);

        assertEquals(9,g.getEdge(26,1));

        g.removeEdge(26,1);

        //Check that we given -1 when there is no such edge in the graph
        assertEquals(-1,g.getEdge(26,1));
        assertEquals(-1,g.getEdge(70,60));
        assertEquals(-1,g.getEdge(30,90));
    }

    @Test
    void addNode() {
        weighted_graph g = graph_creator(10);
        node_info r = g.getNode(5);
        g.addNode(5);
        //Check that no new vertex is created in the heap when trying
        // to add a vertex with a key that already exists in the graph
        assertSame(r,g.getNode(5));
        g.addNode(11);
        //Check that we were able to add a vertex to the graph
        assertNotNull(g.getNode(11));
        //Check that we given null when there is no such vertex in the graph
        assertNull(g.getNode(20));
    }

    @Test
    void connect() {
        weighted_graph g = graph_creator(30);
        for (int i=0; i < 30; i=i+2){
            g.connect(0,i,i+2);
        }
        //A test that there is no connection of a edge between a vertex and itself
        assertEquals(false,g.hasEdge(0,0));
        assertEquals(true,g.hasEdge(0,4));
        assertEquals(false,g.hasEdge(50,60));
        assertEquals(false,g.hasEdge(30,30));

        g.connect(0,2,-5);

        assertEquals(4,g.getEdge(0,2));

    }

    @Test
    void removeNode() {
        weighted_graph g = graph_creator(30);
            g.removeNode(5);
            g.removeNode(10);

            //Check that we were able to remove a vertex from the graph
            assertEquals(null,g.getNode(5));

    }

    @Test
    void removeEdge() {
        weighted_graph g=graph_creator(20);
        Collection<node_info > nodes = g.getV();
        for (node_info n: nodes) {
            g.connect(n.getKey(),6,n.getKey());
        }
        g.removeEdge(2,2);
        g.removeEdge(30,30);
        g.removeEdge(2,30);

        assertTrue(g.hasEdge(2, 6));
        assertTrue(g.hasEdge(0, 6));

        g.removeEdge(0,6);
        //Check that we were able to remove a edge from the graph
        assertFalse(g.hasEdge(0, 6));
    }

    @Test
    void getV(){
        weighted_graph g=graph_creator(10);
        assertEquals(0,g.getV(0).size());

    }

    @Test
    void nodeSize() {
        weighted_graph g=graph_creator(50);
        assertEquals(50,g.nodeSize());
        g.removeNode(4);
        assertEquals(49,g.nodeSize());
        g.removeNode(4);
        assertEquals(49,g.nodeSize());
        g.removeNode(100);
        assertEquals(49,g.nodeSize());
        g.removeNode(3);
        g.removeNode(67);
        assertEquals(48,g.nodeSize());
    }

    @Test
    void getMC_edgeSize() {
        weighted_graph g=graph_creator(20);
        assertEquals(0,g.edgeSize());
        assertEquals(20,g.getMC());
        for (int i = 0; i < 20 ; i++) {
            g.connect(0,i,2);
        }
        assertEquals(19,g.edgeSize());
        assertEquals(39,g.getMC());
        g.removeEdge(0,0);
        assertEquals(19,g.edgeSize());
        assertEquals(39,g.getMC());
        g.removeEdge(0,1);
        assertEquals(18,g.edgeSize());
        assertEquals(40,g.getMC());
        g.removeNode(0);
        assertEquals(0,g.edgeSize());
        assertEquals(59,g.getMC());
    }


    @Test
    public void equalTest()
    {
        weighted_graph g1= graph_creator(5);
        assertEquals(g1,g1);
        weighted_graph g2= graph_creator(5);
        assertEquals(g1,g2);
        g1.connect(0,2,3);
        assertNotEquals(g1,g2);
        g2.connect(0,2,4);
        assertNotEquals(g1,g2);

    }

    @Test
    public  void BuildRunTime()
    {
        //Check that it possible to build a graph with a million vertices and ten million sides in a reasonable run time
        assertTimeoutPreemptively(Duration.ofMillis(10000), () -> {

            weighted_graph g=graph_creator(1000000);
            for (int i = 0; i <g.nodeSize() ; i++)
                for (int k= 0; k <11 ; k++)
                    g.connect(i,k,1);

        });

    }
}