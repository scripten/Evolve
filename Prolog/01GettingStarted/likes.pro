/* Likes relationship between some red panda monks */
red_panda(ki).
red_panda(ryko).
red_panda(na).
red_panda(mikem).
red_panda(pi).

/* likes(X, Y) means panda X likes panda Y */
likes(ki, mikem).
/* ryko likes everybody */
likes(ryko, A) :- red_panda(A).
/* everybody likes pi */
likes(A, pi) :- red_panda(A).
