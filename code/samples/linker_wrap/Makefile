#
# Makefile
#
# Developed by Drew Frezell
#

CC = g++
LDFLAGS = -static -Wl,--wrap=malloc -Wl,--wrap=free -Wl,--wrap=realloc -Wl,--wrap=calloc

test: test.o linker_wrap.o

clean:
	-rm -f test *.o core

