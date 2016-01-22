package tr.com.ergindogan.stopword.classifier;

import java.io.IOException;
import java.util.List;

import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.SentenceMorphParse;
import Corpus.Sentence;
import Corpus.TurkishSplitter;

public class Denem {
	
	private static TurkishMorphParser morphParser;
	private static Z3MarkovModelDisambiguator disambiguator;
	private static TurkishSentenceParser sentenceParser;

	private static String myString = "Kla­sik as­tro­lo­ji­de te­mel­de kul­la­nı­lan 7 ge­ze­gen var­dır. Bun­lar, Gü­neş, Ay, Mer­kür, Ve­nüs, Mars, Sa­türn ve Jü­pi­te­r’­dir. Bu 7 ge­ze­gen haf­ta­nın bir gü­nü­nü yö­ne­tir­ler. O ge­ze­gen­le­rin sem­bo­li­ze et­ti­ği ko­nu­la­rı yap­ma­ya baş­la­mak, ba­şa­rı el­de et­mek açı­sın­da önem­lidir­._______  PA­ZAR­TE­Sİ Ay gü­nü, bu yüz­den pa­zar­te­si sen­dro­mu di­ye bir ger­çe­ği­miz var! Ay gün­le­ri ge­çi­ci de­ğiş­ken iş­ler, yu­va, ai­le iş­le­ri, bes­len­me, kı­sa se­ya­hat­ler, de­niz se­ya­hat­le­ri gi­bi fa­ali­yet­le­re uy­gun bir za­man di­li­mi ya­ra­tır­lar._______  SA­LI Mars gü­nü­dür. Sa­lı gün­le­ri pa­zar­te­si re­ha­ve­ti­ni üze­ri­miz­den atıp ha­re­ke­te geç­me za­ma­nı­dır. Ce­sa­ret ge­rek­ti­ren iş­le­re so­yun­mak, risk al­mak için ide­al bir gün­dür._______  ÇAR­ŞAM­BA Mer­kür gü­nü­dür. Her tür­lü bil­gi ve ile­ti­şi­mi en ak­tif şe­kil­de kul­la­na­bi­lir, top­lan­tı­lar ya­pa­bi­lir, im­za­lar ata­bi­lir­si­niz. Ye­ni bir işe baş­la­mak için uy­gun bir gün sa­yı­la­bi­lir. Ke­za bu­gün se­ya­hat­le­re çık­mak, bir şe­yi öğ­ren­me­ye baş­la­mak için de ga­yet gü­zel bir gün­dür._______  PER­ŞEM­BE Jü­pi­ter gü­nü­dür. Bu­gün ti­ca­ret yap­mak, eli­niz­de­ki bir işi ge­liş­tir­mek, bü­yüt­mek için ha­ri­ka bir gün­dür. Hu­kuk­sal ko­nu­lar­la bu­gün il­gi­le­ne­bi­lir­si­niz. Ayrıca şans gü­nü­dür._______  CU­MA Ve­nüs gü­nü­dür. Bu­gün key­fi­mi­ze ke­yif ka­ta­cak ko­nu­lar­la il­gi­len­mek, flört et­mek, mad­di ko­nu­lar­la il­gi­len­mek, tem­bel­lik yap­mak, küs­ler­le ba­rış­mak, alışve­riş­ler yap­mak, gi­yim, mo­da, koz­me­tik­le il­gi­len­mek, es­te­tik ope­ras­yo­nu ol­mak, aş­kı ifa­de et­mek, eğ­len­ce­li ak­ti­vi­te­le­re yö­nel­mek, ro­man­tik or­tam­lar ya­rat­mak için en ide­al gün­ler­den bir ta­ne­si­dir._______  CU­MAR­TE­Sİ Sa­türn gü­nü­dür; en­gel­ler, kı­sıt­lan­ma­lar mey­da­na ge­le­bi­lir. Önem­li iş­le­ri­ni­ze bu gün­ler­de baş­la­ma­ma­lı­sı­nız. Ge­cik­me­ler, sı­kın­tı­lar, en­gel­ler­le kar­şı­la­şa­bi­lir­si­niz._______  PA­ZAR Gü­neş gü­nü­dür. Din­le­nip ener­ji­mi­zi dol­dur­mak, ki­şi­sel ih­ti­yaç­la­rı­mı­zı gi­der­mek, mo­ti­vas­yo­nu­mu­zu top­la­mak, plan ve prog­ram yap­mak için çok gü­zel bir gün­dür. Kut­la­ma­lar, par­ti­ler, or­ga­ni­zas­yon­lar yap­mak için ­de.";
	
	private static String string2 = "Pa­ra… Pa­ra… Pa­ra… Da­ha çok pa­ra… Li­mit­siz kâr ora­nı… Bu kö­şe­de ba­zen “u­lu­sal­cı­lı­k” ne­dir; “so­l” ne­dir yaz­ma­ya ça­lı­şı­yo­rum. Vah­şi ka­pi­ta­liz­min/ne­oli­be­ra­liz­min ül­ke­le­ri, bi­rey­le­ri ne ha­le ge­tir­di­ği­ni so­mut ör­nek­ler­le açık­la­ma­ya ça­lı­şı­yo­rum. Ta­rım sek­tö­rü­ne dik­ka­ti­ni­zi çek­me­ye ça­lı­şı­yo­rum. Bu ne­den­le bu­gün… Can­la­rı­nız­dan bah­set­mek is­ti­yo­rum; ya­ni ço­cuk­la­rı­nız­dan. Bel­ki bu şe­kil­de dik­ka­ti­ni­zi çe­ke­rim; ya­şam­sal teh­li­ke­den ha­ber­dar olur­su­nuz. Bu ne­den­le bu­gün… Ço­cuk­la­rı il­köğ­re­tim­de oku­yan bir an­ne­nin uya­rı mek­tu­bu­nu pay­laş­mak is­ti­yo­rum… Şöy­le di­yor: “Bi­lin­di­ği gi­bi sağ­lık­lı ta­rım uy­gu­lan­ma­yan ta­rım ürün­le­ri, zi­ra­i ilaç­lar­dan kay­nak­la­nan, yı­ka­may­la bi­le geç­me­si müm­kün ol­ma­yan kan­se­ro­jen ka­lın­tı­lar içer­mek­te­dir. Ar­tık mey­ve ve seb­ze­ler da­hil, bu ta­rım ürün­le­ri­ni göz gö­re gö­re tü­ket­mek im­kan­sız ha­le gelmiştir… Sof­ra­la­rı­mız­dan ek­sik edil­me­yen ta­vuk­lar ile il­gi­li ger­çek­le­re ge­lin­ce: ‘Ke­mik­le­ri ge­liş­me­si­n’, ‘sa­de­ce et yap­sın­la­r’ ve ‘ça­bu­cak bü­yü­sün­le­r’ di­ye; yu­mur­ta­dan çı­kar çık­maz hor­mon ve an­ti­bi­yo­tik ve­ri­len; 1 sa­at ka­ran­lık­ta, 23 sa­at ışık al­tın­da bı­ra­kı­la­rak dur­ma­dan ye­me­ye zor­la­nan; ku­luç­ka sü­re­si 17 gü­ne inen; he­nüz 45 gün­lük­ken as­lın­da bir yum­ruk bü­yük­lü­ğün­de ol­ma­sı ge­rek­ti­ği hal­de 1.5 ki­lo­ya ka­dar çı­ka­bi­len ta­vuk­lar, eğer he­men ke­sil­mez­ler ise za­ten ke­mik­le­ri kı­rı­la­rak ken­di­li­ğin­den ölü­yor.";
	
	public static void main(String[] args) throws IOException {
		String sentenceRoot = "";
		
		morphParser = TurkishMorphParser.createWithDefaults();
		disambiguator = new Z3MarkovModelDisambiguator();
		sentenceParser = new TurkishSentenceParser(morphParser, disambiguator);
		
		TurkishSplitter ts = new TurkishSplitter();
		
		List<Sentence> sentences = ts.split(string2);
		
		for(Sentence sentence:sentences){
			SentenceMorphParse sentenceParse = sentenceParser.parse(sentence.toString());
			sentenceParser.disambiguate(sentenceParse);
			
			System.out.println(sentence.toString());
			
			for (SentenceMorphParse.Entry entry : sentenceParse) {
				sentenceRoot += " " + entry.parses.get(0).getLemma();
	        }
			
			System.out.println(sentenceRoot);
			sentenceRoot = "";
		}
		
		
	}

}
