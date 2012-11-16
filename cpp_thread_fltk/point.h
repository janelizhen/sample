#ifndef POINT_H
#define POINT_H

#include "shape.h"
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <string>
#include <sstream>
#include <iostream>

class Point: public Shape {
	public:
		Point(int newx, int newy, Fl_Color c);
		int getX();
		int getY();
		Fl_Color getColor();
		void setX(int newx);
		void setY(int newy);
		void setColor(Fl_Color c);
		void draw();
		std::string toString();
		virtual ~Point();
	private:
		Fl_Color color;
		int x;
		int y;
};

#endif

