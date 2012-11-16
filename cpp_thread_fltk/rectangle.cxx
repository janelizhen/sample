#include "rectangle.h"

// constructor
Rectangle::Rectangle(Point* p, int newwidth, int newheight, bool isFilled): Shape(true) {
	basepoint = p;
	width = newwidth;
	height = newheight;
	filled = isFilled;
}

// accessors to width and height
int Rectangle::getWidth() { return width; }
int Rectangle::getHeight() { return height; }
Point* Rectangle::getBasePoint() { return basepoint; }
bool Rectangle::getFilled() { return filled; }
void Rectangle::setWidth(int newwidth) { width = newwidth; }
void Rectangle::setHeight(int newheight) { height = newheight; }
void Rectangle::setBasePoint(Point* p) { basepoint = p; }
void Rectangle::setFilled(bool isFilled) { filled = isFilled; }

// draw method
void Rectangle::draw(){
	fl_color(basepoint->getColor());
	if (filled){
		fl_rectf(basepoint->getX(), basepoint->getY(), getWidth(), getHeight());
	}
	else{
		fl_rect(basepoint->getX(), basepoint->getY(), getWidth(), getHeight());
	}
}

// toString method
std::string Rectangle::toString(){
	std::stringstream ss;
	ss << "rectangle ";
	uchar r, g, b;
	Fl::get_color(basepoint->getColor(), r, g, b);
	ss << (unsigned int)r << " " << (unsigned int)g << " " << (unsigned int)b << " ";
	ss << basepoint->getX() << " " << basepoint->getY() << " " << width  << " " << height << " " << filled << std::endl;
	return ss.str();
}
 
// abstract deconstructor
Rectangle::~Rectangle(){
	std::cout << "Rectangle::~Rectangle()" << std::endl;
}

