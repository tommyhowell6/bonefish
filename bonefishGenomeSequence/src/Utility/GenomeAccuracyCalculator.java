/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Model.Sequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Kris
 */
public class GenomeAccuracyCalculator {
    private static final int TESTCOVERAGE = 1000;
    private static final int TESTLENGTH = 50;
    
    
    public static String calculateCoverage(Sequence correctGenome, Collection<Sequence> assembledGenome){
        String output = "Genome coverage: ";
        output+="\nAssembled genome of 1 chromosome into "+assembledGenome.size();
        int size=0;
        for(Sequence seq: assembledGenome){
            size+=seq.getBases().length();
        }
        output+="\nCorrect genome size: "+correctGenome.getBases().length()+" Assembled size: "+size;
        double coverageScore = 100;
        double missed = Math.abs(correctGenome.getBases().length()-size);
        missed = missed / (double)correctGenome.getBases().length();
        coverageScore -=missed;
        coverageScore -= (assembledGenome.size()-1)*10;
        output+="\nArbitrary coverage score: "+coverageScore;
        output+="\nWill now test for coverage with some random sequences";
        double coverage = getRandomCoverage(correctGenome.getBases(), (ArrayList<Sequence>) assembledGenome);
        output+="\nOut of"+TESTCOVERAGE+" tests, the assembled genome correctly selected "+coverage;
        output+="\nCoverage percentage: "+(coverage/TESTCOVERAGE)+"%";
        return output;
    }

    /**
     * Attemps a monte-carlo based random verification of genome coverage.
     * @param bases The genome that we are testing for coverage of
     * @param assembledGenome All the pieces of the potentally assembled genome.
     * @return 
     */
    private static int getRandomCoverage(String bases, ArrayList<Sequence> assembledGenome) {
        int testsPerformed =0;
        int successes = 0;
        ArrayList<Sequence> tooShort = new ArrayList<>();
        Random random = new Random();
        while(testsPerformed<TESTCOVERAGE&&assembledGenome.size()>0){
            int indexOfCurrentSequence = random.nextInt(assembledGenome.size());
            if(assembledGenome.get(indexOfCurrentSequence).getBases().length()<TESTLENGTH){
                tooShort.add(assembledGenome.remove(indexOfCurrentSequence));
            }
            else{
                int randomStart = random.nextInt(assembledGenome.get(indexOfCurrentSequence).getBases().length());
                int randomEnd = randomStart +=TESTLENGTH;
                if(randomEnd>assembledGenome.get(indexOfCurrentSequence).getBases().length()-1){
                    randomEnd = assembledGenome.get(indexOfCurrentSequence).getBases().length()-1;
                }
                String sample = assembledGenome.get(indexOfCurrentSequence).getBases().substring(randomStart,randomEnd);
                
                if(bases.contains(sample)){
                    successes++;
                }
            }
        }
        
        assembledGenome.addAll(tooShort);
        return successes;
    }
    
}
