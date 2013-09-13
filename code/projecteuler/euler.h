#ifndef EULER_H
#  define EULER_H

#ifdef __cplusplus
extern "C" {
#endif // __cplusplus

#include <stdint.h>

#define max(a,b) (((a) > (b)) ? (a) : (b))
#define min(a,b) (((a) < (b)) ? (a) : (b))

// table containing all primes up to 9999
extern int prime_table[];
extern int PRIME_TBL_LEN;

int isprime(uint64_t n);
uint64_t lcm(uint64_t n, uint64_t m);
uint64_t gcd(uint64_t n, uint64_t m);
uint64_t factor(uint64_t n);
// return the nth fibonacci number
uint64_t fibo(unsigned int num);

#ifdef __cplusplus
}
#endif // __cplusplus

#endif /* #ifndef EULER_H */

