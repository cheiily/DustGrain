# DustGrain
Dustloop-scraping utility with a CLI interface.\
Written in kotlin with the help of clikt and jsoup.

## Usage
- check `dustgrain -h` or `dustgrain <command> -h` for immediate help.

DustGrain currently offers two commands:
### List
<details>
Polls the frame data website of the specified character and returns a list of all moves,
    as listed under the "input" rubric. 

**Will terminate with an exception if the URL formed with parsed parameters is unreachable/incorrect.**

This functionality can also be accessed via `Model.listMoves`.
#### Arguments
- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version
- pos 1: character - the character's full name, as seen in the url
- flag: pretty
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

#### Return
- List of all moves, as listed under the "input" rubric.

#### Example
Usage:
> .\dustgrain list GBVSR Djeeta

Return:
>  {"moves":["c.L","c.M","c.H","c.XX","c.XXX","c.XX6M","c.XX6H","f.L","f.M","f.H","2L","2M","2H","2U","66L","66M","66H","j.L","j.M","j.H","j.U","5U lv0","5U lv1","5U lv2","5U Lv3","5U Lv4","5[U] ~ X","236L","236M","236H","236[L]","236[M]","236[H]","214L","214L~214L","214M","214M~214M","214H","214H~214H","214H~214H~214H","623L","623M","623H","236U","623U","214U","236236H","236236U","Ground Throw","Air Throw","Raging Strike","Raging Chain","Brave Counter"]}

</details>

### Data
<details>
Polls the frame data website of the specified character and returns a map of the moves properties. 
The map keys are column headers, values are taken from the matching table row. <br>
It is currently impossible to access any extra data, like Gatling Tables or System Data with this utility.

**Will terminate with an exception if the URL formed with parsed parameters is unreachable/incorrect.**

This functionality can also be accessed via `Model.getData`.

#### Arguments
- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version
- pos 1: character - the character's full name, as seen in the url
- pos 2: move - the move's input as seen under the "input" rubric.
- flag: pretty
  - on -> prints human-readable version
  - off -> prints a jsonified map, under the `data` key.

#### Return
- Map of the moves properties, keyed with their rubric headers.

#### Example
Usage:
> .\dustgrain data BBTag Noel_Vermillion "Drive Finisher"

> .\dustgrain data -p GGACR May 4123641236H

Return:
> {"data":{"input":"Drive Finisher","name":"Type II: Bloom Trigger","damage":"1000, 1700","guard":"All","startup":"11","active":"2(7)4","recovery":"33","onBlock":"-16","attribute":"B, BP","invuln":"","p1":"100","p2":"90","cancel":"P","level":"5","blockstun":"20","groundHit":"Crumple, Launch","airHit":"21, 50 + WBounce 50","groundCH":"Crumple, Launch","airCH":"37, 66 + WBounce 50","blockstop":"16, 0","hitstop":"+0","CHstop":"+8"}}

```
input = 4123641236H
name = May and the Jolly Crew
guard = 70 pixels
level =
cancel =
tension =
startup = 7+9
active = 2
recovery = 6
onBlock =
damage = Fatal
gbp =
gbm =
prorate =
invuln = 1~18F All
blockstun =
groundHit =
airHit =
hitstop =
```
</details>