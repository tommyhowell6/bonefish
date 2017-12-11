package hashSequencer;

import Model.Sequence;
import Utility.SequenceFactory;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Kris
 */
public class ErrorTolerantMerger extends SimpleMerger{
    public static final char ERROR_CHAR = '\n';
    public static final double CONTIGUITY_THRESHHOLD = .8;

    /**
     * Merges the provided sequences, keeping in mind that there may very well be errors
     * present in one or the other. In the case error reads are present or reads differ,
     * the read with the lowest error should be preferred. In the event of no read at a 
     * given location, any base will do from the other regardless of accuracy.
     * 
     * @param indexOfOverlap Array containing the location of overlap in each of the sequences.
     * @param first The first sequence and its probabilities.
     * @param second The second sequence and its probabilities.
     * @return A finished, merged sequence.
     */
    @Override
    Sequence mergeSequences(int[] indexOfOverlap,SequenceProbabilityPair first,SequenceProbabilityPair second){
        SequenceProbabilityPair merged;
        //System.out.println("Entered merger with "+Arrays.toString(indexOfOverlap));
        if(indexOfOverlap[0]>indexOfOverlap[1]){
            merged = mergeWithOrderedOverlap(indexOfOverlap[0],indexOfOverlap[1],first,second);
        }
        else{
            merged = mergeWithOrderedOverlap(indexOfOverlap[1],indexOfOverlap[0],second,first);
        }

        return SequenceFactory.makeSequence(merged.bases, merged.accuracy);
    }

    private SequenceProbabilityPair mergeWithOrderedOverlap(int firstIndex, int secondIndex, 
            SequenceProbabilityPair first, SequenceProbabilityPair second) {
        StringBuilder bases = new StringBuilder();
        StringBuilder accuracy = new StringBuilder();
        //System.out.println("Entering nested merger with: "+firstIndex+" index and "+secondIndex);

        boolean done = false;
        int firstPointer=0;
        int secondPointer = firstPointer+(secondIndex-firstIndex);
        while(!done){
            char[] tempOut = getChar(firstPointer,secondPointer,first,second);
            
            if(tempOut!=null){
                bases.append(tempOut[0]);
                accuracy.append(tempOut[1]);
                firstPointer++;
                secondPointer++;
            }
            else{
                done = true;
            }
            
        }
        return new SequenceProbabilityPair(bases.toString(),accuracy.toString());
    }
    /**
     * Figures out which of a set of characters to return on a merge.
     * @param firstIndex
     * @param secondIndex
     * @param bases
     * @param bases0
     * @return 
     */
    private char[] getChar(int firstIndex, int secondIndex, SequenceProbabilityPair firstRead, SequenceProbabilityPair secondRead) {
        char[] output = new char[2];

        //first, dealing with both not existing.
        if(isOutOfRange(firstIndex,firstRead)&&isOutOfRange(secondIndex,secondRead)){
            return null;
        }
        //Now, if either side is past index, the other automatically wins.
        if(isOutOfRange(firstIndex,firstRead)){
            output[0]=secondRead.bases.charAt(secondIndex);
            output[1]=secondRead.accuracy.charAt(secondIndex);
            return output;
        }
        if(isOutOfRange(secondIndex,secondRead)){
            output[0]=firstRead.bases.charAt(firstIndex);
            output[1]=firstRead.accuracy.charAt(firstIndex);
            return output;
        }
        
        char firstChar = firstRead.bases.charAt(firstIndex);
        char secondChar = secondRead.bases.charAt(secondIndex);
        int firstAccuracy = firstRead.accuracy.charAt(firstIndex);
        int secondAccuracy = secondRead.accuracy.charAt(secondIndex);

        if(firstAccuracy > secondAccuracy){
            //System.out.println("thought: "+firstAccuracy+" was bigger than "+secondAccuracy);
            output[0]=firstChar;
            output[1]=(char)firstAccuracy;
        }
        else{
            output[0]=secondChar;
            output[1]=(char)secondAccuracy;
        }
        return output;
    }
     /**
     * Finds the location that the two sequences overlap. Must be tolerant of only partal coverage,
     * the sequence being totally swallowed, or errors in the reads.
     * @param firstSequence First sequence of bases to search.
     * @param secondSequence Second sequence of bases to search
     * @return The array coresponding to the place in the first int[0] and second int[1] sequences
     *  where the pair of sequences overlap.
     */
    @Override
    int[] findOverlapIndex(Sequence firstSequence, Sequence secondSequence){
        return longestCommonSubstring(firstSequence.getBases(),secondSequence.getBases());
    }
    
    public int[] longestCommonSubstring(String input1, String input2){
        String currentLongest="";
        int[] output = new int[2];
        //Start at every position in the String.
        for(int i=0;i<input1.length();i++){
            
            String currentString ="";
            boolean doneWithThisIteration = false;
            for(int j=i;j<input1.length()&&!doneWithThisIteration;j++){
                currentString+=input1.charAt(j);
                if(currentString.length()>currentLongest.length()){
                    int count = StringUtils.countMatches(input1, currentString);
                    if(count>=1){
                        if(StringUtils.countMatches(input2, currentString)>=1){
                            currentLongest = currentString;
                            output[0]=i;
                            output[1]=j;
                        }
                        else{
                            break;
                        }
                        
                    }
                    else if (count ==0){
                        break;
                    }
                }
                
            }
        
        }
        //System.out.println("Longest common substring: "+currentLongest);
        output[1] = StringUtils.indexOf(input2, currentLongest);
        //System.out.println("returning: "+Arrays.toString(output));
        return output;
    }

    private boolean isOutOfRange(int index, SequenceProbabilityPair read) {
        if(index<0){
            return true;
        }
        return index>read.bases.length()-1;
    }
}
