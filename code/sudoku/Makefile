#
# Makefile
#
# Developed by Drew Frezell
#

PROGRAM = sudoku

# here add names of your source codes
SRCS = sudoku.cpp sudoku_board.cpp hash.cpp

# name of objects and header files is created
# _automaticaly_ from $(SOURCES) -- you don't need
# change this
OBJS       = $(sort $(patsubst %.cpp,%.o,$(SRCS)))
DEPS       = $(sort $(patsubst %.cpp,%.d,$(SRCS)))

CC       = $(CROSS_COMPILE)g++
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


