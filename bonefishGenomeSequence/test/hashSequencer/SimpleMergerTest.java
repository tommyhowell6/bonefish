package hashSequencer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kris
 */
public class SimpleMergerTest {
    
    public SimpleMergerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {

    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testMergeTwoOff() {
        System.out.println("Merge of correctly alligning sequences by two base pairs.");
        Sequence sequence1 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("ccatcgggacatccatcaaatc","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        System.out.println("Test of first order");
        SimpleMerger instance = new SimpleMerger();
        Sequence expResult = new SimpleSequence("ccatcgggacatccatcaaatcgg","111111111111111111111111");
        Sequence result = instance.merge(sequences);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of second order");
        result = instance.merge(sequences2);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Merge of sequences two bases off passed.");
    }

    @Test
    public void testMergeOfIdentical() {
        System.out.println("Merging identical sequences. Expected behavior is obvious.");
        Sequence sequence1 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        System.out.println("Test of first order");
        SimpleMerger instance = new SimpleMerger();
        Sequence expResult = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        Sequence result = instance.merge(sequences);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of second order");
        result = instance.merge(sequences2);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Identical merge tests passed.");
    }
    
    @Test
    public void testMergeOfOneBaseOff() {
        System.out.println("Merge of correctly alligning sequences one base off.");
        Sequence sequence1 = new SimpleSequence("catcgggacatccatcaaatcg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        SimpleMerger instance = new SimpleMerger();
        
        System.out.println("Test of first order");
        Sequence expResult = new SimpleSequence("catcgggacatccatcaaatcgg","11111111111111111111111");
        Sequence result = instance.merge(sequences);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of second order");
        result = instance.merge(sequences2);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("One base off merge tests passed.");
    }
    
    @Test
    public void testMergeVerySimple() {
        System.out.println("Merge of correctly alligning sequences one base off. Uses a very simple set of bases");
        Sequence sequence1 = new SimpleSequence("cat","111");
        Sequence sequence2 = new SimpleSequence("atg","111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        SimpleMerger instance = new SimpleMerger();
        
        System.out.println("Test of first order");
        Sequence expResult = new SimpleSequence("catg","1111");
        Sequence result = instance.merge(sequences);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of second order");
        result = instance.merge(sequences2);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("One base off merge tests passed.");
    }
}
