#!/usr/bin/python

colors = [['red', 'green', 'green', 'red' , 'red'],
          ['red', 'red', 'green', 'red', 'red'],
          ['red', 'red', 'green', 'green', 'red'],
          ['red', 'red', 'red', 'red', 'red']]

measurements = ['green', 'green', 'green' ,'green', 'green']


motions = [[0,0],[0,1],[1,0],[1,0],[0,1]]

sensor_right = 0.7

p_move = 0.8

def show(p):
    for i in range(len(p)):
        print p[i]

#DO NOT USE IMPORT
#ENTER CODE BELOW HERE
#ANY CODE ABOVE WILL CAUSE
#HOMEWORK TO BE GRADED
#INCORRECT

# motions = [0, 0]  : no move
#           [0, 1]  : right
#           [0, -1] : left
#           [1, 0]  : down
#           [-1, 0] : up

# print out a cleaner looking table.
def alfshow(p):
    for i in range(len(p)):
        for j in range(len(p[i])):
            print "%1.5f" % p[i][j],
        print

# world dimensions, used for our cyclic movement
WIDTH = len(colors[0])
HEIGHT = len(colors)
def move(p, U):
    q = []
    for i in range(len(p)):
        q.append([])
        for j in range(len(p[i])):
            s = p_move * p[(i - U[0]) % HEIGHT][(j - U[1]) % WIDTH]
            s += p_stay * p[i][j]
            q[i].append(s)
    return q

def sense(p, Z):
    q = []
    normalizer = 0.0
    for i in range(len(p)):
        q.append([])
        for j in range(len(p[i])):
            hit = (Z == colors[i][j])
            q[i].append(p[i][j] * (hit * sensor_right + (1 - hit) * sensor_wrong))
        # NOTE: we calculate our normalizer based on new probability set, q
        # keep track of sum, based on a row at a time.
        normalizer += sum(q[i])

    # apply normalizer to our new probability table, the sum of all probabilities
    # in the cell should now equal 1.0 (give or take floating point error)
    for i in range(len(q)):
        for j in range(len(q[i])):
            q[i][j] /= normalizer
    return q

# inverse above probabilities
sensor_wrong = 1.0 - sensor_right
p_stay = 1.0 - p_move

p = []

# first, count the number of cells, this is used to determine the initial 
# probability for each cell.
cells = 0
for i in range(len(colors)):
    p.append([])
    for j in range(len(colors[i])):
        cells += 1
        # seed the initial cell value, will be later divided
        # by the cell count
        p[i].append(1.0)

# divide the cell value by total number of cells, giving each
# cell an equal probability.
for i in range(len(p)):
    for j in range(len(p[i])):
        p[i][j] /= cells

for i in range(len(motions)):
    u, z = motions[i], measurements[i]
    p = move(p, u)
    p = sense(p, z)


#Your probability array must be printed 
#with the following code.

show(p)

