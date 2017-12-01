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
fof(at_restr, axiom, (![T, Train, N]: (at(T, Train, N) => ((N = s3) | (N = out2) | (N = out3) | (N = out4) | (N = in) | (N = out1) | (N = s1) | (N = s2))))).

% Only input nodes can be open.
fof(open_restr, axiom, (![T, N]: (open(T, N) => input(N)))).

% Only true for input nodes.
fof(input_restr, axiom, (![N]: (input(N) => ((N = in))))).

% Train can exit only in exit nodes.
fof(gate_restr, axiom, (![Train]: ((gate(Train) = out2) | (gate(Train) = out3) | (gate(Train) = out4) | (gate(Train) = out1)))).

% Nodes can not be equal.
fof(distinct_nodes, axiom, ((s3 != out2) & (s3 != out3) & (s3 != out4) & (s3 != in) & (s3 != out1) & (s3 != s1) & (s3 != s2) & (out2 != out3) & (out2 != out4) & (out2 != in) & (out2 != out1) & (out2 != s1) & (out2 != s2) & (out3 != out4) & (out3 != in) & (out3 != out1) & (out3 != s1) & (out3 != s2) & (out4 != in) & (out4 != out1) & (out4 != s1) & (out4 != s2) & (in != out1) & (in != s1) & (in != s2) & (out1 != s1) & (out1 != s2) & (s1 != s2))).

% Train can enter only in input nodes.
fof(enter_values, axiom, (![T, Train, N]: (enter(T, Train, N) => ((N = in))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%--------------------------------------- Switches restriction ------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_s3_values, axiom, (![T]: ((switch(T, s3) = out3) | (switch(T, s3) = out4)))).
fof(switch_s1_values, axiom, (![T]: ((switch(T, s1) = s2) | (switch(T, s1) = s3)))).
fof(switch_s2_values, axiom, (![T]: ((switch(T, s2) = out1) | (switch(T, s2) = out2)))).


fof(switch_s1_with_gate_out2, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out2)) => (switch(T, s1) = s2)))).
fof(switch_s2_with_gate_out2, axiom, (![T, Train]: ((at(T, Train, s2) & (gate(Train) = out2)) => (switch(T, s2) = out2)))).

fof(switch_s1_with_gate_out3, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out3)) => (switch(T, s1) = s3)))).
fof(switch_s3_with_gate_out3, axiom, (![T, Train]: ((at(T, Train, s3) & (gate(Train) = out3)) => (switch(T, s3) = out3)))).

fof(switch_s1_with_gate_out4, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out4)) => (switch(T, s1) = s3)))).
fof(switch_s3_with_gate_out4, axiom, (![T, Train]: ((at(T, Train, s3) & (gate(Train) = out4)) => (switch(T, s3) = out4)))).

fof(switch_s1_with_gate_out1, axiom, (![T, Train]: ((at(T, Train, s1) & (gate(Train) = out1)) => (switch(T, s1) = s2)))).
fof(switch_s2_with_gate_out1, axiom, (![T, Train]: ((at(T, Train, s2) & (gate(Train) = out1)) => (switch(T, s2) = out1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Move restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Possible moves for switch nodes.
fof(moves_s3, axiom, (![T, Train]: (at(succ(T), Train, s3) <=> (at(T, Train, s1))))).
fof(moves_s1, axiom, (![T, Train]: (at(succ(T), Train, s1) <=> (at(T, Train, in))))).
fof(moves_s2, axiom, (![T, Train]: (at(succ(T), Train, s2) <=> (at(T, Train, s1))))).

% Possible moves for gate nodes.
fof(moves_out2, axiom, (![T, Train]: (at(succ(T), Train, out2) <=> (at(T, Train, s2))))).
fof(moves_out3, axiom, (![T, Train]: (at(succ(T), Train, out3) <=> (at(T, Train, s3))))).
fof(moves_out4, axiom, (![T, Train]: (at(succ(T), Train, out4) <=> (at(T, Train, s3))))).
fof(moves_out1, axiom, (![T, Train]: (at(succ(T), Train, out1) <=> (at(T, Train, s2))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Path restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for path_free values.
fof(path_from_to_values, axiom, (![T, Train, N1, N2]: (path_free(T, Train, N1, N2) => (((N1 = in) & (N2 = out2)) | ((N1 = in) & (N2 = out3)) | ((N1 = in) & (N2 = out4)) | ((N1 = in) & (N2 = out1)))))).

% Open node restriction.
fof(open_in, axiom, (![T]: (open(T, in) <=> (?[Train]: (path_free(T, Train, in, gate(Train))))))).

% No node can be occupied for given path.
fof(path_free_from_in_to_out2, axiom, (![T, Train]: (path_free(T, Train, in, out2) <=> (at(T, Train, in) & (gate(Train) = out2) & node_empty(T, in) & node_empty(T, s1) & node_empty(T, s2) & node_empty(T, out2))))).
fof(path_free_from_in_to_out3, axiom, (![T, Train]: (path_free(T, Train, in, out3) <=> (at(T, Train, in) & (gate(Train) = out3) & node_empty(T, in) & node_empty(T, s1) & node_empty(T, s3) & node_empty(T, out3))))).
fof(path_free_from_in_to_out4, axiom, (![T, Train]: (path_free(T, Train, in, out4) <=> (at(T, Train, in) & (gate(Train) = out4) & node_empty(T, in) & node_empty(T, s1) & node_empty(T, s3) & node_empty(T, out4))))).
fof(path_free_from_in_to_out1, axiom, (![T, Train]: (path_free(T, Train, in, out1) <=> (at(T, Train, in) & (gate(Train) = out1) & node_empty(T, in) & node_empty(T, s1) & node_empty(T, s2) & node_empty(T, out1))))).

