# Multi-threaded-voting-system
Multi-threaded client and server application

## Compilation
### Compile all client-side files:
```
cd cwk/client
javac *.java
```
### Compile all server-side files:
```
cd ../server
javac *.java
```

## Running the server:
```
cd cwk/server
java <Voting Options>
```

There should be at least 2 voting options. <br>
Example usage:
```
cd cwk/server
java Server rabbit squirrel
```

## Running the Client
Listing votes:
```
cd cwk/client
java Client list
```
Casting a vote:
```
java Client vote <Vote Option>
```

There should only be one vote casted <br>
Example Usage:
```
java Client vote Rabbit
```