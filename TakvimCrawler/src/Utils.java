import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class Utils {
	
	public static Map<Integer,KoseYazari> getKoseYazarlari(){
		Map<Integer,KoseYazari> yazarlar = new HashMap<Integer,KoseYazari>();
		int i = 0;
		Document doc;
		
		try {
			doc = Jsoup.connect(CommonLinks.BASE_URL + "/yazarlar/arsiv").timeout(TAKVIM.timeout).get();
			
			Element element = doc.select("div[class=writerSearchWidget]").first();
			
			Elements elements = element.select("option[value]");
			
			for(Element el:elements){
				String linkText = "";
				String rawLink = el.attr("abs:value");
				
				if(!rawLink.contains("http://www.takvim.com.tr")){
					linkText = "http://www.takvim.com.tr" + el.attr("abs:value");
				}else{
					linkText = el.attr("abs:value");
				}
				String yazarAdi = el.text();
				KoseYazari koseYazari = new KoseYazari(yazarAdi, linkText);
				yazarlar.put(i, koseYazari);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return yazarlar;
	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int sayfaSayisi){
		Document doc;
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
		for(int i = 0; i < sayfaSayisi; i++){
			int pageNumber = i + 1;
			String updatedAllPassagesLink = koseYazari.getTumYazilariLink() + "?PAGE=" + pageNumber;
			
			try {
				doc = Jsoup.connect(updatedAllPassagesLink).timeout(TAKVIM.timeout).get();
				
				Element element = doc.select("[class=otherWriters]").first();
				
				Elements elements = element.select("a[href]");
				
				Set<String> linkSet = new HashSet<String>();
				
				for(Element el:elements){
					String linkText = el.attr("abs:href");
					linkSet.add(linkText);
				}
				
				for(String link:linkSet){
					KoseYazisi koseYazisi = new KoseYazisi(link, koseYazari.getKoseYazariAdi());
					koseYazilari.add(koseYazisi);
				}
				
			} catch (IOException e) {
				if(e instanceof HttpStatusException){
					HttpStatusException statusException = (HttpStatusException) e;
					if(statusException.getStatusCode() == 404){
						//Page couldn't found.
					}
				}else{
					System.err.println("Read time out... " + " Link : " + updatedAllPassagesLink);
				}
			}
		}
		
		return koseYazilari;
	}
	
	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getKoseYazariAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + ParagraphExtension.getParagraphString(koseYazisi.getParagraphs(), koseYazisi.getKoseYazisi()) + "\n";
	}
}
