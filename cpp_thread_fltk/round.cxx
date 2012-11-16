#include "round.h"

// constructor
Round::Round(Rectangle* box, double sdg, double edg): Shape(true) {
	boundedbox = box;
	startdegree = sdg;
	enddegree = edg;
}

// abstract deconstructor
Round::~Round(){
	std::cout << "Round::~Round()" << std::endl;
}

// draw method
void Round::draw(){
	Point *bp = boundedbox->getBasePoint();
	fl_color(bp->getColor());
	if (boundedbox->getFilled()){
		fl_pie(bp->getX(), bp->getY(),
		       boundedbox->getWidth(), boundedbox->getHeight(),
		       startdegree, enddegree
		);
	}
	else{
		fl_arc(bp->getX(), bp->getY(),
		       boundedbox->getWidth(), boundedbox->getHeight(),
		       startdegree, enddegree
		);
	}
}

// toString method
std::string Round::toString(){
	std::stringstream ss;
	ss << "round ";
	uchar r, g, b;
	Point *bp = boundedbox->getBasePoint();
	Fl::get_color(bp->getColor(), r, g, b);
	ss << (unsigned int)r << " " << (unsigned int)g << " " << (unsigned int)b << " ";
	ss << bp->getX() << " " << bp->getY() << " ";
	ss << boundedbox->getWidth()  << " " << boundedbox->getHeight() << " " << boundedbox->getFilled() << " ";
	ss << startdegree << " " << enddegree << std::endl;
	return ss.str();
}

// accessors
double Round::getStartDegree() { return startdegree; }
double Round::getEndDegree() { return enddegree; }
Rectangle* Round::getBoundedBox() { return boundedbox; }
void Round::setStartDegree(double sdg) { startdegree = sdg; }
void Round::setEndDegree(double edg) { enddegree = edg; }
void Round::setBoundedBox(Rectangle* box) { boundedbox = box; }

