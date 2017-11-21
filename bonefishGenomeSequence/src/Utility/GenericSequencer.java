/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.GenomeAssembler;
import Model.Sequence;
import hashSequencer.Sequencer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * To avoid repeating ourselves, this class can be used to do sequencing from the command line.
 * @author Kris
 */
public class GenericSequencer {
    private static GenomeAssembler assembler;
    
    public static void main(String [] args){
        if(args.length<2){
            System.out.println("Usage: java -jar Sequencer.jar ALGORITHM DIRECTORY_TO_FASTQ");
            System.exit(0);
        }
        
        //Select and instansiate the assembler.
        //This is where other people should add their algorithms!
        if(args[0].toLowerCase().equals("hash")){
            assembler = new Sequencer();
        }
        else{
            //PUT YOUR ELSES BEFORE THIS ONE!
            System.out.println("Invalid algorithm selection.");
            System.exit(0);
        }
        
        System.out.println("Beginning read of input data.");
        ArrayList<Sequence> rawData = importGenomeSequences(args[1]);
        
        if(rawData.isEmpty()){
            System.out.println("Sequencer encountered an error reading from input files and needs to close.");
            System.exit(0);
        }else{
            System.out.println("Input data read successfully!");
        }

        ArrayList<Sequence> output = (ArrayList) assembler.assemble(rawData);
        saveOutputGenome(output);  
    }

    private static void saveOutputGenome(ArrayList<Sequence> finalGenome) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static ArrayList<Sequence> importGenomeSequences(String path){
        FastQReader.initialize(SequenceType.SimpleSequence,PairBehavior.MERGE);
        try {
            return FastQReader.readDirectory(new File(path));
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to read any sequences\n"+ex);
        }
        return new ArrayList<>();
    }
    
}
