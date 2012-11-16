#include "triangle.h"

// constructor
Triangle::Triangle(Point* p1, Point* p2, Point* p3): Shape(true) {
	point1 = p1;
	point2 = p2;
	point3 = p3;
}

// abstract deconstructor
Triangle::~Triangle(){
	std::cout << "Triangle::~Triangle()" << std::endl;
}

// draw method
void Triangle::draw(){
	fl_color(point1->getColor());
	fl_polygon(point1->getX(), point1->getY(),
		   point2->getX(), point2->getY(),
		   point3->getX(), point3->getY()
	);
}

// toString method
std::string Triangle::toString(){
	std::stringstream ss;
	ss << "triangle ";
	uchar r, g, b;
	Fl::get_color(point1->getColor(), r, g, b);
	ss << (unsigned int)r << " " << (unsigned int)g << " " << (unsigned int)b << " ";
	ss << point1->getX() << " " << point1->getY() << " ";
	ss << point2->getX() << " " << point2->getY() << " ";
	ss << point3->getX() << " " << point3->getY() << std::endl;
	return ss.str();
}

// accessors
Point* Triangle::getPoint1(){ return point1; }
Point* Triangle::getPoint2() { return point2; }
Point* Triangle::getPoint3() { return point3; }
void Triangle::setPoint1(Point* p) { point1 = p; }
void Triangle::setPoint2(Point* p) { point2 = p; }
void Triangle::setPoint3(Point* p) { point3 = p; }

