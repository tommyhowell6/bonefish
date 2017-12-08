#! /usr/bin/env python

import sys
from random import choice
from numpy.random import np
import uuid

def randomNuc(DNALength):
    DNA = ""
    for count in range(int(DNALength)):
        DNA += choice("ACGT")
    return DNA

def qualityRead(Length):
    quality = ""
    choices = []
    # probabilities = []
    # probability = 1.0/95
    # probSum = probability
    # elements = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
    # for s in elements:
    #     print (s + " " + str(probability))
    #     choices.append(s)
    #     probabilities.append(probability)
    #     # probability += 
    #     probSum += probability
    # print (probSum)
    # return np.random.choice(choices, p=probabilities)
    for count in range(int(Length)):
        quality += choice("!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~")
    return quality    
        
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
        output.write("@" + readId + "_1" + "\n")
        output.write(DNA[x:x+int(kMerLength)] + "\n")
        output.write("+" + "\n")
        output.write(qualityRead(kMerLength) + "\n")
        output.write("@" + readId + "_2" + "\n")
        output.write(DNA[x+int(kMerLength)+int(gap):x+int(kMerLength)+int(gap)+int(kMerLength)] + "\n")
        output.write("+" + "\n")
        output.write(qualityRead(kMerLength) + "\n")
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
    
