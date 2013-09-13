#include <stdio.h>
#include <stdint.h>
#include <math.h>

#include "euler.h"

int main(int argc, char *argv[])
{
   uint64_t num = 600851475143ULL;
   uint64_t fact;
   uint64_t maxfact = 0;

   while ((fact = factor(num)) != 1) {
       maxfact = max(maxfact, fact);
       num /= fact;
   }

   // our last number is also prime, so check if it's the largest.
   maxfact = max(num, maxfact);

   printf("maxfact = %lld\n", maxfact);
   return 0;
}

