#!/bin/bash

# Ivory - June 2019
# this uses the sra-toolkit, available here:
# https://github.com/ncbi/sra-tools/wiki/Downloads

META=rhizosphere_metadata.txt
COL=4

start=`date +%s` # to know how long this download takes

NUM=$( < $META wc -l)
echo "Table $META has $NUM rows including the hearder."
echo "Taking SRA accession id's from column: $COL."

echo "First row:"
head -n 2 $META

for i in $(seq 2 $NUM)
do
    echo "$i of $NUM"
    SRR=$(sed -ne "${i}p" ${META} | cut -f ${COL})
    SRR="$(echo -e "${SRR}" | tr -d '[:space:]')"
    echo "Getting ${SRR}..."
    fasterq-dump $SRR
    echo " ... gzip ... "
    gzip ${SRR}*
    echo "$SRR is zipped, moving to R1 and R2 subdirs..."
    mv ${SRR}_1.fastq.gz ./R1/.
    mv ${SRR}_2.fastq.gz ./R2/.
    echo "Done with $SRR"
done

fasterq-dump --version

end=`date +%s`
runtime=$((end-start))
minutes=$((runtime/60))
echo "This download took $minutes minutes."

echo "Move some files to subdirs..."
mv ./R1/ERR1456806* ./R1/rhizo_R1_subdir/.
mv ./R1/ERR1456807* ./R1/rhizo_R1_subdir/.
mv ./R1/ERR1456808* ./R1/rhizo_R1_subdir/.
mv ./R1/ERR1456826* ./R1/rhizo_R1_subdir/.
mv ./R1/ERR1456827* ./R1/rhizo_R1_subdir/.
mv ./R1/ERR1456828* ./R1/rhizo_R1_subdir/.

mv ./R2/ERR1456822* ./R2/rhizo_R2_subdir_2files/.
mv ./R2/ERR1456828* ./R2/rhizo_R2_subdir_2files/.

echo "Done!"
