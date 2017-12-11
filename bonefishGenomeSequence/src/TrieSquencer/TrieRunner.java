/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrieSquencer;

import Model.GenomeAssembler;
import Model.Sequence;
import Utility.SequenceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kris
 */
public class TrieRunner implements GenomeAssembler{

    @Override
    public ArrayList<Sequence> assemble(ArrayList<Sequence> sequences) {
        GenomeSequencer genomeSequencer = new GenomeSequencer();
        for(Sequence sequence: sequences){
            genomeSequencer.addNode(sequence.getBases());
        }
        
        List<String> potentialStarts = new ArrayList<>();
        for(Sequence sequence: sequences){
                String potentialStart = sequence.getBases();
                if(genomeSequencer.prefixNotInTrie(potentialStart))
                {
                    potentialStarts.add(potentialStart);
                }
        }
        ArrayList<Sequence> output = new ArrayList<>();
        
        for (String potentialStart : potentialStarts){
                String temp = genomeSequencer.runStartAgainstTrie(potentialStart);
                if(!temp.equalsIgnoreCase("likely error")){
                    output.add(SequenceFactory.makeTestSequence(temp));
                }
                else{
                    System.out.println("Encountered a likely error");
                }
        }
        
        return output;
        
    }
    
}
