import java.io.IOException;

import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.MorphParse;
import zemberek.morphology.parser.SentenceMorphParse;


public class DisambiguateSentences {
	TurkishSentenceParser sentenceParser;

    public DisambiguateSentences(TurkishSentenceParser sentenceParser) {
        this.sentenceParser = sentenceParser;
    }

    void parseAndDisambiguate(String sentence) {
        System.out.println("Sentence  = " + sentence);
        SentenceMorphParse sentenceParse = sentenceParser.parse(sentence);

        System.out.println("Before disambiguation.");
        writeParseResult(sentenceParse);

        System.out.println("\nAfter disambiguation.");
        sentenceParser.disambiguate(sentenceParse);
        writeParseResult(sentenceParse);

    }

    private void writeParseResult(SentenceMorphParse sentenceParse) {
        for (SentenceMorphParse.Entry entry : sentenceParse) {
            System.out.println("Word = " + entry.input);
            System.out.println(entry.parses.get(0).getLemma());
//            for (MorphParse parse : entry.parses) {
//                System.out.println(parse.getLemma());
//            }
        }
    }

    public static void main(String[] args) throws IOException {
        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        TurkishSentenceParser sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
        new DisambiguateSentences(sentenceParser)
                .parseAndDisambiguate("Türkiye'nin  şu abudik durumu hiç de iyi değil.");
    }
}
