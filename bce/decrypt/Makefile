ifeq ($(shell uname), Linux)
  CC         = g++ -fPIC 
  TARGET     = liblinux.so
  LIBLINK    = -shared 
  INCLUDEDIR = -I $(shell echo ${JAVA_HOME})/include -I $(shell echo ${JAVA_HOME})/include/linux
endif

ifeq ($(shell uname), Darwin)
  CC		 = g++ 
  TARGET     = libdarwin.dylib
  LIBLINK    = -dynamiclib
  INCLUDEDIR = -I $(shell echo ${JAVA_HOME})/include -I $(shell echo ${JAVA_HOME})/include/darwin
endif

OBJS = decrypt.o

$(TARGET):$(OBJS)
	$(CC) $(LIBLINK) -o $(TARGET) $(OBJS)

decrypt.o: decrypt.cpp
	$(CC) $(INCLUDEDIR) -c decrypt.cpp

.PHONY:clean
clean:
	rm $(TARGET) $(OBJS)
