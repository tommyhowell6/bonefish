/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src.Utility;

import src.Model.Sequence;
import src.hashSequencer.SimpleSequence;

/**
 * This class is a simple way to change the way sequences are created in our program.
 * By initializing this differently, we can use different sequence data if we want to.
 * @author Kris
 */
public class SequenceFactory {
    private static SequenceType type;
    private static final SequenceType DEFAULT_TYPE = SequenceType.SimpleSequence;
    
    public static void setType(SequenceType inputType){
        type = inputType;
    }
    
    public static Sequence makeSequence(String read, String accuracy, String id){
        switch(type){
            case SimpleSequence:
                return new SimpleSequence(read, accuracy,id);
        }
        
        return null;
    }
    
    public static Sequence makeSequence(String read, String accuracy){
        if(type ==null){
            type = DEFAULT_TYPE;
        }
        
        switch(type){
            case SimpleSequence:
                return new SimpleSequence(read, accuracy);
        }
        
        return null;
    }
    
    /**
     * This is for making sequences with only a read. This should only be used when the score doesn't matter, as in for testing.
     * @param read
     * @return 
     */
    public static Sequence makeTestSequence(String read){
               if(type ==null){
            type = DEFAULT_TYPE;
        }
        
        switch(type){
            case SimpleSequence:
                return new SimpleSequence(read);
        }
        
        return null; 
    }
    
}
