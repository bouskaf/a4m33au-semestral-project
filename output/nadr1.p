%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%----------------------------------------- Linear order ------------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(antisymmetry, axiom, (![X, Y]: ((less(X, Y) & less(Y, X)) => (X = Y)))).
fof(transitivity, axiom, (![X, Y, Z]: ((less(X, Y) & less(Y, Z)) => less(X, Z)))).
fof(totality, axiom, (![X, Y]: (less(X, Y) | less(Y, X)))).
fof(succ, axiom, (![X]: (less(X, succ(X)) & (![Y]: (less(Y, X) | less(succ(X), Y)))))).
fof(pred, axiom, (![X]: ((pred(succ(X)) = X) & (succ(pred(X)) = X)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------- Physical restriction of the train station -------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Train is at certain time only in one place.
fof(at_uniq, axiom, (![T, Train, N1, N2]: ((at(T, Train, N1) & at(T, Train, N2)) => (N1 = N2)))).

% Train already in station can not enter it again.
fof(at_nondup, axiom, (![T, Train, N, OtherN]: (at(T, Train, N) => ~enter(T, Train, OtherN)))).

% Train never enters occupied node.
fof(input_nocol, axiom, (![T, Train, OtherTrain, N]: (at(T, Train, N) => ~enter(T, OtherTrain, N)))).

% Two different trains do not enter the same node in the same time.
fof(enter_uniq, axiom, (![T, Train, OtherTrain, N]: ((enter(T, Train, N) & enter(T, OtherTrain, N)) => (Train = OtherTrain)))).

% If train is in node the node is occupied.
fof(node_occupied, axiom, (![T, Train, N]: (at(T, Train, N) => node_occupied(T, Train, N)))).

% If node is empty there is no train in it.
fof(node_empty, axiom, (![T, N]: (node_empty(T, N) <=> (![Train]: (~at(T, Train, N)))))).

% Node is safe.
fof(node_safe, axiom, (![T, N]: ((safe(T, N)) <=> ( ![Train, OtherTrain]: ((at(T, Train, N) & at(T, OtherTrain, N)) => (Train = OtherTrain)))))).

% Node is not blocked.
fof(node_not_blocked, axiom, (![N]: (notBlocked(N) <=> (![T1, Train]: (((at(T1, Train, N) & ~open(T1, N))) => (?[T2]: (less(T1, T2) & open(T2, N)))))))).

% Train will leave a node in future.
fof(train_will_move, axiom, (![T1, Train, N]: (at(T1, Train, N) => (?[T2]: (will_move(T2, Train) & less(T1, T2)))))).

% Train entered the station in past.
fof(train_entered, axiom, (![T1, Train, N]: ((at(T1, Train, N)) => (?[T2, I] : (enter(T2, Train, I) & less(T2, T1)))))).

% Node cannot be occupied by two different trains.
fof(occupied_only_once, axiom, (![T, Train, OtherTrain, N]: ((occupied(T, Train, N) & occupied(T, OtherTrain, N)) => (Train = OtherTrain)))).

% If there is train on switch node output direction has to remain same.
fof(switch_restr, axiom, (![T, N1, N2]: ((~node_empty(T, N1) & (switch(T, N1) = N2)) => (switch(succ(T), N1) = N2)))).

% Train moves as soon as it is possible.
fof(train_moves, axiom, (![T, Train]: (will_move(T, Train)))).

% Train enters the station as soon as it is possible.
fof(train_enters, axiom, ((?[Train, N1]: ![T2, N2, T1] : ((~at(T2, Train, N2) & ~enter(T2, Train, N2)) & ((empty_node(T2, N1)) & is_input(N1)) & ~at(T1, Train, N2) & less(T1, T2)) => enter(succ(T2), Train,N1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------- Domain restriction of symbols -------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for at values.
fof(at_restr, axiom, (![T, Train, N]: (at(T, Train, N) => ((N = out2) | (N = 1) | (N = 2) | (N = 3) | (N = in) | (N = v) | (N = out1))))).

% Restriction for occupied values.
fof(occupied_values, axiom, (![T, Train, N]: (occupied(T, Train, N) => ((N = out2) | (N = 1) | (N = 2) | (N = 3) | (N = in) | (N = v) | (N = out1))))).

% Nodes can not be equal.
fof(distinct_nodes, axiom, ((out2 != 1) & (out2 != 2) & (out2 != 3) & (out2 != in) & (out2 != v) & (out2 != out1) & (1 != 2) & (1 != 3) & (1 != in) & (1 != v) & (1 != out1) & (2 != 3) & (2 != in) & (2 != v) & (2 != out1) & (3 != in) & (3 != v) & (3 != out1) & (in != v) & (in != out1) & (v != out1))).

% Train can enter only in input nodes.
fof(enter_values, axiom, (![T, Train, N]: (enter(T, Train, N) => ((N = in))))).

% Only true for input nodes.
fof(input_node, axiom, (![N]: (input_node(N) => ((N = in))))).

% Only input nodes can be open.
fof(open_restriction, axiom, (![T, N]: (open(T, N) => is_input(N)))).

% Train can exit only in exit nodes.
fof(gate_restr, axiom, (![Train]: ((gate(Train) = out2) | (gate(Train) = out1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------ Switches values restriction --------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_1_value, axiom, (![T]: ((switch(T, 1) = 2)))).
fof(switch_2_value, axiom, (![T]: ((switch(T, 2) = 3)))).
fof(switch_3_value, axiom, (![T]: ((switch(T, 3) = out1)))).
fof(switch_in_value, axiom, (![T]: ((switch(T, in) = v) | (switch(T, in) = 1)))).
fof(switch_v_value, axiom, (![T]: ((switch(T, v) = out1) | (switch(T, v) = out2)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%----------------------------------------- Switches settings -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(switch_in_out2, axiom, (![T, Train]: ((occupied(T, Train, in) & (gate(Train) = out2)) => (switch(T, in) = v)))).
fof(switch_v_out2, axiom, (![T, Train]: ((occupied(T, Train, v) & (gate(Train) = out2)) => (switch(T, v) = out2)))).

fof(switch_in_out1, axiom, (![T, Train]: ((occupied(T, Train, in) & (gate(Train) = out1)) => (switch(T, in) = v)))).
fof(switch_v_out1, axiom, (![T, Train]: ((occupied(T, Train, v) & (gate(Train) = out1)) => (switch(T, v) = out1)))).

fof(switch_in_out1, axiom, (![T, Train]: ((occupied(T, Train, in) & (gate(Train) = out1)) => (switch(T, in) = 1)))).
fof(switch_1_out1, axiom, (![T, Train]: ((occupied(T, Train, 1) & (gate(Train) = out1)) => (switch(T, 1) = 2)))).
fof(switch_2_out1, axiom, (![T, Train]: ((occupied(T, Train, 2) & (gate(Train) = out1)) => (switch(T, 2) = 3)))).
fof(switch_3_out1, axiom, (![T, Train]: ((occupied(T, Train, 3) & (gate(Train) = out1)) => (switch(T, 3) = out1)))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Move restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(move_input_no_change_in, axiom, (![T, Train]: (enter(T, Train, in) | (at(T, Train, in) & ~open(T, in))) => at(succ(T), Train, in))).
fof(move_include_will_input_from_in_to_v, axiom, (![T, Train]: (((at(T, Train, in) & open(T, in) & will_move(T, Train)) => (at(succ(T), Train, v))) | ((at(T, Train, in) & open(T, in) & ~will_move(T, Train)) => at(succ(T), Train, in))))).
fof(move_include_will_input_from_in_to_1, axiom, (![T, Train]: (((at(T, Train, in) & open(T, in) & will_move(T, Train)) => (at(succ(T), Train, 1))) | ((at(T, Train, in) & open(T, in) & ~will_move(T, Train)) => at(succ(T), Train, in))))).

fof(move_out_stay_or_leave_out2, axiom, (![T, Train]: (((at(T, Train, out2) & ~will_move(T, Train)) => at(succ(T), Train, out2)) | ((at(T, Train, out2) & will_move(T, Train)) => (![N]: (~at(succ(T), Train, N))))))).
fof(move_out_stay_or_leave_out1, axiom, (![T, Train]: (((at(T, Train, out1) & ~will_move(T, Train)) => at(succ(T), Train, out1)) | ((at(T, Train, out1) & will_move(T, Train)) => (![N]: (~at(succ(T), Train, N))))))).

fof(move_switch_include_will_from_in_to_v, axiom, (![T, Train]: (((at(T, Train, in) & (switch(T, in) = v) & ~will_move(T, Train)) => (at(succ(T), Train, in))) | ((at(T, Train, in) & (switch(T, in) = v) & will_move(T, Train)) => (at(succ(T), Train, v)))))).
fof(move_switch_include_will_from_in_to_1, axiom, (![T, Train]: (((at(T, Train, in) & (switch(T, in) = 1) & ~will_move(T, Train)) => (at(succ(T), Train, in))) | ((at(T, Train, in) & (switch(T, in) = 1) & will_move(T, Train)) => (at(succ(T), Train, 1)))))).
fof(move_switch_include_will_from_v_to_out1, axiom, (![T, Train]: (((at(T, Train, v) & (switch(T, v) = out1) & ~will_move(T, Train)) => (at(succ(T), Train, v))) | ((at(T, Train, v) & (switch(T, v) = out1) & will_move(T, Train)) => (at(succ(T), Train, out1)))))).
fof(move_switch_include_will_from_v_to_out2, axiom, (![T, Train]: (((at(T, Train, v) & (switch(T, v) = out2) & ~will_move(T, Train)) => (at(succ(T), Train, v))) | ((at(T, Train, v) & (switch(T, v) = out2) & will_move(T, Train)) => (at(succ(T), Train, out2)))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------------- Occupied restriction -----------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(out2_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, out2) <=> ((occupied(T, Train, out2) & ~(at(T, Train, out2) & ~at(succ(T), Train, out2))) | (at(T, Train, in) & (goal(Train) = out2) & open(T, in)))))).
fof(1_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, 1) <=> ((occupied(T, Train, 1) & ~(at(T, Train, 1) & ~at(succ(T), Train, 1))) | (at(T, Train, in) & (goal(Train) = out1) & open(T, in)))))).
fof(2_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, 2) <=> ((occupied(T, Train, 2) & ~(at(T, Train, 2) & ~at(succ(T), Train, 2))) | (at(T, Train, in) & (goal(Train) = out1) & open(T, in)))))).
fof(3_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, 3) <=> ((occupied(T, Train, 3) & ~(at(T, Train, 3) & ~at(succ(T), Train, 3))) | (at(T, Train, in) & (goal(Train) = out1) & open(T, in)))))).
fof(v_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, v) <=> ((occupied(T, Train, v) & ~(at(T, Train, v) & ~at(succ(T), Train, v))) | (at(T, Train, in) & (goal(Train) = out2) & open(T, in)) | (at(T, Train, in) & (goal(Train) = out1) & open(T, in)))))).
fof(out1_occupied, axiom, (![T, Train]: (occupied(succ(T), Train, out1) <=> ((occupied(T, Train, out1) & ~(at(T, Train, out1) & ~at(succ(T), Train, out1))) | (at(T, Train, in) & (goal(Train) = out1) & open(T, in)))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%------------------------------------------ Path restriction -------------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Restriction for path_open values.
fof(open_values, axiom, (![T, Train, N1, N2]: (path_open(T, Train, N1, N2) => (((N1 = in) & (N2 = out2)) | ((N1 = in) & (N2 = out1)))))).

% No node can be occupied for given path.
fof(path_open_from_in_to_out2, axiom, (![T, Train, OtherTrain]: (path_open(T, Train, in, out2) <=> (at(T, Train, in) & (gate(Train) = out2) & (~occupied(T, OtherTrain, in) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, v) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, out2) | (Train = OtherTrain)))))).
fof(path_open_from_in_to_out1, axiom, (![T, Train, OtherTrain]: (path_open(T, Train, in, out1) <=> (at(T, Train, in) & (gate(Train) = out1) & (~occupied(T, OtherTrain, in) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, v) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, out1) | (Train = OtherTrain)))))).
fof(path_open_from_in_to_out1, axiom, (![T, Train, OtherTrain]: (path_open(T, Train, in, out1) <=> (at(T, Train, in) & (gate(Train) = out1) & (~occupied(T, OtherTrain, in) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, 1) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, 2) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, 3) | (Train = OtherTrain)) & (~occupied(T, OtherTrain, out1) | (Train = OtherTrain)))))).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------------- Open node restriction ----------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(open_in, axiom, (![T]: (open(T, in) <=> (?[Train]: (path_open(T, Train, in, gate(Train))))))).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%--------------------------- Two or more trains will arrive at the same node ----------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(collision_critical, conjecture, (![T, N]: (safe(T, N)))).
