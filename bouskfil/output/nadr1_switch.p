%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%----------------------------------------- Linear order ------------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(antisymmetry, axiom, (![X, Y]: ((less(X, Y) & less(Y, X)) => (X = Y)))).
fof(transitivity, axiom, (![X, Y, Z]: ((less(X, Y) & less(Y, Z)) => less(X, Z)))).
fof(totality, axiom, (![X, Y]: (less(X, Y) | less(Y, X)))).
fof(succ, axiom, (![X]: (less(X, succ(X)) & (![Y]: (less(Y, X) | less(succ(X), Y)))))).
fof(pred, axiom, (![X]: ((pred(succ(X)) = X) & (succ(pred(X)) = X)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------- Physical restriction of the train station --------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Train is at certain time only in one place.
fof(at_uniq, axiom, (![T, Train, N1, N2]: ((at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)))).

% Train already in station can not enter it again.
fof(at_nondup, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).

% Train never enters occupied node.
fof(input_nocol, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).

% Two different trains do not enter the same node in the same time.
fof(enter_uniq, axiom, (![T, Train, OtherTrain, N]: ((enter(T, Train, N) & enter(T, OtherTrain, N)) => (Train = OtherTrain)))).

% If node is empty there is no train in it.
fof(node_empty, axiom, (![T, N]: (node_empty(T, N) <=> (![Train]: (~at(T, Train, N)))))).

% In time T there is no collision in node N.
fof(node_safe, axiom, (![T, N]: ((safe(T, N)) <=> (![Train, OtherTrain]: ((at(T, Train, N) & at(T, OtherTrain, N)) => (Train = OtherTrain)))))).

% In enter node N there never stays blocked train.
fof(not_blocked, axiom, (![N]: (notBlocked(N) <=> (![T1, Train]: (((at(T1, Train, N) & ~open(T1, N))) => (?[T2]: (less(T1, T2) & open(T2, N)))))))).

% Train will leave a node in future.
fof(train_will_move, axiom, (![T1, Train, N]: (at(T1, Train, N) => (?[T2]: (will_move(T2, Train) & less(T1, T2)))))).

% Train entered the station in past.
fof(train_entered, axiom, (![T1, Train, N1]: ((at(T1, Train, N1)) => (?[T2, N2] : (enter(T2, Train, N2) & less(T2, T1)))))).

% Train moves as soon as it is possible.
fof(train_moves, axiom, (![T, Train]: (will_move(T, Train)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------- Domain restriction of symbols --------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for at values.
fof(at_restr, axiom, (![T, Train, N]: (at(T, Train, N) => ((N = out2) | (N = out1) | (N = in2) | (N = in1) | (N = s1) | (N = s2))))).

% Only input nodes can be open.
fof(open_restr, axiom, (![T, N]: (open(T, N) => input(N)))).

% Only true for input nodes.
fof(input_restr, axiom, (![N]: (input(N) => ((N = in2) | (N = in1))))).

% Train can exit only in exit nodes.
fof(gate_restr, axiom, (![Train]: ((gate(Train) = out2) | (gate(Train) = out1)))).

% Nodes can not be equal.
fof(distinct_nodes, axiom, ((out2 != out1) & (out2 != in2) & (out2 != in1) & (out2 != s1) & (out2 != s2) & (out1 != in2) & (out1 != in1) & (out1 != s1) & (out1 != s2) & (in2 != in1) & (in2 != s1) & (in2 != s2) & (in1 != s1) & (in1 != s2) & (s1 != s2))).

% Train can enter only in input nodes.
fof(enter_values, axiom, (![T, Train, N]: (enter(T, Train, N) => ((N = in2) | (N = in1))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%--------------------------------------- Switches restriction ------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_s1_values, axiom, (![T]: ((switch(T, s1) = out1) | (switch(T, s1) = out2)))).
fof(switch_s2_values, axiom, (![T]: ((switch(T, s2) = out1) | (switch(T, s2) = out2)))).


fof(switch_s2_with_gate_out2, axiom, (![T, Train]: ((at(T, Train, s2) & (gate(Train) = out2)) => (switch(T, s2) = out2)))).

fof(switch_s2_with_gate_out1, axiom, (![T, Train]: ((at(T, Train, s2) & (gate(Train) = out1)) => (switch(T, s2) = out1)))).

fof(switch_s1_with_gate_out2, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out2)) => (switch(T, s1) = out2)))).

fof(switch_s1_with_gate_out1, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out1)) => (switch(T, s1) = out1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Move restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Possible moves for switch nodes.
fof(moves_s1, axiom, (![T, Train]: (at(succ(T), Train, s1) <=> (at(T, Train, in1))))).
fof(moves_s2, axiom, (![T, Train]: (at(succ(T), Train, s2) <=> (at(T, Train, in2))))).

% Possible moves for gate nodes.
fof(moves_out2, axiom, (![T, Train]: (at(succ(T), Train, out2) <=> (at(T, Train, s1) | at(T, Train, s2))))).
fof(moves_out1, axiom, (![T, Train]: (at(succ(T), Train, out1) <=> (at(T, Train, s1) | at(T, Train, s2))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Path restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for path_free values.
fof(path_from_to_values, axiom, (![T, Train, N1, N2]: (path_free(T, Train, N1, N2) => (((N1 = in2) & (N2 = out2)) | ((N1 = in2) & (N2 = out1)) | ((N1 = in1) & (N2 = out2)) | ((N1 = in1) & (N2 = out1)))))).

% Open node restriction.
fof(open_in2, axiom, (![T]: (open(T, in2) <=> (?[Train]: (path_free(T, Train, in2, gate(Train))))))).
fof(open_in1, axiom, (![T]: (open(T, in1) <=> (?[Train]: (path_free(T, Train, in1, gate(Train))))))).

% No node can be occupied for given path.
fof(path_free_from_in2_to_out2, axiom, (![T, Train]: (path_free(T, Train, in2, out2) <=> (at(T, Train, in2) & (gate(Train) = out2) & node_empty(T, in2) & node_empty(T, s2) & node_empty(T, out2))))).
fof(path_free_from_in2_to_out1, axiom, (![T, Train]: (path_free(T, Train, in2, out1) <=> (at(T, Train, in2) & (gate(Train) = out1) & node_empty(T, in2) & node_empty(T, s2) & node_empty(T, out1))))).
fof(path_free_from_in1_to_out2, axiom, (![T, Train]: (path_free(T, Train, in1, out2) <=> (at(T, Train, in1) & (gate(Train) = out2) & node_empty(T, in1) & node_empty(T, s1) & node_empty(T, out2))))).
fof(path_free_from_in1_to_out1, axiom, (![T, Train]: (path_free(T, Train, in1, out1) <=> (at(T, Train, in1) & (gate(Train) = out1) & node_empty(T, in1) & node_empty(T, s1) & node_empty(T, out1))))).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%-------------------------- Train is in switch node and the switch is changed --------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_critical, conjecture, (![T, Train, N1, N2]: (((at(T, Train, N1)) & (switch(T, N1) = N2)) => ~(at(succ(T), Train, N1) & (switch(succ(T), N1) != N2))))).
