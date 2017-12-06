package eulerianSequencer;

import Model.GenomeAssembler;
import Model.Sequence;
import Utility.SequenceFactory;
import Utility.SequenceMerger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Kris
 */
public class EulerianSequencer implements GenomeAssembler {
    //private static GenomeHashSet genome;
    private static SequenceMerger merger;


    public static TreeMap<DoubleString, ArrayList<DoubleStringAndBool>> theEdgesMap = new TreeMap<>();
    public static ArrayList<DoubleString> formCycleList = new ArrayList<>();

    @Override
    public ArrayList<Sequence> assemble(ArrayList<Sequence> sequences) {
//does a sequence just have one String? How do the read pairs come in?
        //even get PairedRead is just a sequence. Should I just grab it and split it?
        //it looks like the Sequencer.java isn't using the pairs--it's just matching the stuff with similar stuff. Where's it using
        //read pairs? Or does it?
        /*****
         * 1. Read in sequences - map them out
         * 2. Find start Node
         * 3. Form first Cycle
         * 4. Continue forming Eulerian cycles
         * 5. Return output
         */


        //ArrayList<String> associations = formatFastqIntoRosalindPairs(args); //todo: this is where you'll make the distinction between getting args and getting sequences!

        ArrayList<String> associations = formatSequencesIntoRosalindPairs(sequences); //todo: this is where you'll make the distinction between getting args and getting sequences!


        DoubleString startStrings = findStartString(associations);


        Random r = new Random();

        DoubleString rand = startStrings;
        ArrayList<DoubleString> cycle;
        cycle = formCycle(rand);
        cycle = formRestOfCycle(r, rand, cycle);

        ArrayList<DoubleString> cyclePattern;
        cyclePattern = cycle;

        ArrayList<DoubleString> edges = formEdgesBetweenNodes(cycle, cyclePattern);

        StringBuilder finalString = combineEdgesIntoGenomeWithoutGap(edges, startStrings);


        printGenomeToFile(finalString);

        ArrayList<Sequence> returnMe = new ArrayList<>();
        Sequence finalSequence = SequenceFactory.makeSequence(finalString.toString(), finalString.toString(), "fakeID");
        returnMe.add(finalSequence);

        return returnMe;

    }

    /**
     * This function will need to grab all the sequences given and put them into pairs that are formatted similar to the
     * file in Bioinformatics Algorithm's Ros. problem #15 (e.g. "ACG|GTT\nGTA|TCC")
     * This will allow the rest of the algorithm to carry on as it was doing before.
     * @param sequences
     * @return
     */
    public ArrayList<String> formatSequencesIntoRosalindPairs(ArrayList<Sequence> sequences)
    {
        ArrayList<String> finalReturnList = new ArrayList<>();

        //todo: deal with repeats!
        finalReturnList = returnRosalindFormattedStringsFromSequenceObjects(sequences);




        return finalReturnList;

    }


    //This prints out whatever string is given to it in a file called "ReconstructedOut.txt"
    private static void printGenomeToFile(StringBuilder finalString) {
        try{
            PrintWriter writer = new PrintWriter("ReconstructedOut.txt", "UTF-8");


            writer.print(finalString);


            writer.close();
        } catch (IOException e) {
            System.out.println("Problem with PrintWriter! Exception!");
            // do something
        }
    }


    //todo: got to deal with repeat reads dawg - also what is a sequence supposed to be? Just 2 sequences merged together? I'm confused
    public static TreeMap<String, PairedReadsInfo> convertSequenceObjects(ArrayList<Sequence> sequences)
    {

        TreeMap<String, PairedReadsInfo> allPairs = new TreeMap<>();
        for(int i = 0; i < sequences.size(); i++)
        {
            String id = UUID.randomUUID().toString();
            PairedReadsInfo newPair = new PairedReadsInfo();
            newPair.setString1(sequences.get(i).getBases());
            if(sequences.get(i).getPairedRead() != null) {
                newPair.setString2(sequences.get(i).getPairedRead().getBases());
            }
            newPair.setAccuracy(sequences.get(i).getAccuracy());
            allPairs.put(id, newPair);

        }

        return allPairs;
    }


    //this reads in from a single fastQ file and returns a map with a String (the ID of a particular read) and a PairedReadsInfo object (the rest of the data on that same read)
    public static TreeMap<String, PairedReadsInfo> readFastq(String filename)
    {
        TreeMap<String, PairedReadsInfo> allPairs = new TreeMap<>();


        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (fileScanner.hasNextLine()){ //iterate through and grab the ID, genome, the '+' character, and the accuracy
            String id = fileScanner.nextLine();
            String genome = fileScanner.nextLine();
            String filler = fileScanner.nextLine();
            String accuracy = fileScanner.nextLine();

            String[] idSplit = id.split("_");
            if(allPairs.containsKey(idSplit[0])) //if a read with this ID is already in the program:
            {
                allPairs.get(idSplit[0]).setString2(genome);
            }
            else
            {
                PairedReadsInfo newPair = new PairedReadsInfo();
                newPair.setString1(genome);
                newPair.setAccuracy(accuracy);
                allPairs.put(idSplit[0], newPair);
            }

            //associations.add(st);
        }

        return allPairs;
    }


    //this reads in from a single fastQ files and returns a map with a String (the ID of a particular read) and a PairedReadsInfo object (the rest of the data on that same read)
    public static TreeMap<String, PairedReadsInfo> readFastqTwoFiles(String filename, String filename2)
    {
        TreeMap<String, PairedReadsInfo> allPairs = new TreeMap<>();


        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (fileScanner.hasNextLine()){ //iterate through and grab the ID, genome, the '+' character, and the accuracy
            String id = fileScanner.nextLine();
            String genome = fileScanner.nextLine();
            String filler = fileScanner.nextLine();
            String accuracy = fileScanner.nextLine();

            String[] idSplit = id.split("_");
            if(allPairs.containsKey(idSplit[0])) //if a read with this ID is already in the program:
            {
                allPairs.get(idSplit[0]).setString2(genome);
            }
            else
            {
                PairedReadsInfo newPair = new PairedReadsInfo();
                newPair.setString1(genome);
                newPair.setAccuracy(accuracy);
                allPairs.put(idSplit[0], newPair);
            }

            //associations.add(st);
        }

        try {
            fileScanner = new Scanner(new File(filename2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (fileScanner.hasNextLine()){ //iterate through and grab the ID, genome, the '+' character, and the accuracy
            String id = fileScanner.nextLine();
            String genome = fileScanner.nextLine();
            String filler = fileScanner.nextLine();
            String accuracy = fileScanner.nextLine();

            String[] idSplit = id.split("_");
            if(allPairs.containsKey(idSplit[0])) //if a read with this ID is already in the program:
            {
                allPairs.get(idSplit[0]).setString2(genome);
            }
            else
            {
                PairedReadsInfo newPair = new PairedReadsInfo();
                newPair.setString1(genome);
                newPair.setAccuracy(accuracy);
                allPairs.put(idSplit[0], newPair);
            }

        }

        return allPairs;
    }

    public static ArrayList<String> returnRosalindFormattedStringsFromSequenceObjects(ArrayList<Sequence> sequences) {
        ArrayList<String> associations = new ArrayList<>();

        TreeMap<String, PairedReadsInfo> thePairs = convertSequenceObjects(sequences);
        for (Map.Entry<String, PairedReadsInfo> pair : thePairs.entrySet()) {
            String combinedAssociations = pair.getValue().getString1() + "|" + pair.getValue().getString2();

            associations.add(combinedAssociations);

        }
        return associations;
    }



    //formats fastq files into the format that Rosalind 15 looks like (e.g. "GTTTT|GAAAT") and returns an arraylist of these
    public static ArrayList<String> returnRosalindFormattedStrings(String filename) {
        ArrayList<String> associations = new ArrayList<>();
        TreeMap<String, PairedReadsInfo> thePairs = readFastq(filename);
        for (Map.Entry<String, PairedReadsInfo> pair : thePairs.entrySet()) {
            String combinedAssociations = pair.getValue().getString1() + "|" + pair.getValue().getString2();

            associations.add(combinedAssociations);

        }
        return associations;
    }


    public static ArrayList<String> returnStringsFromTwoFiles(String filename, String filename2) {
        ArrayList<String> associations = new ArrayList<>();
        TreeMap<String, PairedReadsInfo> thePairs = readFastqTwoFiles(filename, filename2);
        for (Map.Entry<String, PairedReadsInfo> pair : thePairs.entrySet()) {
            String combinedAssociations = pair.getValue().getString1() + "|" + pair.getValue().getString2();

            associations.add(combinedAssociations);

        }
        return associations;
    }

    private static StringBuilder combineEdgesIntoGenomeWithoutGap(ArrayList<DoubleString> edges, DoubleString startStrings) {
        StringBuilder finalString = new StringBuilder(""); //now we're iterating through the edges on our graph, building the assembled DNA
        String lastToAdd = "";
        for(int i = 0; i <edges.size(); i++)
        {
            if(i == 0)
            {
                finalString.append(edges.get(i).getString1());
            }
            else if(i == edges.size()-1)
            {
                String adder = edges.get(i).getString1();
                finalString.append(adder.substring(adder.length()-1));
                lastToAdd = edges.get(i).getString2();
            }
            else
            {
                String adder = edges.get(i).getString1();
                finalString.append(adder.substring(adder.length()-1));
            }
        }

        //todo: revise code for gap:
        /*int gapStop = 0;
        for(int i = 0; i < finalString.length(); i++)
        {
            if(finalString.substring(i, i+ startStrings.getString2().length()).equals(startStrings.getString2())) //StringIndexOutOfBounds
            {
                gapStop = i;
                break;
            }
        }

        int gap = gapStop - startStrings.getString1().length();
        //below deals with adding the last few characters of our assembled DNA; it's basically adding those last few gap characters
        ArrayList<String> backwardsGapCharacters = new ArrayList<>();
        for(int i = 0; i < gap; i++)
        {
            backwardsGapCharacters.add(edges.get(edges.size()-2 - i).getString2().substring(0, 1));
        }
        ArrayList<String> gapCharacters = new ArrayList<>();
        for(int i = backwardsGapCharacters.size()-1; i > -1; i--)
        {
            gapCharacters.add(backwardsGapCharacters.get(i));
        }

        for(int i  =0; i < gapCharacters.size(); i++)
        {
            finalString.append(gapCharacters.get(i));
        }*/
        finalString.append(lastToAdd);
        return finalString;
    }


    private static StringBuilder combineEdgesIntoGenome(int gap, ArrayList<DoubleString> edges) {
        StringBuilder finalString = new StringBuilder(""); //now we're iterating through the edges on our graph, building the assembled DNA
        String lastToAdd = "";
        for(int i = 0; i <edges.size(); i++)
        {
            if(i == 0)
            {
                finalString.append(edges.get(i).getString1());
            }
            else if(i == edges.size()-1)
            {
                String adder = edges.get(i).getString1();
                finalString.append(adder.substring(adder.length()-1));
                lastToAdd = edges.get(i).getString2();
            }
            else
            {
                String adder = edges.get(i).getString1();
                finalString.append(adder.substring(adder.length()-1));
            }
        }

        //below deals with adding the last few characters of our assembled DNA; it's basically adding those last few gap characters
        ArrayList<String> backwardsGapCharacters = new ArrayList<>();
        for(int i = 0; i < gap; i++)
        {
            backwardsGapCharacters.add(edges.get(edges.size()-2 - i).getString2().substring(0, 1));
        }
        ArrayList<String> gapCharacters = new ArrayList<>();
        for(int i = backwardsGapCharacters.size()-1; i > -1; i--)
        {
            gapCharacters.add(backwardsGapCharacters.get(i));
        }

        for(int i  =0; i < gapCharacters.size(); i++)
        {
            finalString.append(gapCharacters.get(i));
        }
        finalString.append(lastToAdd);
        return finalString;
    }


    //forms the overlapping strings between nodes into a list of edges. The list is in the order of the path that's traveled to form the full genome.
    private static ArrayList<DoubleString> formEdgesBetweenNodes(ArrayList<DoubleString> cycle, ArrayList<DoubleString> cyclePattern) {
        ArrayList<DoubleString> edges = new ArrayList<>();

        for(int i  =0; i < cyclePattern.size()-1; i++) //now we're creating the edges, which combine peices of the nodes generated
        {
            StringBuilder string1 = new StringBuilder(cycle.get(i).getString1());
            String nextString1 = cycle.get(i+1).getString1();
            string1.append(nextString1.substring(nextString1.length()-1));

            StringBuilder string2 = new StringBuilder(cycle.get(i).getString2());
            String nextString2 = cycle.get(i+1).getString2();
            string2.append(nextString2.substring(nextString2.length()-1));


            DoubleString newDouble = new DoubleString(string1.toString(), string2.toString());
            edges.add(newDouble);
        }
        return edges;
    }

    public static boolean doesCurrentCycleLoseNextConnection(ArrayList<DoubleString> cycle)
    {

        for(int i = 0; i < cycle.size(); i++)
        {
            DoubleString nextStart = cycle.get(i);

            if(theEdgesMap.containsKey(nextStart) && !noEdgesLeft(theEdgesMap.get(nextStart)))
            {
                return true;
            }
        }

        return false;
    }


    //generates the rest of the Eulerian path
    private static ArrayList<DoubleString> formRestOfCycle(Random r, DoubleString rand, ArrayList<DoubleString> cycle) {
        while(edgesOnGraph(theEdgesMap)) //while there are still unused edges on the graph:
        {
            System.out.println("Current cycle size: " + cycle.size());
            ArrayList<DoubleString> newCycle = new ArrayList<>();

            if(!doesCurrentCycleLoseNextConnection(cycle)) {
                //todo: BELOW: There are still edges left on the cycle
                while (!theEdgesMap.containsKey(rand) || noEdgesLeft(theEdgesMap.get(rand))) //keep generating a random key until you find a node with more edges
                {
                    rand = cycle.get(r.nextInt(cycle.size()));
                }
            }
            else
            {

                //todo: write something that finds closest connect to last thing in cycle, and if one can't be found, keep moving backwards
            }
            int newStart = findString(cycle, rand);


            ArrayList<DoubleString> connectedCycle = new ArrayList<>();


            formCycleList = new ArrayList<>();
            newCycle = formCycle(rand);

            connectedCycle = insertList(cycle, newCycle, newStart);

            cycle = connectedCycle;

        }
        return cycle;
    }


    //finds the DoubleString object with the least number of nodes going into it (making it the start of the genome)
    private static DoubleString findStartString(ArrayList<String> associations) {
        TreeMap<DoubleString, ArrayList<DoubleString>> mappedStrings = new TreeMap<>();
        TreeMap<DoubleString, Integer> edgesComingIn = new TreeMap<>();
        TreeMap<DoubleString, Integer> edgesGoingOut = new TreeMap<>();


        for(int i = 0; i < associations.size(); i++) //go through all iterations of the strings found
        {
            String[] splitAssociations = associations.get(i).split("\\|");

            int firstSize = splitAssociations[0].length();
            int secondSize = splitAssociations[1].length();
            String firstPre = splitAssociations[0].substring(0, firstSize-1);
            String secondPre = splitAssociations[1].substring(0, secondSize-1);

            String firstSuf = splitAssociations[0].substring(1, firstSize);
            String secondSuf = splitAssociations[1].substring(1, secondSize);

            DoubleString prefixes = new DoubleString(firstPre, secondPre); //gather the prefixes and suffixes of the current association
            DoubleString suffixes = new DoubleString(firstSuf, secondSuf);

            if(edgesComingIn.containsKey(suffixes)) //if this pair has been seen as a suffix, add 1 to its coming-in edges
            {
                edgesComingIn.replace(suffixes, edgesComingIn.get(suffixes) + 1);
            }
            else //else, add it to the map
            {
                edgesComingIn.put(suffixes, 1);
            }
            if(mappedStrings.containsKey(prefixes)) //now keep track of how many edges going in and out each String has
            {
                mappedStrings.get(prefixes).add(suffixes);
                edgesGoingOut.replace(prefixes, edgesGoingOut.get(prefixes)+1);
            }
            else
            {
                edgesGoingOut.put(prefixes, 1);
                ArrayList<DoubleString> newList = new ArrayList<>();
                newList.add(suffixes);
                mappedStrings.put(prefixes, newList);
            }

        }

        //determine which String to start with:
        int lowestGoingIn = Integer.MAX_VALUE;
        DoubleString startStrings = new DoubleString("", "");
        for (Map.Entry<DoubleString, Integer> entry : edgesGoingOut.entrySet())
        {
            if(!edgesComingIn.containsKey(entry.getKey()))
            {
                startStrings = entry.getKey();
                break;
            }
            else if(edgesComingIn.get(entry.getKey()) < lowestGoingIn)
            {
                lowestGoingIn = edgesComingIn.get(entry.getKey());
                startStrings = entry.getKey();
            }
        }

        //now just make sure you have a DoubleStringAndBool within the map of Strings
        for (Map.Entry<DoubleString, ArrayList<DoubleString>> entry : mappedStrings.entrySet()) {

            ArrayList<DoubleString> oldList = entry.getValue();
            ArrayList<DoubleStringAndBool> newList = new ArrayList<>();
            for(int i  =0; i < oldList.size(); i++)
            {
                DoubleStringAndBool newEntry = new DoubleStringAndBool(oldList.get(i));
                newList.add(newEntry);
            }
            theEdgesMap.put(entry.getKey(), newList);
        }
        return startStrings;
    }

    private static ArrayList<String> formatFastqIntoRosalindPairs(String[] args) {
        ArrayList<String> associations;
        String firstFileName = args[0];
        if(args.length > 3) //if there are 4 arguments, we're reading in 2 files
        {
            String secondFileName = args[3];
            associations = returnStringsFromTwoFiles(firstFileName, secondFileName);
        }
        else
        {
            associations = returnRosalindFormattedStrings(firstFileName);
        }
        return associations;
    }

    //inserts the contents of oldList into newList where newStart's index indicates
    public static ArrayList<DoubleString> insertList(ArrayList<DoubleString> oldList, ArrayList<DoubleString> newList, int newStart)
    {
        ArrayList<DoubleString> savedString = new ArrayList<>();
        for(int i =newStart; i < oldList.size(); i++)
        {
            if(oldList.size() > i) {
                savedString.add(oldList.get(i));
            }
        }
        for(int i = 0; i < newList.size(); i++)
        {
            if(oldList.size() > i + newStart)
            {
                oldList.set(newStart + i, newList.get(i));
            }
            else
            {
                oldList.add(newList.get(i));
            }

        }
        for(int i = 0; i < savedString.size()-1; i++)
        {
            if(oldList.size() > i + newStart + newList.size())
            {
                oldList.set(i + newStart + newList.size(), savedString.get(i+1));
            }
            else {
                oldList.add(savedString.get(i+1));
            }
        }

        return oldList;
    }

    //finds a string within an ArrayList
    public static int findString(ArrayList<DoubleString> theList, DoubleString theString)
    {
        for(int i  =0 ; i< theList.size(); i++)
        {
            if(theList.get(i).equals(theString))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     This function attempts to calculate the differences between the DoubleStrings passed in.
     This means it has to account for differently sized Strings, different individual pairs, or shifted Strings that otherwise match.
     There are, however, different scores given:
     First, if one String contains the other's prefix or suffix (so that a read got an extra char) they'll only get a difference score of one
     If this isn't the case, however, the function just compares each char at every index, adding 1 for every different char
     It also adds the difference of the sizes
     */
    //todo: also investigate just going near the map. It should be stored in "alphabetical" order, but your own comparator might not take into account
    //things like insertions or deletions, which shifts everything but still holds a closer read.
    public static int similarityScoreBetweenDoubleStrings(DoubleString first, DoubleString second)
    {
        int topDifferences = 0;


        String firstTop = first.getString1();
        String secondTop = second.getString1();

        if(!firstTop.equals(secondTop)) {

            String firstTopPre = firstTop.substring(0, firstTop.length() - 1);
            String firstTopSuf = firstTop.substring(1, firstTop.length());

            String secondTopPre = secondTop.substring(0, secondTop.length() - 1);
            String secondTopSuf = secondTop.substring(1, secondTop.length());


            boolean suffixOrPrefixHeld = false;
            if (firstTop.contains(secondTopPre) || firstTop.contains(secondTopSuf)
                    || secondTop.contains(firstTopPre) || secondTop.contains(firstTopSuf)) {
                suffixOrPrefixHeld = true;
            }

            if (!suffixOrPrefixHeld) { //if there's not a perfect match inside (there wasn't just one shift) count all differences

                for (int i = 0; i < firstTop.length(); i++) {
                    if (secondTop.length() <= i) {
                        break;
                    } else if (secondTop.charAt(i) != firstTop.charAt(i)) {
                        topDifferences++; //NOTE: This may cause some problems. If two Strings are the same, except one is just shifted over by only
                        //TWO characters, then its difference score will still be the number of characters in a string
                    }
                }

            }
            topDifferences += Math.abs(firstTop.length() - secondTop.length());
        }



        int bottomDifferences = 0;
        String firstBottom = first.getString2();
        String secondBottom = second.getString2();
        if(!firstBottom.equals(secondBottom)) {

            String firstBottomPre = firstBottom.substring(0, firstTop.length() - 1);
            String firstBottomSuf = firstBottom.substring(1, firstTop.length());

            String secondBottomPre = secondBottom.substring(0, secondTop.length() - 1);
            String secondBottomSuf = secondBottom.substring(1, secondTop.length());


            boolean suffixOrPrefixHeld = false;
            if (firstBottom.contains(secondBottomPre) || firstBottom.contains(secondBottomSuf)
                    || secondBottom.contains(firstBottomPre) || secondBottom.contains(firstBottomSuf)) {
                suffixOrPrefixHeld = true;
            }

            if (!suffixOrPrefixHeld) { //if there's not a perfect match inside (there wasn't just a shift) count all differences

                for (int i = 0; i < firstBottom.length(); i++) {
                    if (secondBottom.length() <= i) {
                        break;
                    } else if (secondBottom.charAt(i) != firstBottom.charAt(i)) {
                        bottomDifferences++; //NOTE: This may cause some problems. If two Strings are the same, except one is just shifted over by only
                        //TWO characters, then its difference score will still be the number of characters in a string
                    }
                }

            }
            bottomDifferences += Math.abs(firstBottom.length() - secondBottom.length());
        }
        return (topDifferences + bottomDifferences);
    }


    public static DoubleString findClosestDoubleString(DoubleString compareMe)
    {
        int lowest = Integer.MAX_VALUE;
        DoubleString bestDoubleSoFar = new DoubleString("fake", "fake");
        for(DoubleString s : theEdgesMap.keySet())
        {

            if(theEdgesMap.containsKey(s)) {
                int newScore = similarityScoreBetweenDoubleStrings(compareMe, s);
                if(newScore < lowest && !noEdgesLeft(theEdgesMap.get(s))) //making sure the edges on this next double string aren't used up and that the newscore's better
                {
                    lowest = newScore;
                    bestDoubleSoFar = s;
                }
            }
        }
        return bestDoubleSoFar;
    }

    //This function takes all the edges in theEdgesMap connected to the "old" DoubleString,
    //gives them to the newDS, then removes everything with the old from theEdgesMap
    //todo: this doesn't work if we grab a node that doesn't exist and try to find something that's already there!
    public static void addEdgesOfOldToNew(DoubleString old, DoubleString newDS)
    {
        ArrayList<DoubleStringAndBool> newList = theEdgesMap.get(old);
        if(!theEdgesMap.containsKey(newDS))
        {
            ArrayList<DoubleStringAndBool> addMeList = new ArrayList<>();
            theEdgesMap.put(newDS, addMeList);
        }
        ArrayList<DoubleStringAndBool> changingList = theEdgesMap.get(newDS);
        for(int i = 0; i < newList.size(); i++)
        {
            changingList.add(newList.get(i));
        }
        theEdgesMap.remove(old);
    }


    //todo: several things are breaking here. Track down the process you're expecting, then follow this every step of the way and see where it breaks
    //forms a cycle using the graph of nodes and edges
    public static ArrayList<DoubleString> formCycle(DoubleString nextNode)
    {
        formCycleList.add(nextNode);
        if(!theEdgesMap.containsKey(nextNode))
        {
            DoubleString closestDS = findClosestDoubleString(nextNode); //todo: if we do this, we'll never need to form another cycle! This is broke
            //^^^in that it just grabs what's closest once a cycle's broken.
            //addEdgesOfOldToNew(nextNode, closestDS);

            DoubleString fake = new DoubleString("fake", "fake");
            if(closestDS.equals(fake)) //if there is no closeDS with a nextNode
            {
                return formCycleList;
            }

            formCycle(closestDS);
            return formCycleList;
        }

        ArrayList<DoubleStringAndBool> keysAsArray = new ArrayList<DoubleStringAndBool>(theEdgesMap.get(nextNode));

        if(noEdgesLeft(keysAsArray))
        {
            return formCycleList;
        }
        else
        {
            Random r = new Random();
            DoubleStringAndBool rand = keysAsArray.get(r.nextInt(keysAsArray.size()));
            while (rand.used) {
                rand = keysAsArray.get(r.nextInt(keysAsArray.size()));
            }
            rand.used = true;
            formCycle(rand.edgeDestination);
            return formCycleList;
        }
    }

    //if there are unused edges left on the graph, returns true
    public static boolean edgesOnGraph(TreeMap<DoubleString, ArrayList<DoubleStringAndBool>> theEdgesMap)
    {
        for(DoubleString s : theEdgesMap.keySet())
        {
            if(theEdgesMap.containsKey(s)) {
                if (!noEdgesLeft(theEdgesMap.get(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    //returns true if there are no unused edges left within an Arraylist of DoubleStringAndBool
    public static boolean noEdgesLeft(ArrayList<DoubleStringAndBool> theList)
    {
        if(theList != null) {
            for (int i = 0; i < theList.size(); i++) {
                if (!theList.get(i).used) {
                    return false;
                }
            }
        }
        return true;
    }


}