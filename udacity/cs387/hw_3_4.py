#!/usr/bin/python

#from hw3_4_util import mod_exp
from random import randrange

def mod_exp(a, b, q):
    """returns a**b % q"""
    #################
    ## Start of your code
    if b == 2:
        return (a * a) % q
    elif b % 2 == 1:
        return (a * mod_exp(a, b - 1, q)) % q
    else:
        x = mod_exp(a, b / 2, q)
        return (x * x) % q

def rabin_miller(n, target=128):
    """returns True if prob(`n` is composite) <= 2**(-`target`)"""
    ###############
    ## Start your code here
    if n % 2 == 0:
        return False

    s = n - 1
    t = 0
    while s % 2 == 0:
        s /= 2
        t += 1

    for i in range(target/2):
        a = randrange(1, n)
        if mod_exp(a, s, n) == 1:
            continue
        else:
            match = False
            for j in range(t):
                if mod_exp(a, s * 2**j, n) == n - 1:
                    match = True
                    break
            if not match:
                return False
    return True
    ## End of your code
    ###############

print rabin_miller(15487469)
