# Igrica-2

Arkanoid is desktop Java application. Arkanoid is a game where the user passes level, 
expensive points on hits target and the star. Each level generates targets in different form and order.  
Each target has randomly generated color and each color given different numbers of points. 
In the game included two hidden targets which can be 'good' or 'bad'. If user hit the 'good' target in the game, 
he gets one more new ball on the board. If he hit the bad target, the racket of player was slowing on 2 seconds. 
The game was followed by sounds and messages which is displayed to the player. Main functionality is mutlithreading. 
The game has 4 main threads which processed independent functionality. Main process works on drawing messages about level, 
number of lives and main messages for the user. Detect collision of the racket of user, balls and targets is a special process. 
Each ball is a new process and they work smoother than others. If a player has more than one ball in a game round, 
the first ball is accelerated. 
Complete code was developed from beginning and not used any of game engines. 
