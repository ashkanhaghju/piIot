import socket
import select

HOST = "192.168.10.138"  # Enter IP or Hostname of your server
PORT = 2010  # Pick an open Port (1000+ recommended), must match the server port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

# Lets loop awaiting for your input
# while True:
#     command = str(input('Enter your command: '))
#     s.send(command.encode("ascii"))
#     reply = s.recv(2048).decode("ascii")
#     print(reply)



for i in range(1,10):
    print(i)



stop = False
while not stop:

        # Check if the client is still connected and if data is available:
        try:
            rdy_read, rdy_write, sock_err = select.select([s, ], [], [])
        except select.error:
            print("Select failed ")

        if len(rdy_read) > 0:
            read_data = s.recv(1024)
            # Check if socket has been closed
            if len(read_data) == 0:
                print(' closed the socket.')
                stop = True
            else:
                print('>>> Received: {}'.format(read_data.rstrip().decode("ascii")))
                if read_data.rstrip() == 'quit':
                    stop = True
                else:
                    o=input("Enter your command: ")
                    print(o)
                    s.send(o.encode("ascii"))

# Close socket
print('Closing connection with {}'.format(s))
s.close()
