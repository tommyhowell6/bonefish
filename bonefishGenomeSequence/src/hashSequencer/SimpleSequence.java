/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hashSequencer;

import Model.Sequence;
import java.util.Objects;

/**
 *
 * @author Kris
 */
public class SimpleSequence implements Sequence{
    private final String bases,accuracy, id;
    private static long mergeNumber = 0;
    private Sequence pair;

    public SimpleSequence(){
        bases = null;
        accuracy = null;
        id = null;
    }

    public SimpleSequence(String base,String acc, String identifier){
        if(base ==null||acc==null|| base.length()!=acc.length()){
            System.out.println("Bases: "+base);
            System.out.println("Accur: "+acc);
            throw new IllegalArgumentException("Please be sure the probability matches the sequence!");
        }
        
        bases = base;
        accuracy = acc;
        id = identifier;
    }
    /**
     * The purpose of this constructor is for making merged reads. They will not have an universal ID, so they are instead made.
     * WARNING! THIS METHOD IS NOT THREAD SAFE!
     * @param base
     * @param acc 
     */
    public SimpleSequence (String base, String acc){
        if(base ==null||acc==null|| base.length()!=acc.length()){
            System.out.println("Encountered an illigial sequence: ");
            System.out.println("Bases: "+base);
            System.out.println("Accur: "+acc);
            throw new IllegalArgumentException("Please be sure the probability matches the sequence!");
        }
        bases = base;
        accuracy = acc;
        id="@merged-sequence-"+(++mergeNumber);
    }
    
    /**
     * This method is used to make a sequence when all we're looking for is the bases. Useful for testing,
     * but do not use when actually running the program.
     * @param base 
     */
    public SimpleSequence(String base){
        String acc="";
        for(int i=0;i<base.length();i++){
           acc+="!";
        }
        accuracy =acc;
        bases = base;
        id="@test-sequence-do-not-use";
    }

    @Override
    public String getBases() {
        return bases;
    }

    @Override
    public String getAccuracy() {
        return accuracy;
    }
    
    @Override
    public boolean equals(Object o){
        if(o==null||o.getClass()!=this.getClass()){
            return false;
        }
        SimpleSequence testObject = (SimpleSequence) o;
        String testBases = testObject.getBases();

        for (int i = 0; i < testBases.length(); i++) {
            if (this.bases.charAt(i) != testBases.charAt(i)){
                return false;
            }
        }
        return true;

       // if(this.hashCode()== o.hashCode()){
       //     return true;
       // }
    }

    @Override
    public int hashCode() {        
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.bases);
        hash = 29 * hash + Objects.hashCode(this.accuracy);
        return hash;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void givePairedRead(Sequence sequence) {
        pair = sequence;
    }

    @Override
    public Sequence getPairedRead() {
        return pair;
    }
    
}
