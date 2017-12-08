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

    final static double ERROR_TOLERANCE = 0.98;
    Map<BaseQuantity, ArrayList<Sequence>> sequences = new LinkedHashMap<>();
    boolean firstRound = true;
    boolean finishedSequencing = false;
    boolean autoReturn = false;
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
            if (values.size() == 0) { //If no more values with this hashcode, remove key from map.
                sequences.remove(key);
            }
            return true;
        }
        return false;
    }

    /**
     * Selects the pair of sequences w/ lowest edit distance in the map.
     * @return Pair of sequences with the least edit distance.
     */
    public SequencePair selectClosestMatch(){
        if (firstRound){
            Iterator itr  = sequences.entrySet().iterator();
            Map.Entry<BaseQuantity, ArrayList<Sequence>> keyValuePair =
                    (Map.Entry<BaseQuantity, ArrayList<Sequence>>)itr.next();
            ArrayList<Sequence> values = keyValuePair.getValue();

            Sequence retvalA = new SimpleSequence(); //First sequence in most-similiar pair
            Sequence retvalB = new SimpleSequence(); //Second seqeunce in most-similiar pair.
            int counter = 0;
            int greatestSoFar = -1;
            while (itr.hasNext()){
                counter++;
                keyValuePair = ((Map.Entry<BaseQuantity, ArrayList<Sequence>>) itr.next());

               if (values.size() > 1) {//If there is more than one sequence in the bucket,
                   int numMatch  = compareBuckets(keyValuePair.getValue(), keyValuePair.getValue(), true);
                   if (numMatch > greatestSoFar){
                       greatestSoFar = numMatch;
                       retvalA = compareBucketsRetvalA;
                       retvalB = compareBucketsRetvalB;
                   }
               }
               System.out.println(counter);
           }
           if (greatestSoFar == -1) {
               firstRound = false;
               return totalBruteForceSearch();
           }
            return new SimpleSequencePair(retvalA, retvalB);
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


    /**
     * The following code will execute a brute force search over the entire map.
     * The next two variables will help the brute force search's helper functions
     * to return the correct pair.
     */
    //TODO: Handle same sequence comparisons
    //TODO: Handle "finished"
    private Sequence compareBucketsRetvalA;
    private Sequence compareBucketsRetvalB;


    /**
     * Compares each bucket to every other bucket, to find the pair of sequences with the lowest edit
     * distance.
     * @return The two sequences that have closest edit distance.
     */

    //Bug fix: Never used innerKeyValuePair; had written keyValuePair instead.
    //Efficiency fix: make inner iteration start at the same location as the outer iteration
    private SequencePair totalBruteForceSearch() {

        Iterator itr  = sequences.entrySet().iterator(); //Iterator for outer

        Sequence retvalA = new SimpleSequence(); //First sequence in most-similiar pair
        Sequence retvalB = new SimpleSequence(); //Second seqeunce in most-similiar pair.

        int greatestSoFar = -1; //Contains smallest edit distance discovered so far.

        //Iterate over each key-bucket pair in the map.
        int outerIndex = 0; //The current outer iteration
        while (itr.hasNext()) {
            //Get key-bucket pair, then get the bucket.
            Map.Entry<BaseQuantity, ArrayList<Sequence>> keyValuePair =
                    (Map.Entry<BaseQuantity, ArrayList<Sequence>>)itr.next();
            ArrayList<Sequence> values = keyValuePair.getValue(); //Getting all sequences with key.

            Iterator innerItr = itr; //DOES NOT need to start from beginning; already checked!
            //Reiterate over each key-bucket pair to compare to the first pair.
            int innerIndex = 0; //The inner iteration
            while (innerItr.hasNext()) {
                boolean sameBucket = (innerIndex == outerIndex); //If the outer iteration and iteration are, same,
                //the following comparision is comparing a bucket with itself.
                Map.Entry<BaseQuantity, ArrayList<Sequence>> innerKeyValuePair =
                        (Map.Entry<BaseQuantity, ArrayList<Sequence>>)itr.next();
                ArrayList<Sequence> innerValues = innerKeyValuePair.getValue(); //Getting all sequences with key

                int greatestInLoop = compareBuckets(values, innerValues, sameBucket); //Returns smallest edit distance
                //within the compared buckets
                //If the edit distance found is smaller than what has yet been discovered, reset the value of
                //smallest discovered edit distance. Reset the return value to the sequences that gave that distance.
                if (greatestInLoop > greatestSoFar){
                    greatestSoFar = greatestInLoop;
                    retvalA = compareBucketsRetvalA;
                    retvalB = compareBucketsRetvalB;
                }
                innerIndex++;
            }
            outerIndex++;
        }
        if (greatestSoFar == -1){
            finishedSequencing = true;
        }
        return new SimpleSequencePair(retvalA, retvalB);
    }

    /**
     * Compares every sequence in a bucket to every other sequence in the other.
     * @param values The values that are being
     * @param innerValues
     * @param sameBucket
     * @return
     */
    private int compareBuckets(ArrayList<Sequence> values, ArrayList<Sequence> innerValues, boolean sameBucket) {

        int greatestSoFar = -1; //Smallest edit distance so far.

       /*
        * Compare each sequence in each bucket with each other.
        */
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < innerValues.size(); j++){
                if (sameBucket && i == j){ //This will be the same sequence; do not compare it!
                    continue;
                }
                int difference = compareSequences(values.get(i), innerValues.get(j)); //Send the sequence to another
                //function that gets the edit distance between the two.
                if (difference > greatestSoFar) { //If new difference is min...
                    greatestSoFar = difference;  //Make it the smallest so far, and
                    compareBucketsRetvalA =  values.get(i); //Set the two closest sequences together.
                    compareBucketsRetvalB = values.get(j);
                }
            }
        }
        return greatestSoFar;
    }

    //BUG FIXED: Best edit distance made to be smallest, not largest.
    private int compareSequences(Sequence A, Sequence B) {

        String aBases = A.getBases(); //The bases in a
        String bBases = B.getBases(); //The bases in b
        //The number of possible side by side positions of each sequence possible.
        int numOfJuxtapositions = aBases.length() + bBases.length() -1; //Verified correct
        int sizeDifference; //The difference in sequence size
        String largeString; //The larger sequence
        String smallString; //The smaller

        /*
         * Determine which sequence is larger. Then:
         * Set largeString/smallString based on this.
         * Set size difference based on which is larger (so size difference is positive)
         *
         */
        if (bBases.length() <= aBases.length()){
            sizeDifference = aBases.length() - bBases.length();
            largeString = aBases;
            smallString = bBases;
        } else {
            numOfJuxtapositions = bBases.length() +  aBases.length() -1;
            sizeDifference = bBases.length() - aBases.length();
            smallString = aBases;
            largeString = bBases;
        }

        int bestMatch = -1; //Best edit distance so far
        for (int i = 0; i < numOfJuxtapositions; i++) { //for each juxtaposition
            ///Edit distance for juxtapositions. Iteration number is i + 1, to make the iteration number in the next
            //function 1 based (not 0 based) and so easier to understand.
            int compVal = compareJuxtapositions(largeString, smallString, i + 1, sizeDifference);
            if ((compVal > bestMatch)){ //if this juxtaposition gives better match
                bestMatch = compVal; //assign this juxtaposition to bestMatch
            }
        }
        return bestMatch;
    }

    private int compareJuxtapositions(String largeString, String smallString, int iteration, int sizeDifference){
        int baseDifference = sizeDifference; //Edit distance. starts at the size difference between two strings.

        int startIndexLarge; //Starting location in outer for loop
        int startIndexSmall; //Staring location in inner for loop
        int numToAdvance;
        /*
         *  In all juxtapostions, the smaller string is represented underneath the larger string.
         *  "Overhang" is defined by the location of the lower string.
         *  Definition of terms:
         *  Left overhang:
         *      AGGGG
         *   GATT
         *
         *  Right overhang:
         *  AGGGG
         *     GATT
         *
         *  No overhang:
         *  AGGGG
         *  GATT
         */

        if (iteration < smallString.length()) { //If there is left overhang:
            baseDifference += smallString.length() - (iteration); //The iteration number = the amount lined up
            startIndexLarge = 0;
            startIndexSmall = smallString.length() - iteration;
            numToAdvance = iteration;
        } else if (iteration > largeString.length()) { //If there is right overhang
            baseDifference += iteration - largeString.length(); //
            startIndexLarge = iteration - smallString.length();
            startIndexSmall = 0;
            numToAdvance = largeString.length() - startIndexLarge;
        } else {
            startIndexLarge = iteration - smallString.length();
            startIndexSmall = 0;
            numToAdvance = smallString.length();
        }

        for (int i = startIndexLarge; i < startIndexLarge + numToAdvance; i++ ){
            for (int j = startIndexSmall; j < startIndexSmall + numToAdvance; j++) {
                baseDifference += compareBases(largeString.charAt(i), smallString.charAt(j));
            }
        }
        if (withinErrorTolerance(numToAdvance, baseDifference)){
            return baseDifference;
        }
        return -1;
    }

    private int compareBases(char firstBase, char secBase) {
        if (firstBase == secBase) {
            return 0;
        }
        return 1;

    }

    private boolean withinErrorTolerance(int overlapSize, int calculatedDistance) {
        double percentageMatched = overlapSize/calculatedDistance;
        return (percentageMatched < ERROR_TOLERANCE );
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
