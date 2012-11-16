#include "point.h"

// constructor
Point::Point(int newx, int newy, Fl_Color c): Shape(true) {
	x = newx;
	y = newy;
	color = c;
}

// accessors for x, y and color
int Point::getX() { return x; }
int Point::getY() { return y; }
Fl_Color Point::getColor() { return color; }
void Point::setX(int newx) { x = newx; }
void Point::setY(int newy) { y = newy; }
void Point::setColor(Fl_Color c) { color = c; }

// draw method
void Point::draw(){
	// draw with fltk functions
	fl_color(color);
	fl_point(x, y);
}

// toString method
std::string Point::toString(){
	std::stringstream ss;
	ss << "point ";
	uchar r, g, b;
	Fl::get_color(color, r, g, b);
	ss << (unsigned int)r << " " << (unsigned int)g << " " << (unsigned int)b << " ";
	ss << x << " " << y << std::endl;
	return ss.str();
}

// abstract deconstructor
Point::~Point() {
	std::cout << "Point::~Point()" << std::endl;
}
