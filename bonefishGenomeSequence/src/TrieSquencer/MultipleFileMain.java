package src.TrieSquencer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tommyhowell on 12/10/17.
 */

public class MultipleFileMain
{
    public static final String FILE_NAME = "bonefishFileNames.txt";
    public static final String OUT_FILE_NAME = "resultGenome.txt";

    public static void main(String[] args)
    {
        try
        {
            GenomeSequencer genomeSequencer = new GenomeSequencer();
            List<String> potentialStarts = new ArrayList<>();
            FileInputStream fStream = new FileInputStream(FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));
            String fastQFile;
            FileInputStream fastQFStream;
            BufferedReader fastQBR;
            while ((fastQFile = br.readLine()) != null)
            {
                fastQFStream = new FileInputStream(fastQFile);
                fastQBR = new BufferedReader(new InputStreamReader(fastQFStream));

                String id;
                while((id = fastQBR.readLine()) != null)
                {
                    genomeSequencer.addNode(parseRead(fastQBR));
                }
                fastQBR.close();
            }
            br.close();

            fStream = new FileInputStream(FILE_NAME);
            br = new BufferedReader(new InputStreamReader(fStream));

            while ((fastQFile = br.readLine()) != null)
            {
                fastQFStream = new FileInputStream(fastQFile);
                fastQBR = new BufferedReader(new InputStreamReader(fastQFStream));

                String id;
                while ((id = fastQBR.readLine()) != null)
                {
                    String potentialStart = parseRead(fastQBR);
                    if (genomeSequencer.prefixNotInTrie(potentialStart))
                    {
                        potentialStarts.add(potentialStart);
                    }
                    //could add some optimization here
                }
                fastQBR.close();
            }
            br.close();

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUT_FILE_NAME), "utf-8"));

            for (String potentialStart : potentialStarts)
            {
                SequenceHolder sequenceHolder = genomeSequencer.runStartAgainstTrie(potentialStart);
                if(!sequenceHolder.isError())
                {
                    writer.write(sequenceHolder.getSequence() + "\n");
                }
            }
            writer.close();

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
