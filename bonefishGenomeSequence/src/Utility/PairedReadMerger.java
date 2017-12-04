package Utility;

import Model.Sequence;
import Model.SequencePair;

/**
 *
 * @author Kris
 */
public class PairedReadMerger {
    
    
    /**
     * Merges a paired read based on the most accurate base pairs contained in either half of the read.
     * @param sequences
     * @return A single sequences representing a merged read of the two sequences.
     */
    public static Sequence mergePairedRead(SequencePair sequences){
        String firstSequence = sequences.getFirstSequence().getBases();
        String firstAccuracy = sequences.getFirstSequence().getAccuracy();
        
        //As per the FASTQ format, the second sequence in a read actually starts from the other end. We need to reverse the order of the reads
        //and bases.
        String secondSequence =sequences.getSecondSequence().getBases();
        StringBuilder sequenceReverser = new StringBuilder(secondSequence);
        secondSequence = sequenceReverser.reverse().toString();
        
        //We must do the same thing to the accuracy.
        String secondAccuracy = sequences.getSecondSequence().getAccuracy();
        StringBuilder accuracyReverse = new StringBuilder(secondAccuracy);
        secondAccuracy = accuracyReverse.reverse().toString();
        
        StringBuilder readOutput = new StringBuilder();
        StringBuilder accuracyOutput = new StringBuilder();
        
        for(int i =0;i<firstSequence.length();i++){
            //Remember, in the FASTQ format, lower ASCII values are WORSE reads. The best possible read is ~ at 126 and the worst is ! at 34.
            
            //If this is the case, we want our first read. It's the better read.
            if((int)firstAccuracy.charAt(i)>=(int)secondAccuracy.charAt(i)){
                readOutput.append(firstSequence.charAt(i));
                accuracyOutput.append(firstAccuracy.charAt(i));
            }
            //We want our second read instead. Don't forget to flip it!
            else{
                readOutput.append(invertChar(secondSequence.charAt(i)));
                accuracyOutput.append(secondAccuracy.charAt(i));  
            }   
        }
        String newID = sequences.getFirstSequence().getID().substring(0, sequences.getFirstSequence().getID().length()-3);
        
        
        return SequenceFactory.makeSequence(readOutput.toString(), accuracyOutput.toString(), newID);
    }
    
    public static char invertChar(char input){
        switch(input){
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'C':
                return 'G';
            case 'G':
                return 'C';
            default:
                return '?';
        }
    }
/**
 * This method flips the second of the two sequences and maintains all over values, returning them both.
 * @param sequences
 * @return 
 */
    public static Sequence[] flipPairedRead(SequencePair sequences) {
        //We must reverse the direction this is performed as the other end of the paired reads begin from the other side of the read strand.
        String rawSequence = sequences.getSecondSequence().getBases();
        StringBuilder sequenceReverse = new StringBuilder(rawSequence);
        rawSequence = sequenceReverse.reverse().toString();
        
        StringBuilder flippedSequence = new StringBuilder();
        for(char c : rawSequence.toCharArray()){
            flippedSequence.append(invertChar(c));
        }
        
        //We must also reverse the accuracy direction, or else we're going to intorduce garbage accuracy data into our system.
        String accuracy = sequences.getSecondSequence().getAccuracy();
        StringBuilder accuracyReverser = new StringBuilder(accuracy);
        accuracy = accuracyReverser.reverse().toString();
        
        Sequence newSequence = SequenceFactory.makeSequence(flippedSequence.toString(),accuracy , sequences.getSecondSequence().getID());
        Sequence[] output = {sequences.getFirstSequence(),newSequence};
        return output;
    }
    
    /**
     * Takes a paired read and discards the read with the highest error. If the first read is discarded, the second read must be flipped before it can be returned.
     * @param sequences
     * @return 
     */
    public static Sequence discardWorseRead(SequencePair sequences){
        int firstError = 0;
        int secondError = 0;
        
        String firstAccuracy = sequences.getFirstSequence().getAccuracy();
        String secondAccuracy = sequences.getSecondSequence().getAccuracy();
        
        for(int i=0;i<firstAccuracy.length();i++){
            firstError += (int)firstAccuracy.charAt(i);
            secondError+= (int)secondAccuracy.charAt(i);
        }
        
        //We don't have to flip, we're discarding the second half of the paired read.
        if(firstError<=secondError){
            return sequences.getFirstSequence();
        }
        //We need the second sequence. This means we have to flip it.
        else{
            Sequence[] flipped = flipPairedRead(sequences);
            return flipped[1];
        }
    }
    
}
