/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

/**
 *
 * @author Kris
 */
public interface Sequence {
    //Returns the bases stored in the sequence
    public String getBases();
    
    //Returns the accuracy list of each base in the sequence.
    public String getAccuracy();
    
}
