#include "shape.h"
#include "canvas.h"
#include <iostream>

// constructor
Canvas::Canvas(int X,int Y,int W,int H,Fl_Color B): Fl_Widget(X,Y,W,H){
	background = B;
}

// draw method
void Canvas::draw(){
	fl_push_clip(x(), y(), w(), h());

	fl_color(background);
	fl_rectf(x(), y(), w(), h());

	//TODO: actually draw a shape here
	for (unsigned int i=0; i<shapes.size(); i++){
		shapes[i]->draw();
	}

	fl_pop_clip();
}

// enqueueShape method
void Canvas::enqueueShape(Shape* s){
	shapes.push_back(s);
	set_changed();
}

// toString method
std::string Canvas::toString(){
	std::stringstream ss;
	for (unsigned int i=0; i< shapes.size(); ++i){
		ss << shapes[i]->toString();
	}
	return ss.str();
}

