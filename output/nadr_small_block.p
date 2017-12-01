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
fof(at_restr, axiom, (![T, Train, N]: (at(T, Train, N) => ((N = out2) | (N = s) | (N = in) | (N = out1))))).

% Only input nodes can be open.
fof(open_restr, axiom, (![T, N]: (open(T, N) => input(N)))).

% Only true for input nodes.
fof(input_restr, axiom, (![N]: (input(N) => ((N = in))))).

% Train can exit only in exit nodes.
fof(gate_restr, axiom, (![Train]: ((gate(Train) = out2) | (gate(Train) = out1)))).

% Nodes can not be equal.
fof(distinct_nodes, axiom, ((out2 != s) & (out2 != in) & (out2 != out1) & (s != in) & (s != out1) & (in != out1))).

% Train can enter only in input nodes.
fof(enter_values, axiom, (![T, Train, N]: (enter(T, Train, N) => ((N = in))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%--------------------------------------- Switches restriction ------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_s_values, axiom, (![T]: ((switch(T, s) = out1) | (switch(T, s) = out2)))).


fof(switch_s_with_gate_out2, axiom, (![T, Train]: ((at(T, Train, s) & (gate(Train) = out2)) => (switch(T, s) = out2)))).

fof(switch_s_with_gate_out1, axiom, (![T, Train]: ((at(T, Train, s) & (gate(Train) = out1)) => (switch(T, s) = out1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Move restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Possible moves for switch nodes.
fof(moves_s, axiom, (![T, Train]: (at(succ(T), Train, s) <=> (at(T, Train, in))))).

% Possible moves for gate nodes.
fof(moves_out2, axiom, (![T, Train]: (at(succ(T), Train, out2) <=> (at(T, Train, s))))).
fof(moves_out1, axiom, (![T, Train]: (at(succ(T), Train, out1) <=> (at(T, Train, s))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Path restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for path_free values.
fof(path_from_to_values, axiom, (![T, Train, N1, N2]: (path_free(T, Train, N1, N2) => (((N1 = in) & (N2 = out2)) | ((N1 = in) & (N2 = out1)))))).

% Open node restriction.
fof(open_in, axiom, (![T]: (open(T, in) <=> (?[Train]: (path_free(T, Train, in, gate(Train))))))).

% No node can be occupied for given path.
fof(path_free_from_in_to_out2, axiom, (![T, Train]: (path_free(T, Train, in, out2) <=> (at(T, Train, in) & (gate(Train) = out2) & node_empty(T, in) & node_empty(T, s) & node_empty(T, out2))))).
fof(path_free_from_in_to_out1, axiom, (![T, Train]: (path_free(T, Train, in, out1) <=> (at(T, Train, in) & (gate(Train) = out1) & node_empty(T, in) & node_empty(T, s) & node_empty(T, out1))))).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------ The input node remains permanently closed ------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(block_critical, conjecture, (![N]: (input(N) => notBlocked(N)))).
