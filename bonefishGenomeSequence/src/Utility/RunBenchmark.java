/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src.Utility;

import src.Model.GenomeAssembler;
import src.Model.SampleGenome;
import src.TrieSquencer.TrieRunner;
import src.hashSequencer.Sequencer;

/**
 *
 * @author Kris
 */
public class RunBenchmark {
    private static final GenomeAssembler TEST_ASSEMBLER = new TrieRunner();
    private static final UniversalBenchmark TEST_BENCHMARK = new UniversalBenchmark(TEST_ASSEMBLER);
    public static final String INFILE_FASTQ = "output.fastq";
    public static void main (String [] args){
        System.out.println("Preparing to run benchmark.");
        int genomeSize = 100000;
        int readLength = 150;
        int readOverlap = 10;
        boolean includePairs = false;
        boolean includeErrors = false;
//        SampleGenome testGenome = SampleGenomeFactory.buildSampleGenome(genomeSize, readLength, readOverlap, includeErrors, includePairs);
        SampleGenome testGenome = FromFileGenomeFactory.readGenome(INFILE_FASTQ);
        TEST_BENCHMARK.performBenchmarkWithGenome(testGenome);
        
        System.out.println("Benchmark complete.");
    }
    
}
