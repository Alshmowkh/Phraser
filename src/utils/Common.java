package utils;

import java.util.Arrays;
import java.util.List;
import static utils.Miss.pl;

public class Common {

    public static List<Character> diacriticList() {
        Character[] diacritics = new Character[]{'َ', 'ِ', 'ُ', 'ْ', 'ّ', 'ً', 'ٍ', 'ٌ'};
        return Arrays.asList(diacritics);

    }

    public static String deDiacritic(String term) {
        String res = "";
        if (term != null) {
            char[] chars = term.toCharArray();

            for (Character ch : chars) {
                if (diacriticList().contains(ch)) {
                    continue;
                } else if (ch.equals('ٱ')) {
                    ch = 'ا';
                }
                res = res + ch;
            }
        }

        return res;
    }

}
