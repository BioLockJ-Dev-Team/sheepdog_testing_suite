#!/bin/bash
# someUserScript.sh

echo "My custom script is running..."
echo hello world

echo "My script runs in the script directory."
touch I-ran-here

echo "And it creates a file."
OUT="../output/myOutputFile.txt"
touch $OUT
echo "# I created this output at:" >> $OUT
echo "# $(date)" >> $OUT

echo "It can access other files in the pipeline,"
echo "for example, my output file gives the number of sequences"
echo "that were removed from each sample by the 02_RarefySeqs module."


SEP="\t"
echo -e "sample file${SEP}reads before${SEP}reads after${SEP}reads removed" >> $OUT

RSOUT=$(ls ../../02_RarefySeqs/output/*fastq)
for FILE in $RSOUT
do
	SAMPLE=${FILE##*/}
	SAMPLE=${SAMPLE%%.fastq}
	BEFORE=$(< "../../01_SeqFileValidator/output/${SAMPLE}_R1.fastq" wc -l)
	b=`expr $BEFORE / 4`
	AFTER=$(< "../../02_RarefySeqs/output/${SAMPLE}.fastq" wc -l)
	a=`expr $AFTER / 4`
	diff=`expr $b - $a`
	echo -e "${SAMPLE}${SEP}$b${SEP}$a${SEP}${diff}" >> $OUT
done
