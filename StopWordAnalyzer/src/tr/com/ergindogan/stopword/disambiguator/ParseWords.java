package tr.com.ergindogan.stopword.disambiguator;

import java.io.IOException;
import java.util.List;

import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.parser.MorphParse;

public class ParseWords {

    TurkishMorphParser parser;

    public ParseWords(TurkishMorphParser parser) {
        this.parser = parser;
    }

    public void parse(String word) {
        System.out.println("Word = " + word);
        List<MorphParse> parses = parser.parse(word);
        for (MorphParse parse : parses) {
            System.out.println(parse.formatLong());
        }
    }

    public static void main(String[] args) throws IOException {
        TurkishMorphParser parser = TurkishMorphParser.createWithDefaults();
        new ParseWords(parser).parse("gittikleri");
    }
}
