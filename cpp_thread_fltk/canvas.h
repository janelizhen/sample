#ifndef CANVAS_H
#define CANVAS_H

#include "shape.h"
#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/fl_draw.H>
#include <vector>

class Canvas : public Fl_Widget {
	public:
		Canvas(int X, int Y, int W, int H, Fl_Color B);
		void draw();
		void enqueueShape(Shape* s);
		std::string toString();
	private:
		Fl_Color background;
		std::vector<Shape*> shapes;
};

#endif
