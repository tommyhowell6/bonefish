package Triehard;

import Model.Sequence;
import Utility.SequenceFactory;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testWalk() {
        System.out.println("Test for walking the tree.");
        Sequence s = SequenceFactory.makeTestSequence("ATCG");
        Sequence s2 = SequenceFactory.makeTestSequence("GGGT");
        ArrayList<Sequence> sequences = new ArrayList<>();
        sequences.add(s);
        sequences.add(s2);
        
        SimpleTrie instance = new SimpleTrie();
        instance.addAll(sequences);
        assertEquals(instance.size(),2);
        
        //We've now added the test sequences, so we can walk.
        Sequence testSequence = SequenceFactory.makeTestSequence("AATCGGGTTCAATCGGGGT");
        List<Integer> output = instance.walk(testSequence);
        output.stream().forEach((i) -> {
            System.out.print(i.toString()+" ");
        });
        System.out.println("");
        System.out.println("test complete");
        assertTrue(output.contains(new Integer(1)));
        assertTrue(output.contains(new Integer(4)));
        assertTrue(output.contains(new Integer(11)));
        assertTrue(output.contains(new Integer(15)));
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
