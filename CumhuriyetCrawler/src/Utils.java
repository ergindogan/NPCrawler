import java.io.IOException;
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
	
	public static int getIdFromLink(String link){
		try {
			String[] items = link.substring(1,link.length()).split("/");
			return Integer.parseInt(items[1]);
		} catch (Exception e) {
			System.err.println("Link = " + link);
		}
		return 0;
	}
	
	public static List<KoseYazari> getKoseYazarlari(){
		List<KoseYazari> yazarlar = new ArrayList<KoseYazari>();
		Document doc;
		try {
			doc = Jsoup.connect("http://www.cumhuriyet.com.tr/yazarlar").get();
			
			Element element = doc.select("div#tum-yazarlar").first();
			
			Elements links = element.getElementsByTag("a");
			KoseYazari yazar = new KoseYazari();
			
			int i = 1;
			for (Element link : links) {
				
				String linkHref = link.attr("href");
				String linkText = link.text();
				
				if(i%2 != 0){
					yazar.setKoseYazariAdi(linkText);
					yazar.setId(Utils.getIdFromLink(linkHref));
					yazar.setTumYazilariLink(linkHref);
				}else{
					yazar.setSonYaziBaslik(linkText);
					yazar.setSonYaziLink(linkHref);
					yazarlar.add(yazar);
					yazar = new KoseYazari();
				}
				i++;
			}
			return yazarlar;
			
		} catch (IOException e) {
			e.printStackTrace();
			return yazarlar;
		}
	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int aySayisi){
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		String linkHref = "";
		
		for(int i = 1;i < aySayisi * 2; i++){
			String url = getolderUrl(i, koseYazari.getTumYazilariLink(), koseYazari.getId());
			
			Document doc;
			try {
				doc = Jsoup.connect(url).timeout(CUMHURIYET.timeout).get();
				
				Element element = doc.select("ul#article-list").first();
				
				Elements links = element.getElementsByTag("a");
				
				for (Element link : links) {
					linkHref = link.attr("href");
				    String linkText = link.text();
				    
				    if(linkHref.contains("/haber/turkiye") || linkHref.contains("/haber/secim_2015") || linkHref.contains("/haber/diger")){
				    	continue;
				    }
				    
				    String plot = "";
				    String dateString = "";
				    String koseYazisiLink = "";
				    
				    
				    String[] items = linkText.split(" ");
				    
				    for(int j = 0; j < 3; j++){
				    	dateString = dateString + items[j] + " ";
				    }
				    dateString.trim();
				    
				    for(int j = 4; j < items.length; j++){
				    	plot = plot + items[j] + " ";
				    }
				    plot.trim();
				    
				    koseYazisiLink = linkHref;
				    
				    KoseYazisi koseYazisi = new KoseYazisi(Utils.getIdFromLink(linkHref),dateString, plot, koseYazisiLink);
				    koseYazisi.setYazarAdi(koseYazari.getKoseYazariAdi());
				    koseYazilari.add(koseYazisi);
				    
				}
				
			} catch (IOException e) {
				System.err.println("Yazar id : " + koseYazari.getId() + "Link = " + linkHref);
				e.printStackTrace();
			}
		}
		
		return koseYazilari;
	}
	
	private static String getolderUrl(int i, String url, int yazarId){
		if(i == 1){
			return url;
		}else{
			String[] items = url.split(Integer.toString(yazarId));
			return items[0] + yazarId + "/" + i + items[1];
		}
	}
	
	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + ParagraphExtension.getParagraphString(koseYazisi.getParagraphs(), koseYazisi.getKoseYazisi()) + "\n";
	}

}
