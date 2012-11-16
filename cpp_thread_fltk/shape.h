#ifndef SHAPE_H
#define SHAPE_H

#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <FL/Fl.H>
#include <FL/fl_draw.H>

class Shape {
	public:
		Shape(bool isFinal);
		void addShape(Shape* s);
		std::vector<Shape*> explodeShape();
		virtual void draw();
		virtual std::string toString();
		virtual ~Shape();
		static Shape * parseShape(const std::string &);
	private:
		bool finalized;
		std::vector<Shape*> components;
};

#endif

