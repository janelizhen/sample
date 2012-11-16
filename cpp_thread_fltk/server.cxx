#include "shape.h"
#include "point.h"
#include "rectangle.h"
#include "line.h"
#include "round.h"
#include "triangle.h"
#include "complexshape.h"
#include "canvas.h"

#include "serversocket.h"
#include "socketexception.h"
#include <cstdlib>
#include <string.h>
#include <iostream>
#include <pthread.h>
#include <unistd.h>
#include <FL/Fl.H>
#include <FL/fl_draw.H>

#define PORT	60000

Shape *toShape(const std::string &);
void *startCanvas(void *);

Canvas *c;

int main(int argc, char *argv[]){

	Fl_Double_Window window(600, 600);
	Canvas canvas(0, 0, 600, 600, FL_WHITE);
	c = &canvas;

	window.end();
	window.show(argc,argv);

	pthread_t child;
	pthread_create(&child, NULL, startCanvas, NULL );

	std::cout << "running..." << std::endl;
	try{
		ServerSocket server(60000);

		while (true){
			ServerSocket new_sock;
			server.accept(new_sock);

			try{
				while(true){
					std::string data;
					new_sock >> data;
					if (data.compare("recv") == 0){
						new_sock << c->toString();
					}
					else{
						c->enqueueShape(Shape::parseShape(data));
						c->redraw();
						Fl::flush();
					}
				}
			}
			catch(SocketException&){}
		}
	}
	catch(SocketException& e){
		std::cout << "Exception was caught:" << e.description() << std::endl << "Exiting..." << std::endl;
	}

	return 0;
}

void *startCanvas(void *){
	return (void *) Fl::run();
}


