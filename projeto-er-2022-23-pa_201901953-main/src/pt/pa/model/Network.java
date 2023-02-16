package pt.pa.model;

import pt.pa.graph.GraphImpl;
import pt.pa.graph.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import pt.pa.StratLoader.Loader;
import pt.pa.StratLoader.LoaderDemo;
import pt.pa.graph.*;
import pt.pa.memento.Memento;
import pt.pa.memento.Originator;

public class Network implements Originator {
    private GraphImpl<Stop, Route> graph;
    private Loader loader;

    public Network() {
        graph = new GraphImpl<>();
        loader = new LoaderDemo();
    }

    public void insertStop(Stop stop) {
        graph.insertVertex(stop);
    }

    public void insertRoute(String code1, String code2, Route route) {
        graph.insertEdge(getStopByCode(code1), getStopByCode(code2), route);
    }

    public void removeRoute(String id) {
        for (Edge<Route, Stop> e : graph.edges()) {
            if (e.element().getId().equalsIgnoreCase(id)) {
                graph.removeEdge(e);
            }
        }
    }

    public void removeRoute(Route route){
        graph.removeEdge(getEdgeByRoute(route));
    }

    public int getNoAdj(Vertex<Stop> a) {
        int count = 0;
        for (Vertex<Stop> v : graph.vertices()) {
            if (graph.areAdjacent(v, a)) {
                count++;
            }
        }
        return count;
    }

    public void insertRoute(String code1, String code2, int distance, int duration) {
        Route r = new Route(distance, duration, getStopByCode(code1), getStopByCode(code2));
    }


    public Stop getStopByCode(String code) {
        for (Vertex<Stop> r : graph.vertices()) {
            if (code.equalsIgnoreCase(r.element().getCode())) {
                return r.element();
            }
        }
        return null;
    }

    public Edge<Route, Stop> getEdgeByRoute(Route route) {
        for (Edge<Route, Stop> e : graph.edges()) {
            if (e.element().getId().equalsIgnoreCase(route.getId())) {
                return e;
            }
        }
        return null;
    }

    public int getNoStop() {
        return graph.numVertices();
    }

    public int getNoRoutes() {
        return graph.numEdges();
    }

    public void reset() {
        graph = new GraphImpl<>();
    }


    @Override
    public String toString(){
        return graph.toString();
    }

    public SortedMap<Vertex<Stop>,Integer> noAdj(){
        System.out.println("Number of Vertices: "+ graph.vertices());
        SortedMap<Vertex<Stop>, Integer> NumberAdj= new TreeMap<>((o1, o2) -> {
            int comp = Integer.compare(graph.incidentEdges(o2).size(), graph.incidentEdges(o1).size());
            return comp == 0 ? 1 : comp;
        });
        for(Vertex<Stop> vertex: graph.vertices()){
            NumberAdj.put(vertex,graph.incidentEdges(vertex).size());
        }
        return NumberAdj;
    }

    public Graph<Stop,Route> getGraph(){
        return graph;
    }

    @Override
    public Memento saveState() {
        return new MyMemento(graph);
    }

    @Override
    public void restoreState(Memento state) {
        graph= (GraphImpl<Stop, Route>) state.getGraph();
    }

    public void load() throws FileNotFoundException {

        try{

            HashMap<Stop, ArrayList<Route>> map = loader.loadAll();

            ArrayList<Route> routes = new ArrayList<>();

            for(Stop key : map.keySet()){
                insertStop(key);
                for(Route route : map.get(key)){
                    try{insertRoute(route.getStops().getKey().getCode(), route.getStops().getValue().getCode(), route);}
                    catch (InvalidVertexException e){}
                }
            }
        }catch(FileNotFoundException err){}
        System.out.println("Operation not successfull");
    }

    private class MyMemento implements Memento{

        private GraphImpl<Stop,Route> currGraph;

        public MyMemento(GraphImpl<Stop,Route> g){
            currGraph=new GraphImpl<>();
            for(Vertex<Stop> s: graph.vertices()){
                currGraph.insertVertex(s.element());
            }
            for(Edge<Route,Stop> e : graph.edges()){
                currGraph.insertEdge(e.vertices()[0],e.vertices()[1],e.element() );
            }
        }

        @Override
        public Graph<Stop, Route> getGraph() {
            return currGraph;
        }
    }


}
