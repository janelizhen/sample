#ifndef TRIANGLE_H
#define TRIANGLE_H

#include "shape.h"
#include "point.h"
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <iostream>
#include <string>
#include <sstream>

class Triangle: public Shape {
	public:
		Triangle(Point* p1, Point* p2, Point* p3);
		Point* getPoint1();
		Point* getPoint2();
		Point* getPoint3();
		void setPoint1(Point* p);
		void setPoint2(Point* p);
		void setPoint3(Point* p);
		void draw();
		std::string toString();
		virtual ~Triangle();
	private:
		Point* point1;
		Point* point2;
		Point* point3;
};

#endif
