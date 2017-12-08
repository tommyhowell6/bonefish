/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashSequencer;

import Model.Sequence;
import Model.SequencePair;
import Utility.SequenceMerger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kris
 */
public class ErrorTolerantMergerTest {
    
    public ErrorTolerantMergerTest() {
    }
    /**
     * Test of mergeSequences method, of class ErrorTolerantMerger.
     */
    @Test
    public void testMergeSequences() {
        System.out.println("mergeSequences");
        int[] indexOfOverlap = new int[2];
        indexOfOverlap[0]=0;
        indexOfOverlap[1]=2;
        SequenceProbabilityPair sequence1 = new SequenceProbabilityPair("atcgggacatccatcaaatcgg","1111111111111111111111");
        SequenceProbabilityPair sequence2 = new SequenceProbabilityPair("ccatcgggacatccatcaaatc","1111111111111111111111");
        ErrorTolerantMerger instance = new ErrorTolerantMerger();
        Sequence expResult = new SimpleSequence("ccatcgggacatccatcaaatcgg","111111111111111111111111");
        Sequence result = instance.mergeSequences(indexOfOverlap, sequence1, sequence2);
        assertEquals(expResult, result);
        indexOfOverlap[0]=2;
        indexOfOverlap[1]=0;
        result = instance.mergeSequences(indexOfOverlap, sequence2, sequence1);
        assertEquals(expResult, result);
    }

    /**
     * Test of findOverlapIndex method, of class ErrorTolerantMerger.
     */
    @Test
    public void testFindOverlapIndex() {
        System.out.println("findOverlapIndex");
        Sequence sequence1 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("ccatcgggacatccatcaaatc","1111111111111111111111");

        ErrorTolerantMerger instance = new ErrorTolerantMerger();
        int[] expResult = new int[2];
        expResult[0]=0;
        expResult[1]=2;
        int[] result = instance.findOverlapIndex(sequence1, sequence2);
        assertArrayEquals(expResult, result);
    }
    
        @Test
    public void testShortOverlapIndex() {
        System.out.println("findOverlapIndex");
        Sequence sequence1 = new SimpleSequence("FART","1111");
        Sequence sequence2 = new SimpleSequence("RTBLRFDANGDONG","RTBLRFDANGDONG");

        ErrorTolerantMerger instance = new ErrorTolerantMerger();
        int[] expResult = new int[2];
        expResult[0]=2;
        expResult[1]=0;
        int[] result = instance.findOverlapIndex(sequence1, sequence2);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testMergeTwoOff() {
        System.out.println("Merge of correctly alligning sequences by two base pairs.");
        Sequence sequence1 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("ccatcgggacatccatcaaatc","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        System.out.println("Test of first order");
        SequenceMerger instance = new ErrorTolerantMerger();
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
        SequenceMerger instance = new ErrorTolerantMerger();
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
        
        SequenceMerger instance = new ErrorTolerantMerger();
        
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
        
        SequenceMerger instance = new ErrorTolerantMerger();
        
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
    
    @Test
    public void testContiguious(){
        System.out.println("Merge of one sequence completely swallowed by another.");
        Sequence sequence1 = new SimpleSequence("catcgggacatccatcaaatcg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("tccat","11111");
        SequenceMerger instance = new ErrorTolerantMerger();
        Sequence result = instance.merge(new SimpleSequencePair(sequence1,sequence2));
        System.out.println("Merged result returned: "+result.getBases());
        assertNotNull(result);
        assertEquals(result,sequence1);
        
        System.out.println("Finished test of overlapped sequence");        
    }
    
    @Test
    public void testOneError(){
              System.out.println("Merge of correctly alligning sequences one base off.");
        Sequence sequence1 = new SimpleSequence("catcgggacatccatcaaa-cg","1111111111111111111!11");
        Sequence sequence2 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        SequenceMerger instance = new ErrorTolerantMerger();
        
        System.out.println("Test of first order");
        Sequence expResult = new SimpleSequence("catcgggacatccatcaaatcgg","11111111111111111111111");
        Sequence result = instance.merge(sequences);
        System.out.println("Merged with one error result: "+result.getBases());
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of second order");
        result = instance.merge(sequences2);
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of a single error passed");  
    }
    
        @Test
    public void testTwoErrors(){
        System.out.println("Merge of correctly alligning sequences with two errors.");
        Sequence sequence1 = new SimpleSequence("catcgggac-tccatcaaa-cg","111111111!111111111!11");
        Sequence sequence2 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequenceMerger instance = new ErrorTolerantMerger();
        
        System.out.println("Test of first order");
        Sequence expResult = new SimpleSequence("catcgggacatccatcaaatcgg","11111111111111111111111");
        Sequence result = instance.merge(sequences);
        System.out.println("Merged with one error result: "+result.getBases());
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of two errors passed");  
    }
    
    @Test
    public void testThreeErrors(){
        System.out.println("Merge of correctly alligning sequences with two errors.");
        Sequence sequence1 = new SimpleSequence("catcgggac-tcc-tcaaa-cg","111111111!111!11111!11");
        Sequence sequence2 = new SimpleSequence("atcgggacatccatcaaatcgg","1111111111111111111111");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequenceMerger instance = new ErrorTolerantMerger();
        
        System.out.println("Test of first order");
        Sequence expResult = new SimpleSequence("catcgggacatccatcaaatcgg","11111111111111111111111");
        Sequence result = instance.merge(sequences);
        System.out.println("Merged with one error result: "+result.getBases());
        assertEquals(expResult.getBases(), result.getBases());
        assertEquals(expResult.getAccuracy(),result.getAccuracy());
        
        System.out.println("Test of three errors passed");  
    }
    
    @Test
    public void testTrailingErrors(){
        System.out.println("Merge of correctly alligning sequences one base off.");
        Sequence sequence1 = new SimpleSequence("catcgggacatccatcaaatcg","1111111111111111111111");
        Sequence sequence2 = new SimpleSequence("atcgggacatc----------g","11111111111!!!!!!!!!!1");
        
        SequencePair sequences = new SimpleSequencePair(sequence1,sequence2);
        SequencePair sequences2 = new SimpleSequencePair(sequence2,sequence1);
        
        SequenceMerger instance = new ErrorTolerantMerger();
        
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
}
