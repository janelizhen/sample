#include "shape.h"
#include "point.h"
#include "line.h"
#include "triangle.h"
#include "rectangle.h"
#include "round.h"

// constructor
Shape::Shape(bool isFinal){
	finalized = isFinal;
	if (finalized)
		components.push_back(this);
}

// addToShape method
void Shape::addShape(Shape* s){
	if (finalized){
		throw "cannot add shape to a finalized shape";
	}
	else{
		components.push_back(s);
	}
}

// explodeShape method
std::vector<Shape*> Shape::explodeShape(){
	return components;
}

// abstract draw method
void Shape::draw(){
	return;
}

// abstract toString method
std::string Shape::toString(){
	return std::string();
}

// abstract deconstructor
Shape::~Shape() {
	std::cout << "Shape::~Shape()" << std::endl;
}

// static parseShape method
Shape *Shape::parseShape(const std::string &message){
	std::string token;
	std::stringstream ss(message);

	std::string shape;
	double params[10];
	int i = -1;
	while (getline(ss, token, ' ')){
		if (i == -1) {
			shape = token;
		}
		else{
			std::stringstream stm;
			stm.str(token);
			stm >> params[i];
		}
		i++;
	}	
	
	Fl_Color color = fl_rgb_color(params[0], params[1], params[2]);

	Shape *s;
	if (shape.compare("point") == 0){
		s = new Point(params[3], params[4], color);
	}
	if (shape.compare("line") == 0){
		s = new Line(new Point(params[3], params[4], color),
			     new Point(params[5], params[6], color));
	}
	if (shape.compare("rectangle") == 0){
		s = new Rectangle(new Point(params[3], params[4], color),
				  params[5], params[6], params[7]);
	}
	if (shape.compare("round") == 0){
		s = new Round(new Rectangle(new Point(params[3], params[4], color),
					    params[5], params[6], params[7]),
					    params[8], params[9]);
	}
	if (shape.compare("triangle") == 0){
		s = new Triangle(new Point(params[3], params[4], color),
				 new Point(params[5], params[6], color),
				 new Point(params[7], params[8], color));
	}

	return s;
}

