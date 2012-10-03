my_len([], 0).
my_len([H | T], L) :- my_len(T, K), L is K + 1.
