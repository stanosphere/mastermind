# Mastermind

Mastermind is a fantastic board game from the 70s all about breaking codes. I played it a little on holiday the other week. To see how to play please do follow this [link](https://en.wikipedia.org/wiki/Mastermind_(board_game\) "Wiki Page").

Since I had to quarantine after the holiday I thought I'd have a go at implementing some algorithms to solve mastermind. The version I played on holiday was the classic version with 4 holes and 6 colours so that's what I'll be solving. I thought it might be interesting for each algorithm if I looked at the worst case for number of guesses needed and also the different types of average guesses needed.

Also I kind of just wanted to practice my scala!

Maybe later I'll try this sort of thing for a game of mastermind with `n` holes and `m` colours (although I'd be surprised if there isn't a paper out there somewhere about this already!)

## Classic Mastermind

In this version there are 4 holes and 6 colours and we don't allow blanks. So that's 6 ^ 4 = 1296 possible codes. 

Since in this version there are 4 holes there are also 4 feedback holes. 
A feedback hole can either have a Black peg, a White peg, or be blank; there are 3 states each hole can have.
So how many feedbacks are possible in total?
- Let's use the standard "stars and bars" [trick](https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics\))
- There are 3 states a hole can have so that corresponds to 3 "buckets" (Black, White, Blank)
- Order the buckets like this: Black on the right, White in the middle, and Blank on the left
- We use bars (`|`) to separate the buckets 
- And then we have to choose which bucket each of our 4 "peg"s can go in
- Easiest to see by example
    - ★|★|★★ - corresponds to 1 Black peg, 1 White peg, and 2 empty holes
    - ★|★★★| - corresponds to 1 Black peg, 3 White pegs, and no empty holes
    - ★★★★|| - corresponds to 4 Black pegs: a win!
    - etc...
- Great! So how many will that be in total?
    - There are 6 total items
    - There are 6! ways to arrange 6 distinct items
    - However the 2 bars are identical so we don't care if we swap them so we can divide by 2!
    - Similarly the stars are identical so we don't care how they are ordered so we divide by 4!
    - Giving us 6! / (4! * 2!) = 6 * 5 / 2 = 3 * 5 = 15
- In general for a game with `n` holes the answer will be (n + 2)! / (n! * 2!) 
- = (n + 1) * (n + 2) / 2

The way both these algorithms work is to start with an eligible set of possibilities for what the hidden code could be and then at each step try to whittle this eligible set down until we guess the correct hidden code. In the next two sections I'll describe how each algorithm works.

### Brute Force

1. Start be guessing `Code(White, White, Black, Black)`
2. Submit your guess
3. Look at the feedback
4. If it's 4 black pegs: well done you've won. Enjoy the money I hope it makes you very happy
5. For each code in the eligible set suppose that it is the hidden code
    - Calculate what the feedback would be for each code in the eligible set
    - If it's the same as the feedback from your guess then you keep it
    - If it's not the same then you get rid of it
6. Now that the eligible set has been reduced choose a code from it as your next guess
    - Any code will do but I just went with the first one
    - Repeat steps 2 -> 6 until you win or get bored