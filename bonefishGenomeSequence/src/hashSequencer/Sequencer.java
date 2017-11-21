/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

import Utility.SequenceMerger;
import Utility.SequencePair;
import Utility.Sequence;
import java.util.ArrayList;

/**
 *
 * @author Kris
 */
public class Sequencer {
    private static GenomeHashSet genome;
    private static SequenceMerger merger;
    
    public static void main (String [] args){
        
        
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
        ArrayList rawData = importGenomeSequences();
        addSequencesToHashSet(rawData);
        while(!genome.finished()){
            SequencePair match = genome.selectClosestMatch();
            Sequence merged = merger.merge(match);
            genome.remove(match.getFirstSequence());
            genome.remove(match.getSecondSequence());
            genome.add(merged);           
        }
        
//        saveOutputGenome();
    }
    //Read files in passed directory into 
    private static ArrayList importGenomeSequences(){
        
        
        return null;
    }
    
    
    //
    private static void addSequencesToHashSet(ArrayList input){
        
        
    }

    private static void saveOutputGenome() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
