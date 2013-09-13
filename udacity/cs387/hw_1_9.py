#!/usr/bin/python

import sys

hex2list_lut = [
    [0, 0, 0, 0], [0, 0, 0, 1],
    [0, 0, 1, 0], [0, 0, 1, 1],
    [0, 1, 0, 0], [0, 1, 0, 1],
    [0, 1, 1, 0], [0, 1, 1, 1],
    [1, 0, 0, 0], [1, 0, 0, 1],
    [1, 0, 1, 0], [1, 0, 1, 1],
    [1, 1, 0, 0], [1, 1, 0, 1],
    [1, 1, 1, 0], [1, 1, 1, 1],
]

def string_to_bits(m):
    s = []
    for byte in m:
        n1 = hex2list_lut[(ord(byte) >> 4) & 0x0f]
        n2 = hex2list_lut[ord(byte) & 0x0f]
        s += n1[1:] + n2
    return s

def bits_to_string(m):
    # it's assumed there are 7 bits per character.
    s = ""
    if len(m) % 7 != 0:
        print "warning: invalid message length to convert"
    for i in range(0, len(m), 7):
        c = m[i]
        c = c << 1 | m[i+1]
        c = c << 1 | m[i+2]
        c = c << 1 | m[i+3]
        c = c << 1 | m[i+4]
        c = c << 1 | m[i+5]
        c = c << 1 | m[i+6]
        s += chr(c)
    return s

# converts the ciphertext string to a list of bits.
def convert_bits(c):
    return [ord(x) - ord('0') for x in c]


def xor(m, k):
    c = []
    for m1, k1 in zip(m, k):
        c.append(m1 ^ k1)
    return c

def scan(needle, haystack):
    import string
    #upper_test_bits = string_to_bits(string.upper(needle))
    lower_test_bits = string_to_bits(needle)
    validchrset = set(chr(c) for c in range(32, 127))
    filterset = set("@#$%^&*{}[]\\|`~<>;:+=-_")
 
    l = []
    for x in range(0, len(haystack) - (len(lower_test_bits) - 7), 7):
        lower_m2 = bits_to_string(xor(haystack[x:x+len(lower_test_bits)], lower_test_bits))
        lower_s2 = set(lower_m2)
        if not lower_s2.isdisjoint(filterset):
            continue
        if lower_s2.issubset(validchrset):
            l.append((x/7, lower_m2))
    return l


def main(*args):
    c1 = convert_bits("1010110010011110011111101110011001101100111010001111011101101011101000110010011000000101001110111010010111100100111101001010000011000001010001001001010000000010101001000011100100010011011011011011010111010011000101010111111110010011010111001001010101110001111101010000001011110100000000010010111001111010110000001101010010110101100010011111111011101101001011111001101111101111000100100001000111101111011011001011110011000100011111100001000101111000011101110101110010010100010111101111110011011011001101110111011101100110010100010001100011001010100110001000111100011011001000010101100001110011000000001110001011101111010100101110101000100100010111011000001111001110000011111111111110010111111000011011001010010011100011100001011001101110110001011101011101111110100001111011011000110001011111111101110110101101101001011110110010111101000111011001111")
    c2 = convert_bits("1011110110100110000001101000010111001000110010000110110001101001111101010000101000110100111010000010011001100100111001101010001001010001000011011001010100001100111011010011111100100101000001001001011001110010010100101011111010001110010010101111110001100010100001110000110001111111001000100001001010100011100100001101010101111000100001111101111110111001000101111111101011001010000100100000001011001001010000101001110101110100001111100001011101100100011000110111110001000100010111110110111010010010011101011111111001011011001010010110100100011001010110110001001000100011011001110111010010010010110100110100000111100001111101111010011000100100110011111011001010101000100000011111010010110111001100011100001111100100110010010001111010111011110110001000111101010110101001110111001110111010011111111010100111000100111001011000111101111101100111011001111")

    common_words = []
    fd = open("count_1w.txt", "r+")
    while True:
        word = fd.readline()
        if word == "":
            break
        word = word.split('\t')[0]
        common_words.append(word)
    fd.close()
    common_set = set(common_words)

    m1_m2 = xor(c1, c2)

    #             1         2         3         4         5
    #    12345678901234567890123456789012345678901234567890
    m1= "I visualize a time when we will be to robots what " + \
        "dogs are to humans, and I'm rooting for the machin" + \
        "es.  (Claude Shannon)"
    #             1         2         3         4         5
    #    12345678901234567890123456789012345678901234567890
    m2= "Anyone who considers arithmetical methods of produ" + \
        "cing random digits is, of course, in a state of si" + \
        "n. (John von Neumann)"
    # possible:
    # 54 : " are to " -> " random "
    # 84 : ", in a state of sin." -> "ing for the machines"

    k1 = xor(c1, string_to_bits(m1))
    k2 = xor(c2, string_to_bits(m2))

    if k1 == k2:
        print "yes:"


    l = scan(m1, m1_m2)
    for s in l:
        print s
    sys.exit(0)

    for test_word in common_words:
        if len(test_word) < 5:
            continue
        l = scan(" " + test_word + " ", m1_m2)
        for w in l:
            print "'%s' -> '%s'" % (test_word, w)


if __name__ == "__main__":
    sys.exit(main(*sys.argv))

