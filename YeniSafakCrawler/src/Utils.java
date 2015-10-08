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
			doc = Jsoup.connect("http://www.yenisafak.com.tr/yazarlar/").get();
			
			Element content = doc.select("ul[class=dropdown-list other]").first();
			
			Elements links = content.getElementsByTag("a");
			for (Element link : links) {
				String value = link.text();
				String linkText = link.attr("href");
				
				if(!linkText.contains("GAZETE YAZARLARI")){
					KoseYazari koseYazari = new KoseYazari(value, linkText);
					yazarlar.add(koseYazari);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return yazarlar;

	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int id){
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
		Document doc;
		
		for(int i = ((id - 1) * YENISAFAK.pagesToCrawl) + 1; i <= id * YENISAFAK.pagesToCrawl; i++){
			try {
				doc = Jsoup.connect(koseYazari.getKoseYazariLink() + i + "&years=T%C3%BCm%20Y%C4%B1llar&filterType=True").timeout(YENISAFAK.timeout).get();
				
				Element content = doc.select("div[class=articles]").first();
				
				Elements articles = content.getElementsByTag("article");
				
				for (Element article : articles) {
					
					Element hrefElement = article.getElementsByTag("a").first();
					Element dateElement = article.select("time[class=article]").first();
					
					String yaziLink = hrefElement.attr("href");
					String title = hrefElement.text();
					String date = dateElement.text();
					
					KoseYazisi koseYazisi = new KoseYazisi(yaziLink, title, koseYazari.getKoseYazariAdi(), date);
					
					koseYazilari.add(koseYazisi);
				}

				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return koseYazilari;
	}

	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + koseYazisi.getKoseYazisi() + "\n";
	}

}
