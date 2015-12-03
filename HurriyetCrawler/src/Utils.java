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
			doc = Jsoup.connect("http://www.hurriyet.com.tr/yazarlar/").timeout(HURRIYET.timeout).get();
			    			
			Element content = doc.select("select[class=selectColFromArch]").first();
			
			Elements links = content.getElementsByTag("option");
			for (Element link : links) {
				String value = link.attr("value");
				String linkText = link.text();
				if(!value.equals("#") && !value.isEmpty()){
					KoseYazari koseYazari = new KoseYazari(Integer.parseInt(value), linkText);
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
		try {
			for (int year = 2015; year > 2015 - HURRIYET.yearsToCrawl; year--) {
				doc = Jsoup.connect("http://hurarsiv.hurriyet.com.tr/Yazarlar/Default.aspx?ID=" + koseYazari.getId() + "&ay=" + id + "&yil=" + year + "&p=1").timeout(HURRIYET.timeout).get();
				
				Element content = doc.select("ul[class=hsaca-list h-archive-article-list]").first();
				
				Elements links = content.getElementsByTag("a");
				for (Element link : links) {
					String value = link.attr("href");
					String linkText = link.text();
					if(!value.equals("#")){
						KoseYazisi koseYazisi = new KoseYazisi(linkText, value, koseYazari.getKoseYazariAdi());
						koseYazilari.add(koseYazisi);
					}
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		return koseYazilari;
	}

	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + koseYazisi.getKoseYazisi() + "\n";
	}

}
