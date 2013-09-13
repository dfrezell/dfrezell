#include <stdio.h>
#include <map>

#include "euler.h"

using namespace std;

int main(int argc, char *argv[]) {
    unsigned int i;
    uint64_t chain;
    int count;
    map<uint64_t,int> m;
    map<uint64_t,int>::iterator it;

    m[1] = 1;
    m[2] = 2;

    for (i = 2; i < 1000000; i++) {
        chain = i;
        count = 1;
        while (chain != 1) {
            chain = (chain & 0x01) ? ((3 * chain) + 1) : (chain / 2);
            if (chain < i) {
                break;
            }
            count++;
        }
        m[i] = m[chain] + count;
    }

    chain = 0;
    count = 0;
    it = m.begin();
    while (it != m.end()) {
        if (it->second > count) {
            count = it->second;
            chain = it->first;
        }
        it++;
    }

    printf("%lld has chain length of %d\n", chain, count);

    return 0;
}

// vim: set ts=4:
