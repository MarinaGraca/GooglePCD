# GooglePCD
### PCD Project - Concurrent and Distributed Programming
### 3º Year, 2º Semester | 2019-2020

We want to develop a desktop grid which allows us to search words in a large news set.\
We'll have a Client where we can write the word to look for.\
Clients connect to the Server, send it the Tasks they want executed by the Workers, and wait for the Server to send them the execution results.

Start the Server: `java Server news`
Start the Client: `java Client localhost`

GUI specs:
* textbox where the user writes the word/expression to find
* button to start the search
* search result in news list, with no of occurrences and title of the news file
* text area where the text from the selected news file is shown

Start the Worker: `java Worker localhost`\
(...)
