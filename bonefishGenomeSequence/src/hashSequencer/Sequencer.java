package hashSequencer;

import Model.GenomeAssembler;
import Model.Sequence;
import Utility.SequenceMerger;
import Model.SequencePair;
import java.util.ArrayList;

/**
 *
 * @author Kris
 */
public class Sequencer implements GenomeAssembler{
    private static GenomeHashMap genome = new SimpleHashMap();
    private static SequenceMerger merger;
    
    @Override
    public ArrayList<Sequence> assemble(ArrayList<Sequence> sequences) {
        ArrayList<Sequence> output = new ArrayList<>();
                
        /*****
         * 1. Add genome to hashSet.
         * 2. while (!genome.finished()):
         *      a. Select two sequences with greatest allignment
         *      b. Merge those sequences
         *      c. Remove both from hashSet.
         *      d. Add new sequence to hashSet.
         * 
         * 3. Return finished genome.
         */
        addSequencesToHashSet((ArrayList<Sequence>) sequences);
        System.out.println("Genome started with "+genome.size()+" sequences.");
        while(!genome.finished()){
            SequencePair match = genome.selectClosestMatch();
            Sequence merged=null;
            if(match!=null){
                merged = merger.merge(match);
                if(merged!=null){
                    genome.remove(match.getFirstSequence());
                    genome.remove(match.getSecondSequence());
                    genome.add(merged);
                }
                else{
                    System.out.println("We were unable to merge the suggested sequences!");
                    break;
                }     
            }
            else{
                System.out.println("We were unable to find the two closest sequences to match.");
                break;
            }
        }
        System.out.println("Genome finsihed with "+genome.size()+" sequences.");
        return output;
    }

    
    private static void addSequencesToHashSet(ArrayList<Sequence> input){
        input.stream().forEach((input1) -> {
            genome.add(input1);
        });
    }   
}
