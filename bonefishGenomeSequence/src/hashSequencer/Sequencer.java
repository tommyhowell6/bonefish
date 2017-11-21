/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

import Utility.FastQReader;
import Utility.Sequence;
import Utility.SequenceMerger;
import Utility.SequencePair;
import Utility.SequenceType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kris
 */
public class Sequencer {
    private static GenomeHashSet genome;
    private static SequenceMerger merger;
    
    public static void main (String [] args){
        if(args.length<1){
            System.out.println("Usage: java -jar Sequencer.jar DIRECTORY_TO_FASTQ");
            System.exit(0);
        }
        
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
        ArrayList<Sequence> rawData = importGenomeSequences(args[0]);
        if(rawData.isEmpty()){
            System.out.println("Sequencer encountered an error reading from input files and needs to close.");
            System.exit(0);
        }
        
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
    private static ArrayList<Sequence> importGenomeSequences(String path){
        FastQReader.initialize(SequenceType.SimpleSequence);
        try {
            return FastQReader.readDirectory(new File(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sequencer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to read any sequences\n"+ex);
        }
        return new ArrayList<>();
    }
    
    
    //
    private static void addSequencesToHashSet(ArrayList input){
        
        
    }

    private static void saveOutputGenome() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
