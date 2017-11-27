package Triehard;

import Model.Sequence;

/**
 *
 * @author Kris
 */
public interface BasicTrie {
    
    /**
     * Adds a sequence to the data strcuture.
     * @param s 
     */
    public void addSequence(Sequence s);
    /**
     * Tests to see if a given sequence is inside the given trie.
     * @param s
     * @return True if the sequence can be found, false otherwise.
     */
    public boolean contains (Sequence s);
    /**
     * Removes a sequence from the Trie. May not be implemented.
     * @param s 
     */
    public void removeSequence (Sequence s);
    /**
     * Mostly for Rosalind problems. outputs the Trie in the correct format for the problems.
     * @return String coresponding to the Trie as desired for the rosalaind problems.
     */
    public String rosalindOutput ();
    
    public int size();
}
