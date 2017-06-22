%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%---------------------------------- Domain restrictions of symbols -------------------------------------%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fof(at_restr, axiom, ![T, Train, N]: (
    at(T, Train, N) => ((N = out2) | (N = in) | (N = v) | (N = out1))
)).

fof(open_restr, axiom, ![T, N]: (
    open(T, N) => ((N = in))
)).

fof(gate_restr, axiom, ![Train]: (
    open(T, N) => ((gate(Train) = in))
)).

fof(distinct_nodes, axiom, (
    (out2 != in) & (out2 != v) & (out2 != out1) & 
    (in != v) & (in != out1) & 
    (v != out1)
)).

