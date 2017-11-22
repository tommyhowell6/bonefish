/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.GenomeAssembler;
import Model.SampleGenome;
import Model.Sequence;
import java.util.ArrayList;

/**
 * Use this class to benchmark any assembler so long as it obeys the rules of the interfaces in the "Model" folder.
 * @author Kris
 */
public class UniversalBenchmark {
    private final GenomeAssembler assembler;
    
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
     */
    public void performBenchmark(int genomeSize, int readLength, int readOverlap, boolean genomeErrors){
        System.out.println("Beginning benchmark of assembler: "+assembler.getClass().toString());
        System.out.println("Building sample genome of length: "+genomeSize);
        long time = System.currentTimeMillis();
        SampleGenome genome = SampleGenomeFactory.buildSampleGenome(genomeSize,readLength,readOverlap,genomeErrors);
        time = time - System.currentTimeMillis();
        System.out.println("Genome generated in "+time+" miliseconds.");
        
        performBenchmarkWithGenome(genome);

    }

    /**
     * Performs a benchmark with a genome already generated. Useful if you want to test the same genome on multiple assemblers to compare their accuracy.
     * 
     * @param genome Sample genome to use in testing.
     */
    public void performBenchmarkWithGenome(SampleGenome genome) {
        long time = System.currentTimeMillis();
        System.out.println("Benchmark starting.");
        ArrayList<Sequence> output = assembler.assemble((ArrayList<Sequence>) genome.getReads());
        time = time -System.currentTimeMillis();
        System.out.println("Benchmark complete in: "+time+" miliseconds.");
        //Now that we're finished, check to see how good a job we did.
        if(output.size()==1){
            System.out.println("Assembler successfully assembled complete chromosome!");
        }
        else{
            System.out.println("Final assembly resulted incorrectly in "+output.size()+" chromosomes.");
        }
        
        //First, see if we have the correct sequence.
        if(genome.getGenome().getBases().equals(output.get(0).getBases())){
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
        }
        
    }
    
    
}
