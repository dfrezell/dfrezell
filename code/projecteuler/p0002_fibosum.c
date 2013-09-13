#include <stdio.h>

int main(int argc, char *argv[])
{
   unsigned int sum = 0;
   int i = 2;
   int val = 0;

   for (i = 2; val <= 4000000; i++) {
      val = fibo(i);
      if (!(val & 0x01)) {
         sum += val;
      }
   }
   printf("sum = %d\n", sum);
   return 0;
}

