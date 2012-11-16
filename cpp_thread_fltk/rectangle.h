#ifndef RECTANGLE_H
#define RECTANGLE_H

#include "point.h"
#include "shape.h"
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <iostream>
#include <string>
#include <sstream>

class Rectangle: public Shape {
	public:
		Rectangle(Point* p, int newwidth, int newheight, bool isFilled);
		int getWidth();
		int getHeight();
		Point* getBasePoint();
		bool getFilled();
		void setWidth(int newidth);
		void setHeight(int newheight);
		void setBasePoint(Point* p);
		void setFilled(bool isFilled);
		void draw();
		std::string toString();
		virtual ~Rectangle();
	private:
		Point* basepoint;
		bool filled;
		int width;
		int height;
};

#endif
