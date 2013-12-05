ISPN-3767
=========

This project reproduced the issue described in ISPN-3767: https://issues.jboss.org/browse/ISPN-3767


To compile it, just run the ./build.sh script, manually reproduced the commands in it.

To run it, fire up all the run[14].sh files (which symlink to run.sh). If running on non Unix
system, manually creates 4 equivalent .bat files.

Follow the output of node2, which will looked the following:

   Loading data in  27615ms
   Done.
   Query:indexedField:indexMe0
   Results from query (1)
   Value(indexedField=indexMe0, data={})
   Done.
   Re index with Mass Indexer ... 390ms. Wait for MassIndexer to flush(): Pause for 60, 60 59 58 57 56
   55 54 53 52 51 50 49 48 47 46 45 44 43 42 41 40 39 38 37 36 35 34 33 32 31 30 29 28 27 26 25 24 23
   22 21 20 19 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 ... Done.
   Query:indexedField:indexMe0
   Results from query (0)

Note that after running the Mass Indexer, the query now fails, returning 0.
