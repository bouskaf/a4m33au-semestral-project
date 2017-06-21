package bouskfil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 21.06.17.
 */
public class TrainStation {

    private Map map;
    private String name;
    private ArrayList<Node> ins;
    private ArrayList<Node> outs;
    private ArrayList<Node> crossings;

    public TrainStation(String name, Map map) {
        this.name = name;
        this.map = map;
        generateInsOutsCrossings();
    }

    private void generateInsOutsCrossings(){
        for (Object node : this.map.values()) {
            if (node.getPred().size() == 0) {
                node.setIn(true);
            }
            if (node.getNext().size() == 0) {
                node.setOut(true);
            }
            if (node.getNext().size() > 1) {
                node.setCrossing(true);
            }
        }
    }



}

class Node {
    private String name;
    private ArrayList<Node> pred;
    private ArrayList<Node> next;
    private boolean in, out, crossing;

    public Node(String name) {
        this.name = name;
        this.pred = new ArrayList<>();
        this.next = new ArrayList<>();
    }

    public void addPred(Node node) {
        this.pred.add(node);
    }

    public void addNext(Node node) {
        this.next.add(node);
    }

    public ArrayList<Node> getPred() {
        return pred;
    }

    public ArrayList<Node> getNext() {
        return next;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public void setCrossing(boolean crossing) {
        this.crossing = crossing;
    }
}
