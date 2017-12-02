/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import Model.SampleGenome;
import Model.Sequence;
import java.util.Collection;

/**
 *
 * @author Kris
 */
public class SimpleSampleGenome implements SampleGenome{
    private final Sequence modelGenome;
    private final Collection<Sequence> reads;
    
    public SimpleSampleGenome(Sequence seq, Collection<Sequence> red){
        modelGenome = seq;
        reads = red;
    }

    @Override
    public Sequence getGenome() {
        return modelGenome;
    }

    @Override
    public Collection<Sequence> getReads() {
        return reads;
    }
    
}
