package src.Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import src.Model.SampleGenome;
import src.Model.Sequence;

/**
 * Created by tommyhowell on 12/8/17.
 */

public class FromFileGenomeFactory
{

    public static final String ANSWERS = "answer.txt";

    public static final SampleGenome readGenome(String fileName)
    {
        FileInputStream fStream = null;
        FileInputStream answerFStream = null;
        SimpleSampleGenome sampleGenome = null;
        try
        {
            answerFStream = new FileInputStream(ANSWERS);
            BufferedReader answerBR = new BufferedReader(new InputStreamReader(answerFStream));
            String genomeAnswer = answerBR.readLine();
            Sequence genome = SequenceFactory.makeSequence(genomeAnswer, genomeAnswer);

            fStream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            List<Sequence> sequenceList = new ArrayList<>();

            String id;
            String data;
            String line;
            String quality;
            while ((id = br.readLine()) != null)
            {
                data = br.readLine();
                line = br.readLine();
                quality = br.readLine();
                sequenceList.add(SequenceFactory.makeSequence(data,quality));
            }

            sampleGenome = new SimpleSampleGenome(genome, sequenceList);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sampleGenome;
    }

}
