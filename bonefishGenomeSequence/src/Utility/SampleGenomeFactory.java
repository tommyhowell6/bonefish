package src.Utility;

import src.Model.SampleGenome;
import src.Model.Sequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Kris
 */
class SampleGenomeFactory {
    public static final int DEFAULT_READ_LENGTH = 150;
    public static final int DEFAULT_READ_OVERLAP = 50;
    public static final boolean DEFAULT_HAS_ERRORS = false;
    private static final int ERROR_THRESHHOLD = 114;
    public static final char ERROR_CHAR = '-';
    private static long TOTAL_SEQUENCES=0;
    private static String clean_accuracy = "";
    /**
     * Uses the defaults when building a sample genome.
     * @param genomeSize
     * @return The generated sample genome.
     */
    public static SampleGenome buildSampleGenome(int genomeSize){
        return buildSampleGenome(genomeSize, DEFAULT_READ_LENGTH,DEFAULT_READ_OVERLAP,DEFAULT_HAS_ERRORS, false);
    }
    /**
     * Constructs a sample genome without reading in from files.
     * @param genomeSize Total length of the finished genome.
     * @param readLength Length of each read.
     * @param readOverlap How much each individual base pair will be overlapped.
     * @param genomeErrors Whether or not this genome contains errors.
     * @return The collection of sequence objects corresponding to the reads. 
     * 
     */
    public static SampleGenome buildSampleGenome(int genomeSize, int readLength, int readOverlap, boolean genomeErrors, boolean includePairs) {
        Sequence finalGenome = generateGenome(genomeSize);
        Collection<Sequence> output = new ArrayList<>();
        Random generator = new Random();
        int numForCoverage = genomeSize/readLength;
        int numReads = numForCoverage * readOverlap;
        
        //Generate an individual read. There are many, many of these, taken at random.
        for(int i=0;i<numReads;i++){
            int begin = generator.nextInt(genomeSize);
            int end = begin+readLength;
            if(end>=genomeSize)
                end = genomeSize-1;
            
            String cleanBases = finalGenome.getBases().substring(begin, end);
            String pairBases = "";
            Sequence pair = null;
            Sequence currentSequence;
            
            //If we need to include pairs in the genome we're making, we should make the bases for that as well.
            if(includePairs){
                String temp = new StringBuilder(cleanBases).reverse().toString();
                for(int k=0;k<temp.length();k++){
                    pairBases+=PairedReadMerger.invertChar(temp.charAt(k));
                }
            }
            
            if(genomeErrors){
                currentSequence = generateDirtySequence(cleanBases);
                if(includePairs){
                    pair = generateDirtySequence(pairBases);
                }
            }
            //We don't need errors or anything so we can just make a new sequence right here.
            else{
                StringBuilder accuracy = new StringBuilder();
                for(int j=0;j<cleanBases.length();j++){
                    //Randomly select a probability character that indicates a relatively clean read.
                    int tempCharacter = generator.nextInt(30)+33;
                    char tempChar = (char)tempCharacter;
                    accuracy.append(tempChar);
                }
                currentSequence = SequenceFactory.makeSequence(cleanBases, accuracy.toString(),getID());
                if(includePairs){
                    pair = SequenceFactory.makeSequence(pairBases, accuracy.toString());
                }
            }
            //Now we need to finish the pair.
            if(includePairs&&pair!=null){
                Sequence halfOfPair = SequenceFactory.makeSequence(pair.getBases(), pair.getAccuracy(),getPairID(currentSequence.getID()));
                
                currentSequence.givePairedRead(halfOfPair);
                halfOfPair.givePairedRead(currentSequence);
                //System.out.println("Added: "+halfOfPair);
                output.add(halfOfPair);
            }
           // System.out.println("Added: "+currentSequence);
            output.add(currentSequence);
        }
        
        

        return guarenteeCoverage(finalGenome,output,readLength,includePairs);
    }

    private static Sequence generateGenome(int genomeSize) {
        StringBuilder output = new StringBuilder();
        StringBuilder probability = new StringBuilder();
        Random generator = new Random();
        int num;
        
        for(int i=0;i<genomeSize;i++){
            probability.append('!');
            num = generator.nextInt(4);
            switch(num){
                case 0:
                    output.append('A');
                    break;
                case 1:
                    output.append('T');
                    break;
                case 2:
                    output.append('C');
                    break;
                case 3:
                    output.append('G');
                    break;
            }
        }
        return SequenceFactory.makeSequence(output.toString(),probability.toString());
    }

    /**
     * We need a sequence that includes some errors. I'm kinda guessing on these since I haven't seen real reads.
     * @param cleanBases The read before we introduce some fake errors.
     * @return 
     */
    private static Sequence generateDirtySequence(String cleanBases) {
        StringBuilder accuracy = new StringBuilder();
        Random generator = new Random();
        for(int i=0;i<cleanBases.length();i++){
            int thisChar = generator.nextInt(93)+33;
            char accChar = (char)thisChar;
            accuracy.append(accChar);
            if(thisChar>=ERROR_THRESHHOLD){
                String start = cleanBases.substring(0, i);
                String rest = ERROR_CHAR+cleanBases.substring(i+1);
                cleanBases = start+rest;
            }
        }

        return SequenceFactory.makeSequence(cleanBases, accuracy.toString());
    }

    private static String getID() {
        long id = ++TOTAL_SEQUENCES;
        String output = "@KRIS_TEST_SEQUENCER:"+id+":READ/1";
        
        return output;
    }
    
    private static String getPairID(String input) {
        String output = input.substring(0,input.length()-2)+"/2";
        return output;
    }

    /**
     * If we want to guarentee that the entire sample genome is covered, we must add a few more sequences.
     * This will also randomize the final order of the output before it is returned.
     * @param finalGenome
     * @param output
     * @return sample genome with reads that guarentee total coverage.
     */
    private static SampleGenome guarenteeCoverage(Sequence finalGenome, Collection<Sequence> output, int readLength, boolean hasPair) {
        String bases = finalGenome.getBases();
        int startIndex = 0;
        while(startIndex<bases.length()){
            int endIndex = startIndex + readLength;
            if(endIndex >= bases.length()){
                endIndex = bases.length()-1;
            }
            String readBases = bases.substring(startIndex,endIndex);
            String accuracy = getAccuracy(readBases.length());
            String id = getID();
            Sequence tempSequence = SequenceFactory.makeSequence(readBases, accuracy, id);
            if(hasPair){
                String temp = new StringBuilder(readBases).reverse().toString();
                String pairBases = "";
                for(int k=0;k<temp.length();k++){
                    pairBases+=PairedReadMerger.invertChar(temp.charAt(k));
                }
                Sequence tempPairedSequence = SequenceFactory.makeSequence(pairBases, accuracy,getPairID(id));
                tempSequence.givePairedRead(tempPairedSequence);
                tempPairedSequence.givePairedRead(tempSequence);
                output.add(tempPairedSequence);
            }
            
            output.add(tempSequence);
            startIndex+=readLength;
        }
        
        
        return new SimpleSampleGenome(finalGenome,randomizeReads((ArrayList<Sequence>) output));
    }
    
    private static String getAccuracy(int readLength){
        if(clean_accuracy.length()==readLength){
            return clean_accuracy;
        }
        clean_accuracy ="";
        for(int i=0;i<readLength;i++){
            clean_accuracy+="~";
        }
        return clean_accuracy;
    }

    /**
     * Shuffles the order of the random reads, that way we don't get everything at the same time.
     * @param output unshuffled list of sequences.
     * @return phsudorandomized list of the same sequences in a different order.
     */
    private static Collection<Sequence> randomizeReads(ArrayList<Sequence> raw) {
        Collection<Sequence> output = new ArrayList<>();
        Random random = new Random();
        
        while(raw.size()>0){
            int index = random.nextInt(raw.size());
            Sequence tempSequence = raw.remove(index);
            output.add(tempSequence);
        }
        
        return output;
    }
}
