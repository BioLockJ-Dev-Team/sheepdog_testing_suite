#!/bin/bash

OUT=../output/ballad.txt

echo "There's no place I can be" >> $OUT
echo "Since I found Serenity" >> $OUT
echo "Burn the land and boil the sea" >> $OUT
echo "You can't take the sky from me." >> $OUT

# from "The Ballad of Serenity", Sonny Rhodes

# make this text appear in the log too.
cat $OUT
