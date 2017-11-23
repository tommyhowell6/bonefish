/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.SampleGenome;
import Model.Sequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Kris
 */
class SampleGenomeFactory {
    private static final int DEFAULT_READ_LENGTH = 150;
    private static final int DEFAULT_READ_OVERLAP = 50;
    private static final boolean DEFAULT_HAS_ERRORS = false;
    private static final int ERROR_THRESHHOLD = 114;
    public static final char ERROR_CHAR = '-';
    /**
     * Uses the defaults when building a sample genome.
     * @param genomeSize
     * @return The generated sample genome.
     */
    public static SampleGenome buildSampleGenome(int genomeSize){
        return buildSampleGenome(genomeSize, DEFAULT_READ_LENGTH,DEFAULT_READ_OVERLAP,DEFAULT_HAS_ERRORS);
    }
    /**
     * Constructs a sample genome without reading in from files.
     * @param genomeSize Total length of the finished genome.
     * @param readLength Length of each read.
     * @param readOverlap How much each individual base pair will be overlapped.
     * @param genomeErrors Whether or not this genome contains errors.
     * @return The collection of sequence objects corresponding to the reads. 
     * 
     */
    public static SampleGenome buildSampleGenome(int genomeSize, int readLength, int readOverlap, boolean genomeErrors) {
        Sequence finalGenome = generateGenome(genomeSize);
        Collection<Sequence> output = new ArrayList<>();
        Random generator = new Random();
        int numForCoverage = genomeSize/readLength;
        int numReads = numForCoverage * readOverlap;
        
        //Generate an individual read. There are many, many of these, taken at random.
        for(int i=0;i<numReads;i++){
            int begin = generator.nextInt(genomeSize);
            int end = begin+readLength;
            if(end>=genomeSize)
                end = genomeSize-1;
            
            String cleanBases = finalGenome.getBases().substring(begin, end);
            Sequence currentSequence;
            if(genomeErrors){
                currentSequence = generateDirtySequence(cleanBases);
            }
            //We don't need errors or anything so we can just make a new sequence right here.
            else{
                StringBuilder accuracy = new StringBuilder();
                for(int j=0;j<cleanBases.length();j++){
                    //Randomly select a probability character that indicates a relatively clean read.
                    int tempCharacter = generator.nextInt(30)+33;
                    char tempChar = (char)tempCharacter;
                    accuracy.append(tempChar);
                }
                currentSequence = SequenceFactory.makeSequence(cleanBases, accuracy.toString());
            }
            output.add(currentSequence);
        }
        
        
        return new SimpleSampleGenome(finalGenome,output);
    }

    private static Sequence generateGenome(int genomeSize) {
        StringBuilder output = new StringBuilder();
        StringBuilder probability = new StringBuilder();
        Random generator = new Random();
        int num;
        
        for(int i=0;i<genomeSize;i++){
            probability.append('!');
            num = generator.nextInt(4);
            switch(num){
                case 0:
                    output.append('A');
                    break;
                case 1:
                    output.append('T');
                    break;
                case 2:
                    output.append('C');
                    break;
                case 3:
                    output.append('G');
                    break;
            }
        }
        return SequenceFactory.makeSequence(output.toString(),probability.toString());
    }

    /**
     * We need a sequence that includes some errors. I'm kinda guessing on these since I haven't seen real reads.
     * @param cleanBases The read before we introduce some fake errors.
     * @return 
     */
    private static Sequence generateDirtySequence(String cleanBases) {
        StringBuilder accuracy = new StringBuilder();
        Random generator = new Random();
        for(int i=0;i<cleanBases.length();i++){
            int thisChar = generator.nextInt(93)+33;
            char accChar = (char)thisChar;
            accuracy.append(accChar);
            if(thisChar>=ERROR_THRESHHOLD){
                String start = cleanBases.substring(0, i);
                String rest = ERROR_CHAR+cleanBases.substring(i+1);
                cleanBases = start+rest;
            }
        }

        return SequenceFactory.makeSequence(cleanBases, accuracy.toString());
    }
    
}
