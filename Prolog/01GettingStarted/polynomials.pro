/* polynomials are lists of coefficients */
/* x^2 + 3x + 8 = [8, 3, 1] */

/* eval(Poly, X, V) : Poly(X) = V */
eval([], X, 0).
eval([C | Cs], X, V) :-
    eval(Cs, X, V1),
    V is (V1 * X) + C.

/* derive(F, FPrime) */
/* derive(F, Power, FPrime). */
derive([F|Fs], FPrime) :-
    derive(Fs, 1, FPrime).

derive([], _, []).
derive([C | Cs], Power, [CPrime | CsPrime]) :-
    NextPower is Power + 1,
    derive(Cs, NextPower, CsPrime),
    CPrime is Power * C.
