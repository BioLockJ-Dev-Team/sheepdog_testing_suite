#!/bin/bash

OUT=../output/originalFinalFrontier.txt

echo "Space: the final frontier. " >> $OUT
echo "These are the voyages of the starship Enterprise. " >> $OUT
echo "Its five-year mission: to explore strange new worlds. " >> $OUT
echo "To seek out new life and new civilizations. " >> $OUT
echo "To boldly go where no man has gone before!" >> $OUT

cat $OUT
