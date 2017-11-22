/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.SampleGenome;

/**
 *
 * @author Kris
 */
class SampleGenomeFactory {

    /**
     * Constructs a sample genome without reading in from files.
     * @param genomeSize Total length of the finished genome.
     * @param readLength Length of each read.
     * @param readOverlap How much each individual base pair will be overlapped.
     * @param genomeErrors Whether or not this genome contains errors.
     * @return The collection of sequence objects corresponding to the reads. 
     * 
     */
    public static SampleGenome buildSampleGenome(int genomeSize, int readLength, int readOverlap, boolean genomeErrors) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
