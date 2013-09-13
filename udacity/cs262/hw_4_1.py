#!/usr/bin/python

import sys
from collections import namedtuple

State = namedtuple("State", "x ab cd j")


def addtochart(theset, index, elt):
    if elt not in theset[index]:
        theset[index].append(elt)
        return True
    return False

def closure(grammar, i, x, ab, cd):
    return [State(g[0], [], g[1], i) for g in grammar if len(cd) and cd[0] == g[0]]

def shift(tokens, i, x, ab, cd, j):
    if len(cd) and tokens[i] == cd[0]:
        return State(x, ab + cd[:1], cd[1:], j)
    return None

def reductions(chart, i, x, ab, cd, j):
    return [State(p.x, p.ab + p.cd[:1], p.cd[1:], p.j) 
            for p in chart[j] 
            if len(cd) == 0 and len(p.cd) and p.cd[0] == x]

grammar = [
    ["S", ["id", "(", "OPTARGS", ")"]],
    ["OPTARGS", []],
    ["OPTARGS", ["ARGS",]],
    ["ARGS", ["exp", ",", "ARGS"]],
    ["ARGS", ["exp",]],
]
tokens = ["id", "(", "exp", ",", "exp", ")"]

def parse(tokens, grammar):
    tokens = tokens + ["eof"]
    chart = {}
    start_rule = grammar[0]
    for i in range(len(tokens) + 1):
        chart[i] = []
    start_state = State(start_rule[0], [], start_rule[1], 0)
    chart[0] = [start_state]
    for i in range(len(tokens)):
        while True:
            changes = False
            for state in chart[i]:
                # Current state == x -> a b . c d , j
                # option 1: for each grammar rule c -> p q r
                # (where the c's match) make a next state:
                #   c -> . p q r , i
                # English: We're about to start parsing a "c", but
                #  "c" may be something like "exp" with its own
                #  production rules.  We'll bring in those productions rules.
                next_states = closure(grammar, i, state.x, state.ab, state.cd)
                for n in next_states:
                    changes = addtochart(chart, i, n) or changes


                # Current state == x -> a b . c d , j
                # options 2: If tokens[i] == c, make a next state:
                #   x -> a b c . d , j
                # in chart[i + 1]
                # English: We're looking to parse token 'c' next
                #  and the current token is exactly 'c'!  Aren't we lucky!
                #  So we can parse over it and move to j + 1
                next_state = shift(tokens, i, state.x, state.ab, state.cd, state.j)
                if next_state is not None:
                    changes = addtochart(chart, i+1, next_state) or changes

                # Current state == x -> a b . c d , j
                # Option 3: If cd is [], the state is just x -> a b . , j
                # for each p -> q . x r , l in chart[j]
                # make a new state:
                #   p -> q x . r, l in chart[i]
                # English: We finished parsing an "x" with this token,
                #  but that may have been a sub-step (like matching "exp -> 2"
                #  in "2 + 3").  We should update the higher-level rules as well.
                next_states = reductions(chart, i, state.x, state.ab, state.cd, state.j)
                for n in next_states:
                    changes = addtochart(chart, i, n) or changes

            if not changes:
                break

    for i in range(len(tokens)):
        print "== chart", str(i)
        for s in chart[i]:
            print "\t", s.x, "->",
            for sym in s.ab:
                print sym,
            print ".",
            for sym in s.cd:
                print sym,
            print "from", str(s.j)

    accepting_state = State(start_rule[0], start_rule[1], [], 0)
    return accepting_state in chart[len(tokens) - 1]

print parse(tokens, grammar)
