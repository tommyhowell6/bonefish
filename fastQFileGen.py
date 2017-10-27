#! /usr/bin/env python

import sys
from random import choice
import uuid

def randomNuc(DNALength):
    DNA = ""
    for count in range(int(DNALength)):
        DNA += choice("ACGT")
    return DNA
        
def makePairedReads(output, DNALength, kLength, gap):
    #line 1 
    #  @ID
    #line 2
    #  seq
    #line 3
    #  +
    #line 4
    #  quality of the read
    DNA = ""
    DNA = randomNuc(DNALength)
    readLength = (int(kLength) * 2) + int(gap)
    print (DNA)
    print (len(DNA))
    print (readLength)
    x = 0
    while (x < len(DNA) + 1 - readLength):
        readId = str(uuid.uuid4())
        print("@" + readId + "_1")
        print(DNA[x:x+int(kMerLength)])
        print("+")
        print("this line is the quality of the read")
        print("@" + readId + "_2")
        print(DNA[x+int(kMerLength)+int(gap):x+int(kMerLength)+int(gap)+int(kMerLength)])
        print("+")
        print("this line is the quality of the read")
        x += 1
    
#main
#get number of arguments 0 is file name
countSysArgv = 0
for item in sys.argv:
    countSysArgv += 1

if countSysArgv == 5:
    file1 = sys.argv[1]
    genomeLength = sys.argv[2]
    kMerLength = sys.argv[3]
    gapLength = sys.argv[4]
    output = open(file1, "w")
    print(genomeLength)
    makePairedReads(output, genomeLength, kMerLength, gapLength)
    output.close()
else:
    print "ERROR: use the following command"
    print "\t python fastQFileGen.py fastQOutputFileName genomeLength kMerLength lengthBetweenReads"
    print "\t python fastQFileGen.py output.fastq 1000 50 100"
    