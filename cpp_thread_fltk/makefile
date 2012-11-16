CXX		= $(shell fltk-config --cxx)
DEBUG		= -g
CXXFLAGS	= $(shell fltk-config --use-gl --use-images --cxxflags) -Wall -I -lpthread
LDFLAGS		= $(shell fltk-config --use-gl --use-images --ldflags)
LDSTATIC	= $(shell fltk-config --use-gl --use-images --ldstaticflags)
LINK		= $(CXX)

#TARGET = testdraw
#OBJS = testdraw.o shape.o point.o rectangle.o line.o round.o triangle.o complexshape.o canvas.o
#SRCS = testdraw.cxx shape.cxx point.cxx rectangle.cxx line.cxx round.cxx triangle.o complexshape.cxx canvas.cxx

TARGETS = server
OBJSS = server.o serversocket.o socket.o shape.o point.o rectangle.o line.o round.o triangle.o complexshape.o canvas.o
SRCSS = server.cxx socket.cxx serversocket.cxx

TARGETC = client
OBJSC = client.o clientsocket.o socket.o
SRCSC = client.cxx socket.cxx clientsocket.cxx

.SUFFIXES: .o .cxx
%.o: %.cxx
	$(CXX) $(CXXFLAGS) $(DEBUG) -c $<

drawclient: $(TARGETC)
	$(LINK) -o $(TARGETC) $(OBJSC)
 
drawserver: $(TARGETS)
	$(LINK) -o $(TARGETS) $(OBJSS) $(LDSTATIC)

draw: 	$(TARGET)
	$(LINK) -o $(TARGET) $(OBJS) $(LDSTATIC)

all: client server draw

$(TARGET):$(OBJS)
testdraw.o: testdraw.cxx shape.h point.h rectangle.h line.h round.h complexshape.h canvas.h
shape.o: shape.cxx shape.h
point.o: point.cxx point.h shape.h
complexshape.o: complexshape.cxx complexshape.h shape.h
canvas.o: canvas.cxx canvas.h
rectangle.o: rectangle.cxx rectangle.h shape.h point.h
line.o: line.cxx line.h shape.h point.h
round.o: round.cxx round.h shape.h rectangle.h
triangle.o: triangle.cxx triangle.h shape.h point.h

$(TARGETS):$(OBJSS)
server.o: server.cxx shape.h point.h rectangle.h line.h round.h complexshape.h canvas.h serversocket.h socketexception.h
socket.o: socket.cxx socket.h
serversocket.o: serversocket.cxx serversocket.h socketexception.h
shape.o: shape.cxx shape.h
point.o: point.cxx point.h shape.h
complexshape.o: complexshape.cxx complexshape.h shape.h
canvas.o: canvas.cxx canvas.h
rectangle.o: rectangle.cxx rectangle.h shape.h point.h
line.o: line.cxx line.h shape.h point.h
round.o: round.cxx round.h shape.h rectangle.h
triangle.o: triangle.cxx triangle.h shape.h point.h


$(TARGETC):$(OBJSC)
client.o: client.cxx clientsocket.h socketexception.h
socket.o: socket.cxx socket.h
clientsocket.o: clientsocket.cxx clientsocket.h socketexception.h

clean:
	rm -f *.o > /dev/null
	rm -f $(TARGET) $(TARGETC) $(TARGETS) > /dev/null
