/*
 * p0017_numletter.cpp
 *
 */

#include <map>
#include <string>

using namespace std;

int main(int argc, char *argv[]) {
    // prime the map with the strings.
    map<int,string*> nmap;
    int count = 0;
    string *word;
    int i;

    nmap[0] = new string("");
    nmap[1] = new string("one");
    nmap[2] = new string("two");
    nmap[3] = new string("three");
    nmap[4] = new string("four");
    nmap[5] = new string("five");
    nmap[6] = new string("six");
    nmap[7] = new string("seven");
    nmap[8] = new string("eight");
    nmap[9] = new string("nine");
    nmap[10] = new string("ten");
    nmap[11] = new string("eleven");
    nmap[12] = new string("twelve");
    nmap[13] = new string("thirteen");
    nmap[14] = new string("fourteen");
    nmap[15] = new string("fifteen");
    nmap[16] = new string("sixteen");
    nmap[17] = new string("seventeen");
    nmap[18] = new string("eighteen");
    nmap[19] = new string("nineteen");
    nmap[20] = new string("twenty");
    nmap[30] = new string("thirty");
    nmap[40] = new string("forty");
    nmap[50] = new string("fifty");
    nmap[60] = new string("sixty");
    nmap[70] = new string("seventy");
    nmap[80] = new string("eighty");
    nmap[90] = new string("ninety");
    nmap[100] = new string("hundred");
    nmap[1000] = new string("thousand");

    string concat;
    int ones;
    int tens;
    int hundreds;
    int thousands;

    for (i = 1; i <= 1000; i++) {
        word = nmap[i];
        concat.clear();

        ones = i % 10;
        tens = ((i % 100) / 10) * 10;
        hundreds = ((i % 1000) / 100) * 100;
        thousands = ((i % 10000) / 1000) * 1000;

        if (word) {
            if (thousands) {
                concat += *nmap[1];
            }
            if (hundreds) {
                concat += *nmap[hundreds/100];
            }
            concat += *word;
            printf("%s is %d letters\n", concat.c_str(), concat.size());
            count += concat.size();
        } else {
            if (hundreds) {
                concat += *nmap[hundreds/100];
                concat += *nmap[100];
            }

            if ((ones + tens) == 0) {
                // do nothing
            } else if (ones + tens < 20) {
                if (hundreds) {
                    concat += "and";
                }
                concat += *nmap[ones + tens];
            } else {
                if (hundreds) {
                    concat += "and";
                }
                concat += *nmap[tens];
                concat += *nmap[ones];
            }
            printf("%s is %d letters\n", concat.c_str(), concat.size());
            count += concat.size();
        }
    }

    printf("count = %d\n", count);

    return 0;
}

// vim: set ts=4:
