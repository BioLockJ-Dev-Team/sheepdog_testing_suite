

# if (!requireNamespace("BiocManager", quietly = TRUE))
#   install.packages("BiocManager")
# 
# BiocManager::install("DESeq2")
# browseVignettes("DESeq2")
library(DESeq2)

source("../BioLockJ_Lib.R")

## optional
#library("BiocParallel")
## if using BiocParallel
# register(MulticoreParam( getProperty("script.numThreads", 1) ))


##############################################

print("Finding inputs...")

# arg 1 - counts table file
# arg 2 - metadata file
# arg 3 - scriptString - a string to distinguish the output of this script from others in the same module
args = commandArgs(trailingOnly=TRUE)

print(paste("counts table:", args[1]))
print(paste("metadata:", args[2]))
print(paste("script string:", args[3]))

designString = getProperty("deseq2.designFormula")
print(paste("design:", designString))
design = as.formula(designString) 

##############################################

print("I'm the custom one!!!")

##############################################

# print("Reading inputs...")

# countData = t(readBljTable(args[1]))
# colData = readBljTable(args[2])


# ##############################################

# print("Launching DESeq...")

# dds1 <- DESeqDataSetFromMatrix(countData = countData,
#                                colData = colData,
#                                design= design)
# dds2 <- DESeq(dds1)


##############################################

# print("Extracting and writting results...")

# for (s in resultsNames(dds2)){
#   res = results(dds2, name=s)
#   resOrdered <- res[order(res$pvalue),]
#   print(paste(c("Summary for contrast:", s)))
  
#   summary(resOrdered)
#   outfile = paste0(c("../output/", args[3], s, ".tsv"), collapse = "")
#   print(paste(c("Saving to file:", outfile)))
#   write.table(cbind(row.names(resOrdered), as.data.frame(resOrdered)),
#               file=outfile,
#               sep="\t",
#               quote=FALSE,
#               row.names = FALSE,
#               col.names = TRUE)
# }


# or to shrink log fold changes association with condition:
# res <- lfcShrink(dds, coef="condition_trt_vs_untrt", type="apeglm")

##############################################

print("Getting citation info ...")

cite = citation("DESeq2")
writeLines(cite$textVersion)
writeLines(cite$textVersion, "../temp/citation.txt")

print("Done!")
