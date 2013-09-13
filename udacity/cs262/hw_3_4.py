#!/usr/bin/env python

# Infinite Mind Reading
#
# Just as a context-free grammar may be 'empty', it may also have an
# infinite language. We say that the language for a grammar is infinite if
# the grammar accepts an infinite number of different strings (each of
# which is of finite length). Most interesting (and creative!) languages
# are infinite.
# 
# For example, the language of this grammar is infinite:
#
# grammar1 = [ 
#       ("S", [ "S", "a" ] ),        # S -> S a
#       ("S", [ "b", ]) ,            # S -> b 
#       ] 
#
# Because it accepts the strings b, ba, baa, baaa, baaaa, etc. 
#
# However, this similar grammar does _not_ have an infinite language: 
#
# grammar2 = [ 
#       ("S", [ "S", ]),             # S -> S 
#       ("S", [ "b", ]) ,            # S -> b 
#       ] 
#
# Because it only accepts one string: b. 
#
# For this problem you will write a procedure cfginfinite(grammar)
# that returns True (the value True, not the string "True") if the grammar
# accepts an infinite number of strings (starting from any symbol). Your
# procedure should return False otherwise. 
#
# Consider this example: 
# 
# grammar3 = [ 
#       ("S", [ "Q", ] ),        # S -> Q
#       ("Q", [ "b", ]) ,        # Q -> b
#       ("Q", [ "R", "a" ]),     # Q -> R a 
#       ("R", [ "Q"]),           # R -> Q
#       ] 
#
# The language of this grammar is infinite (b, ba, baa, etc.) because it is
# possible to "loop" or "travel" from Q back to Q, picking up an "a" each
# time. Since we can travel around the loop as often as we like, we can
# generate infinite strings. By contrast, in grammar2 it is possible to
# travel from S to S, but we do not pick up any symbols by doing so. 
#
# Important Assumption: For this problem, you may assume that for every
# non-terminal in the grammar, that non-terminal derives at least one
# non-empty finite string.  (You could just call cfgempty() from before to
# determine this, so we'll assume it.)  
#
# Hint 1: Determine if "Q" can be re-written to "x Q y", where either x
# or y is non-empty. 
#
# Hint 2: The "Important Assumption" above is more important than it looks:
# it means that any rewrite rule "bigger" than ("P", ["Q"]) adds at least
# one token. 
#
# Hint 3: While cfginfinite(grammar) is not recursive, you may want to
# write a helper procedure (that determines if Q can be re-written to "x Q
# y" with |x+y| > 0 ) that _is_ recursive. Watch out for infinite loops:
# keep track of what you have already visited. 

def expand_one(stmt, grammar, expanders, nonterminals):
    for i, s in enumerate(stmt):
        if s in nonterminals:
            while len(expanders):
                e = expanders.pop(0)
                if s == grammar[e][0]:
                    yield stmt[:i] + grammar[e][1] + stmt[i+1:]

def expand(original, stmt, grammar, expanders, nonterminals):
    if original[0] in stmt and len(stmt) > original[1]:
        return True
    if len(expanders) == 0:
        return False
    
    for new_stmt in expand_one(stmt, grammar, expanders, nonterminals):
        if expand(original, new_stmt, grammar, expanders[:], nonterminals):
            return True
    return False

def cfginfinite(grammar): 
    nonterminals = set(map(lambda x: x[0], grammar))
    expanders = []
    for i in range(len(grammar)):
        for r in grammar[i][1]:
            if r in nonterminals:
                expanders.append(i)
                break
    # strategy:
    #  - pick a non-terminal
    #  - apply expansion rules until we get the same non-terminal with terminal
    #    or we exhausted all of the expansion rules
    for nterm in nonterminals:
        if expand((nterm, 1), [nterm], grammar, expanders[:], nonterminals):
            return True
    return False

    
# We have provided a few test cases. You will likely want to write your own
# as well. 

grammar1 = [ 
      ("S", [ "S", "a" ]), # S -> S a
      ("S", [ "b", ]) , # S -> b 
      ] 
print cfginfinite(grammar1) == True

grammar2 = [ 
      ("S", [ "S", ]), # S -> S 
      ("S", [ "b", ]) , # S -> b 
      ] 

print cfginfinite(grammar2) == False

grammar3 = [ 
      ("S", [ "Q", ]), # S -> Q
      ("Q", [ "b", ]) , # Q -> b
      ("Q", [ "R", "a" ]), # Q -> R a 
      ("R", [ "Q"]), # R -> Q
      ] 

print cfginfinite(grammar3) == True

grammar4 = [  # Nobel Peace Prizes, 1990-1993
      ("S", [ "Q", ]),
      ("Q", [ "Mikhail Gorbachev", ]) ,
      ("Q", [ "P", "Aung San Suu Kyi" ]),
      ("R", [ "Q"]),
      ("R", [ "Rigoberta Tum"]),
      ("P", [ "Mandela and de Klerk"]),
      ] 

print cfginfinite(grammar4) == False
