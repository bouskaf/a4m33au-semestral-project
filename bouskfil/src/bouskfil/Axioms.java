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
    private String preFolder = "output/";

    public Axioms(TrainStation trainStation, String name, String critical) {
        this.trainStation = trainStation;
        this.name = name;

        if (critical.equals("nothing")) {
            createAxioms(0);
        }

        if (critical.equals("switch")){
            createAxioms(1);
        }

        if (critical.equals("collision")) {
            createAxioms(2);
        }

        if (critical.equals("block")) {
            createAxioms(3);
        }

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


    public void createAxioms(int type) {
        printLinearOrder();
        printPhysicalRestriction();
        printDomainRestriction();
        printSwitchesRestriction();
        printMoveRestriction();
        printPathRestriction();

        switch (type) {
            case 0:
                break;
            case 1:
                printSwitchCritical();
                break;
            case 2:
                printCollisionCritical();
                break;
            case 3:
                printBlockCritical();
                break;
        }
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
        sb.append("%---------------------------- Physical restriction of the train station --------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // Train is at certain time only in one place
        sb.append("% Train is at certain time only in one place.\n");
        sb.append("fof(at_uniq, axiom, (![T, Train, N1, N2]: ((at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)))).\n\n");

        // *** Train already in station can not enter it again
        sb.append("% Train already in station can not enter it again.\n");
        //sb.append("fof(at_nondup, axiom, (![T, Train, N, OtherN]: (at(T, Train, N) => ~enter(T, Train, OtherN)))).\n\n");
        sb.append("fof(at_nondup, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).\n\n");

        // Train never enters occupied node
        sb.append("% Train never enters occupied node.\n");
        sb.append("fof(input_nocol, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).\n\n");

        // Two different trains do not enter the same node in the same time
        sb.append("% Two different trains do not enter the same node in the same time.\n");
        sb.append("fof(enter_uniq, axiom, (![T, Train, OtherTrain, N]: ((enter(T, Train, N) & enter(T, OtherTrain, N)) => (Train = OtherTrain)))).\n\n");

        // If node is empty there is no train in it
        sb.append("% If node is empty there is no train in it.\n");
        sb.append("fof(node_empty, axiom, (![T, N]: (node_empty(T, N) <=> (![Train]: (~at(T, Train, N)))))).\n\n");

        // Node is safe
        sb.append("% In time T there is no collision in node N.\n");
        sb.append("fof(node_safe, axiom, (![T, N]: ((safe(T, N)) <=> (![Train, OtherTrain]: ((at(T, Train, N) & at(T, OtherTrain, N)) => (Train = OtherTrain)))))).\n\n");

        // Node is not blocked
        sb.append("% In enter node N there never stays blocked train.\n");
        sb.append("fof(not_blocked, axiom, (![N]: (notBlocked(N) <=> (![T1, Train]: (((at(T1, Train, N) & ~open(T1, N))) => (?[T2]: (less(T1, T2) & open(T2, N)))))))).\n\n");

        // Train will leave a node in future
        sb.append("% Train will leave a node in future.\n");
        sb.append("fof(train_will_move, axiom, (![T1, Train, N]: (at(T1, Train, N) => (?[T2]: (will_move(T2, Train) & less(T1, T2)))))).\n\n");

        // Train entered the station in past
        sb.append("% Train entered the station in past.\n");
        sb.append("fof(train_entered, axiom, (![T1, Train, N1]: ((at(T1, Train, N1)) => (?[T2, N2] : (enter(T2, Train, N2) & less(T2, T1)))))).\n\n");

        // If there is train on switch node output direction has to remain the same
        //sb.append("% If there is train on switch node output direction has to remain same.\n");
        //sb.append("fof(switch_restr, axiom, (![T, N1, N2]: (((switch(T, N1) = N2) & ~node_empty(T, N1)) => (switch(succ(T), N1) = N2)))).\n\n");

        // Train moves as soon as it is possible
        sb.append("% Train moves as soon as it is possible.\n");
        sb.append("fof(train_moves, axiom, (![T, Train]: (will_move(T, Train)))).\n\n\n");


        writeToFile(sb.toString(), true);
    }

    private void printDomainRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------------- Domain restriction of symbols --------------------------------------%\n");
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

        // Only input nodes can be open
        sb.append("% Only input nodes can be open.\n");
        sb.append("fof(open_restr, axiom, (![T, N]: (open(T, N) => input(N)))).\n\n");

        // Only true for input nodes
        sb.append("% Only true for input nodes.\n");
        sb.append("fof(input_restr, axiom, (![N]: (input(N) => (");
        size = trainStation.getIns().size();
        i = 0;
        for (Node node : trainStation.getIns()) {
            i++;
            sb.append("(N = " + node.getName() + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");

        // Train can exit only in exit nodes
        sb.append("% Train can exit only in exit nodes.\n");
        sb.append("fof(gate_restr, axiom, (![Train]: (");
        size = trainStation.getOuts().size();
        i = 0;
        for (Node node : trainStation.getOuts()) {
            i++;
            sb.append("(gate(Train) = " + node.getName() + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append(")).\n\n");

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

        sb.append("\n");
        writeToFile(sb.toString(), true);
    }

    private void printSwitchesRestriction() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%--------------------------------------- Switches restriction ------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        for (Node node : trainStation.getMap().values()) {
            if (!node.isOut() && !node.isIn()) {
                sb.append("fof(switch_" + node.getName() + "_values, axiom, (![T]: (");
                int i = 0;

                for (Node succ : node.getNext()) {
                    i++;
                    sb.append("(switch(T, " + node.getName() + ") = " + succ.getName() + ")" + ((i == node.getNext().size()) ? "" : " | "));
                }
                sb.append("))).\n");
            }
        }
        sb.append("\n\n");

        for (Stack<Node> path : trainStation.getPaths()) {
            Node end = path.get(path.size() - 1);
            for (int i = 0; i < path.size() - 1; i++) {
                if (!path.get(i).isIn() ) {
                    sb.append("fof(switch_" + path.get(i).getName() + "_with_gate_" + end.getName() + ", axiom, ");
                    sb.append("(![T, Train]: ((at(T, Train, " + path.get(i).getName() + ") & (gate(Train) = " + end.getName() + "))");
                    sb.append(" => ");
                    sb.append("(switch(T, " + path.get(i).getName() + ") = " + path.get(i + 1).getName() + ")))).\n");
                }

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


        sb.append("% Possible moves for switch nodes.\n");
        for (Node node : trainStation.getCrossings()) {
            sb.append("fof(moves_" + node.getName() + ", axiom, (![T, Train]: (at(succ(T), Train, " + node.getName() + ") <=> (");
            int i = 0;
            for (Node pred : node.getPred()) {
                i++;
                sb.append("at(T, Train, " + pred.getName() + ")");
                if (i != node.getPred().size()) {
                    sb.append(" | ");
                }
            }
            sb.append(")))).\n");
        }
        sb.append("\n");

        sb.append("% Possible moves for gate nodes.\n");
        for (Node node : trainStation.getOuts()) {
            sb.append("fof(moves_" + node.getName() + ", axiom, (![T, Train]: (at(succ(T), Train, " + node.getName() + ") <=> (");
            int i = 0;
            for (Node pred : node.getPred()) {
                i++;
                sb.append("at(T, Train, " + pred.getName() + ")");
                if (i != node.getPred().size()) {
                    sb.append(" | ");
                }
            }
            sb.append(")))).\n");
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
        sb.append("% Restriction for path_free values.\n");
        sb.append("fof(path_from_to_values, axiom, (![T, Train, N1, N2]: (path_free(T, Train, N1, N2) => (");
        int i = 0;
        for (String s : list) {
            sb.append(s + (i == list.size() - 1 ? "" : " | "));
            i++;
        }
        sb.append(")))).\n\n");

        sb.append("% Open node restriction.\n");
        for (Node node : trainStation.getIns()) {
            sb.append("fof(open_" + node.getName() + ", axiom, (![T]: (open(T, " + node.getName() + ") <=> (?[Train]: (path_free(T, Train, ");
            sb.append(node.getName() + ", gate(Train))");
            sb.append("))))).\n");
        }
        sb.append("\n");

        sb.append("% No node can be occupied for given path.\n");


        for (Stack<Node> path : trainStation.getPaths()) {
            Node start = path.get(0);
            Node end = path.get(path.size() - 1);

            sb.append("fof(path_free_from_" + start.getName() + "_to_" + end.getName() + ", axiom, (![T, Train]: ");
            sb.append("(path_free(T, Train, " + start.getName() + ", " + end.getName() + ") <=> (at(T, Train, " + start.getName() + ") & ");
            sb.append("(gate(Train) = " + end.getName() + ") & ");

            i = 0;
            for (Node node : path) {
                i++;
                sb.append("node_empty(T, " + node.getName() + ")");
                if (i != path.size()) {
                    sb.append(" & ");
                }
            }
            sb.append(")))).\n");
        }

        sb.append("\n");

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

        sb.append("fof(block_critical, conjecture, (![N]: (input(N) => notBlocked(N)))).\n");

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
