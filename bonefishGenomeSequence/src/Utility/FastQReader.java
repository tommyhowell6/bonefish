package Utility;

import Model.Sequence;
import Model.SequencePair;
import hashSequencer.SimpleSequencePair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  The purpose of this utility is to import FastQ files into the standard read format.
 * @author Kris
 */
public class FastQReader {

    private static PairBehavior pairBehavior;
    
    /**
     *
     * @param sequenceType
     * @param behavior
     */
    public static void initialize(SequenceType sequenceType, PairBehavior behavior){
        SequenceFactory.setType(sequenceType);
        pairBehavior = behavior;
        
    }
    
    /**
     * This method uses the readFile method to read every single sequence in a given directory.
     * Will recursively search in nested files as well.
     * @param folder
     * @return 
     * @throws java.io.FileNotFoundException 
     */
    public static ArrayList<Sequence> readDirectory(File folder) throws FileNotFoundException{
        ArrayList<Sequence> output = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                output.addAll(readDirectory(fileEntry));
            } else {
                output.addAll(readFile(fileEntry));
            }
        }
        
        return output;
    }
    
    //This class reads in a single fastQ file. Obviously many such reads would be required before we can begin.
    public static ArrayList<Sequence> readFile(File fileName) throws FileNotFoundException{
        ArrayList<Sequence> output = new ArrayList<>();

        System.out.println("Recieved file in: "+fileName);
        //We need to make sure we don't read a file that isn't a fastq.
        if(!fileName.toString().contains(".fastq")){
            System.out.println("Attempted to read a non FASTQ file: "+fileName.toString());
            return output;
        }

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
                            lastSequence = SequenceFactory.makeSequence(workingRead, workingAccuracy, workingID);
                        }
                        else{
                            Sequence thisSequence =SequenceFactory.makeSequence(workingRead, workingAccuracy, workingID);
                            thisSequence.givePairedRead(lastSequence);
                            lastSequence.givePairedRead(thisSequence);
                            output.addAll(Arrays.asList(handlePair(new SimpleSequencePair(lastSequence,thisSequence))));
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

    private static Sequence[] handlePair(SequencePair sequences) {
        
        switch(pairBehavior){
            case DUMB:
                Sequence[] output = {sequences.getFirstSequence(),sequences.getSecondSequence()};
                return output;
            case MERGE:
                Sequence[] output2 = {PairedReadMerger.mergePairedRead(sequences)};
                return output2;
            case DISCARD:
                Sequence[] output3 = {PairedReadMerger.discardWorseRead(sequences)};
                return output3;
            case FLIP:
                return PairedReadMerger.flipPairedRead(sequences);
        }
        return null;
    }
    private enum ReadState {
        ID, READ, BLANK, ACCURACY
    }
}
