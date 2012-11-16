#ifndef CLIENTSOCKET_H
#define CLIENTSOCKET_H

#include "socket.h"

class ClientSocket : private Socket{
	public:
		ClientSocket(std::string host, int port);
		virtual ~ClientSocket(){};

		const ClientSocket& operator << (const std::string&) const;
		const ClientSocket& operator >> (std::string&) const;
};

#endif

