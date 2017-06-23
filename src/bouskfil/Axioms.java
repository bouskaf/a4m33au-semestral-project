package bouskfil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    }

    public void createAxioms() {
       // printLinearOrder();
       // printPhysicalRestrictions();
       // printPhysicalStructure();
         printDomainRestrictions();

    }

    private void printLinearOrder() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%----------------------------------------- Linear order ------------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // antisymmetry
        sb.append("fof(antisymmetry, axiom, (![X, Y]: ((less(X, Y) & less(Y, X)) => (X = Y)))).\n\n");

        // transitivity
        sb.append("fof(transitivity, axiom, (![X, Y, Z]: ((less(X, Y) & less(Y, Z)) => less(X, Z)))).\n\n");

        // totality
        sb.append("fof(totality, axiom, (![X, Y]: (less(X, Y) | less(Y, X)))).\n\n");

        // succ
        sb.append("fof(succ, axiom, (![X]: (less(X, succ(X)) & (![Y]: (less(Y, X) | less(succ(X), Y)))))).\n\n");

        // pred
        sb.append("fof(pred,axiom, (![X]: ((pred(succ(X)) = X) & (succ(pred(X)) = X)))).\n\n");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), false));
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPhysicalRestrictions() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------- Physical restrictions of the train station -------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // Train is at certain time only in one place
        sb.append("% Train is at certain time only in one place.\n");
        sb.append("fof(at_uniq, axiom, (![T, Train, N1, N2]: ((at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)))).\n\n");

        // Train never enters occupied node
        sb.append("% Train never enters occupied node.\n");
        sb.append("fof(input_nocol, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).\n\n");

        // Two different trains do not enter the same node in the same time
        sb.append("% Two different trains do not enter the same node in the same time.\n");
        sb.append("fof(enter_uniq, axiom, (![T, Train, OtherTrain, N]: ((enter(T, Train, N) & enter(T, OtherTrain, N)) => (Train = OtherTrain)))).\n\n");

        // Train already in station can not enter it again
        sb.append("% Train already in station can not enter it again.\n");
        sb.append("fof(double_entry, axiom, (![T, Train, N, OtherN]: (at(T, Train, N) => ~enter(T, Train, OtherN)))).\n\n");

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
        sb.append("fof(train_enters, axiom, ((?[Train, N1]: ![T2, N2, T1] : ((~at(T2, Train, N2) & ~enter(T2, Train, N2)) & ((empty_node(T2, N1)) & is_input(N1)) & ~at(T1, Train, N2) & less(T1, T2)) => enter(succ(T2), Train,N1)))).\n\n");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), false));
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPhysicalStructure() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%------------------------------- Physical model of the train station -----------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");
        for (Node node : trainStation.getMap().values()) {
            sb.append("fof(at_");
            sb.append(node.getName());
            sb.append(", axiom, ![T, Train]: (\n    at(succ(T), Train, ");
            sb.append(node.getName());
            sb.append(") <=> (\n");
            if (node.isIn()) {
                sb.append("    enter(T, Train, ");
                sb.append(node.getName());
                sb.append(") | (at(T, Train, ");
                sb.append(node.getName());
                sb.append(") & ~open(T, ");
                sb.append(node.getName());
                sb.append("))\n    )\n");
            } else {
                for (int i = 0; i < node.getPred().size(); i++) {
                    Node pred = node.getPred().get(i);
                    sb.append("    (at(T, Train, ");
                    sb.append(pred.getName());
                    sb.append(")");
                    sb.append((pred.isIn()) ? " & open(T, " + pred.getName() + ")" : "");
                    sb.append((pred.isCrossing()) ? " & switch(T, " + pred.getName() + ") = " + node.getName() + ")" : "");
                    sb.append((i == node.getPred().size() - 1) ? "    \n)" : " |\n");
                }
            }
            sb.append(")).\n\n");
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), false));
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printDomainRestrictions() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%---------------------------------- Domain restrictions of symbols -------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");

        // Restriction for at values
        sb.append("fof(at_restriction, axiom, (![T, Train, N]: (at(T, Train, N) => (");
        int size = trainStation.getMap().keySet().size();
        int i = 0;
        for (String key : trainStation.getMap().keySet()) {
            i++;
            sb.append("(N = " + key + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");

        // Nodes cannot be equal
        ArrayList<String> names = new ArrayList<>(trainStation.getMap().keySet());
        sb.append("fof(distinct_nodes, axiom, (");
        size = names.size();
        for (int j = 0; j < size - 1; j++) {
            for (int k = j + 1; k < size; k++) {
                sb.append("(" + names.get(j) + " != " + names.get(k) + ")");
                sb.append(((j == size - 2) ? "" : " & "));
                //sb.append(((k == size - 1) ? "\n" : ""));
            }
        }
        sb.append(")).\n\n");

        // Restriction for occupied values
        sb.append("fof(occupied_restriction, axiom, (![T, Train, N]: (occupied(T, Train, N) => (");
        size = trainStation.getMap().keySet().size();
        i = 0;
        for (String key : trainStation.getMap().keySet()) {
            i++;
            sb.append("(N = " + key + ")" + ((i == size) ? ")" : " | "));
        }
        sb.append("))).\n\n");






        sb.append("fof(open_restr, axiom, ![T, N]: (\n");
        sb.append("    open(T, N) => (");
        size = trainStation.getIns().size();
        String next;
        for (int j = 0; j < size; j++) {
            next = trainStation.getIns().get(j).getName();
            sb.append("(N = " + next + ")" + ((j == size - 1) ? ")\n" : " | "));
        }
        sb.append(")).\n\n");


        sb.append("fof(gate_restr, axiom, ![Train]: (\n");
        sb.append("    open(T, N) => (");
        size = trainStation.getIns().size();
        for (int j = 0; j < size; j++) {
            next = trainStation.getIns().get(j).getName();
            sb.append("(gate(Train) = " + next + ")" + ((j == size - 1) ? ")\n" : " | "));
        }
        sb.append(")).\n\n");



        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), false));
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
