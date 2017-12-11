/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

/**
 * Simple container class to be used to pass sequences around internally.
 */
public class SequenceProbabilityPair {


    String bases;
    String accuracy;

    public SequenceProbabilityPair(String base, String acc) {
        bases = base;
        accuracy = acc;
    }
    
}
