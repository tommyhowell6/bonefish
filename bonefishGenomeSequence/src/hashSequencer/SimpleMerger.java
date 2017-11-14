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
        
        
        
        return null;
    }
    
}
