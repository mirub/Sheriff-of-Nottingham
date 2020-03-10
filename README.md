Student: Banu Miruna Elena
Group: 321CA

   For this homework I have implemented the simplified version of the boardgame
Sheriff of Nottingham in Java using the basic concepts of object-oriented
programming. This version was based on three different types of player strategy:
basic, greedy and bribed.

The Basic Strategy:
   The basic player is the playes which is always honest, thus I have created
their bag by checking which legal good in their hand is the most profitable and
has the biggest id. Unless they have illegal cards, case where they select the
most profitable card out the pack and declare it apple, the basic player will
send the bag with the items as above mentioned chosen. 
   When their role is the sheriff, the basic player will check everyone's bag
regardless the fact that they can end up without money. If this happens (the
player has 16 coins left, they will cease the checking process and their role
as sheriff ends.

The Greedy Strategy:
   The greedy player emulates the basic strategy, the sole difference being
that, in even rounds, they add another illegal card.
   When they play as the sheriff, they will accept bribe from everyone that
offers it. Otherwise, they will check the player the basic way.

The Bribe Strategy:
   The bribed player has a strategy different to the others, meaning that:
they will add every illegal card they possess (if they have money to pay the
eventual penalty they get) and complete the bag with the most profitable legal
goods they possess (being also limited by the eventual penalty).
   When they play as sheriff, they will only check the players on their left
and right (regardless of the existence of bribe) and will collect bribe from
the other players if it exist (if it does not, they will leave the player
alone).

The Main Game Logic:
   I have implemented a class that contains the game logic where I create arrays
for the players and cards, set the player types and browse through the rounds and
each player to play their respective role. I have also implemented here the King
and Queen bonus function which gives the players the rightful bonus at the end of
the game. 
