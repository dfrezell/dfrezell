#
# Makefile
#
# Developed by Drew Frezell
#

PROGRAM = test

# here add names of your source codes
SRCS = test.c

# name of objects and header files is created
# _automaticaly_ from $(SOURCES) -- you don't need
# change this
OBJS       = $(sort $(patsubst %.c,%.o,$(SRCS)))
DEPS       = $(sort $(patsubst %.c,%.d,$(SRCS)))

CC       = $(CROSS_COMPILE)gcc
CXX      = $(CROSS_COMPILE)g++
AR       = $(CROSS_COMPILE)ar

XTRA_FLAGS = -g -DDEBUG
#XTRA_FLAGS = -O2 
DEFS     =
INCLUDES =
CFLAGS   = -Wall -Wshadow $(DEFS) $(INCLUDES) $(XTRA_FLAGS)
CXXFLAGS = -Wall -Wshadow $(DEFS) $(INCLUDES) $(XTRA_FLAGS)

LIBS     =
LDFLAGS  = $(LIBS)

all: $(PROGRAM)
	
$(PROGRAM): $(OBJS)

clean:
	-rm -f $(PROGRAM) *.o core

