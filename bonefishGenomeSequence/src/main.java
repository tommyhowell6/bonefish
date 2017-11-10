import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Jesse on 10/12/2017.
 */
public class main {

    public static TreeMap<DoubleString, ArrayList<DoubleStringAndBool>> theEdgesMap = new TreeMap<>();
    public static ArrayList<DoubleString> formCycleList = new ArrayList<>();



    public static void main(String[] args)
    {
        //read in the Strings and then just redo it cause it's way different


        int k = Integer.parseInt(args[0]);
        int gap = Integer.parseInt(args[1]);

        ArrayList<String> associations = formatFastqIntoRosalindPairs(args);


        DoubleString startStrings = findStartString(k, associations);


        Random r = new Random();

        DoubleString rand = startStrings;
        ArrayList<DoubleString> cycle;
        cycle = formCycle(rand);
        cycle = formRestOfCycle(r, rand, cycle);

        ArrayList<DoubleString> cyclePattern;
        cyclePattern = cycle;

        ArrayList<DoubleString> edges = formEdgesBetweenNodes(cycle, cyclePattern);

        StringBuilder finalString = combineEdgesIntoGenome(gap, edges);

        printGenomeToFile(finalString);


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

    //generates the rest of the Eulerian path
    private static ArrayList<DoubleString> formRestOfCycle(Random r, DoubleString rand, ArrayList<DoubleString> cycle) {
        while(edgesOnGraph(theEdgesMap)) //while there are still unused edges on the graph:
        {
            System.out.println("Current cycle size: " + cycle.size());
            ArrayList<DoubleString> newCycle = new ArrayList<>();


            while(!theEdgesMap.containsKey(rand) || noEdgesLeft(theEdgesMap.get(rand))) //keep generating a random key until you find a node with more edges
            {
                rand = cycle.get(r.nextInt(cycle.size()));
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
    private static DoubleString findStartString(int k, ArrayList<String> associations) {
        TreeMap<DoubleString, ArrayList<DoubleString>> mappedStrings = new TreeMap<>();
        TreeMap<DoubleString, Integer> edgesComingIn = new TreeMap<>();
        TreeMap<DoubleString, Integer> edgesGoingOut = new TreeMap<>();


        for(int i = 0; i < associations.size(); i++) //go through all iterations of the strings found
        {
            String[] splitAssociations = associations.get(i).split("\\|");

            String firstPre = splitAssociations[0].substring(0, k-1);
            String secondPre = splitAssociations[1].substring(0, k-1);

            String firstSuf = splitAssociations[0].substring(1, k);
            String secondSuf = splitAssociations[1].substring(1, k);

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
        String firstFileName = args[2];
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

    //forms a cycle using the graph of nodes and edges
    public static ArrayList<DoubleString> formCycle(DoubleString nextNode)
    {
        formCycleList.add(nextNode);
        if(!theEdgesMap.containsKey(nextNode))
        {
            return formCycleList;
        }

        ArrayList<DoubleStringAndBool> keysAsArray = new ArrayList<DoubleStringAndBool>(theEdgesMap.get(nextNode));

        if(noEdgesLeft(keysAsArray))
        {
            return formCycleList;
        }

        else {
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
        for(int i = 0; i < theList.size(); i++)
        {
            if(!theList.get(i).used)
            {
                return false;
            }
        }
        return true;
    }
}
