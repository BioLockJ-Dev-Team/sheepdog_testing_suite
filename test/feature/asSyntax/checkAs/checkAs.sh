#! /bin/bash

OUT="../output/showThatAsWorked.txt"

ls ../.. | grep ^0 - > $OUT

#echo "" >> $OUT
#echo "summary.txt:" >> $OUT
#egrep "SeqFileValidator|Sfv|RarefySeqs|Rseqs|GenMod|Bash" ../../summary.txt >> $OUT

#echo "" >> $OUT
#echo "Used properties:" >> $OUT
#egrep "SeqFileValidator|Sfv|RarefySeqs|Rseqs|GenMod|Bash" ../../FINAL_*.properties >> $OUT
