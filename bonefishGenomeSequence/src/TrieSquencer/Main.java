package src.TrieSquencer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main
{

    public static final String OUTFILE_FASTQ = "output.fastq";

    public static void main(String[] args)
    {
        try
        {
            FileInputStream fStream = new FileInputStream(OUTFILE_FASTQ);
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));


            GenomeSequencer genomeSequencer = new GenomeSequencer();
            String id;
            while((id = br.readLine()) != null)
            {
                genomeSequencer.addNode(parseRead(br));
            }
            br.close();

            fStream = new FileInputStream(OUTFILE_FASTQ);
            br = new BufferedReader(new InputStreamReader(fStream));

            List<String> potentialStarts = new ArrayList<>();
            String line1;
            while((line1 = br.readLine()) != null)
            {
                String potentialStart = parseRead(br);
                if(genomeSequencer.prefixNotInTrie(potentialStart))
                {
                    potentialStarts.add(potentialStart);
                }
                //could do something here
            }
            br.close();
            for (String potentialStart : potentialStarts)
            {
                System.out.println(genomeSequencer.runStartAgainstTrie(potentialStart));
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String parseRead(BufferedReader br) throws IOException
    {
        String text = br.readLine();
        String plusSign = br.readLine();
        String readQuality = br.readLine();
        return text;
    }
}
