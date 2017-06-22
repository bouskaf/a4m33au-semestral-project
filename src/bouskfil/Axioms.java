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
        //printLinearOrdering();
        //printPhysicalStructure();
        //printPhysicalRestrictions();
        printDomainRestrictions();

    }

    private void printLinearOrdering() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
        sb.append("%----------------------------------------- Linear order ------------------------------------------------%\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n");
        // antisymmetry
        sb.append("fof(antisymmetry, axiom, (\n");
        sb.append("    ![X, Y]: ((lesseq(X, Y) & lesseq(Y, X)) => (X = Y))\n");
        sb.append(")).\n\n");
        // transitivity
        sb.append("fof(transitivity, axiom, (\n");
        sb.append("    ![X, Y, Z]: ((lesseq(X, Y) & lesseq(Y, Z)) => lesseq(X, Z))\n");
        sb.append(")).\n\n");
        // totality
        sb.append("fof(totality, axiom, (\n");
        sb.append("    ![X, Y]: (lesseq(X, Y) | lesseq(Y, X))\n");
        sb.append(")).\n\n");
        // succ
        sb.append("fof(succ, axiom, (\n");
        sb.append("    ![X]: (lesseq(X, succ(X)) & (![Y]: (lesseq(Y, X) | lesseq(succ(X), Y))\n");
        sb.append(")).\n\n");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), true));
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
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), true));
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
        // a train is at certain time only in one place
        sb.append("% the train is at certain time only in one place\n");
        sb.append("fof(at_uniq, axiom, ![T, Train, N1, N2]: (\n");
        sb.append("    (at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)\n");
        sb.append(")).\n\n");
        // a train which is in a station does not enter it
        sb.append("% a train which is in a station does not enter it\n");
        sb.append("fof(at_nondup, axiom, ![T, Train, N, OtherN]: (\n");
        sb.append("    at(T, Train, N) => ~enter(T, Train, OtherN)\n");
        sb.append(")).\n\n");
        // a train never enters occupied node
        sb.append("% a train never enters occupied node\n");
        sb.append("fof(input_nocol, axiom, ![T, Train, OtherTrain, N]: (\n");
        sb.append("    at(T, Train, N) => ~enter(T, OtherTrain, N)\n");
        sb.append(")).\n\n");
        // two different trains do not enter the same node in the same time
        sb.append("% two different trains do not enter the same node in the same time\n");
        sb.append("fof(enter_uniq, axiom, ![T, Train1, Train2, N]: (\n");
        sb.append("    (enter(T, Train1, N) & enter(T, Train2, N)) => (Train1 = Train2)\n");
        sb.append(")).\n\n");

        // crossing restriction
        sb.append("fof(switch_restriction, axiom, ![T]: (\n");
        for (int i = 0; i < trainStation.getCrossings().size(); i++) {
            Node node = trainStation.getCrossings().get(i);
            sb.append("    (");
            for (int j = 0; j < node.getNext().size(); j++) {
                Node next = node.getNext().get(j);
                sb.append("(switch(T, " + node.getName() + ") = " + next.getName() + ")");
                sb.append((j == node.getNext().size() - 1) ? ")" : " | ");
            }
            sb.append((i == trainStation.getCrossings().size()) ? "\n" : "&\n");
        }
        sb.append(")).\n\n");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), true));
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
        sb.append("fof(at_restr, axiom, ![T, Train, N]: (\n");
        sb.append("    at(T, Train, N) => (");

        int size = trainStation.getMap().keySet().size();
        int i = 0;
        for (String key : trainStation.getMap().keySet()) {
            i++;
            sb.append("(N = " + key + ")" + ((i == size) ? ")\n" : " | "));
        }
        sb.append(")).\n\n");


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

        ArrayList<String> names = new ArrayList<>(trainStation.getMap().keySet());
        sb.append("fof(distinct_nodes, axiom, (\n");
        size = names.size();
        for (int j = 0; j < size - 1; j++) {
            sb.append("    ");
            for (int k = j + 1; k < size; k++) {
                sb.append("(" + names.get(j) + " != " + names.get(k) + ")");
                sb.append(((j == size - 2) ? "" : " & "));
                sb.append(((k == size - 1) ? "\n" : ""));
            }
        }
        sb.append(")).\n\n");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(preFolder + name), true));
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
