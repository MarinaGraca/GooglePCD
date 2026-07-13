# GooglePCD
### PCD Project - Concurrent and Distributed Programming
### 3º Year, 2º Semester | 2019-2020

We want to develop a desktop grid which allows us to search words in a large news set.\
We'll have a Client where we can write the word to look for.\
Clients connect to the Server, send it the Tasks they want executed by the Workers, and wait for the Server to send them the execution results.

Start the Server: `java Server news`\
Output:
```
Server running
648 files in the folder
Server socket: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=9090]
```

Start the Client: `java Client localhost`\
Output:
```
Client thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=9090,localport=52680]
Address: localhost/127.0.0.1
```

GUI specs:
* textbox where the user writes the word/expression to find
* button to start the search
* search result in news list, with number of occurrences and title of the news file, in descending order
* text area where the text from the selected news file is shown

Start the Worker: `java Worker localhost`\
Output:
```
Worker thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=9090,localport=52683]
Address: localhost/127.0.0.1
```

```
Server running
648 files in the folder
Server socket: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=9090]
Connection accepted on socket: Socket[addr=/127.0.0.1,port=52680,localport=9090]
Connection accepted on socket: Socket[addr=/127.0.0.1,port=52683,localport=9090]
```
Usage:
1. On the textbox on the top, enter the word/expression you want to look for (**hoje**)
2. Click on the Search button and wait for the results

```
Client thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=9090,localport=52680]
Address: localhost/127.0.0.1
Looking for: hoje
Client looking for hoje
Searching...
648 files searched!
310 files matched!
```
