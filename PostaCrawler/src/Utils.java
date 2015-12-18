import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			doc = Jsoup.connect(CommonLinks.BASE_URL + "/yazarlar").timeout(POSTA.timeout).get();
			
			Elements allAuthors = doc.select("[class=yazarTumu]");
			
			for(Element author:allAuthors){
				Elements allAuthorUnfilteredLinks = author.select("a[href]");
				
				for(Element link:allAuthorUnfilteredLinks){
					String linkText = link.attr("abs:href");
					
					if(linkText.contains(CommonLinks.BASE_URL + "/yazarlar/")){
						KoseYazari koseYazari = new KoseYazari(link.text(), linkText);
						yazarlar.put(i, koseYazari);
						i++;
					}
					
				}
				
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
			String updatedAllPassagesLink = koseYazari.getTumYazilariLink() + "&PAGE=" + pageNumber + "&PageSize=" + POSTA.pagerYaziSayisi;
			
			try {
				doc = Jsoup.connect(updatedAllPassagesLink).timeout(POSTA.timeout).get();
				
				Element allPassagesContent = doc.select("[class=aramaSonuc]").first();
				
				Elements links = allPassagesContent.select("a[href]");
				
				for(Element link:links){
					String ignoreFlag = link.attr("class");
					String linkText = link.attr("abs:href");
					
					if(!ignoreFlag.equals("kategori")){
						KoseYazisi koseYazisi = new KoseYazisi(linkText, koseYazari.getKoseYazariAdi());
						koseYazilari.add(koseYazisi);
					}
				}
				
			} catch (IOException e) {
//				e.printStackTrace();
				System.err.println("Read time out... " + " Link : " + updatedAllPassagesLink);
			}
		}
		
		return koseYazilari;
	}
	
	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getKoseYazariAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + ParagraphExtension.getParagraphString(koseYazisi.getParagraphs(), koseYazisi.getKoseYazisi()) + "\n";
	}
	
}
