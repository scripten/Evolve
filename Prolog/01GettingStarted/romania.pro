road(oradea, zerind).
road(oradea, sibiu).
road(neamt, iasi).
road(zerind, arad).
road(iasi, vaslui).
road(arad, sibiu).
road(arad, timisoara).
road(sibiu, fagaras).
road(sibiu, rimnicu_vikea).
road(fagaras, bucharest).
road(vaslui, urziceni).
road(timisoara, lugoj).
road(rimnicu_vikea, pitesti).
road(rimnicu_vikea, cralova).
road(lugoj, mehadia).
road(pitesti, cralova).
road(pitesti, bucharest).
road(mehadia, drobeta).
road(bucharest, urziceni).
road(bucharest, giurgiu).
road(urziceni, hirsova).
road(drobeta, cralova).
road(hirsova, eforie).
/* ------------------------------------------ */
road(zerind, oradea).
road(sibiu, oradea).
road(iasi, neamt).
road(arad, zerind).
road(vaslui, iasi).
road(sibiu, arad).
road(timisoara, arad).
road(fagaras, sibiu).
road(rimnicu_vikea, sibiu).
road(bucharest, fagaras).
road(urziceni, vaslui).
road(lugoj, timisoara).
road(ikea, pitesti, rimnicu).
road(ikea, cralova, rimnicu).
road(mehadia, lugoj).
road(cralova, pitesti).
road(bucharest, pitesti).
road(drobeta, mehadia).
road(urziceni, bucharest).
road(giurgiu, bucharest).
road(hirsova, urziceni).
road(cralova, drobeta).
road(eforie, hirsova).

/* is there a path from Current to the Goal? Explored is the set of
   explored nodes */
/* base case (needed for recursive definitions) */
exists_path(Goal, Goal, _).
/* recursive case */
exists_path(Current, Goal, Explored) :- 
    road(Current, Neighbor), 
    legal(Neighbor, Explored),
    exists_path(Neighbor, Goal, [Neighbor | Explored]).

/* A node is legal, relative to a set, if it does not appear in the 
   set of nodes */
legal(Node, []).
legal(Node, [Head|Tail]) :- Node \== Head, legal(Node, Tail).

better_path(G, G, _, [G]).
better_path(Current, Goal, Explored, [Current | Tail]) :-
    road(Current, Neighbor),
    legal(Neighbor, Explored),
    better_path(Neighbor, Goal, [Neighbor | Explored], Tail).

extract([], Sofar, Sofar).
extract([node(HCity, HDistance) | T], node(SCity, SDistance), Best) :-
    writef(node(HCity, HDistance)),
    HDistance < SDistance,
    extract(T, node(HCity, HDistance), Best).
extract([node(HCity, HDistance) | T], node(SCity, SDistance), Best) :-
    HDistance >= SDistance,
    extract(T, node(SCity, SDistance), Best).
