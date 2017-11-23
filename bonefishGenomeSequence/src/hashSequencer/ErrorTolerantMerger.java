/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hashSequencer;

import Model.Sequence;

/**
 *
 * @author Kris
 */
public class ErrorTolerantMerger extends SimpleMerger{

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
        throw new UnsupportedOperationException("Not yet implemented");
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
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
