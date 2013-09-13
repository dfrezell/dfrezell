/*
 * test.cpp
 *
 * Developed by Drew Frezell
 *
 */

#include <string>

using namespace std;

class TestClass {
public:
    TestClass(const char *name) : m_i(32), m_j(64), m_name(name) {
    }
    virtual ~TestClass() {}

    virtual string GetName() { return m_name; }
private:
    int m_i;
    int m_j;
    string m_name;
};

int main(int argc, char *argv[])
{
    TestClass *test = new TestClass(__FUNCTION__);
    unsigned char *ptr = NULL;

    ptr = (unsigned char *)malloc(1000);
    memcpy(ptr, test, sizeof(*test));
    printf("name = %s\n", test->GetName().c_str());
    for (int i = 0; i < sizeof(*test); i++) {
        printf("0x%2.2x%c", ptr[i], ((i % 16) == 15) ? '\n' : ' ');
    }
    free(ptr);
    delete test;

    return 0;
}

// vim: set ts=4:

