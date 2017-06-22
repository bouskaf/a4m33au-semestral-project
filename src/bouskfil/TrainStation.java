package bouskfil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 21.06.17.
 */
public class TrainStation {

    private HashMap<String, Node> map;
    private String name;
    private ArrayList<Node> ins;
    private ArrayList<Node> outs;
    private ArrayList<Node> crossings;

    public TrainStation(String name, HashMap<String, Node> map) {
        this.name = name;
        this.map = map;
        this.ins = new ArrayList<>();
        this.outs = new ArrayList<>();
        this.crossings = new ArrayList<>();
        generateInsOutsCrossings();
    }

    private void generateInsOutsCrossings(){
        for (Node node : this.map.values()) {
            if (node.getPred().size() == 0) {
                ins.add(node);
            } else if (node.getNext().size() == 0) {
                outs.add(node);
            } else if (node.getNext().size() > 1) {
                crossings.add(node);
            }
        }
    }

    public HashMap<String, Node> getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Node> getIns() {
        return ins;
    }

    public ArrayList<Node> getOuts() {
        return outs;
    }

    public ArrayList<Node> getCrossings() {
        return crossings;
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

    public String getName() {
        return name;
    }

    public boolean isIn() {
        return in;
    }

    public boolean isOut() {
        return out;
    }

    public boolean isCrossing() {
        return crossing;
    }
}
