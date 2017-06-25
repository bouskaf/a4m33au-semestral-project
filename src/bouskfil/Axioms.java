package bouskfil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by mac on 21.06.17.
 */
public class Axioms {

    private TrainStation trainStation;
    private String name;
    private String preFolder = "./output/";

    public Axioms(TrainStation trainStation, String name) {
        this.trainStation = trainStation;
        this.name = name;
        createAxioms();
    }

    private void writeToFile(String content, boolean append) {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), append));
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createAxioms() {
        printLinearOrder();
        printPhysicalRestriction();
        printDomainRestriction();
        printSwitchesRestriction();
        printMoveRestriction();
        printOccupiedRestriction();
        printPathRestriction();
        printOpenNodeRestriction();

        //printSwitchCritical();
        //printBlockCritical();
        printCollisionCritical();

    }


    private void printLinearOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%----------------------------------------- Linear order ------------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // antisymmetry
        sb.append("fof(antisymmetry, axiom, (![X, Y]: ((less(X, Y) & less(Y, X)) => (X = Y)))).\n");

        // transitivity
        sb.append("fof(transitivity, axiom, (![X, Y, Z]: ((less(X, Y) & less(Y, Z)) => less(X, Z)))).\n");

        // totality
        sb.append("fof(totality, axiom, (![X, Y]: (less(X, Y) | less(Y, X)))).\n");

        // succ
        sb.append("fof(succ, axiom, (![X]: (less(X, succ(X)) & (![Y]: (less(Y, X) | less(succ(X), Y)))))).\n");

        // pred
        sb.append("fof(pred, axiom, (![X]: ((pred(succ(X)) = X) & (succ(pred(X)) = X)))).\n\n\n");

        writeToFile(sb.toString(), false);
    }

    private void printPhysicalRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------- Physical restriction of the train station -------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // Train is at certain time only in one place
        sb.append("% Train is at certain time only in one place.\n");
        sb.append("fof(at_uniq, axiom, (![T, Train, N1, N2]: ((at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)))).\n\n");

        // Train already in station can not enter it again
        sb.append("% Train already in station can not enter it again.\n");
        sb.append("fof(at_nondup, axiom, (![T, Train, N, OtherN]: (at(T, Train, N) => ~enter(T, Train, OtherN)))).\n\n");

        // Train never enters occupied node
        sb.append("% Train never enters occupied node.\n");
        sb.append("fof(input_nocol, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).\n\n");

        // Two different trains do not enter the same node in the same time
        sb.append("% Two different trains do not enter the same node in the same time.\n");
        sb.append("fof(enter_uniq, axiom, (![T, Train, OtherTrain, N]: ((enter(T, Train, N) & enter(T, OtherTrain, N)) => (Train = OtherTrain)))).\n\n");

        // If train is in node the node is occupied
        sb.append("% If train is in node the node is occupied.\n");
        sb.append("fof(node_occupied, axiom, (![T, Train, N]: (at(T, Train, N) => node_occupied(T, Train, N)))).\n\n");

        // If node is empty there is no train in it
        sb.append("% If node is empty there is no train in it.\n");
        sb.append("fof(node_empty, axiom, (![T, N]: (node_empty(T, N) <=> (![Train]: (~at(T, Train, N)))))).\n\n");

        // Node is safe
        sb.append("% Node is safe.\n");
        sb.append("fof(node_safe, axiom, (![T, N]: ((safe(T, N)) <=> ( ![Train, OtherTrain]: ((at(T, Train, N) & at(T, OtherTrain, N)) => (Train = OtherTrain)))))).\n\n");

        // Node is not blocked
        sb.append("% Node is not blocked.\n");
        sb.append("fof(node_not_blocked, axiom, (![N]: (notBlocked(N) <=> (![T1, Train]: (((at(T1, Train, N) & ~open(T1, N))) => (?[T2]: (less(T1, T2) & open(T2, N)))))))).\n\n");

        // Train will leave a node in future
        sb.append("% Train will leave a node in future.\n");
        sb.append("fof(train_will_move, axiom, (![T1, Train, N]: (at(T1, Train, N) => (?[T2]: (will_move(T2, Train) & less(T1, T2)))))).\n\n");

        // Train entered the station in past
        sb.append("% Train entered the station in past.\n");
        sb.append("fof(train_entered, axiom, (![T1, Train, N]: ((at(T1, Train, N)) => (?[T2, I] : (enter(T2, Train, I) & less(T2, T1)))))).\n\n");

        // Node cannot be occupied by two different trains
        sb.append("% Node cannot be occupied by two different trains.\n");
        sb.append("fof(occupied_only_once, axiom, (![T, Train, OtherTrain, N]: ((occupied(T, Train, N) & occupied(T, OtherTrain, N)) => (Train = OtherTrain)))).\n\n");

        // If there is train on switch node output direction has to remain the same
        sb.append("% If there is train on switch node output direction has to remain same.\n");
        sb.append("fof(switch_restr, axiom, (![T, N1, N2]: ((~node_empty(T, N1) & (switch(T, N1) = N2)) => (switch(succ(T), N1) = N2)))).\n\n");

        // Train moves as soon as it is possible
        sb.append("% Train moves as soon as it is possible.\n");
        sb.append("fof(train_moves, axiom, (![T, Train]: (will_move(T, Train)))).\n\n");

        // Train enters the station as soon as it is possible
        sb.append("% Train enters the station as soon as it is possible.\n");
        sb.append("fof(train_enters, axiom, ((?[Train, N1]: ![T2, N2, T1] : ((~at(T2, Train, N2) & ~enter(T2, Train, N2)) & ((empty_node(T2, N1)) & is_input(N1)) & ~at(T1, Train, N2) & less(T1, T2)) => enter(succ(T2), Train,N1)))).\n\n\n");

        writeToFile(sb.toString(), true);
    }

    private void printDomainRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------------- Domain restriction of symbols -------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // Restriction for at values
        sb.append("% Restriction for at values.\n");
        sb.append("fof(at_restr, axiom, (![T, Train, N]: (at(T, Train, N) => (");
        int size = trainStation.getMap().keySet().size();
        int i = 0;
        for (String key : trainStation.getMap().keySet()) {
            i++;
            sb.append("(N = " + key + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");

        // Restriction for occupied values
        sb.append("% Restriction for occupied values.\n");
        sb.append("fof(occupied_values, axiom, (![T, Train, N]: (occupied(T, Train, N) => (");
        size = trainStation.getMap().keySet().size();
        i = 0;
        for (String key : trainStation.getMap().keySet()) {
            i++;
            sb.append("(N = " + key + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");

        // Nodes cannot be equal
        ArrayList<String> names = new ArrayList<>(trainStation.getMap().keySet());
        sb.append("% Nodes can not be equal.\n");
        sb.append("fof(distinct_nodes, axiom, (");
        size = names.size();
        for (int j = 0; j < size - 1; j++) {
            for (int k = j + 1; k < size; k++) {
                sb.append("(" + names.get(j) + " != " + names.get(k) + ")");
                sb.append(((j == size - 2) ? "" : " & "));
            }
        }
        sb.append(")).\n\n");


        // Train can enter only in input nodes
        sb.append("% Train can enter only in input nodes.\n");
        sb.append("fof(enter_values, axiom, (![T, Train, N]: (enter(T, Train, N) => (");
        size = trainStation.getIns().size();
        i = 0;
        for (Node node : trainStation.getIns()) {
            i++;
            sb.append("(N = " + node.getName() + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");


        // Only true for input nodes
        sb.append("% Only true for input nodes.\n");
        sb.append("fof(input_node, axiom, (![N]: (input_node(N) => (");
        size = trainStation.getIns().size();
        i = 0;
        for (Node node : trainStation.getIns()) {
            i++;
            sb.append("(N = " + node.getName() + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");

        // Only input nodes can be open
        sb.append("% Only input nodes can be open.\n");
        sb.append("fof(open_restriction, axiom, (![T, N]: (open(T, N) => is_input(N)))).\n\n");


        // Train can exit only in exit nodes
        sb.append("% Train can exit only in exit nodes.\n");
        sb.append("fof(gate_restr, axiom, (![Train]: (");
        size = trainStation.getOuts().size();
        i = 0;
        for (Node node : trainStation.getOuts()) {
            i++;
            sb.append("(gate(Train) = " + node.getName() + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append(")).\n\n\n");

        writeToFile(sb.toString(), true);
    }

    private void printSwitchesRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%------------------------------------ Switches values restriction --------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Node node : trainStation.getMap().values()) {
            if (!node.isOut()) {
                sb.append("fof(switch_" + node.getName() + "_value, axiom, (![T]: (");
                int i = 0;

                for (Node succ : node.getNext()) {
                    i++;
                    sb.append("(switch(T, " + node.getName() + ") = " + succ.getName() + ")" + ((i == node.getNext().size()) ? "" : " | "));
                }
                sb.append("))).\n");
            }
        }
        sb.append("\n\n");

        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%----------------------------------------- Switches settings -------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Stack<Node> path : trainStation.getPaths()) {
            Node end = path.get(path.size() - 1);
            for (int i = 0; i < path.size() - 1; i++) {
                sb.append("fof(switch_" + path.get(i).getName() + "_" + end.getName() + ", axiom, ");
                sb.append("(![T, Train]: ((occupied(T, Train, " + path.get(i).getName() + ") & (gate(Train) = " + end.getName() + "))");
                sb.append(" => ");
                sb.append("(switch(T, " + path.get(i).getName() + ") = " + path.get(i + 1).getName() + ")))).\n");
            }
            sb.append("\n");
        }
        sb.append("\n");

        writeToFile(sb.toString(), true);
    }

    private void printMoveRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%------------------------------------------ Move restriction -------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Node node : trainStation.getIns()) {
            sb.append("fof(move_input_no_change_" + node.getName() + ", axiom, (![T, Train]: (enter(T, Train, " + node.getName() + ") | (at(T, Train, ");
            sb.append(node.getName() + ") & ~open(T, " + node.getName() + "))) => at(succ(T), Train, " + node.getName() + "))).\n");

            for (Node next : node.getNext()) {
                sb.append("fof(move_include_will_input_from_" + node.getName() + "_to_" + next.getName() + ", axiom, (![T, Train]: (((at(T, Train, ");
                sb.append(node.getName() + ") & open(T, " + node.getName() + ") & will_move(T, Train)) => (at(succ(T), Train, " + next.getName() + ")))");
                sb.append(" | ((at(T, Train, " + node.getName() + ") & open(T, " + node.getName() + ") & ~will_move(T, Train)) => at(succ(T), Train, " + node.getName() + "))))).\n");
            }
        }
        sb.append("\n");

        for (Node node : trainStation.getOuts()) {
            sb.append("fof(move_out_stay_or_leave_" + node.getName() + ", axiom, (![T, Train]: (((at(T, Train, " + node.getName() + ") & ~will_move(T, Train))");
            sb.append(" => at(succ(T), Train, " + node.getName() + ")) | ((at(T, Train, " + node.getName() + ") & will_move(T, Train)) => (![N]: (~at(succ(T), Train, N))))))).\n");
        }
        sb.append("\n");

        for (Node node : trainStation.getCrossings()) {
            for (Node next : node.getNext()) {
                sb.append("fof(move_switch_include_will_from_" + node.getName() + "_to_" + next.getName() + ", axiom, (![T, Train]: (((at(T, Train, ");
                sb.append(node.getName() + ") & (switch(T, " + node.getName() + ") = " + next.getName() + ") & ~will_move(T, Train)) => (at(succ(T), Train, ");
                sb.append(node.getName() + "))) | ((at(T, Train, " + node.getName() + ") & (switch(T, " + node.getName() + ") = " + next.getName() + ") & ");
                sb.append("will_move(T, Train)) => (at(succ(T), Train, " + next.getName() + ")))))).\n");
            }
        }
        sb.append("\n\n");

        writeToFile(sb.toString(), true);

    }

    private void printOccupiedRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------------------- Occupied restriction -----------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Node node : trainStation.getMap().values()){
            if (!node.isIn()){
                sb.append("fof(" + node.getName() + "_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, " + node.getName() + ") <=> ");
                sb.append("((occupied(T, Train, " + node.getName() + ") & ~(at(T, Train, " + node.getName() + ") & ~at(succ(T), Train, " + node.getName() + ")))");

                ArrayList<String> list = new ArrayList<>();
                for (Stack<Node> path : trainStation.getPaths()) {
                    if (path.contains(node)) {
                        Node start = path.get(0);
                        Node end = path.get(path.size() - 1);
                        String string = "(at(T, Train, " + start.getName() + ") & (goal(Train) = " + end.getName() + ") & open(T, " + start.getName() + "))";
                        if (!list.contains(string)) {
                            list.add(string);
                        }
                    }
                }

                sb.append(" | ");
                int i = 0;
                for (String string : list) {
                    sb.append(string + (i == list.size() - 1 ? "" : " | "));
                    i++;
                }

                sb.append(")))).\n");
            }
        }
        sb.append("\n\n");

        writeToFile(sb.toString(), true);

    }

    private void printPathRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%------------------------------------------ Path restriction -------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        ArrayList<String> list = new ArrayList<>();
        for (Stack<Node> path : trainStation.getPaths()) {
            String string = "((N1 = " + path.get(0).getName() + ") & (N2 = " + path.get(path.size() - 1).getName() + "))";
            if (!list.contains(string)) {
                list.add(string);
            }
        }
        sb.append("% Restriction for path_open values.\n");
        sb.append("fof(open_values, axiom, (![T, Train, N1, N2]: (path_open(T, Train, N1, N2) => (");
        int i = 0;
        for (String s : list) {
            sb.append(s + (i == list.size() - 1 ? "" : " | "));
            i++;
        }
        sb.append(")))).\n\n");

        sb.append("% No node can be occupied for given path.\n");
        for (Stack<Node> path : trainStation.getPaths()) {
            Node start = path.get(0);
            Node end = path.get(path.size() - 1);

            sb.append("fof(path_open_from_" + start.getName() + "_to_" + end.getName() + ", axiom, (![T, Train, OtherTrain]: ");
            sb.append("(path_open(T, Train, " + start.getName() + ", " + end.getName() + ") <=> (at(T, Train, " + start.getName() + ") & ");
            sb.append("(gate(Train) = " + end.getName() + ") & ");

            list = new ArrayList<>();
            for (Node node : path) {
                String string = "(~occupied(T, OtherTrain, " + node.getName() + ") | (Train = OtherTrain))";
                list.add(string);
            }
            i = 0;
            for (String string : list) {
                sb.append(string + (i == list.size() - 1 ? "" : " & "));
                i++;
            }
            sb.append(")))).\n");
        }
        sb.append("\n");

        writeToFile(sb.toString(), true);
    }

    private void printOpenNodeRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------------------- Open node restriction ----------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Node node : trainStation.getIns()) {
            sb.append("fof(open_" + node.getName() + ", axiom, (![T]: (open(T, " + node.getName() + ") <=> (?[Train]: (path_open(T, Train, ");
            sb.append(node.getName() + ", gate(Train))");
        }
        sb.append("))))).\n\n\n");

        writeToFile(sb.toString(), true);
    }


    public void printSwitchCritical() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%-------------------------- Train is in switch node and the switch is changed --------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        sb.append("fof(switch_critical, conjecture, (![T, Train, N1, N2]: (((at(T, Train, N1)) & (switch(T, N1) = N2)) => ~(at(succ(T), Train, N1) & (switch(succ(T), N1) != N2))))).\n");

        writeToFile(sb.toString(), true);
    }

    public void printBlockCritical() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%------------------------------ The input node remains permanently closed ------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        sb.append("fof(block_critical, conjecture, (![N]: (is_input(N) => notBlocked(N)))).\n");

        writeToFile(sb.toString(), true);
    }

    public void printCollisionCritical() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%--------------------------- Two or more trains will arrive at the same node ----------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        sb.append("fof(collision_critical, conjecture, (![T, N]: (safe(T, N)))).\n");

        writeToFile(sb.toString(), true);
    }
}
