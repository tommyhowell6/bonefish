package src.Utility;

import src.Model.GenomeAssembler;
import src.Model.SampleGenome;
import src.Model.Sequence;
import java.util.ArrayList;

/**
 * Use this class to benchmark any assembler so long as it obeys the rules of the interfaces in the "Model" folder.
 * @author Kris
 */
public class UniversalBenchmark {
    private final GenomeAssembler assembler;
    private static final int DEFAULT_GENOME_SIZE = 1000000;
    
    public UniversalBenchmark(GenomeAssembler in){
        assembler = in;
    }
    
    /**
     * Performs a benchmark of a given genome assembler algorithm. The algorithm must obey the interfaces in the model package.
     * results are outputed to the console.
     * @param genomeSize The total length of the finished genome in base pairs.
     * @param readLength The length of individual reads.
     * @param readOverlap The amount of reads that will cover a sample section of the genome.
     * @param genomeErrors Whether or not the sample reads contain errors and blank spots.
     * @param pairs Whether or not to include the pairs from the reads.
     */
    public void performBenchmark(int genomeSize, int readLength, int readOverlap, boolean genomeErrors, boolean pairs){
        System.out.println("Beginning benchmark of assembler: "+assembler.getClass().toString());
        System.out.println("Building sample genome of length: "+genomeSize);
        long time = System.currentTimeMillis();
        SampleGenome genome = SampleGenomeFactory.buildSampleGenome(genomeSize,readLength,readOverlap,genomeErrors, pairs);
        time = System.currentTimeMillis()-time;
        System.out.println("Genome generated in "+time+" miliseconds.");
        
        performBenchmarkWithGenome(genome);

    }
    public void performBenchmark(){
        performBenchmark(DEFAULT_GENOME_SIZE,SampleGenomeFactory.DEFAULT_READ_LENGTH,SampleGenomeFactory.DEFAULT_READ_OVERLAP,SampleGenomeFactory.DEFAULT_HAS_ERRORS, false);
    }

    /**
     * Performs a benchmark with a genome already generated. Useful if you want to test the same genome on multiple assemblers to compare their accuracy.
     * 
     * @param genome Sample genome to use in testing.
     * @return number of miliseconds required for assembling the sample genome.
     */
    public long performBenchmarkWithGenome(SampleGenome genome) {
        long time = System.currentTimeMillis();
        System.out.println("Benchmark starting.");
        ArrayList<Sequence> output = assembler.assemble((ArrayList<Sequence>) genome.getReads());
        time = System.currentTimeMillis()-time;
        System.out.println("Benchmark complete in: "+time+" miliseconds.");
        //Now that we're finished, check to see how good a job we did.
        if(output.size()==1){
            System.out.println("Assembler successfully assembled complete chromosome!");
        }
        else{
            System.out.println("Final assembly resulted incorrectly in "+output.size()+" chromosomes.");
        }
        
        //First, see if we have the correct sequence.
        if(output.size()==1&&genome.getGenome().getBases().equals(output.get(0).getBases())){
            System.out.println("Genome assembly was perfectly accurate! Congraduations!");
        }
        //If we don't, we should see how many of our sequences are contained in the correct genome.
        else{
            int contained =0;
            String finalGenome = genome.getGenome().getBases();
            for(Sequence sequence: output){
                if(finalGenome.contains(sequence.getBases())){
                    contained+=sequence.getBases().length();
                }
            }
            
            double coverage = (double)contained/(double)finalGenome.length();
            if(coverage<1){
                System.out.println("Genome coverage = "+coverage);
            }
            else{
                System.out.println("Genome incorrectly assembled. No accuracy reading is possible.");
            }
			if(output.size() == 1) {
                double correctRatio = getCorrectRatio(output.get(0).getBases(), finalGenome);
                System.out.println("Ratio of genome bases correct within first output Sequence: " + correctRatio);
            }
        }
        
        return time;
    }
	
	
	
	
    /**
     *
     * This function *attempts* to calculate the ratio of matching genome bases between the final genome
     * and the given output (if output only has 1 string).
     *
     * This is done by checking each cyclical rotation of the finalGenome against the given output
     * and keeping the highest match score
     * (this accounts for shifts that may have pushed the output off by any number of chars,
     * though each cyclical rotation only accounts for one shift of varying length, and not multiple, which is a big problem
     * in trying to understand just how close the genome was).
     *
     * Some examples include: (finalGenome, output(0), resultingScore)
     * (ACAAT, ACAAT, 1.0)
     * (ACAAT, AGGGG, 0.2)
     * (ACAAT, AAAT, 0.6)
     * (ACTGACTGACTG, ACTACTGACT, 0.333)
     * (ACTGACTGACTG, AAGGAAGGAAGG, 0.5)
     * ^^^These last 2 show the current problem with this comparison. By skipping just two bases but getting the other 10,
     * one output gets 33% accuracy. However, for an algorithm that switches 6 of the bases with an incorrect one,
     * it still gets 50% accuracy. This will be especially harmful on longer comparisons. More work may be done
     * to improve this comparison to truly determine if our sequencers are effective.
     *
     * While picking randomly from 4 chars gives a 25% (0.25) chance of picking the right char,
     * this doesn't necessarily mean a great output will be significantly higher than 0.25,
     * especially if the sequence is accurate and just shifted over in multiple spots. We may need
     * to develop more helpful ways of checking the accuracy if a single Sequence is returned.
     * @param onlyOutput
     * @param finalGenome
     * @return
     */
    private double getCorrectRatio(String onlyOutput, String finalGenome) {
        ArrayList<String> cyclicRotations = new ArrayList<>();

        for(int i = 0; i < finalGenome.length(); i++)
        {
            StringBuilder newSB = new StringBuilder();
            newSB.append(finalGenome.substring(finalGenome.length()-i));
            newSB.append(finalGenome.substring(0, finalGenome.length()-i));
            cyclicRotations.add(newSB.toString());

        }

        double bestScore = 0;
        for(int rotationIndex = 0; rotationIndex < cyclicRotations.size(); rotationIndex++)
        {


            double equalBases = 0;
            for (int i = 0; i < finalGenome.length(); i++) {
                if (onlyOutput.length() <= i) {
                    break;
                } else {
                    if (cyclicRotations.get(rotationIndex).charAt(i) == onlyOutput.charAt(i)) {
                        equalBases++;
                    }
                }
            }

            if(equalBases > bestScore)
            {
                bestScore = equalBases;
            }
        }
        return bestScore / finalGenome.length();
    }
       
}
