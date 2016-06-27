LAX Relative Power Index
===================================

MNLaxpower can be found at mnlaxpower.com

The [Rating Percentage Index](https://en.wikipedia.org/wiki/Rating_Percentage_Index), commonly known as the RPI, is a quantity used to rank sports teams based upon a team's wins and losses and its strength of schedule. It is one of the sports rating systems by which NCAA basketball, baseball, softball, hockey, soccer, lacrosse, and volleyball teams are ranked. This system has been in use in college basketball since 1981 to aid in the selecting and seeding of teams appearing in the men's playoffs, and for the women's tournament since its inception in 1982. In its current formulation, the index comprises a team's winning percentage (25%), its opponents' winning percentage (50%), and the winning percentage of those opponents' opponents (25%). The opponents' winning percentage and the winning percentage of those opponents' opponents both comprise the strength of schedule (SOS). Thus, the SOS accounts for 75% of the RPI calculation and is 2/3 its opponents' winning percentage and 1/3 its opponents' opponents' winning percentage.

The RPI lacks theoretical justification from a statistical standpoint. Other ranking systems which include the margin of victory of games played or other statistics in addition to the win/loss results have been shown to be a better predictor of the outcomes of future games. However, because the margin of victory has been manipulated in the past by teams or individuals in the context of gambling, the RPI can be used to mitigate motivation for such manipulation.

RPI (Ratings Percentage Index) is a formula is traditionally defined as follows: 

> RPI = 0.25 * WP + 0.50 * OWP + 0.25 * OOWP

Here is an example schedule for 4 teams named A, B, C, D:

  | A | B | C | D
------------ | ------------- | ------------- | ------------- | -------------
A | . | 1 | 1 | . 
B | 0 | . | 0 | 0 
C | 0 | 1 | . | 1 
D | . | 1 | 0 | .

* 1 = win for team in row
* 0 = loss for team in row
* . = not played

Each 1 in a team's row represents a win, and each 0 represents a loss. So team C has wins against B and D, and a loss against A. Team A has wins against B and C, but has not played D. 

WP, OWP, and OOWP are defined for each team as follows: 
* **WP** (Winning Percentage) is the fraction of a teams games that they have won.
* **OWP** (Opponents' Winning Percentage) is the average WP of a team's opponents, after first throwing out the games they have played against each other.  For example, if you throw out games played against team D, then team B has WP = 0 and team C has WP = 0.5. Therefore team D has OWP = 0.5 * (0 + 0.5) = 0.25. Similarly, team A has OWP = 0.5, team B has OWP = 0.5, and team C has OWP = 2/3.
* **OOWP** (Opponents' Opponents' Winning Percentage) is the average OWP of a team's opponents. OWP is exactly the number computed in the previous step.  For example, team A has OOWP = 0.5 * (0.5 + 2/3) = 7/12.

Putting it all together, team A has 
> RPI = (0.25 * 1) + (0.5 * 0.5) + (0.25 * 7 / 12) = 0.6458333...
