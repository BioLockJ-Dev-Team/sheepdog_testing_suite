#!/bin/bash

# Ivory - June 2019
# this uses the sra-toolkit, available here:
# https://github.com/ncbi/sra-tools/wiki/Downloads

META=rhizosphere_metadata.txt
COL=5

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
    if [ "$SRR" != "NA" ];
        then
        echo "Getting ${SRR}..."
        fasterq-dump $SRR
        echo " ... gzip ... "
        gzip ${SRR}*
        echo "Done with $SRR"
    else
        echo "$SRR is not used by any tests in this suite. Do not download it."
    fi
done

fasterq-dump --version

end=`date +%s`
runtime=$((end-start))
minutes=$((runtime/60))
echo "This download took $minutes minutes."

echo "Move some files to subdirs..."
mv ./ERR1456806_1* ./R1/rhizo_R1_subdir/.
mv ./ERR1456807_1* ./R1/rhizo_R1_subdir/.
mv ./ERR1456808_1* ./R1/rhizo_R1_subdir/.
mv ./ERR1456826_1* ./R1/rhizo_R1_subdir/.
mv ./ERR1456827_1* ./R1/rhizo_R1_subdir/rhizo_R1_subdir_2files/.
mv ./ERR1456828_1* ./R1/rhizo_R1_subdir/rhizo_R1_subdir_2files/.

mv ./ERR1456827_2* ./R2/rhizo_R2_subdir_2files/.
mv ./ERR1456828_2* ./R2/rhizo_R2_subdir_2files/.

mv ./ERR1456790* ./R1andR2_2pairs/.
mv ./ERR1456791* ./R1andR2_2pairs/.

mv ./ERR*_1 ./R1/.
mv ./ERR*_2 ./R2/.

echo "Done!"
