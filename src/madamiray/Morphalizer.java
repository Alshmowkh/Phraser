/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madamiray;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.List;
import phraser.Sentence;
import utils.ReadFromFile;

/**
 *
 * @author bakee
 */
public class Morphalizer {

    String corpus;

    public void setCorpus(String path) {
        corpus = path;
    }

    public List prepareSelectedSent(String sent) {
        String[] pathFile = {"F:\\Master\\Thesis\\Implementations\\IP_Detector\\IP\\simple_corpus_NS.txt", "./IP/simple_corpus_NS.txt"};
        if (corpus == null) {
            corpus = pathFile[0];
        }
        List<String> sentences = new ArrayList();
        int s = 0;
        if (sent.matches("\\d+")) {
            s = Integer.parseInt(sent);
            boolean startWithZero = sent.startsWith("0");
            if (startWithZero) {
                sentences = new ReadFromFile(corpus).sentences().subList(s, sentences.size());
            } else {
                sentences.add(new ReadFromFile(corpus).sentence(s));
            }

        } else {
            s = 1;
            sentences.add(sent);
        }
        return sentences;
    }

    public static List morphlize(List sentences) {

        List morphed = new ArrayList();

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sentences);
        for (int i = 0; i < sentences.size(); i++) {
            outseg = mada.getMorpholizing().get(i);
            sentence = new Sentence(outseg);
            morphed.add(sentence);
        }

        return morphed;
    }

    public static Sentence morphlize(String sentenceIn) {

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sentenceIn);
        outseg = mada.getMorpholizing().get(0);
        sentence = new Sentence(outseg);

        return sentence;
    }

}
