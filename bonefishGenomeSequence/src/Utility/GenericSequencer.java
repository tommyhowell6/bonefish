package Utility;

import Model.GenomeAssembler;
import Model.Sequence;
import hashSequencer.Sequencer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        
        //Select and instantiate the assembler.
        //This is where other people should add their algorithms!
        if(args[0].toLowerCase().equals("hash")){
            assembler = new Sequencer();
        }
        else if(args[0].toLowerCase().equals("euler")){
            assembler = new EulerianSequencer();
        }
        else{
            //PUT YOUR ELSES BEFORE THIS ONE!
            System.out.println("Invalid algorithm selection.");
            System.exit(0);
        }
        
        System.out.println("Beginning read of input data.");
        System.out.println("Data located at: "+args[1]);
        String property = System.getProperty("user.dir")+args[1];
        ArrayList<Sequence> rawData = importGenomeSequences(property);
        
        if(rawData.isEmpty()){
            System.out.println("Sequencer encountered an error reading from input files and needs to close.");
            System.exit(0);
        }else{
            System.out.println("Input data read successfully!");
        }

        ArrayList<Sequence> output = (ArrayList<Sequence>) assembler.assemble(rawData);
        try {  
            saveOutputGenome(output);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenericSequencer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found: "+ex);
        } catch (IOException ex) {
            Logger.getLogger(GenericSequencer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IO Exception: "+ex);
        }
    }

    private static void saveOutputGenome(ArrayList<Sequence> finalGenome) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("OUTPUT.txt"), "utf-8"))) {
            writer.write("GENOME ASSEMBLY OUTPUT\n");
            writer.write("Total Unsorted Sequences: "+finalGenome.size()+"\n");
            for(Sequence sequence:finalGenome){
                writer.write("Begin sequence:");
                writer.write(sequence.getID()+"\n");
                writer.write(sequence.getBases());
            }
         }
    }

    private static ArrayList<Sequence> importGenomeSequences(String path){
        FastQReader.initialize(SequenceType.SimpleSequence,PairBehavior.MERGE);
        try {
            File file = new File(path);

            return FastQReader.readDirectory(file);
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to read any sequences\n"+ex);
        }
        return new ArrayList<>();
    }
}
