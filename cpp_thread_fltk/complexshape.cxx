#include <iostream>
#include "shape.h"
#include "complexshape.h"

ComplexShape::ComplexShape() : Shape(false){}

void ComplexShape::draw(){
	std::vector<Shape*> components = explodeShape();
	for (unsigned int i=0; i<components.size(); i++){
		components[i]->draw();
	}
}

ComplexShape::~ComplexShape(){
	std::cout << "ComplexShape::~ComplexShape()" << std::endl;
}

std::string ComplexShape::toString(){
	std::stringstream ss;
	ss << "complex" << std::endl;
	std::vector<Shape*> components = explodeShape();
	for (unsigned int i=0; i<components.size(); i++){
		ss << components[i]->toString();
	}
	ss << "endcomplex" << std::endl;
	return ss.str();
}


