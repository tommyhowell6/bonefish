/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

/**
 *
 * @author Kris
 */
public interface Sequence {
    //Returns the bases stored in the sequence
    public String getBases();
    
    //Returns the accuracy list of each base in the sequence.
    public String getAccuracy();
    
    //Returns the ID of this Sequence, which we might need or might not one day. Who knows.
    public String getID();
    
    //Passes in a pointer to the pair of this paired read.
    public void givePairedRead(Sequence sequence);
    
    //Returns the pair of this read.
    public Sequence getPairedRead();
    
}
