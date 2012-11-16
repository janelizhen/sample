#ifndef LINE_H
#define LINE_H

#include "shape.h"
#include "point.h"
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <sstream>
#include <string>
#include <iostream>

class Line: public Shape {
	public:
		Line(Point* bp, Point* ep);
		Point* getBasePoint();
		Point* getEndPoint();
		void setBasePoint(Point* bp);
		void setEndPoint(Point* ep);
		void draw();
		std::string toString();
		virtual ~Line();
	private:
		Point* basepoint;
		Point* endpoint;
};

#endif
