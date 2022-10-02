package phraser;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import madamiray.Madamiray;
import utils.Miss;
import static utils.Miss.pl;
import utils.ReadFromFile;

public class Phraser {

//    Sentence sentence;
    String corpus;

    public void setCorpus(String path) {
        corpus = path;
    }

    Sentence reIndexing(Sentence sentence) {
        for (Word w : sentence) {
            w.index = w.index() - 1;
        }
        sentence.count = sentence.count() - 1;
        return sentence;
    }

    Sentence normlizeI(Sentence words, int index) {

        Word fw = words.get(index);
        String valuef = fw.value().trim();
        List<String> apolishers = new Letter().abolishers();
        if (apolishers.contains(valuef) || fw.isConj() || fw.isPartNeg()) {
            words.remove(index);
            words = reIndexing(words);
            words = normlizeI(words, index);
        }
        if (fw.hasConj()) {
            fw.changeFeature("value", valuef.substring(1));

        }
        return words;
    }

    Sentence normlize(Sentence words, int index) {

        Word fw = words.get(index);
        if (fw.isConj()) {
            words.remove(index);
            words = reIndexing(words);
            words = normlize(words, index);
        }
        return words;
    }

    public void ini() throws FileNotFoundException, IOException {
        String[] pathFile = {"F:\\Master\\Thesis\\Implementations\\IP_Detector\\IP\\simple_corpus_NS.txt", "./IP/simple_corpus_NS.txt"};
        System.out.print("Enter sentence ID:");
        if (corpus == null) {
            corpus = pathFile[0];
        }
        String sent = new Scanner(System.in, "windows-1256").nextLine();

        new Miss().printList(phrases2(sent));
    }

    public List<Phrase> phrases2(String sent) {
        List<Phrase> phrases = null;
        List<String> sentences = new ArrayList();
        int s = 0;
        if (sent.matches("\\d+")) {
            s = Integer.parseInt(sent);
            sentences = new ReadFromFile(corpus).sentences();
            if (s > sentences.size()) {
                pl("Sentence is not exist with ID" + s);
                System.exit(1);
            }
        } else {
            s = 1;
            sentences.add(sent);
        }

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
//        pl(sentences.size());
        boolean startWithZero = sent.startsWith("0");
        if (!startWithZero) {
            mada = new Madamiray(sentences.get(s - 1));
            outseg = mada.getMorpholizing().get(0);
            new Miss().printAllSeg(outseg);
            sentence = new Sentence(outseg);
//            pl(sentence);
            phrases = phrases(sentence);
        } else {
            sentences = sentences.subList(s > 0 ? s - 1 : 0, sentences.size());
            mada = new Madamiray(sentences);
            for (int i = 0; i < sentences.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
                sentence = new Sentence(outseg);
//                pl(sentence);
                
                phrases = phrases(sentence);

//                new Miss().printList(phrases2);
                new Miss().next();
//                pl("");
            }
        }

        return phrases;
    }

    public List<Phrase> phrases1(String sent) {
        List<Phrase> phrases = null;

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sent);
        outseg = mada.getMorpholizing().get(0);
        new Miss().printAllSeg(outseg);
        sentence = new Sentence(outseg);
        phrases = phrases(sentence);
        
        return phrases;
    }

    public List<Phrase> phrases(int sent) {
        List<Phrase> phrs;
        String sentenceString = new ReadFromFile(corpus).sentence(sent);

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sentenceString);
        outseg = mada.getMorpholizing().get(0);
        new Miss().printAllSeg(outseg);
        sentence = new Sentence(outseg);
//            pl(sentence);
        phrs = phrases(sentence);

        return phrs;
    }

    public List<List<Phrase>> phrasesAll(int sentNum) {

        List<List<Phrase>> phrAllSents;

        List<String> sentences = new ReadFromFile(corpus).sentences();
        if (sentNum > sentences.size()) {
            pl("Sentence is not exist with ID" + sentNum);
            System.exit(1);

        }

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        List<Phrase> phrases;
        phrAllSents = new ArrayList();
        sentences = sentences.subList(sentNum, sentences.size());
        mada = new Madamiray(sentences);
        for (int i = 0; i < sentences.size(); i++) {
            outseg = mada.getMorpholizing().get(i);
            sentence = new Sentence(outseg);
//                pl(sentence);
            phrases = phrases(sentence);
            phrAllSents.add(phrases);
//                new Miss().printList(phrases2);
//                pl("");
        }

        return phrAllSents;
    }

    List<Phrase> phrases(Sentence sentence) {
//        pl(sentence);
        sentence = normlize(sentence, 0);
        Phrase phrase;
        List<Phrase> phrases = new ArrayList();
        int index = 0;
        int indxp = -1;
        while (index < sentence.count() - 1) {
            phrase = this.readPhrase(sentence, index);
            phrase.setType();
            phrase.index = indxp++;
            index = phrase.get(phrase.size() - 1).index() + 1;
            phrases.add(phrase);
//            pl(phrase);
        }
        return phrases;
    }

    Phrase readPhrase(Sentence sentence, int index) {
        Phrase phrase = new Phrase();
        Word w1, w2;
        int i = index;
        boolean reduce = true;

        for (i = index; i < sentence.size() - 1 && reduce; i++) {
            w1 = sentence.get(i);

            if (w1.isPrep() && !w1.hasEnc()
                    || w1.isConj()
                    || w1.isInterrogation()
                    || w1.isPunc()
                    || w1.isRel()) {

                phrase.add(w1);
                phrase.setType();
                return phrase;
//                i++;
//                w1 = sentence.get(i);
            }
            if (i < sentence.size() - 1) {
                w2 = sentence.get(i + 1);
                reduce = noReduce(w1, w2);
            }
            phrase.add(w1);
        }
        return phrase;
    }

    boolean noReduce(Word w1, Word w2) {
//        pl(w1 + "\t" + w2);
        return w1.isNoun() && w2.isPunc()
                || w1.isAdj() && w2.isPunc()
                || w1.isDTadj() && w2.isPunc()
                || w1.isPnoun() && w2.isPunc()
                || w1.isNoun() && w2.isNoun() && !w1.hasEnc()
                || w1.isNoun() && w2.isNoun() && !w1.hasEnc() && !w2.hasEnc()
                || w1.isNoun() && w2.isDTnoun()
                || w1.isDTnoun() && w2.isDTnoun() && !w2.hasPrc1()
                || w1.isNoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isPnoun() && !w2.hasConj()
                || w1.isDTnoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isDTnoun()
                || w1.isPronX() && w2.isDTnoun()
                //-------------------------------
                || w1.isNoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isDTnoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isAdj() && w2.isAdj()
                || w1.isDTadj() && w2.isDTadj()
                //--------------------------------
                || w1.isPartNeg() && w2.isVerb();
    }

    public static void main(String[] args) throws IOException {
        Phraser cls = new Phraser();

        cls.ini();
//        cls.detectDocument("");
    }

}
