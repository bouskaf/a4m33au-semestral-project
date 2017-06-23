%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------- Domain restrictions of symbols -------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(at_restriction, axiom, (![T, Train, N]: (at(T, Train, N) => ((N = out2) | (N = 1) | (N = 2) | (N = 3) | (N = in) | (N = v) | (N = out1))))).

fof(distinct_nodes, axiom, ((out2 != 1) & (out2 != 2) & (out2 != 3) & (out2 != in) & (out2 != v) & (out2 != out1) & (1 != 2) & (1 != 3) & (1 != in) & (1 != v) & (1 != out1) & (2 != 3) & (2 != in) & (2 != v) & (2 != out1) & (3 != in) & (3 != v) & (3 != out1) & (in != v) & (in != out1) & (v != out1))).

fof(occupied_restriction, axiom, (![T, Train, N]: (occupied(T, Train, N) => ((N = out2) | (N = 1) | (N = 2) | (N = 3) | (N = in) | (N = v) | (N = out1))))).

fof(open_restr, axiom, ![T, N]: (
    open(T, N) => ((N = in))
)).

fof(gate_restr, axiom, ![Train]: (
    open(T, N) => ((gate(Train) = in))
)).

