/* len(List, Size) is the length of list List equal to Size? */
len([], 0).
len([X | Xs], S) :-
    len(Xs, SizeOfXs),
    S is SizeOfXs + 1.

/* member(List, Element). Is Element a member of the list List? */
member([Element | Elements], Element).
member([X | Elements], Element) :-
    X \== Element, 
    member(Elements, Element).

