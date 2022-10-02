package phraser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Phraser_v2 {

    List sentences;

    public Phraser_v2(Sentence input) {
        sentences = new ArrayList();
        sentences.add(input);
    }

    public Phraser_v2(List input) {
        sentences = input;
    }

    private Sentence reIndexing(Sentence sentence) {
        for (Word w : sentence) {
            w.index = w.index() - 1;
        }
        sentence.count = sentence.count() - 1;
        return sentence;
    }

   
    private Sentence normlize(Sentence words, int index) {

        Word fw = words.get(index);
        if (fw.isConj()) {
            words.remove(index);
            words = reIndexing(words);
            words = normlize(words, index);
        }
        return words;
    }

    public List getPhrases() {
        if (sentences == null) {
            return null;
        }
        List constituents = new ArrayList();
        Constituent con;
        Sentence sentence;
        Iterator itr = sentences.iterator();
        while (itr.hasNext()) {
            sentence = (Sentence) itr.next();
            con = phrasing(sentence);
            constituents.add(con);
        }
        return constituents;
    }

   
    private Constituent phrasing(Sentence sentence) {

        sentence = normlize(sentence, 0);
        Phrase phrase;
        Constituent constituents = new Constituent();
        int index = 0;
        int indxp = -1;
        while (index < sentence.count() - 1) {
            phrase = this.readPhrase(sentence, index);
            phrase.setType();
            phrase.index = indxp++;
            index = phrase.get(phrase.size() - 1).index() + 1;
            constituents.add(phrase);

        }
        return constituents;
    }

    private Phrase readPhrase(Sentence sentence, int index) {
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

    private boolean noReduce(Word w1, Word w2) {
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

}
