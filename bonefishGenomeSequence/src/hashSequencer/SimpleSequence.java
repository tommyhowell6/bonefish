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
    
    public SimpleSequence(String base,String acc, String identifier){
        if(base ==null||acc==null|| base.length()!=acc.length()){
            throw new IllegalArgumentException("Please be sure the probability matches the sequence!");
        }
        
        bases = base;
        accuracy = acc;
        id = identifier;
    }
    /**
     * The purpose of this constructor is for making merged reads. They will not have an universal ID, so they are instead made.
     * WARING! THIS METHOD IS NOT THREAD SAFE!
     * @param base
     * @param acc 
     */
    public SimpleSequence (String base, String acc){
        if(base ==null||acc==null|| base.length()!=acc.length()){
            throw new IllegalArgumentException("Please be sure the probability matches the sequence!");
        }
        bases = base;
        accuracy = acc;
        id="@merged-sequence-"+(++mergeNumber);
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
        if(this.hashCode()==o.hashCode()){
            return true;
        }
        return false;
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
