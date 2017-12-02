package hashSequencer;

import Model.Sequence;
import Model.SequencePair;

import java.util.*;

/**
 * Creates a simple hash map mapping an object that has the number of each base as the key,
 * and the sequence as the value.
 * @author Stephen
 */
public class SimpleHashMap implements GenomeHashMap {

    Map<BaseQuantity, ArrayList<Sequence>> sequences = new HashMap<>();
    boolean firstRound = true;
    boolean finishedSequencing = false;
    /**
     * Adds a sequence to the hash multimap.
     * @param sequence The generic sequence to be added
     * @return True if added; false if key could not be generated (if sequence does not only have nucleotides)
     */
    public boolean add(Sequence sequence){

        BaseQuantity key = generateKey(sequence);
        if(key != null) {
            ArrayList<Sequence> values;
            if ((values = sequences.get(key)) != null) {
                values.add(sequence);
            } else {
                values = new ArrayList<>();
                values.add(sequence);
                sequences.put(key, values);
            }
            return true;
        }
        return false;
    }
    //TODO: Make brute force algorithm remove sequences instantly, to avoid iteration over the list of values in remove()
    /**
     * Removes sequence from map
     * @param sequence Sequence to be removed
     * @return True if sequence is removed; false if key cannot be generated, or if sequence has not
     * been placed in map.
     */
     //Bug fixed 11/25: The existence of the key does not guarantee existence of sequence;
            //checks for existence of sequence when removed from the array retrieved from the map,
            //to ensure that returns the correct value.
    public boolean remove(Sequence sequence){
        BaseQuantity key = generateKey(sequence);
        if (key == null) {
            return false;
        }

        ArrayList<Sequence> values = sequences.get(key);
        if (values != null && values.remove(sequence)) { //If null, sequence does not exist.
            //Also, the iteration over the key is bad.
            //Problem: Equals in SimpleSequence currently uses the HashCode. But... everything in the value
            //list has the same hash code. This will be a common occurrence. So...iterate over entire thing?
            if (values.size() == 0) { //If no more values with this hashcode, remove key from map.
                sequences.remove(key);
            }
            return true;
        }
        return false;
    }

    public SequencePair selectClosestMatch(){
        if (firstRound){
            firstRound = false;
            return firstRoundClosestMatch();
        } else {
            return totalBruteForceSearch();
        }

    }
    //TODO: Verify if size should return key value pairs or num of sequences in map.
    /**
     * Get size of hash map (number of key-value pairs).
     * This is NOT the number of sequences in the map.
     * @return integer representing number of key-value pairs in map.
     */
    public int size(){
        return sequences.size();
    }

    /**
     * Returns true if finished.
     * @return
     */
    public boolean finished(){
        return finishedSequencing;
    }

    private SequencePair firstRoundClosestMatch() {
        return null;
    }

    //TODO: Handle same sequence comparisons
    //TODO: Handle "finished"
    private Sequence compareBucketsRetvalA;
    private Sequence compareBucketsRetvalB;
    private SequencePair totalBruteForceSearch() {

        Iterator itr  = sequences.entrySet().iterator();
        Sequence retvalA = new SimpleSequence();
        Sequence retvalB = new SimpleSequence();

        int smallestSoFar = Integer.MAX_VALUE;

        while (itr.hasNext()) {
            Map.Entry<BaseQuantity, ArrayList<Sequence>> keyValuePair =
                    (Map.Entry<BaseQuantity, ArrayList<Sequence>>)itr.next();
            ArrayList<Sequence> values = keyValuePair.getValue();

            Iterator innerItr = sequences.entrySet().iterator();

            while (innerItr.hasNext()) {
                Map.Entry<BaseQuantity, ArrayList<Sequence>> innerKeyValuePair =
                        (Map.Entry<BaseQuantity, ArrayList<Sequence>>)itr.next();
                ArrayList<Sequence> innerValues = keyValuePair.getValue();
                int smallestInLoop = compareBuckets(values, innerValues);

                if (smallestInLoop < smallestSoFar){
                    smallestSoFar = smallestInLoop;
                    retvalA = compareBucketsRetvalA;
                    retvalB = compareBucketsRetvalB;
                }
            }
        }
        return new SimpleSequencePair(retvalA, retvalB);
    }

    private int compareBuckets(ArrayList<Sequence> values, ArrayList<Sequence> innerValues) {

        int smallestSoFar = Integer.MAX_VALUE;
        int iterationLimit;
        if (values.size() < innerValues.size()){
            iterationLimit = values.size();
        } else {
            iterationLimit = innerValues.size();
        }

        for (int i = 0; i < iterationLimit; i++) {
            for (int j = 0; j < iterationLimit; j++){
              int difference = compareSequences(values.get(i), innerValues.get(j));
              if (difference < smallestSoFar) {
                  smallestSoFar = difference;
                  compareBucketsRetvalA =  values.get(i);
                  compareBucketsRetvalB = values.get(j);
              }
            }
        }
        return smallestSoFar;
    }

    private int compareSequences(Sequence A, Sequence B) {

        String aBases = A.getBases();
        String bBases = B.getBases();
        int numOfJuxtapositions;
        int sizeDifference;
        String largeString = null;
        String smallString = null;

        if (bBases.length() <= aBases.length()){
            numOfJuxtapositions = aBases.length() + ((2 * bBases.length()) -1);
            sizeDifference = aBases.length() - bBases.length();
            largeString = aBases;
        } else {
            numOfJuxtapositions = bBases.length() +((2 * bBases.length()) -1);
            sizeDifference = bBases.length() - aBases.length();
            smallString = bBases;
        }

        int bestMatch = 0;
        for (int i = 0; i < numOfJuxtapositions; i++) {
            int compVal = compareJuxtapositions(largeString, smallString, i, sizeDifference);
            if ((compVal > bestMatch)){
                bestMatch = compVal;
            }
        }
        return bestMatch;
    }

    private int compareJuxtapositions(String largeString, String smallString, int iteration, int sizeDifference){
        int baseDifference = sizeDifference;

        int startIndexLarge;
        int startIndexSmall;
        int numToAdvance;

        if (iteration < smallString.length() - 1) {
            baseDifference += smallString.length() - (iteration + 1);
            startIndexLarge = 0;
            startIndexSmall = smallString.length() - iteration - 1;
            numToAdvance = iteration + 1;
        } else if (iteration > largeString.length()) {
            baseDifference += iteration - smallString.length();
            startIndexLarge = iteration - smallString.length() + 1;
            startIndexSmall = 0;
            numToAdvance = smallString.length() - (largeString.length() - iteration + 1);
        } else {
            startIndexLarge = iteration - smallString.length() + 1;
            startIndexSmall = 0;
            numToAdvance = smallString.length();
        }

        for (int i = startIndexLarge; i < startIndexLarge + numToAdvance; i++ ){
            for (int j = startIndexSmall; j < startIndexSmall + numToAdvance; j++) {
                baseDifference += compareBases(largeString.charAt(i), smallString.charAt(j));
            }
        }
        return baseDifference;
    }

    private int compareBases(char firstBase, char secBase) {
        if (firstBase == secBase) {
            return 0;
        }
        return 1;
    }

    private BaseQuantity generateKey(Sequence sequence) {
        String bases = sequence.getBases();
        BaseQuantity key = new BaseQuantity();
        for (int i = 0; i < bases.length(); i++){
            if (bases.charAt(i) == 'A'){
                key.numA++;
            }else if (bases.charAt(i) == 'T'){
                key.numT++;
            }else if (bases.charAt(i) == 'C'){
                key.numC++;
            }else if (bases.charAt(i) == 'G') {
                key.numG++;
            }else {
                return null;
            }
        }
        return key;
    }
    public class BaseQuantity{
        int numA;
        int numT;
        int numC;
        int numG;

        @Override
        public boolean equals(Object obj){
            if (obj.getClass() != this.getClass()){
                return false;
            }
            BaseQuantity testSubject = (BaseQuantity)obj;

            if (testSubject.numA == this.numA && testSubject.numT == this.numT){
                if (testSubject.numG == this.numG && testSubject.numC == this.numC) {
                    return true;
                }
            }
            return false;
        }
    }
}
