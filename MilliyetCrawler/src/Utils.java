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
	
	public static int getIdFromLink(String link){
		String[] items = link.substring(1,link.length()).split("/");
		int returnInt = -1;
		
		if(items.length < 6){
			return -1;
		}else{
			try {
				String idString = items[6];
				returnInt = Integer.parseInt(idString);
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		
		return returnInt;
	}
	
	public static List<KoseYazari> getKoseYazarlari(){
		List<KoseYazari> yazarlar = new ArrayList<KoseYazari>();
		Document doc;
		try {
			doc = Jsoup.connect("http://www.milliyet.com.tr/Yazar.aspx?aType=Yazarlar").get();
			
			Element a = doc.select("select#ddlyazarlar").first();
			
			Elements all = a.getAllElements();
			
			for(Element el:all){
				if(!el.attr("value").isEmpty()){
					KoseYazari koseYazari = new KoseYazari(Integer.parseInt(el.attr("value")), el.text());
					yazarlar.add(koseYazari);
				}
			}
			
			return yazarlar;
			
		} catch (IOException e) {
			e.printStackTrace();
			return yazarlar;
		}
	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int id){
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
		
		for(int i = 0; i < MILLIYET.pagesToCrawl / MILLIYET.fetcherCount; i++){
			try{
				Document doc = Jsoup.connect("http://www.milliyet.com.tr/DetayliArama.aspx?aType=YazarTumYaziArsiv&PAGE=" + ((id + MILLIYET.fetcherCount * i) + 1) + "&ItemsPerPage=10&AuthorID=" + koseYazari.getId()).timeout(MILLIYET.timeout).get();
				
				Element content = doc.select("div[class=gundem]").first();
				
				Elements links = content.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					if(!linkHref.contains("cadde")){
						String dateString = link.select("em[class=date]").first().text();
						
						KoseYazisi koseYazisi = new KoseYazisi(getIdFromLink(linkHref), dateString, linkHref);
						koseYazisi.setYazarAdi(koseYazari.getKoseYazariAdi());
						
						koseYazilari.add(koseYazisi);
					}else{
						System.out.println("Cadde : " + linkHref);
					}
					
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
