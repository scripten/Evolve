/* breadth_first(Q, History, Solution):
   Base Case: 
 */
breadth_first(Queue, _, FinalMoves):-
  dequeue(b(Position, Path), Queue, _),
  final_state(Position),
  reverse(Path, [], FinalMoves).
breadth_first(Queue0, History, FinalMoves):-
  dequeue(b(Position, Path), Queue0, Queue1),
  findall(Move, move(Position, Move), Moves),
  filter(Moves, Position, Path, History, Queue1, Queue),
  breadth_first(Queue, [Position|History], FinalMoves).

filter([], _, _, _, Queue, Queue).
filter([Move|Moves], Position, Path, History, Queue0, Queue):-
  update(Position, Move, Position1),
  legal(Position1),
  not(member(Position1, History)),
  !,
  enqueue(b(Position1, [Move|Path]), Queue0, Queue1),
  filter(Moves, Position, Path, History, Queue1, Queue).
filter([_|Moves], Position, Path, History, Queue0, Queue):-
  filter(Moves, Position, Path, History, Queue0, Queue).

/* empty_queue(Empty): true when Empty is instantiated to an empty
   queue. */
empty_queue(q(zero, Ys, Ys)).  

/* enqueue(Element, QWithoutElement, QWithElement): add a new element
   to the tail of the queue. The s(N) is used to keep track of the
   length of the queue (so that the head can be found for the dequeue
   operation). */
enqueue(X, q(N, Ys, [X|Zs]), q(s(N), Ys, Zs)).

/* dequeue(Element, QWithElement, QWithoutElement): Strip the first 
   Element off of the QWithElement. 

   Note: The s(N) predicate is being used to represent counting
   numbers: zero, s(zero), s(s(zero)), s(s(s(zero))), ...
   Thus it encodes the length of the queue. Removing one s(N)
   decrements the number represented.
 */
dequeue(X, q(s(N), [X|Ys], Zs), q(N, Ys, Zs)).

/* test_bfs(ProblemIdentifier, SolutionToProblem): 
   uses the defined interface to create an instance of the problem and
   then sets up a queue and calls the bfs solver. */
test_bfs(Problem, Moves) :-
    initial_state(Problem, State),
    empty_queue(QInit),
    enqueue(b(State, []), QInit, Queue),
    breadth_first(Queue, [], Moves).

/* reverse(List, Tsil): List and Tsil are reverse of one another. */
reverse(Xs, Ys) :-
	reverse(Xs, [], Ys).

/* reverse(List, ReverseSoFar, FinishedReverse): Builds ReverseSoFar
   on the way down. At the bottom, match the accumulated reversed list
   to the results. */
reverse([X|Xs], Accumulator, Ys) :-
	reverse(Xs, [X | Accumulator], Ys).
reverse([], Accumulator, Accumulator).
