package Triehard;

import Model.Sequence;
import Utility.SequenceFactory;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Kris
 */
public class SimpleTrieTest {
    
    public SimpleTrieTest() {
    }
    
    /**
     * Test of addSequence method, of class SimpleTrie.
     */
    @Test
    public void testAddOneSequence() {
        System.out.println("addSequence");
        Sequence s = SequenceFactory.makeTestSequence("ATAGA");
        SimpleTrie instance = new SimpleTrie();
        instance.addSequence(s);
        assertEquals(instance.size(),1);
        System.out.println(instance.rosalindOutput());
        
    }

    /**
     * Test of adding more than one sequence.
     */
    @Test
    public void testAddManySequences() {
        System.out.println("addSequence for three sequences.");
        Sequence s = SequenceFactory.makeTestSequence("ATAGA");
        Sequence s2 = SequenceFactory.makeTestSequence("ATC");
        Sequence s3 = SequenceFactory.makeTestSequence("GAT");
        ArrayList<Sequence> sequences = new ArrayList<>();
        sequences.add(s);
        sequences.add(s2);
        sequences.add(s3);
        
        SimpleTrie instance = new SimpleTrie();
        instance.addAll(sequences);
        assertEquals(instance.size(),3);
        System.out.println(instance.rosalindOutput());
    }

    /**
     * Test of removeSequence method, of class SimpleTrie.
     */

    public void testRemoveSequence() {
        System.out.println("removeSequence");
        Sequence s = null;
        SimpleTrie instance = new SimpleTrie();
        instance.removeSequence(s);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
