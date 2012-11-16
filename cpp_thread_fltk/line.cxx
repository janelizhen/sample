#include "line.h"

// constructor
Line::Line(Point* bp, Point* ep): Shape(true) {
	basepoint = bp;
	endpoint = ep;
}

// abstract deconstructor
Line::~Line(){
	std::cout << "Line::~Line()" << std::endl;
}

// draw method
void Line::draw(){
	fl_color(basepoint->getColor());
	fl_line(basepoint->getX(), basepoint->getY(), basepoint->getX(), basepoint->getY());
}

// toString method
std::string Line::toString(){
	std::stringstream ss;
	ss << "line ";
	uchar r, g, b;
	Fl::get_color(basepoint->getColor(), r, g, b);
	ss << (unsigned int)r << " " << (unsigned int)g << " " << (unsigned int)b << " ";
	ss << basepoint->getX() << " " << basepoint->getY() << " " << endpoint->getX() << " " << endpoint->getY() << std::endl;
	return ss.str();
}

// accessors
Point* Line::getBasePoint(){ return basepoint; }
Point* Line::getEndPoint() { return endpoint; }
void Line::setBasePoint(Point* bp) { basepoint = bp; }
void Line::setEndPoint(Point* ep) { endpoint = ep; }

