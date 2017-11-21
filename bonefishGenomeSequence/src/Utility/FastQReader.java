/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *  The purpose of this utility is to import FastQ files into the standard read format.
 * @author Kris
 */
public class FastQReader {
    private static SequenceFactory sequencePrinter;
    
    /**
     *
     * @param sequenceType
     */
    public static void initialize(SequenceType sequenceType){
        sequencePrinter = new SequenceFactory(sequenceType);
    }
    
    /**
     * This method uses the readFile method to read every single sequence in a given directory.
     * @param directory
     * @return 
     */
    public static ArrayList<Sequence> readDirectory(File directory){
        ArrayList<Sequence> output = new ArrayList<>();
        
        
        return output;
    }
    
    //This class reads in a single fastQ file. Obviously many such reads would be required before we can begin.
    public static ArrayList<Sequence> readFile(File fileName) throws FileNotFoundException{
        ArrayList<Sequence> output = new ArrayList<>();

        System.out.println("Recieved file in: "+fileName);

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(fileName.toPath(), charset)) {
            String line = null;
            ReadState lastState = ReadState.ACCURACY;
            String workingRead="";
            String workingAccuracy=""; 
            String workingID="";
            Sequence lastSequence =null;
            while ((line = reader.readLine()) != null) {

                switch(lastState){
                    //We are expecting a new ID.
                    case ACCURACY: 
                        workingID=line;
                        lastState = ReadState.ID;
                        break;
                    //We are expecting a sequence.
                    case ID:
                        workingRead = line;
                        lastState = ReadState.READ;
                        break;
                    case READ:
                        lastState = ReadState.BLANK;
                        //we don't need to do anything here. This line is usually blank in FASTQ files.
                        break;
                    //We are expecting accuracy data.
                    case BLANK:
                        workingAccuracy = line;
                        lastState = ReadState.ACCURACY;
                        //TODO: What are we going to do with our pairs? For now, I'll just past references.
                        if(lastSequence==null){
                            lastSequence = sequencePrinter.makeSequence(workingRead, workingAccuracy, workingID);
                        }
                        else{
                            Sequence thisSequence =sequencePrinter.makeSequence(workingRead, workingAccuracy, workingID);
                            thisSequence.givePairedRead(lastSequence);
                            lastSequence.givePairedRead(thisSequence);
                            output.add(lastSequence);
                            output.add(thisSequence);
                            lastSequence=null;
                        }                       
                        break;    
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return output;
    }
    
}