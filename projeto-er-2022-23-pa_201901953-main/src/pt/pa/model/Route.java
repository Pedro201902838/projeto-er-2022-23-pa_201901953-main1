package pt.pa.model;

import javafx.util.Pair;
import java.util.Arrays;

public class Route {
    private String id;
    private int duration;
    private int distance;
    private Pair<Stop, Stop> stops;

    public Route(int distance, int duration, Stop start, Stop end) {
        this.distance = distance;
        this.duration = duration;
        stops = new Pair<>(start, end);
        id = createId(start.getCode(), end.getCode());
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public Pair<Stop, Stop> getStops() {
        return stops;
    }

    public boolean containsCity(String code) {
        return (stops.getKey().getCode() == code || stops.getValue().getCode() == code);
    }

    public static String createId(String code1, String code2) {
        String arr[] = {code1, code2};
        Arrays.sort(arr);
        return arr[0] + arr[1];
    }

    @Override
    public String toString() {
        return "ID:" + getId() + "\n" + this.distance + "KMS \n" + "Duration: " + this.duration + "m,\n" + "Stops:" +
                "First Stop: " + this.stops.getKey().getCode() + "\n Second Stop: " +
                this.stops.getValue().getCode() + "\n";
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

}