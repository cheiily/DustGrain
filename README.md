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
  - Key : Stat (as per the option)


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
  - Key : Table name

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
  - Key : Table name

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
  - Key: Table name

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
  - Key: Table name

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
Find moves adhering to a custom set of filters.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: stat - stat column to filter
- option: table (-t, --table)
  - Table to limit the search to
- option: filter, one of either set, sets can contain multiple options:
  - numeric (--num)
    - less than (-lt)
    - less or equal (-le)
    - equal (-eq)
    - greater than (-gt)
    - greater or equal (-ge)
  - text (--text)
    - equal string (-eqs)
    - contains (-l, --cont)
    - starts with (-s, --start)
    - ends with (-e, --end)
    - custom regex (-r, --regex)
- headers: (--headers, --headers-only)
  - only display the found move's first column, otherwise map to the found value


#### Return

- Map with table names as keys and query result as values. 
  - Without -h, the result is another map of header to found value.
  - With -h, the result is simply a list of the moves' headers.
- The map only contains tables with results.
  - Key: Table name

#### Example
Usage:
```shell
.\dustgrain find moves GBVSR Djeeta --num onBlock -gt -5 -lt 0
```
```shell
.\dustgrain find moves GBVSR Djeeta --text input -s '2' --cont '4' -e 'L' --headers
```
Return:
```json
{"Normal Moves":{"c.H":"-3","c.XX":"-3","c.XXX":"-4","c.XX6M":"-4","c.XX6H":"-4","2L":"-2","2M":"-3"},"Unique Action":{"5U lv0":"-2","5U lv1":"-2"},"Skills":{"236[L]":"-4","236[M]":"-4","214M":"-4","214U":"-4"}}
```
```json
{"Skills":["214L","214L~214L"]}
```

</details>

### Wikitable
Handling the cross-tables, usually containing info about gatling/combo links.

<details><summary>Headers</summary>
Extract header values.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to extract data from
- flag: vertical (-v, --vertical)
  - Retrieve vertical headers
- flag: horizontal (-h, --horizontal)
  - Retrieve horizontal headers


#### Return

- Map of header orientation to list of header values
  - Keys: "Vertical Headers" and "Horizontal Headers"
  - Both will be present in case of no flag or both flags.

#### Example
Usage:
```shell
.\dustgrain wikitable headers BBCF Noel_Vermillion "Ground Revolver Action Table"
```
Return:
```json
{"Vertical Headers":["5A[3]","5B[1]","5C[1]","2A[3]","2B[2]","2C","6A","6B","6C","3C"],"Horizontal Headers":["A","B","C","D","Cancels"]}
```

</details>

<details><summary>Row</summary>
Get a full data row, matched by header or index.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to extract data from
- only one of:
  - option: index (-i, --index)
  - option: header (-h, --header)

#### Return

- Map of row header to list of values
  - Key: row header

#### Example
Usage:
```shell
.\dustgrain wikitable row BBCF Noel_Vermillion "Ground Revolver Action Table" -h 2C
```
```shell
.\dustgrain wikitable row BBCF Noel_Vermillion "Ground Revolver Action Table" -i 0
```
Return:
```json
{"2C":["-","-","5C, 3C","5D, 2D, 6D, 4D","Special/Super"]}
```
```json
{"5A[3]":["5A[+], 2A, 6A","5B, 2B, 6B","5C, 2C, 6C, 3C","5D, 2D, 6D, 4D","Throw, Jump, Special/Super"]}
```

</details>

<details><summary>Column</summary>
Get a full column of data, matched by header or index.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to extract data from
- only one of:
  - option: index (-i, --index)
  - option: header (-h, --header)

#### Return

- Map of column header to list of values
  - Key: column header

#### Example
Usage:
```shell
.\dustgrain wikitable column BBCF Noel_Vermillion "Ground Revolver Action Table" -i 0
```
```shell
.\dustgrain wikitable column BBCF Noel_Vermillion "Ground Revolver Action Table" -h Cancels
```
Return:
```json
{"A":["5A[+], 2A, 6A","6A","-","5A, 2A","6A","-","-","-","-","-"]}
```
Return:
```json
{"Cancels":["Throw, Jump, Special/Super","Throw, Jump, Special/Super","Throw, Jump[-], Special/Super","Throw, Special/Super","Throw, Special/Super","Special/Super","Throw, Jump, Special/Super","Special/Super","Throw, Jump (2nd Hit), Special/Super","22B/C[-]"]}
```

</details>

<details><summary>Cell</summary>
Get a specific cell, by indices or headers.

#### Arguments
<details><summary>Common arguments</summary>

- pos 0: wiki - dustloop's sub-wiki url module, can be either the full name, enclosed in quotes, or the short version. **CASE SENSITIVE!**
- pos 1: character - the character's full name, as seen in the url
- flag: pretty (-p, --pretty)
  - on -> prints human-readable version
  - off -> returns a jsonified list, under a `moves` key

</details>

- pos 2: table - Table to extract data from
- only one of:
  - both:
    - option: row index (-ri, -yi, --rowindex)
    - option: column index (-ci, -xi, --colindex)
  - both
    - option: row header (-rh, -ri, --rowheader)
    - option: column header (-ch, xh, --colheader)

#### Return

- Map of index/header crossing to cell value
  - Key: index or header crossing, e.g. `1x3`, `2CxD`

#### Example
Usage:
```shell
.\dustgrain wikitable cell BBCF Noel_Vermillion "Ground Revolver Action Table" -ci 3 -ri 1
```
```shell
.\dustgrain wikitable cell BBCF Noel_Vermillion "Ground Revolver Action Table" -ch C -rh 2A[3]
```
Return:
```json
{"1x3":"5D, 2D, 6D, 4D"}
```
```json
{"2A[3]xC":"5C, 2C, 6C"}
```

</details>
