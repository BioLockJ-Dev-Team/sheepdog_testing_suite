

# if (!requireNamespace("BiocManager", quietly = TRUE))
#   install.packages("BiocManager")
# 
# BiocManager::install("edgeR")
# browseVignettes("edgeR")
library(edgeR)

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

designString = getProperty("edger.designFormula")
print(paste("design:", designString))
design = as.formula(designString) 

##############################################

print("I'm the custom one!!!")

##############################################

# print("Reading inputs...")

# countData = t(readBljTable(args[1]))
# colData = readBljTable(args[2])


# ##############################################

# writeLines(c("", "Launching edgeR..."))

# dgList <- DGEList(counts=countData, genes=rownames(countData))
# countsPerMillion <- cpm(dgList)
# countCheck <- countsPerMillion > 1
# keep <- which(rowSums(countCheck) >= 2)
# dgList <- dgList[keep,]
# dgList <- calcNormFactors(dgList, method="TMM")
# sampleType<-metaData$design
# designMat <- model.matrix(~sampleType)
# dgList <- estimateGLMCommonDisp(dgList, design=designMat)
# dgList <- estimateGLMTrendedDisp(dgList, design=designMat)
# dgList <- estimateGLMTagwiseDisp(dgList, design=designMat)
# fit <- glmFit(dgList, designMat)
# lrt <- glmLRT(fit)


##############################################

# print("Extracting and writting results...")

# res <- lrt$table
# resOrdered <- res[order(res$PValue),]

# out_design_name<-gsub("[+]","_",design)
# out_design_name<-gsub("~","",out_design_name)

# outfile <- paste0(c("../output/", args[3], out_design_name, ".tsv"), collapse = "")
# write.table(cbind(row.names(resOrdered), as.data.frame(resOrdered)),
#     file=outfile,
#     sep="\t",
#     quote=FALSE,
#     row.names = FALSE,
#     col.names = TRUE)


##############################################

print("Getting citation info ...")

cite = citation("edgeR", auto=TRUE)
writeLines(cite$textVersion)
writeLines(cite$textVersion, "../temp/citation.txt")

print("Done!")
