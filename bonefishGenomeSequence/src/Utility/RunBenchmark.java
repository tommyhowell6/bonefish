/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.GenomeAssembler;
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
        TEST_BENCHMARK.performBenchmark(100000, 150, 50, false);
        System.out.println("Benchmark complete.");
    }
    
}
