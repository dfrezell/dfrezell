#
# Makefile
#
# Developed by Drew Frezell
#

PROGRAM = regdump

# here add names of your source codes
SRCS = regdump.c

# name of objects and header files is created
# _automaticaly_ from $(SOURCES) -- you don't need
# change this
OBJS       = $(sort $(patsubst %.c,%.o,$(SRCS)))
DEPS       = $(sort $(patsubst %.c,%.d,$(SRCS)))

CC       = $(CROSS_COMPILE)gcc
CXX      = $(CROSS_COMPILE)g++
AR       = $(CROSS_COMPILE)ar

#XTRA_FLAGS = -g -ggdb -DDEBUG
#XTRA_FLAGS = -O2 
CFLAGS   = -Iinclude -Wall -Wshadow $(XTRA_FLAGS)
CPPFLAGS = -Wall -Wshadow $(XTRA_FLAGS)
LDFLAGS  =
DEFS     =
INCLUDES = 
LIBS     =

all: $(PROGRAM)
	
$(PROGRAM): $(OBJS)

clean:
	-rm -f *.o core

