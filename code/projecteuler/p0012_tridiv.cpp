#include <stdio.h>

#include "euler.h"
#include <map>

using namespace std;

int main(int argc, char *argv[])
{
    int numf = 1;
    uint64_t num;
    uint64_t sum;
    uint64_t osum;
    uint64_t f;
    int maxfc = 0;
    map<uint64_t,int> pdiv;
    map<uint64_t,int>::iterator it;

    for (num = 20; num < 30000; num++) {
        sum = (num * (num + 1)) / 2;
        // keep the original sum around since we modify sum below.
        osum = sum;
        // loop over sum and find every prime factor.  Store the count
        // of all the prime factors
        while ((f = factor(sum)) != 1) {
            pdiv[f]++;
            sum /= f;
        }
        pdiv[sum]++;
        numf = 1;

        // this is the magic that calculates the number of divisors,
        // the formula is as follows:
        //    osum = p1^v1 * p2^v2 * p3^v3 * ... * pk^vk
        // in short, a composite number is the product of prime numbers,
        // where each prime can occur 'v' times
        // to find the number of divisors, use this:
        //    d(n) = (v1 + 1)(v2 + 1)...(vk + 1)
        //
        // this is the reason I'm keepin the map, to keep a count of each
        // prime divisor, which serves as the 'v'
        //
        // thank you wikipedia:
        //    http://en.wikipedia.org/wiki/Divisor
        it = pdiv.begin();
        while (it != pdiv.end()) {
            numf *= it->second + 1;
            it++;
        }

        pdiv.clear();
        maxfc = max(numf, maxfc);

        if (maxfc > 500) {
            break;
        }
    }

    printf("%lld has maxfc = %d\n", osum, maxfc);

    return 0;
}

/* vim: set ts=4: */
