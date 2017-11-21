/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hashSequencer;

import Utility.SequencePair;
import Utility.Sequence;

/**
 *
 * @author Kris
 */
public class SimpleSequencePair implements SequencePair{
    private final Sequence firstSequence, secondSequence;
    
    public SimpleSequencePair(Sequence seq1, Sequence seq2){
        if(seq1==null|| seq2==null){
            throw new IllegalArgumentException("You cannot create a sequence pair without two sequences");
        }
        firstSequence = seq1;
        secondSequence = seq2;    
    }

    @Override
    public Sequence getFirstSequence() {
        return firstSequence;
    }

    @Override
    public Sequence getSecondSequence() {
        return secondSequence;
    }
    
}
