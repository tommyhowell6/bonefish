/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Utility;

import src.Model.Sequence;
import src.Model.SequencePair;

/**
 *
 * @author Kris
 */
public interface SequenceMerger {
    
    public Sequence merge(SequencePair sequences);
}
