github: https://github.com/Taunk714/Checkmate

# instruction
If you want to look at the code about algolia, start from java->data->AlgoliaDataSource.java
For mac, please use command+b to go into a method and command+[ to go back.
There are many other files that are not part of the mini-research, so please start from the AlgoliaDataSource.java i mentioned above or SearchFragment.java

Login: If your want to create notes, you will see a login page. Please use test@test.com and test123 to login.
Create notes: click "+" button in the right bottom of the homepage. The data will be send to algolia.
Search:
click the search in the bottom menu, and type keywords and search. To search you mush click search of the SearchView or press enter.
For ranking, "number asc" and "number desc" button work. After click the button you still need to use searchView to search and get the new result.
For filter, you can enter sentence like "number: 8 TO 20" to choose those whose number is within 8 to 20. This is the syntax of algolia.
Typo is allowed. E.g. Reachel for Rachel
Synonyms are allowed.(only for "happy", "joy" and "glad", we only set this)