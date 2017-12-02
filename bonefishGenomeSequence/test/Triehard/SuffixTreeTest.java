package Triehard;

import Utility.SequenceFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Kris
 */
public class SuffixTreeTest {
    public SuffixTreeTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of rosalindOutput method, of class SuffixTree.
     */
    @Test
    public void testRosalindOutput() {
        System.out.println("rosalindOutput");
        SuffixTree instance = new SuffixTree();
        instance.addSequence(SequenceFactory.makeTestSequence("ATAAATG$"));
        String expResult = "";
        String result = instance.rosalindOutput();
        System.out.println(result);
        assertEquals(12,SuffixNode.getNodes());
    }

    /**
     * Test of size method, of class SuffixTree.
     */
    @Test
    public void testSize() {
        System.out.println("Simple test of the size method. This will also execute an add.");
        SuffixTree instance = new SuffixTree();
        assertEquals(instance.size(),0);
        instance.addSequence(SequenceFactory.makeTestSequence("ATAAATG$"));
        int expResult = 1;
        int result = instance.size();
        assertEquals(expResult, result);
        System.out.println("Size test complete.");
    }
    
    
    @Test
    public void testOfLargeSequence() throws IOException{
        String property = System.getProperty("user.dir")+"\\problems\\trie-example.txt";
        File testDirectory = new File(property);
        Charset charset = Charset.forName("US-ASCII");
        String testString="ATCTACCAGCAGTGAACATGGGAGGACCAGTAAGGAAGGCTTACCCTCGATGTGTTACAGACTCGTTCGTAGGGTGTATAACGCCGCCGCTGG$";
        ArrayList<String> edges = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(testDirectory.toPath(), charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.length()>0){
                    edges.add(line);
                }
            }
            
        SuffixTree tree = new SuffixTree();
        tree.addSequence(SequenceFactory.makeTestSequence(testString));
        
        System.out.println("Finished adding the string. Testing for correct number of nodes.");
        
        ArrayList<String> output = (ArrayList<String>) tree.toArray();
        int size = output.size();

        boolean foundAll = true;
        //Make sure we have every edge in our output.
        for(String sequence: edges){
            if(foundAll){
                foundAll = output.contains(sequence);
            }
            
            int index = output.indexOf(sequence);
            if(index>=0){
                output.remove(index);
            }
        }
        
        System.out.println("There were "+output.size()+" sequences not found.");
        for(String sequence: output){
            System.out.println(sequence);
        }
        
        assertEquals(edges.size(),size);
        assertEquals(0,output.size());
        assertTrue(foundAll);
    }
}

}