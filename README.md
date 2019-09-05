Multi-threading Online Dictionary
=================================

<img src="https://user-images.githubusercontent.com/53499528/64305964-7be2fa80-cfd4-11e9-8c61-6298ca8d4a28.png" width="480px">

<br>

<img src="https://user-images.githubusercontent.com/53499528/64306880-910d5880-cfd7-11e9-9a14-2d70e522f2e9.png" width="600px">

Overview
--------

The system allows users to use interactive interface for searching the meaning of word, adding a new word and removing an existing word without having inconsistent state.

Lowest level of abstraction of sockets and threads with thread pool are implemented for the system in order to connect server and client, and deal with concurrency. Also any errors are handled in the way that users can notice what kind of error occurs.

Usage
-----

### To start server
```
$ java -jar DictionaryServer.jar -p [port] -df [path]
```

`-p` : port number  
`df` : path to dictionary file from current directory

<br>

Dictionary file can be .json, .csv and .txt as long as written in following manner.
```
{
    "word1": ["meaning1"],
    "word2": ["meaning1", "meaning2", "meaning3"],
}
```

### To start client
```
$ java -jar DictionaryClient.jar -sa [server address] -sp [port]
```

`-sa` : server addreaa
`-sp` : port number  

