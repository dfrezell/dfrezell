#!/usr/bin/python

# Title: FSM Optimization
# 
# Challenge Problem: 2 Stars
#
# Lexical analyzers are implemented using finite state machines generated
# from the regular expressions of token definition rules. The performance
# of a lexical analyzer can depend on the size of the resulting finite
# state machine. If the finite state machine will be used over and over
# again (e.g., to analyze every token on every web page you visit!), we
# would like it to be as small as possible (e.g., so that your webpages
# load quickly). However, correctness is more important than speed: even
# an optimized FSM must always produce the right answer.  
#
# One way to improve the performance of a finite state machine is to make
# it smaller by removing unreachable states. If such states are removed,
# the resulting FSM takes up less memory, which may make it load faster or
# fit better in a storage-constrained mobile device.
#
# For this assignment, you will write a procedure nfsmtrim that removes
# "dead" states from a non-deterministic finite state machine. A state is
# (transitively) "dead" if it is non-accepting and only non-accepting
# states are reachable from it. Such states are also called "trap" states:
# once entered, there is no escape. In this example FSM for r"a*" ...
#
# edges = { (1,'a') : [1] ,
#           (1,'b') : [2] ,
#           (2,'b') : [3] ,
#           (3,'b') : [4] } 
# accepting = [ 1 ] 
# 
# ... states 2, 3 and 4 are "dead": although you can transition from 1->2,
# 2->3 and 3->4 on "b", you are doomed to rejection if you do so. 
#
# You may assume that the starting state is always state 1. Your procedure
# nfsmtrim(edges,accepting) should return a tuple (new_edges,new_accepting)
# corresponding to a FSM that accepts exactly the same strings as the input
# FSM but that has all dead states removed. 
#
# Hint 1: This problem is tricky. Do not get discouraged. 
#
# Hint 2: Think back to the nfsmaccepts() procedure from the "Reading
# Machine Minds" homework problem in Unit 1. You are welcome to reuse your
# code (or the solution we went over) to that problem. 
#
# Hint 3: Gather up all of the states in the input machine. Filter down
# to just those states that are "live". new_edges will then be just like
# edges, but including only those transitions that involve live states.
# new_accepting will be just like accepting, but including only those live
# states. 

class fsmstate:
    def __init__(self, start, edge, end, parent):
        # the start state
        self.start = start
        # the edge, or letter, that gets us to the next state
        self.edge = edge
        # the ending state
        self.end = end
        # our parent state, namely our parents start and edge that lead
        # us to our start state
        self.pkey = None
        # hold a reference to our parent so we can walk back the
        # tree to add all the states to our new map
        self.parent = parent
        self.key = (start, edge)
        if parent:
            self.pkey = parent.key
    
    def __eq__(self, o):
        return self.start == o.start and self.edge == o.edge and \
               self.end == o.end and self.pkey == o.pkey

    def __repr__(self):
        return "<%d, '%s', %d : %s>" % (self.start, self.edge, \
                                        self.end, self.pkey)
               
    def predecessors(self, edges):
        return [fsmstate(e[0], e[1], s, self) \
                for e in edges if e[0] == self.end for s in edges[e]]
    

def nfsmtrim(edges, accepting): 
    # Write your code here.
    frontier = []
    explored = []
    new_edges = {}
    new_accepting = []
    
    # populate our frontier with all possible fsmstates in state(1)
    frontier = fsmstate(None, None, 1, None).predecessors(edges)
    
    while len(frontier):
        curr = frontier.pop(0)
        if curr in explored:
            continue
        explored.append(curr)
        # iterate through the list of children and add them to the stack
        for p in curr.predecessors(edges):
            frontier.insert(0, p)
        # check to see if we end in an accepting state
        if curr.end in accepting:
            # add accepting states into our new list as we find them
            if curr.end not in new_accepting:
                new_accepting.append(curr.end)
            # walk our parent tree and add each fsmstate to our new edges
            child, parent = curr, curr.parent
            while True:
                # are we done walking the lineage?
                if parent is None:
                    break
                # add child to our new edges
                if child.key not in new_edges:
                    new_edges[child.key] = [child.end]
                elif child.end not in new_edges[child.key]:
                    new_edges[child.key].append(child.end)
                    # we shouldn't have to do this
                    new_edges[child.key].sort(reverse=True)
                # go to the next ancestor
                child, parent = parent, parent.parent
    return (new_edges, new_accepting)


# We have included a few test cases, but you will definitely want to make
# your own. 

edges1 = { (1,'a') : [1] ,
           (1,'b') : [2] ,
           (2,'b') : [3] ,
           (3,'b') : [4] ,
           (8,'z') : [9] , } 
accepting1 = [ 1 ] 
(new_edges1, new_accepting1) = nfsmtrim(edges1,accepting1) 
print new_edges1, new_accepting1
print new_edges1 == {(1, 'a'): [1]}
print new_accepting1 == [1] 

(new_edges2, new_accepting2) = nfsmtrim(edges1,[]) 
print new_edges2, new_accepting2
print new_edges2 == {}
print new_accepting2 == [] 

(new_edges3, new_accepting3) = nfsmtrim(edges1,[3,6]) 
print new_edges3, new_accepting3
print new_edges3 == {(1, 'a'): [1], (1, 'b'): [2], (2, 'b'): [3]}
print new_accepting3 == [3]

edges4 = { (1,'a') : [1] ,
           (1,'b') : [2,5] ,
           (2,'b') : [3] ,
           (3,'b') : [4] ,
           (3,'c') : [2,1,4] } 
accepting4 = [ 2 ] 
(new_edges4, new_accepting4) = nfsmtrim(edges4, accepting4) 
print new_edges4
print new_edges4 == { 
  (1, 'a'): [1],
  (1, 'b'): [2], 
  (2, 'b'): [3], 
  (3, 'c'): [2, 1], 
}
print new_accepting4 == [2]

edges5 = {(1, 'a'): [2],
         (2, 'b'): [4],
         (3, 'c'): [2],
         (4, 'b'): [5, 7],
         (5, 'b'): [6],
         (7, 'b'): [1]
        }
accepting5 = [2]
(new_edges5, new_accepting5) = nfsmtrim(edges5, accepting5) 
print new_edges5
print new_edges5 == {(1, 'a'): [2], (2, 'b'): [4], (4, 'b'): [7], (7, 'b'): [1]}
print new_accepting5 == [2]
