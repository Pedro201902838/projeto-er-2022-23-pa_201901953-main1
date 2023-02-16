
package com.brunomnsilva.smartgraph;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import pt.pa.PlacementStrategy.PlacementCustomStrat;
import pt.pa.controller.CustomContainer;
import pt.pa.Controller.NetworkController;
import pt.pa.graph.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartStylableNode;
import pt.pa.model.Network;
import pt.pa.model.Route;
import pt.pa.model.Stop;

/**
 *
 * @author brunomnsilva
 */
public class Main extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) throws FileNotFoundException {
        Network n = new Network();
        //Graph<String, String> g = build_sample_digraph();
        build_flower_graph(n);
        Graph<Stop, Route> g = n.getGraph();
        System.out.println(g);
        NetworkController controller = new NetworkController(n);


        SmartPlacementStrategy strategy = new PlacementCustomStrat();
        //SmartPlacementStrategy strategy = new SmartRandomPlacementStrategy();
        SmartGraphPanel<Stop, Route> graphView = new SmartGraphPanel<>(g, strategy);
        graphView.setStyle(" -fx-background-image: url(\"../../datasets/demo/img/dark.png\");\n" +
                " -fx-background-repeat: no-repeat;\n" +
                " -fx-background-size: 1024 768;\n" +
                " -fx-background-position: center center;");



        /*
        After creating, you can change the styling of some element.
        This can be done at any time afterwards.
        */
        if (g.numVertices() > 0) {
            //graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
        }

        /*
        Basic usage:
        Use SmartGraphDemoContainer if you want zoom capabilities and automatic layout toggling
        */
        //Scene scene = new Scene(graphView, 1024, 768);
        Scene scene = new Scene(new CustomContainer(graphView, controller), 1024, 768);


        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        /*
        IMPORTANT: Must call init() after scene is displayed so we can have width and height values
        to initially place the vertices according to the placement strategy
        */
        graphView.init();

        /*
        Bellow you can see how to attach actions for when vertices and edges are double clicked
         */
        graphView.setVertexDoubleClickAction((SmartGraphVertex<Stop> graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());

            //toggle different styling
            if( !graphVertex.removeStyleClass("myVertex") ) {
                /* for the golden vertex, this is necessary to clear the inline
                css class. Otherwise, it has priority. Test and uncomment. */
                //graphVertex.setStyle(null);

                graphVertex.addStyleClass("myVertex");
            }

            //want fun? uncomment below with automatic layout
            //g.removeVertex(graphVertex.getUnderlyingVertex());
            //graphView.update();
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            //dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");

            //graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");

            //uncomment to see edges being removed after click
            //Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            //g.removeEdge(underlyingEdge);
            //graphView.update();
        });

        /*
        Should proceed with automatic layout or keep original placement?
        If using SmartGraphDemoContainer you can toggle this in the UI
         */
        //graphView.setAutomaticLayout(true);

        /*
        Uncomment lines to test adding of new elements
         */
        //continuously_test_adding_elements(g, graphView);
        //stage.setOnCloseRequest(event -> {
        //    running = false;
        //});
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        return g;
    }

    private Graph<Stop, Route> build_flower_graph(Network n) throws FileNotFoundException {

        Graph<Stop, Route> g = new GraphImpl<>();
        n.load();
        g = n.getGraph();/**
         g.insertVertex("A");
         g.insertVertex("B");
         g.insertVertex("C");
         g.insertVertex("D");
         g.insertVertex("E");
         g.insertVertex("F");
         g.insertVertex("G");

         g.insertEdge("A", "B", "1");
         g.insertEdge("A", "C", "2");
         g.insertEdge("A", "D", "3");
         g.insertEdge("A", "E", "4");
         g.insertEdge("A", "F", "5");
         g.insertEdge("A", "G", "6");

         g.insertVertex("H");
         g.insertVertex("I");
         g.insertVertex("J");
         g.insertVertex("K");
         g.insertVertex("L");
         g.insertVertex("M");
         g.insertVertex("N");

         g.insertEdge("H", "I", "7");
         g.insertEdge("H", "J", "8");
         g.insertEdge("H", "K", "9");
         g.insertEdge("H", "L", "10");
         g.insertEdge("H", "M", "11");
         g.insertEdge("H", "N", "12");

         g.insertEdge("A", "H", "0");

         //g.insertVertex("ISOLATED");
         **/
        return g;
    }

    private static final Random random = new Random(/* seed to reproduce*/);

    private void continuously_test_adding_elements(Graph<String, String> g, SmartGraphPanel<String, String> graphView) {
        //update graph
        running = true;
        final long ITERATION_WAIT = 3000; //milliseconds

        Runnable r;
        r = () -> {
            int count = 0;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (running) {
                try {
                    Thread.sleep(ITERATION_WAIT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                //generate new vertex with 2/3 probability, else connect two
                //existing
                String id = String.format("%02d", ++count);
                if (random.nextInt(3) < 2) {
                    //add a new vertex connected to a random existing vertex
                    Vertex<String> existing = get_random_vertex(g);
                    Vertex<String> vertexID = g.insertVertex(("V" + id));
                    g.insertEdge(existing, vertexID, ("E" + id));

                    //this variant must be called to ensure the view has reflected the
                    //underlying graph before styling a node immediately after.
                    graphView.updateAndWait();

                    //color new vertices
                    SmartStylableNode stylableVertex = graphView.getStylableVertex(vertexID);
                    if(stylableVertex != null) {
                        stylableVertex.setStyle("-fx-fill: orange;");
                    }
                } else {
                    Vertex<String> existing1 = get_random_vertex(g);
                    Vertex<String> existing2 = get_random_vertex(g);
                    g.insertEdge(existing1, existing2, ("E" + id));

                    graphView.update();
                }


            }
        };

        new Thread(r).start();
    }

    private static Vertex<String> get_random_vertex(Graph<String, String> g) {

        int size = g.numVertices();
        int rand = random.nextInt(size);
        Vertex<String> existing = null;
        int i = 0;
        for (Vertex<String> v : g.vertices()) {
            existing = v;
            if (i++ == rand) {
                break;
            }
        }
        return existing;
    }
}


