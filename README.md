# Tic Tac Toe
The famous old game so simple that everyone can play. All you need to do is get three in a line.. by row , by column even or by diagonal.

## Requirements
##### Maven installed
##### java 17
##### Postman or Curl

## How to play?
Easy!! Clone the code or download the release and you get a zip file with the code
and run the command

` mvn spring-boot:run `

What is then happening is server starts up on port 80 (this just to make it simple when you send your requests)

### Setup the Game
Now first setup the game, I am going to show with Curl command

    curl --location --request POST 'localhost/api/game' \
    --header 'Content-Type: application/json' \
    --data '{
        "name":"Player",
        "piece":"x"
    }'

Response

    {
        "gameId": "d5b8c55c-06cb-475a-9db5-e84608ed7fb0"
    }


Here Player 1 is created and also and Computer player is created but game is not started.
The response you get is importent since you going to need it to make moves or join in a Player 2.

### Make player VS player game
For join second player send after you have gotten the id, use this Curl cmd and paste in the id. Switch out the name of the player to your likeing

    curl --location --request PUT 'localhost/api/{id}/join' \
    --header 'Content-Type: application/json' \
    --data '{
        "name":"Player2"
    }'
And response you get is

    {
        "name": "Player2",
        "piece": "O",
        "message": "Two player game set! X makes first move"
    }

### Make moves
The board in the game have 9 positions
3 rows and 3 columns.
The game starts when a request for a move is sent and a join request will be invalid at that point.
If a join request is sent response is

    {
        "message": "Game already started"
    }


So to make a request postion to row 1 and colum 1 would look like this

    curl --location --request PUT 'localhost/api/{id}/move' \
    --header 'Content-Type: application/json' \
    --data '{
        "piece":"x",
        "row":1,
        "column":1
    }'

and the response would be

     {
        "message": "Current status of game",
        "pieceX": [
            {
                "row": 1,
                "column": 1
            }
        ],
        "pieceO": []
    }

now is player 1 try to a move before player 2 it will respond with.

    {
        "message": "Wrong players turn!"
    }

If player 2 tries to make move on already taken postion resonse would be..

    {
        "message": "Move already done"
    }

When player 2 makes a legit move it is player 1 turn

    curl --location --request PUT 'localhost/api/d5b8c55c-06cb-475a-9db5-e84608ed7fb0/move' \
    --header 'Content-Type: application/json' \
    --data '{
        "piece":"o",
        "row":1,
        "column":2
    }'

Response

    {
        "message": "Current status of game",
        "pieceX": [
            {
                "row": 1,
                "column": 1
            }
        ],
        "pieceO": [
            {
                "row": 1,
                "column": 2
            }
        ]
    }

Game end when either this response comes

    {
        "message": "Player made a winning move""
    }

or

     {
        "message": "GAME OVER! No more moves possible"
     }

### Player VS Computer
The only diffrence here is that a join request is not needed. Computer player is allways created and a join request allways overwrite the Player 2.
When Player makes a move Computer player takes a random position that has not been taken and send it back in the response

    {
        "message": "Current status of game",
        "pieceX": [
            {
                "row": 1,
                "column": 1
            }
        ],
        "pieceO": [
            {
                "row": 3,
                "column": 1
            }
        ]
    }

And it is allways Players next turn
# Enjoy the game!