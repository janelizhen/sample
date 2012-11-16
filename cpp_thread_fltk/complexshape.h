#ifndef COMPLEXSHAPE_H
#define COMPLEXSHAPE_H

class ComplexShape: public Shape {
	public:
		ComplexShape();
		void draw();
		virtual ~ComplexShape();
		std::string toString();
};

#endif
