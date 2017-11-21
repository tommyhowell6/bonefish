/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import hashSequencer.SimpleSequence;

/**
 * This class is a simple way to change the way sequences are created in our program.
 * By initializing this differently, we can use different sequence data if we want to.
 * @author Kris
 */
public class SequenceFactory {
    private final SequenceType type;
    
    public SequenceFactory(SequenceType inputType){
        type = inputType;
    }
    
    public Sequence makeSequence(String read, String accuracy, String id){
        switch(type){
            case SimpleSequence:
                return new SimpleSequence(read, accuracy,id);
        }
        
        return null;
    }
    
    public Sequence makeSequence(String read, String accuracy){
        switch(type){
            case SimpleSequence:
                return new SimpleSequence(read, accuracy);
        }
        
        return null;
    }
    
}
