#ifndef ROUND_H
#define ROUND_H

#include "shape.h"
#include "rectangle.h"
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <iostream>
#include <string>
#include <sstream>

class Round: public Shape {
	public:
		Round(Rectangle* box, double sdg, double edg);
		double getStartDegree();
		double getEndDegree();
		Rectangle* getBoundedBox();
		void setStartDegree(double sdg);
		void setEndDegree(double edg);
		void setBoundedBox(Rectangle* box);
		void draw();
		std::string toString();
		virtual ~Round();
	private:
		Rectangle* boundedbox;
		double startdegree;
		double enddegree;

};

#endif
