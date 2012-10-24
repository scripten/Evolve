/*
   union(Xs,Ys,Us) :- Us is the union of the elements in Xs and Ys. 
*/

union([X|Xs],Ys,Us) :- member(X,Ys), union(Xs,Ys,Us).
union([X|Xs],Ys,[X|Us]) :- nonmember(X,Ys), union(Xs,Ys,Us).
union([],Ys,Ys).

/*
   intersection(Xs,Ys,Is) :- Is is the intersection of the elements in Xs and Ys. 
*/

intersection([X|Xs],Ys,[X|Is]) :- member(X,Ys), intersection(Xs,Ys,Is).
intersection([X|Xs],Ys,Is) :- nonmember(X,Ys), intersection(Xs,Ys,Is).
intersection([],Ys,[]).

/*
   union_intersect(Xs,Ys,Us) :- 
	Us and is are the union and intersection, respectively, of the
		elements in Xs and Ys.  
*/

union_intersect([X|Xs],Ys,Us,[X|Is]) :- 
	member(X,Ys), union_intersect(Xs,Ys,Us,Is).
union_intersect([X|Xs],Ys,[X|Us],Is) :- 
	nonmember(X,Ys), union_intersect(Xs,Ys,Us,Is).
union_intersect([],Ys,Ys,[]).
