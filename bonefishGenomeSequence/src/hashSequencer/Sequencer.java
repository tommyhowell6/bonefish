/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

import Model.GenomeAssembler;
import Model.Sequence;
import Utility.SequenceMerger;
import Model.SequencePair;
import java.util.ArrayList;

/**
 *
 * @author Kris
 */
public class Sequencer implements GenomeAssembler{
    private static GenomeHashSet genome;
    private static SequenceMerger merger;
    
    @Override
    public ArrayList<Sequence> assemble(ArrayList<Sequence> sequences) {
        ArrayList<Sequence> output = new ArrayList<>();
                
        /*****
         * 1. Add genome to hashSet.
         * 2. while (!genome.finished()):
         *      a. Select two sequences with greatest allignment
         *      b. Merge those sequences
         *      c. Remove both from hashSet.
         *      d. Add new sequence to hashSet.
         * 
         * 3. Return finished genome.
         */
        addSequencesToHashSet((ArrayList) sequences);
        while(!genome.finished()){
            SequencePair match = genome.selectClosestMatch();
            Sequence merged = merger.merge(match);
            genome.remove(match.getFirstSequence());
            genome.remove(match.getSecondSequence());
            genome.add(merged);           
        }
        
        return output;
    }

    
    private static void addSequencesToHashSet(ArrayList input){
        
    }   
}
