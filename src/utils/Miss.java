/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import edu.columbia.ccls.madamira.configuration.MorphFeatureSet;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import phraser.Phrase;
import phraser.Word;

public class Miss {

    public void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q' || c == '0') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    public void printAllSeg(OutSeg seg) {
        List<edu.columbia.ccls.madamira.configuration.Word> words = seg.getWordInfo().getWord();
        for (edu.columbia.ccls.madamira.configuration.Word w : words) {
            try {
                MorphFeatureSet wf = w.getAnalysis().get(0).getMorphFeatureSet();
                p("Diac:" + wf.getDiac() + "\t");
                p("Gloss:" + wf.getGloss() + "\t");
                p("Pos:" + wf.getPos() + "\t");
                p("Enc0:" + wf.getEnc0() + "\t");
                p("Prc0:" + wf.getPrc0() + "\t");
                p("Prc1:" + wf.getPrc1() + "\t");
                p("Prc2:" + wf.getPrc2() + "\t");
                p("Stem:" + wf.getStem() + "\t");
               

                pl("");
            } catch (Exception e) {
            }
        }
    }

    public void printAllTags(OutSeg seg) {
        List<edu.columbia.ccls.madamira.configuration.Word> words = seg.getWordInfo().getWord();
        for (edu.columbia.ccls.madamira.configuration.Word w : words) {
            try {
                MorphFeatureSet wf = w.getAnalysis().get(0).getMorphFeatureSet();

                p(wf.getPos() + "\t");

            } catch (Exception e) {
            }
        }
    }

    public void printAllWordTag(OutSeg seg) {
        List<edu.columbia.ccls.madamira.configuration.Word> words = seg.getWordInfo().getWord();
        for (edu.columbia.ccls.madamira.configuration.Word w : words) {
            try {
                MorphFeatureSet wf = w.getAnalysis().get(0).getMorphFeatureSet();
                p(wf.getDiac() + ":" + wf.getPos() + "\t");
            } catch (Exception e) {
            }
        }
    }

    public void printWords(List<Word> words) {
        for (Word w : words) {
            p(w.diac() + "/" + w.tag() + "\t");
        }
    }

    public static void pl(Object o) {
        System.out.println(o);
    }

    void p(Object o) {
        System.out.print(o);
    }

    public void printList(List list) {
        list.stream().forEach((l) -> {
            printPhrase((Phrase) l);
        });
    }

    public void printPhrase(Phrase phrase) {
        pl(phrase.getTypeTB() + "\t" + phrase);
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("yes");
        list.add("no");
        ListIterator lit = list.listIterator();
        pl(lit.next());
//        lit.add(lit.previous());
        pl(lit.next());

    }

}
