# DustGrain
Dustloop-scraping utility with a CLI interface written in kotlin with the help of clikt and jsoup.

Version 2.0 introduces a major rework to the tool. 
At its core, the scraping functionality has been changed to allow for easier data grouping into tables, as present on Dustloop.\
Thanks to that change, the tool now offers much more utility, going from 2 commands to 12!

## Usage
- Check `dustgrain -h` or `dustgrain <command> -h` for immediate help.
- All commands require common `wiki` and `character` positional arguments. These will be used to form the query url and should be passed as visible in the real URL. E.g. `BBCF` abd `Noel_Vermillion`.
- All commands return data as json. Commands where the output would otherwise be a raw list or value, package it into a single-element map.
  The key is usually literally equivalent to the command's most specific piece of input, e.g. `dustgrain move GBVS Djeeta c.L` will key the data with `c.L`.
  This can slightly vary with some more advanced commands, but is applicable to the vast majority of them. See the `Key : <>` note in each return section.
- In case of error the app will exit with non-zero status code and print the error to the error stream.
  Additionally, an error json of `{"error":""}` will be printed to the standard stream.
- All commands also take a `-p` or `--pretty` flag to present the data in a more readable way. 


## Commands

DustGrain's commands are organized in 4 groups:

### Data
Main use case. General character/move data polling with little to no filtration.

<details><summary>Move</summary>
All statistics regarding the specified move, or just the one asked for with --stat.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key
  
</details>

- pos 2: move - haracter's move by input
- option: stat (-s, --stat)
  - Specifc stat value (column) to peek. This will change the return value.

#### Return
- Map of property name (column header) to its value.
  - Key : move (pos 2)
- If -s was used, single-element map of requested prop to its value. 
  - Key : none, not packaged (stat).


#### Example
Usage:
```shell
.\dustgrain data move GBVSR Djeeta c.L
```
```shell
.\dustgrain data move GBVSR Djeeta c.L -s startup
```
Return:
```json
{"c.L":{"input":"c.L","damage":"600","guard":"Mid","startup":"5","active":"3","recovery":"6","onBlock":"+4","onHit":"+8","level":"0","invuln":""}}
```
```json
{"startup":"5"}
```

</details>

<details><summary>System</summary>
Character's system data, as provided by the separate table. Usually including HP, pre-jump, unique movement options, etc.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

#### Return
- Map of property name (column header) to its value.
  - Key : character (pos 1)

#### Example
Usage:
```shell
.\dustgrain data system GBVSR Djeeta
```
Return:
```json
{"Djeeta":{"name":"Djeeta","health":"16,000","prejump":"4f","backdash":"18f","Unique Movement Options":""}}
```

</details>

### List
Polling extra data provided in an aggregated format.

<details><summary>Moves</summary>
List available moves for the specified character. Optionally cut down to one category (table).

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- option: table (-t, --table) 
  - Table/category to narrow down the list to.

#### Return

# TODO this vvvvvvvvvvvvvvvvvv
- Map of category name to list of move inputs.
  - Key : none, not packaged (table header, category name)

#### Example
Usage:
```shell

```
```shell
.\dustgrain list moves GBVSR Djeeta --table "Normal Moves"
```
Return:
```json

```
```json
{"Normal Moves":["c.L","c.M","c.H","c.XX","c.XXX","c.XX6M","c.XX6H","f.L","f.M","f.H","2L","2M","2H","2U","66L","66M","66H","j.L","j.M","j.H","j.U"]}
```

</details>

<details><summary>Stat</summary>
List the specified property for every move. Optionally narrow it down to a specific move category.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: stat - Property to list
- option: table (-t, --table) 
  - Table/category to narrow down the list to.

#### Return

# TODO this vvvvvvvvvvvvvvvvvv
- Map of category name to list of move inputs.
  - Key : none, not packaged (table header/category name)

#### Example
Usage:
```shell
.\dustgrain list stat GBVSR Djeeta startup
```
```shell

```
Return:
```json
{"Normal Moves":{"c.L":"5","c.M":"6","c.H":"8","c.XX":"9","c.XXX":"12","c.XX6M":"12","c.XX6H":"18","f.L":"6","f.M":"8","f.H":"10","2L":"6","2M":"7","2H":"11","2U":"7","66L":"8","66M":"12","66H":"15","j.L":"5","j.M":"6","j.H":"7","j.U":"12"},"Unique Action":{"5U lv0":"26/16*","5U lv1":"26/16*","5U lv2":"26/16*","5U Lv3":"26/16*","5U Lv4":"26/16*","5[U] ~ X":""},"Skills":{"236L":"16","236M":"16","236H":"16","236[L]":"41","236[M]":"41","236[H]":"30","214L":"14","214L~214L":"20","214M":"20","214M~214M":"20","214H":"15","214H~214H":"11","214H~214H~214H":"17","623L":"6","623M":"12","623H":"11","236U":"11","623U":"8","214U":"13"},"Skybound Arts":{"236236H":"7+7","236236U":"7+6"},"Other":{"Ground Throw":"4","Air Throw":"5","Raging Strike":"28","Raging Chain":"","Brave Counter":"10"}}
```
```json

```

</details>

<details><summary>Stats</summary>
List all properties available in this table (headers).

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to extract available property names from.

#### Return

- List of column headers (property names) of the table.
  - Key: table

#### Example
Usage:
```shell
.\dustgrain list stats GBVSR Djeeta "Normal Moves"
```
Return:
```json
{"Normal Moves":["input","damage","guard","startup","active","recovery","onBlock","onHit","level","invuln"]}
```

</details>

<details><summary>Tables</summary>
List tables available for this character. Optionally narrow it down to specific table types.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

This flag group narrows down the search to specific categories.
The default behavior is equivalent to `-c -d -w`.
- flag: cargo (-c, --cargo)
- flag: data (-d, --data)
- flag: wiki (-w, --wiki)

#### Return

- Map of table type to list of table headers, matching that type.
  - Key: none, not packaged (table type).

#### Example
Usage:
```shell
.\dustgrain list tables GBVSR Djeeta
```
```shell
.\dustgrain list tables GBVSR Djeeta -d
```
Return:
```json
{"Cargo Tables":["System Data"],"Data Tables":["Normal Moves","Unique Action","Skills","Skybound Arts","Other"],"Cross Tables":[]}
```
```json
{"Data Tables":["Normal Moves","Unique Action","Skills","Skybound Arts","Other"]}
```

</details>

### Find
Searching normal data(cargo) tables for specific elements.

<details><summary>Cell</summary>
Find the value of a specific cell of a standard data table, via indices.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to peek.
- option(required): row index (-ri, -y, --rowindex)
- option(required): column index (-ci, -x, --colindex)

#### Return

- Single element map with value equal to the extracted cell value.
  - Key: Crossing of the passed coordinates, e.g. "0x2", "2x3"

#### Example
Usage:
```shell
.\dustgrain find cell GBVSR Djeeta "Normal Moves" -ri 2 -ci 3
```
```shell
.\dustgrain find cell GBVSR Djeeta "Normal Moves" -ri 2 -ci 0
```
Return:
```json
{"2x3":"8"}
```
```json
{"2x0":"c.H"}
```

</details>

<details><summary>Moves</summary>

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>



#### Return



#### Example
Usage:
```shell

```
Return:
```json

```

</details>

### Wikitable
Handling the cross-tables, usually containing info about gatling/combo links.

<details><summary>Headers</summary>

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>



#### Return



#### Example
Usage:
```shell

```
Return:
```json

```

</details>

<details><summary>Row</summary>

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>



#### Return



#### Example
Usage:
```shell

```
Return:
```json

```

</details>

<details><summary>Column</summary>

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>



#### Return



#### Example
Usage:
```shell

```
Return:
```json

```

</details>

<details><summary>Cell</summary>

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>



#### Return



#### Example
Usage:
```shell

```
Return:
```json

```

</details>




# OLD - DELETE THIS
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
```shell
.\dustgrain list GBVSR Djeeta
```
Return:
```json
{"moves":["c.L","c.M","c.H","c.XX","c.XXX","c.XX6M","c.XX6H","f.L","f.M","f.H","2L","2M","2H","2U","66L","66M","66H","j.L","j.M","j.H","j.U","5U lv0","5U lv1","5U lv2","5U Lv3","5U Lv4","5[U] ~ X","236L","236M","236H","236[L]","236[M]","236[H]","214L","214L~214L","214M","214M~214M","214H","214H~214H","214H~214H~214H","623L","623M","623H","236U","623U","214U","236236H","236236U","Ground Throw","Air Throw","Raging Strike","Raging Chain","Brave Counter"]}
```
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
```shell 
.\dustgrain data BBTag Noel_Vermillion "Drive Finisher"
```
```shell
.\dustgrain data -p GGACR May 4123641236H
```
Return:
```json
{"data":{"input":"Drive Finisher","name":"Type II: Bloom Trigger","damage":"1000, 1700","guard":"All","startup":"11","active":"2(7)4","recovery":"33","onBlock":"-16","attribute":"B, BP","invuln":"","p1":"100","p2":"90","cancel":"P","level":"5","blockstun":"20","groundHit":"Crumple, Launch","airHit":"21, 50 + WBounce 50","groundCH":"Crumple, Launch","airCH":"37, 66 + WBounce 50","blockstop":"16, 0","hitstop":"+0","CHstop":"+8"}}
```
```text
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