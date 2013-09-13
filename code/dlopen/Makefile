#
# Makefile
#
# Developed by Drew Frezell
#

PROGRAM=main
LIBRARY=libdllib.so

all: $(PROGRAM) $(LIBRARY)

$(PROGRAM): main.c
	gcc -o $@ $^ -ldl

$(LIBRARY): dllib.c
	gcc -shared -Wl,-soname,$@.1 -o $@ $^

clean:
	-rm -f $(PROGRAM) $(LIBRARY) *.o core


