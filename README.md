# Sudoku-

This is a simple program for solving sudoku puzzles of various difficulties in the least time available. The encouragement to
write this very program was gained from Project Euler problem #96. 

https://projecteuler.net/problem=96

The first solution implemented by myself used a brute force alghoritm to find the fitting combination of numbers that appropriate
to the rules of sudoku puzzle. Current implementation is an optimization which first uses a series of some known sudoku alghoritms to
fill as much before again attempting the brute force technique. While implementing these alghoritms following link was helpful as it 
explained the alghoritms and best way to combine them in order to solve a puzzle.

http://www.math.cornell.edu/~mec/Summer2009/meerkamp/Site/Introduction.html

The program takes the name of the file including the sudoku puzzles as argument.
The format of the file is left same as it is given in the problem. 
A sample for format is as following.
File Format Sample:
Grid 01
003020600
900305001
001806400
008102900
700000008
006708200
002609500
800203009
005010300
Grid 02
200080300
060070084
030500209
000105408
000000000
402706000
301007040
720040060
004010003
...





