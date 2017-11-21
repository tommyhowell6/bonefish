/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import hashSequencer.SimpleSequence;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kris
 */
public class FastQReaderTest {
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("initialize FastQReader");
        SequenceType sequenceType = SequenceType.SimpleSequence;
        FastQReader.initialize(sequenceType);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readFile method, of class FastQReader.
     * @throws java.lang.Exception
     */
    @Test
    public void testReadFile() throws Exception {
        System.out.println("readFile");
        URL path = FastQReaderTest.class.getResource("test.fastq");
        File f = new File(path.getFile());

        ArrayList<Sequence> result = FastQReader.readFile(f);
        assertEquals(result.size(), 1602);
        
        Sequence firstSequence = new SimpleSequence("AATGACAATGCGTTCATACTCTGTTACTCCATAGAGCGCTCACGAAATAC","_WOEXwkk{#?]mNHL7d9U@Siy\"mCY0q};B2u*#Nk&wF~S!GI\\t0","@f9351371-e30e-4fa2-b177-d666a217eb83_1");
        Sequence lastSequence = new SimpleSequence("CTTTGGGCTCCGTTCTTCTGCCCAAGAGGAGGTTGGCTGCTCAACACTGC","eV\"tH9IiU\"l-tk|:8}c^I3PmX<0&EgDZ +Nk9u!{1]1MK'hZct","@d1a47937-0686-4c7a-be74-46dad72d3bfa_2");
        assertEquals(result.get(0),firstSequence);
        assertEquals(result.get(result.size()-1),lastSequence);
        assertEquals(result.get(0).getPairedRead(),result.get(1));
    }
    
    @Test
    public void testReadDirectory() throws Exception {
        System.out.println("in Read Directory Test");

        String property = System.getProperty("user.dir")+"\\sample_sequences";
        File testDirectory = new File(property);
        
        ArrayList<Sequence> result = FastQReader.readDirectory(testDirectory);
        assertEquals(result.size(), 6408);
        Sequence firstSequence = new SimpleSequence("AATGACAATGCGTTCATACTCTGTTACTCCATAGAGCGCTCACGAAATAC","_WOEXwkk{#?]mNHL7d9U@Siy\"mCY0q};B2u*#Nk&wF~S!GI\\t0","@f9351371-e30e-4fa2-b177-d666a217eb83_1");
        Sequence lastSequence = new SimpleSequence("CTTTGGGCTCCGTTCTTCTGCCCAAGAGGAGGTTGGCTGCTCAACACTGC","eV\"tH9IiU\"l-tk|:8}c^I3PmX<0&EgDZ +Nk9u!{1]1MK'hZct","@d1a47937-0686-4c7a-be74-46dad72d3bfa_2");
        
        System.out.println("Searching for a few sequences in the read.");
        boolean found = result.contains(firstSequence);
        assertTrue(found);
        found = result.contains(lastSequence);
        assertTrue(found);
        
        System.out.println("Read directory test complete.");
    }
    
}