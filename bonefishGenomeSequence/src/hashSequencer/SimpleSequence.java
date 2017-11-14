/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hashSequencer;

import java.util.Objects;

/**
 *
 * @author Kris
 */
public class SimpleSequence implements Sequence{
    private final String bases,accuracy;
    
    public SimpleSequence(String base,String acc){
        if(base ==null||acc==null|| base.length()!=acc.length()){
            throw new IllegalArgumentException("Please be sure the probability matches the sequence!");
        }
        
        bases = base;
        accuracy = acc;

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
    
}
