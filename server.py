import socket
import select
import time


def action(client_sock, read_data):
    time.sleep(1)
    if read_data.rstrip() == 'ok':
        client_sock.send('success!!!'.encode("ascii"))
    else:
        client_sock.send('not success!!!'.encode("ascii"))


# def action(client_sock, read_data):
#     #client_sock.send(read_data)
#     t=""


class SocketServer:
    """ Simple socket server that listens to one single client. """

    def __init__(self, host=socket.gethostname(), port=2010):
        """ Initialize the server with a host and port to listen to. """
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.host = host
        self.port = port
        self.sock.bind((host, port))
        self.sock.listen(1)

    def close(self):
        """ Close the server socket. """
        print('Closing server socket (host {}, port {})'.format(self.host, self.port))
        if self.sock:
            self.sock.close()
            self.sock = None

    def run_server(self):
        """ Accept and handle an incoming connection. """
        print('Starting socket server (host {}, port {})'.format(self.host, self.port))

        client_sock, client_addr = self.sock.accept()

        print('Client {} connected'.format(client_addr))
        client_sock.send('Im connected you: '.encode("ascii"))

        stop = False
        while not stop:
            if client_sock:

                # Check if the client is still connected and if data is available:
                try:
                    rdy_read, rdy_write, sock_err = select.select([client_sock, ], [], [])
                except select.error:
                    print('Select() failed on socket with {}'.format(client_addr))
                    return 1

                if len(rdy_read) > 0:
                    read_data = client_sock.recv(1024)
                    # Check if socket has been closed
                    if len(read_data) == 0:
                        print('{} closed the socket.'.format(client_addr))
                        stop = True
                    else:
                        print('>>> Received: {}'.format(read_data.rstrip().decode("ascii")))
                        if read_data.rstrip() == 'quit':
                            stop = True
                        else:
                            action(client_sock, read_data.decode("ascii"))

            else:
                print("No client is connected, SocketServer can't receive data")
                stop = True

        # Close socket
        print('Closing connection with {}'.format(client_addr))
        client_sock.close()
        return 0


def main():
    server = SocketServer(host="192.168.10.138")
    server.run_server()
    print('Exiting')


if __name__ == "__main__":
    main()
