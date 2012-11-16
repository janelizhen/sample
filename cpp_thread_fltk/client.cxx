#include "clientsocket.h"
#include "socketexception.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <cstdlib>

#define PORT	60000

void getFileInfo(const std::string &, std::string &, bool &);

int main (int argc, char* argv[]){
	try{
		if (argc < 2){
			std::cout << "Usage: " << argv[0] << " hostname" << std::endl;
			return 0;
		}
		ClientSocket client_socket (argv[1], 60000);

		std::string message;
		std::ifstream ifs;
		std::ofstream ofs;
		bool mode = true;	// stdin mode
		bool filemode = true;	// send file mode
		std::string filename;

		try{
			while (true){
				if (mode){
					// stdin mode
					std::cout << "Draw> ";
					getline(std::cin, message);
					if (message.compare("close") == 0)
						return 0;
					if (message.compare("file") == 0){
						mode = false;
					}
					else{
						client_socket << message;
					}
				}
				else{
					// file mode
					std::cout << "File> ";
					getline(std::cin, message);

					if (message.compare("close") == 0)
						return 0;
					if (message.compare("draw") == 0){
						mode = true;
					}
					else{
						getFileInfo(message, filename, filemode);

						if (filemode){
							//send file
							ifs.open(filename.c_str());
							while (!ifs.eof()){
								getline(ifs, message);
								client_socket << message;
							}
							ifs.close();
						}
						else {
							//receive file
							ofs.open(filename.c_str());
							client_socket << "recv";
							client_socket >> message;
							ofs << message;
							ofs.close();
						}
					}
				}
			}
		}
		catch (SocketException&) {}

	}
	catch (SocketException& e){
		std::cout << "Exception was caught: " << e.description() << std::endl;
	}

	return 0;
}

void getFileInfo(const std::string &message, std::string &filename, bool &filemode){
	std::stringstream ss(message);
	std::string token;

	int i = 0;
	while (getline(ss, token, ' ')){
		// "send" send a shape file to draw on server
		// "recv" receive shapes drawn on server to a file
		if (i == 0) filemode = (token.compare("send")==0);
		else filename = token;
		i++;
	}

	return;
}
