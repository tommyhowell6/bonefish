package hashSequencer;

import Model.Sequence;
import Model.SequencePair;
import Utility.SequenceFactory;
import Utility.SequenceMerger;

/**
 *
 * @author Kris
 */
public class SimpleMerger implements SequenceMerger{
    private static final double ERROR_THRESHHOLD = .5;
    
    /**
     * This is the most basic possible merger. It will search for a match between two sequences by reading both sequences, trying to find the point where they overlap.
     * This means it will not handle errors AT ALL.
     * 
     * @param sequences, pair of seqeuences with their assosiated probabilities.
     * @return a single sequence with merged probabilities.
     */
    @Override
    public Sequence merge(SequencePair sequences) {
        //First, we'll check to see if the sequences are the same.
        if(sequences.getFirstSequence().getBases().equals(sequences.getSecondSequence().getBases())){
            return sequences.getFirstSequence();
        }
        
        //Next, see if one sequence is completely contained inside the second sequence.
        if(sequences.getFirstSequence().getBases().contains(sequences.getSecondSequence().getBases())){
            //The first sequence totally contains the second sequence.
            return sequences.getFirstSequence();
        }else if(sequences.getSecondSequence().getBases().contains(sequences.getFirstSequence().getBases())){
            //the second sequence completely contains the first sequence.
            return sequences.getSecondSequence();
        } 
       
        String firstSequence = sequences.getFirstSequence().getBases();
        String firstProbabilities = sequences.getFirstSequence().getAccuracy();
        String secondSequence = sequences.getSecondSequence().getBases();
        String secondProbabilities = sequences.getSecondSequence().getAccuracy();
        
        //First, we proceed with the assumption that the second sequence in the order is the suffix.
        int[] indexOfOverlap = findOverlapIndex(sequences.getFirstSequence(),sequences.getSecondSequence());
        
        //If we are unable to find an overlap, it means the other sequence was the suffix. We need to switch them.
        if(indexOfOverlap[0]!=-1){
            
        }
        if(indexOfOverlap[0]==-1){
            firstSequence = sequences.getSecondSequence().getBases();
            firstProbabilities = sequences.getSecondSequence().getAccuracy();
            secondSequence = sequences.getFirstSequence().getBases();
            secondProbabilities = sequences.getFirstSequence().getAccuracy();
            
            indexOfOverlap = findOverlapIndex(sequences.getSecondSequence(),sequences.getFirstSequence());
        }
        
        //We've screwed up badly, neither side was able to find a match.
        if(indexOfOverlap[0] ==-1){
            return null;
        }
        
        //From here on out it's safe to assume that we found a match somewhere. Hurray!
        SequenceProbabilityPair first = new SequenceProbabilityPair(firstSequence,firstProbabilities);
        SequenceProbabilityPair second = new SequenceProbabilityPair(secondSequence,secondProbabilities);
        
        return mergeSequences(indexOfOverlap, first, second);
    }
    
    Sequence mergeSequences(int[] indexOfOverlap,SequenceProbabilityPair first,SequenceProbabilityPair second){
        String firstSequence = first.bases;
        String secondSequence = second.bases;
        String firstProbabilities = first.accuracy;
        String secondProbabilities = second.accuracy;
        
        String outputBases = firstSequence.substring(0, indexOfOverlap[0]);
        outputBases+=secondSequence.substring(indexOfOverlap[1]);      
        String outputProbabilities = firstProbabilities.substring(0, indexOfOverlap[0]);
        outputProbabilities+=secondProbabilities.substring(indexOfOverlap[1]); 
        return SequenceFactory.makeSequence(outputBases,outputProbabilities);
    }
    
    /**
     * Find the place where the sequences you're merging overlap. This implementation is meant to be overwritten.
     * @param firstSequence
     * @param secondSequence
     * @return 
     */
    int[] findOverlapIndex(Sequence first, Sequence second){
        String firstSequence = first.getBases();
        String secondSequence = second.getBases();
 //       System.out.println("Testing for merge of strings: "+firstSequence+" "+firstSequence.length()+" and "+secondSequence+" "+secondSequence.length());
        int[] output = new int[2];
        output[0]=-1;
        output[1]=-1;

        for(int i=0;i<firstSequence.length();i++){
            String thisWord = firstSequence.substring(i);
            for(int j=0;j<secondSequence.length();j++){
                String possibleSuffix = secondSequence.substring(j);
                //we want these to be the same length, so we can throw these out.
                if(possibleSuffix.length()>thisWord.length()){
                    possibleSuffix = possibleSuffix.substring(0,thisWord.length());
                }
   //             System.out.println("Comparing: "+thisWord+" and "+possibleSuffix);
                if(thisWord.equals(possibleSuffix)){
            //We have found the suffix we're looking for.
  //                  System.out.println("Found Match!");
                    if(thisWord.length()>=secondSequence.length()*ERROR_THRESHHOLD){
                        output[1] = j;
                        output[0] = i;
                        return output;
                    }  
                }
            }
        }
        
        return output;
    }
    
    /**
     * Simple container class to be used to pass sequences around internally.
     */
    class SequenceProbabilityPair{
        public SequenceProbabilityPair(String base,String acc){
            bases = base;
            accuracy = acc;
        }
        String bases;
        String accuracy;
    }
}
