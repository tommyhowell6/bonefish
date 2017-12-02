/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.util.Collection;

/**
 *
 * @author Kris
 */
public interface SampleGenome {
    
    /**
     * Access the single large file coresponding to a chromosome.
     * @return The genome.
     */
    public Sequence getGenome();
    /**
     * Returns all the reads in the sample genome.
     * @return The sample genome.
     */
    public Collection<Sequence> getReads();
    
}
