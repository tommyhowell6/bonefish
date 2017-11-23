package Utility;

import Model.SampleGenome;
import Model.Sequence;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Kris
 */
public class SampleGenomeFactoryTest {
    
    public SampleGenomeFactoryTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of buildSampleGenome method, of class SampleGenomeFactory.
     */
    @Test
    public void testBuildSampleGenome_int() {
        System.out.println("buildSampleGenome");
        int genomeSize = 1000000;
        int expectedReads = ((genomeSize/150)*50)-10;
        SampleGenome result = SampleGenomeFactory.buildSampleGenome(genomeSize);
        assertNotNull(result);
        assertEquals(result.getGenome().getBases().length(),genomeSize);
        System.out.println("Sample produced: "+result.getGenome().getBases().length()+" reads.");
        System.out.println("Sample Genome Section: \n"+result.getGenome().getBases().substring(0, 100));
        boolean greater = result.getReads().size() >= expectedReads;
        System.out.println("Generated "+result.getReads().size()+" sample reads.");
        assertTrue(greater);

    }

    /**
     * Test of buildSampleGenome method, of class SampleGenomeFactory.
     */
    @Test
    public void testBuildSampleGenomeWithErrors() {
        System.out.println("Testing sample genome with errors present.");
        int genomeSize = 1000000;
        int readLength = 100;
        int readOverlap = 10;
        int expectedReads = ((genomeSize/readLength)*readOverlap)-10;
        boolean genomeErrors = true;
        SampleGenome result = SampleGenomeFactory.buildSampleGenome(genomeSize, readLength, readOverlap, genomeErrors);
        assertNotNull(result);
        assertEquals(result.getGenome().getBases().length(),genomeSize);
        System.out.println("Sample produced: "+result.getGenome().getBases().length()+" base long genome.");
        System.out.println("Sample Genome Section: \n"+result.getGenome().getBases().substring(0, 100));
        boolean greater = result.getReads().size() >= expectedReads;
        System.out.println("Generated "+result.getReads().size()+" sample reads.");
        assertTrue(greater); 
        System.out.println("Test to make sure some of the random reads have errors.");
        boolean done = false;
        int index =0;
        String error = ""+SampleGenomeFactory.ERROR_CHAR;
        for(Sequence sequence: result.getReads()){
            if(done){
                break;
            }
            if(sequence.getBases().contains(error)){
                done = true;
            }
        }
        assertTrue(done);
     
    }
    
}
