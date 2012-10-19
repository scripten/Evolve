my_len([], 0).
my_len([H | T], L) :- 
	my_len(T, K), L is K + 1.

/* setify(List, Set). */
setify([], []).
setify([X|Xs], Ys) :-
	member(X, Xs), !,
	setify(Xs, Ys).
setify([X|Xs], [X|Ys]) :-
       setify(Xs, Ys).

list([]).
list([As | Xs]) :- 
	list(Xs).
/* append(Xs, Ys, Zs) : Zs is a list that is the 
   concatenation of lists Xs and Ys. Note Xs and Ys 
   must be lists for this to be correct. */
append([], Ys, Ys) :-
	list(Ys).
append([X|Xs], Ys, [X|Zs]) :-
	append(Xs, Ys, Zs).

/* Counting version of append/4. */
append([], Ys, Ys, 1) :-
	list(Ys).
append([X|Xs], Ys, [X|Zs], C) :-
	append(Xs, Ys, Zs, D),
	C is D + 1.

appendR(Xs, Ys, Zs) :-
	appendR(Xs, Ys, Zs, 1).

appendR([], Ys, Ys, Indent) :-
	format('~*|~tappend([], ~w, ~w).~n', [Indent, Ys, Ys]),
	list(Ys).

appendR([X|Xs], Ys, [X|Zs], Indent) :-
	format('~*|~tappend([~w|~w], ~w, [~w|~w]).~n', 
	       [Indent, X, Xs, Ys, X, Zs]),
	I is Indent + 4,
	appendR(Xs, Ys, Zs, I).

appendU(Xs, Ys, Zs) :-
	appendU(Xs, Ys, Zs, 1).

appendU([], Ys, Ys, Indent) :-
	format('~*|~t>>> append([], Ys, Ys).~n', [Indent]),
	MoreIndent is Indent + 11,
	format('~*|~tYs=~w,~n', [MoreIndent, Ys]),
	list(Ys),
	format('~*|~t<<< append([], Ys, Ys).~n', [Indent]),
	format('~*|~tYs=~w,~n', [MoreIndent, Ys]).

appendU([X|Xs], Ys, [X|Zs], Indent) :-
	format('~*|~t>>> append([X | Xs], Ys, [X | Zs]).~n', [Indent]),
	MoreIndent is Indent + 11,
	format('~*|~tX=~w,~n', [MoreIndent, X]),
	format('~*|~tXs=~w,~n', [MoreIndent, Xs]),
	format('~*|~tYs=~w,~n', [MoreIndent, Ys]),
	format('~*|~tZs=~w,~n', [MoreIndent, Zs]),

	I is Indent + 4,
	appendU(Xs, Ys, Zs, I),
	format('~*|~t<<< append([X | Xs], Ys, [X | Zs]).~n', [Indent]),
	format('~*|~tX=~w,~n', [MoreIndent, X]),
	format('~*|~tXs=~w,~n', [MoreIndent, Xs]),
	format('~*|~tYs=~w,~n', [MoreIndent, Ys]),
	format('~*|~tZs=~w,~n', [MoreIndent, Zs]).

/* Xs is a prefix of Ys */
prefix(Xs, Ys) :-
	append(Xs, As, Ys).

suffix(Xs, Ys) :-
	append(As, Xs, Ys).

member(X, Ys) :-
	append(As, [X | Xs], Ys).

reverseA([], []).
reverseA([X|Xs], Zs) :-
	reverseA(Xs, Ys),
	append(Ys, [X], Zs).

reverseA([], [], 1).
reverseA([X|Xs], Zs, C) :-
	reverseA(Xs, Ys, D),
	append(Ys, [X], Zs, E),
	C is D + E + 1.

reverseA_counter(Xs, Ys) :-
	reverseA(Xs, Ys, Count),
	format('Count = ~d', Count).

reverseB(Xs, Ys) :-
	reverseB(Xs, [], Ys).

reverseB([X|Xs], Accumulator, Ys) :-
	reverseB(Xs, [X | Accumulator], Ys).
reverseB([], Accumulator, Accumulator).

reverseB_counter(Xs, Ys) :-
	reverseB(Xs, [], Ys, Count),
	format('Count = ~d', Count).

reverseB([X|Xs], Accumulator, Ys, C) :-
	reverseB(Xs, [X | Accumulator], Ys, D),
	C is D + 1.
reverseB([], Accumulator, Accumulator, 1).
