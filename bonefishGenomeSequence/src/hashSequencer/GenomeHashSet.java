/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

import Model.SequencePair;
import Model.Sequence;

/**
 *
 * @author Kris
 */
public interface GenomeHashSet {
    
    public void add(Sequence sequence);
    
    public void remove(Sequence sequence);
    
    public SequencePair selectClosestMatch();
    
    public int size();
    
    public boolean finished();
}
