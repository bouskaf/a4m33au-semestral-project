package bouskfil;

import java.util.*;

/**
 * Created by mac on 21.06.17.
 */
public class TrainStation {

    private String name;

    private HashMap<String, Node> map;

    private ArrayList<Node> ins;
    private ArrayList<Node> outs;
    private ArrayList<Node> crossings;

    private ArrayList<Stack<Node>> paths;
    private Stack<Node> tempPath;
    private Set<Node> onPath;


    public TrainStation(String name, HashMap<String, Node> map) {
        this.name = name;
        this.map = map;
        this.ins = new ArrayList<>();
        this.outs = new ArrayList<>();
        this.crossings = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.tempPath = new Stack<>();
        this.onPath = new HashSet<>();
        generateInsOutsCrossings();
        generatePaths();
    }

    private void generateInsOutsCrossings(){
        for (Node node : this.map.values()) {
            if (node.getPred().size() == 0) {
                ins.add(node);
            }
            if (node.getNext().size() == 0) {
                outs.add(node);
            }
            if (node.getNext().size() > 1) {
                crossings.add(node);
            }
        }
    }

    private void generatePaths() {
        for (Node start : this.ins) {
            for (Node end : this.outs) {
                createPath(start, end);
            }
        }
    }

    private void createPath(Node start, Node end) {
        tempPath.push(start);
        onPath.add(start);
        if (start.getName().equals(end.getName())) {
            paths.add((Stack<Node>) tempPath.clone());
        } else {
            for (Node next : start.getNext()) {
                if (!onPath.contains(next)) {
                    createPath(next, end);
                }
            }
        }
        tempPath.pop();
        onPath.remove(start);
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

    public ArrayList<Stack<Node>> getPaths() {
        return paths;
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

    @Override
    public String toString() {
        return this.name;
    }
}
