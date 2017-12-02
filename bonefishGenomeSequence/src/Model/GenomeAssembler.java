/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.util.ArrayList;

/**
 *
 * @author Kris
 */
public interface GenomeAssembler {
    
    /**
     * A method that will be called when the Assembler will be passed a genome to assemble. Recieves the list of reads as input.
     * @param sequences
     * @return A collection containing the remaining reads in the genome, preferably 1.
     */
    public ArrayList<Sequence> assemble(ArrayList<Sequence> sequences);
    
}
