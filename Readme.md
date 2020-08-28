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
Let's use the standard stars and bars [trick](https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)).
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

The way both these algorithms work is to start with an eligible set of possibilities for what the hidden code could be and then at each step try to whittle this eligible set down until we guess the correct hidden code. 
Knuth's is better than my brute force one but takes much longer to run.
In the next two sections I'll describe how each algorithm works.

### Brute Force
With my implementation on my little macbook this algorithm took seconds to run for all 1296 hidden codes. 

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
    
#### Results
```
Results for brute-force algorithm:
Number of guesses needed on the left, 
amount of hidden codes that needed that number of guesses on the left

Map(
  1 -> 1,
  2 -> 12,
  3 -> 96,
  4 -> 408,
  5 -> 608,
  6 -> 160,
  7 -> 11
)

Weighted Mean -> 6022 / 1296 = 4.64660...
Worst Case    -> 7
Median        -> 5
Mode          -> 5
```

### Knuth's Algorithm
The key difference between this and my brute force one is that Knuth is much more intelligent about choosing the next guess.
He uses a minimax technique to do this.
The next guess is determined by looking at all unused codes and calculating the minimum number of possibilities that could be eliminated from the eligible set. 
And then choosing the guess that maximises this.
This can give situations where the guess you make isn't actually part of the eligible set but it helps reduce the eligible set more than anything else!

1. Start be guessing `Code(White, White, Black, Black)`
2. Submit your guess
3. Look at the feedback
4. If it's 4 black pegs: well done you've won. Enjoy the money I hope it makes you very happy
5. For each code that hasn't been used before (not just those in the eligible set) assign it a score based on the minimum number of possibilities it might eliminate
    - For each unused code iterate through all 15 possible feedbacks to calculate the number of possibilities that code would eliminate in the eligible set for each possible feedback
    - For each unused code take the minimum of the above as the score for that unused code
6. You are now ready to choose your guess
    - It's just one of the codes from step 5 with the maximum score
    - If you can find a code with the maximum score that also appears in the eligible set then you should use that
    - otherwise any guess with the maximum score will do
    - Repeat steps 2 -> 6 until you win or get bored
    
With my implementation on my little macbook this algorithm took hours to run for all 1296 hidden codes. 
At first I thought it might be that my implementation sucked!
But if you think about how many operations have to happen for the worst case of this algorithm (which happens for over half the codes) then it's not so unreasonable that it takes so long.
    
#### Results
```
Results for knuth algorithm:
Number of guesses needed on the left, 
amount of hidden codes that needed that number of guesses on the left
Map(
  1 -> 1,
  2 -> 6,
  3 -> 64,
  4 -> 545,
  5 -> 680
)

Weighted Mean -> 5785 / 1296 = 4.46373...
Worst Case    -> 5
Median        -> 5
Mode          -> 5
```

## Run

I mean I just use intellij for everything and click the little green run arrow in the gutter because I'm a n00b

But if you're a terminal person:
Assuming you have scala and sbt do this:

```
sbt compile
```

To look at the algorithm result summaries (As derived from the CSVs in `/out/mastermind`) you can do:
```
sbt "runMain mastermind.InspectResults"
```

To run one of the algorithms on all possible input codes and produce an output CSV you can do;
```
sbt "runMain mastermind.RunAlgorithm ALGORITHM_NAME"
```
where (at the moment) ALGORITHM_NAME is either `knuth` or `brute-force`