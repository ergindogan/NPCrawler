import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class Utils {
	
	public static List<KoseYazari> getKoseYazarlari(){
		List<KoseYazari> yazarlar = new ArrayList<KoseYazari>();
		Document doc;
		try {
			doc = Jsoup.connect("http://www.sabah.com.tr/yazarlar/arsiv").get();
			
			Element content = doc.select("div[class=yazarList2]").first();
			
			Elements links = content.getElementsByTag("a");
			for (Element link : links) {
			  String koseYazariLink = link.attr("href");
			  String koseYazariAdi = link.text();
			  
			  KoseYazari koseYazari = new KoseYazari(koseYazariAdi, koseYazariLink);
			  yazarlar.add(koseYazari);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return yazarlar;

	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int pageCount, int id) throws ParseException{
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
		Document doc;
		
		for(int i = 0; i < pageCount; i++){
			try {
				doc = Jsoup.connect(koseYazari.getKoseYazariLink() + (id + i * SABAH.fetcherCount ) ).get();
				
				Element content = doc.select("div[class=arsivList]").first();
				
				Elements links = content.getElementsByTag("a");
				
				
				for (Element link : links) {
					
					String linkHref = link.attr("href");
					String date = link.select("em").text();
					String title = link.select("strong").text();
					
					KoseYazisi koseYazisi = new KoseYazisi(linkHref, title, koseYazari.getKoseYazariAdi(), date);
					koseYazilari.add(koseYazisi);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		return koseYazilari;
	}

	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + ParagraphExtension.getParagraphString(koseYazisi.getParagraphs(), koseYazisi.getKoseYazisi()) + "\n";
	}

}
