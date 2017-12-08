/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.GenomeAssembler;
import Model.SampleGenome;
import TrieSquencer.TrieRunner;
import hashSequencer.Sequencer;

/**
 *
 * @author Kris
 */
public class RunBenchmark {
    private static final GenomeAssembler TEST_ASSEMBLER = new TrieRunner();
    private static final UniversalBenchmark TEST_BENCHMARK = new UniversalBenchmark(TEST_ASSEMBLER); 
    public static void main (String [] args){
        System.out.println("Preparing to run benchmark.");
        int genomeSize = 100000;
        int readLength = 150;
        int readOverlap = 10;
        boolean includePairs = false;
        boolean includeErrors = false;
        SampleGenome testGenome = SampleGenomeFactory.buildSampleGenome(genomeSize, readLength, readOverlap, includeErrors, includePairs);
        
        TEST_BENCHMARK.performBenchmarkWithGenome(testGenome);
        
        System.out.println("Benchmark complete.");
    }
}
