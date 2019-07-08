
# This script assumes that it is called sometime after the RarefySeqs module
# and that the RarefySeqs module was called after SeqFileValidator

getNumReads <- function(filePath){
  cmd=paste0(c("< ", filePath, " wc -l" ), collapse="")
  lines=system(cmd, intern=T)
  numLines = trimws(lines)
  reads = as.numeric(numLines) / 4
  return(reads)
}

getModOutputFiles <- function(modName){
  modDir = dir("../../", modName)
  files=dir(paste0(c("../../",modDir, "/output/"),collapse=""), ".f")
  files=paste0("../../", modDir , "/output/", files)
  return(files)
}

seqFileVal.files = getModOutputFiles( "SeqFileValidator" )
sfv.reads = sapply(seqFileVal.files, getNumReads)


rarify.files = getModOutputFiles( "RarefySeqs" )
rfy.reads = sapply(rarify.files, getNumReads)

system("mkdir ../output")

png("../output/ReadsBeforeAfterRarifying.png")
boxplot(sfv.reads, rfy.reads, las=1, 
        col = c("coral", "dodgerblue3"), 
        names = c("before", "after"),
        ylab="reads per file")
title("Rarification")
dev.off()

