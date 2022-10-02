package test;

import java.util.List;
import java.util.Scanner;
import madamiray.Morphalizer;
import phraser.Phraser_v2;
import utils.Miss;
import static utils.Miss.pl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bakee
 */
public class Test {

    String corpus;

    public void ini() {
        String[] pathFile = {"F:\\Master\\Thesis\\Implementations\\IP_Detector\\IP\\simple_corpus_NS.txt", "./IP/simple_corpus_NS.txt"};
        System.out.print("Enter sentence ID:");
        if (corpus == null) {
            corpus = pathFile[0];
        }
        String sent = new Scanner(System.in, "windows-1256").nextLine();

        Morphalizer morphalizer = new Morphalizer();
        List sentences;
        sentences = morphalizer.prepareSelectedSent(sent);

        sentences = Morphalizer.morphlize(sentences);
        Phraser_v2 phraser = new Phraser_v2(sentences);
        sentences = phraser.getPhrases();
        pl(sentences.get(0));

    }

    public static void main(String[] args) {

        new Test().ini();
//        cls.detectDocument("");
    }

}
