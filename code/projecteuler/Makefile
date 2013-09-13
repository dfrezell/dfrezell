#
# Makefile
#

CSRCS       = $(wildcard p*.c)
CPROG       = $(sort $(patsubst %.c,%,$(CSRCS)))
CXXSRCS     = $(wildcard p*.cpp)
CXXPROG     = $(sort $(patsubst %.cpp,%,$(CXXSRCS)))

CC       = $(CROSS_COMPILE)gcc
CXX      = $(CROSS_COMPILE)g++
AR       = $(CROSS_COMPILE)ar

XTRA_FLAGS = -g -ggdb -DDEBUG
#XTRA_FLAGS = -O2 
CPPFLAGS   = -Wall -Wshadow $(XTRA_FLAGS)
LDFLAGS  = -lm
DEFS     =
INCLUDES =
LIBS     =

all: $(CPROG) $(CXXPROG)

$(CPROG): euler.o

$(CXXPROG): euler.o

clean:
	-rm -f *.o core $(CPROG) $(CXXPROG)
	-rm -rf *.dSYM
