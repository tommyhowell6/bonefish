/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hashSequencer;

/**
 *
 * @author Kris
 */
public class SimpleMerger implements SequenceMerger{
    private static final double threshhold = .5;
    
    
    /**
     * This is the most basic possible merger. It will search for a match between two sequences by reading both sequences, trying to find the point where they overlap.
     * This means it will not handle errors very well.
     * 
     * @param sequences, pair of seqeuences with their assosiated probabilities.
     * @return a single sequence with merged probabilities.
     */
    @Override
    public Sequence merge(SequencePair sequences) {
        //First, we'll check to see if the sequences are the same.
        if(sequences.getFirstSequence().equals(sequences.getSecondSequence())){
            return sequences.getFirstSequence();
        }
        String firstSequence = sequences.getFirstSequence().getBases();
        String firstProbabilities = sequences.getFirstSequence().getAccuracy();
        String secondSequence = sequences.getSecondSequence().getBases();
        String secondProbabilities = sequences.getSecondSequence().getAccuracy();

        
        //First, we proceed with the assumption that the second sequence in the order is the suffix.
        int[] indexOfOverlap = searchForOverlap(firstSequence,secondSequence);
        
        //If we are unable to find an overlap, it means the other sequence was the suffix. We need to switch them.
        if(indexOfOverlap[0]==-1){
            firstSequence = sequences.getSecondSequence().getBases();
            firstProbabilities = sequences.getSecondSequence().getAccuracy();
            secondSequence = sequences.getFirstSequence().getBases();
            secondProbabilities = sequences.getFirstSequence().getAccuracy();
            
            indexOfOverlap = searchForOverlap(firstSequence,secondSequence);
        }
        
        //We've screwed up badly, neither side was able to find a match.
        if(indexOfOverlap[0] ==-1){
            return null;
        }
        
        //From here on out it's safe to assume that we found a match somewhere. Hurray!
        String outputBases = firstSequence.substring(0, indexOfOverlap[0]);
        outputBases+=secondSequence.substring(indexOfOverlap[1]);
        
        String outputProbabilities = firstProbabilities.substring(0, indexOfOverlap[0]);
        outputProbabilities+=secondProbabilities.substring(indexOfOverlap[1]);
        
        Sequence output = new SimpleSequence(outputBases,outputProbabilities);
        
        return output;
    }
    
    /**
     * Basic searching algorithm. Returns -1 if no overlap can be found. 
     * @param firstSequence
     * @param secondSequence
     * @return The starting character in the second sequence where the overlap occurs.
     */
    private int[] searchForOverlap(String firstSequence, String secondSequence){
        System.out.println("Testing for merge of strings: "+firstSequence+" "+firstSequence.length()+" and "+secondSequence+" "+secondSequence.length());
        int[] output = new int[2];
        output[0]=-1;
        output[1]=-1;
        
        for(int i=0;i<firstSequence.length();i++){
            //System.out.println("Iterating on letter "+firstSequence.charAt(i)+" "+i+" of first sequence.");
            int match = 0;
            for(int j=0;j<secondSequence.length();j++){
                //System.out.println("\tIterating on letter "+secondSequence.charAt(j)+" "+j+" of second sequence.");
                if(firstSequence.charAt(i)==secondSequence.charAt(j)){
                    if(match==0){
                        System.out.print("Match: "+firstSequence.charAt(i));
                    }
                    else{
                        System.out.print(firstSequence.charAt(i));
                    }
                    match++;
                    
                }
                else{
                    if(match>0){
                        System.out.println("Match broken with: "+firstSequence.charAt(i)+" "+secondSequence.charAt(j));
                    }
                    match=0;
                    
                }
            }
            //We have found the suffix we're looking for.
            if(match>=secondSequence.length()*threshhold){
                output[1] = secondSequence.length()-match;
                output[0] = i;
                return output;
            }
        }
        
        return output;
    }
}
