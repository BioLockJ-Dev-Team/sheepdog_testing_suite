#! /bin/bash

# create output with subdirectories

echo "I'm at the top of output dir." > ../output/layer1.txt

mkdir ../output/dir1 # leave empty

mkdir ../output/dir2
echo "I'm in dir 2." > ../output/dir2/layer2.txt
mkdir ../output/dir2/deep
echo "I'm deep in the output under dir 2." > ../output/dir2/deep/layer3.txt

mkdir -p ../output/dir3/way/down/deep
echo "I'm deep in the output under dir 3." > ../output/dir3/way/down/deep/deep.txt
