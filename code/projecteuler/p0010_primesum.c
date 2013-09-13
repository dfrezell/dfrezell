#include <stdio.h>
#include <stdint.h>

#include "euler.h"

int main(int argc, char *argv[])
{
   uint64_t sum = 5;
   uint64_t prime = 5;

   while (prime < 2000000) {
      if (isprime(prime)) {
         sum += prime;
      }
      prime += 2;
   }

   printf("sum = %lld\n", sum);
   return 0;
}

