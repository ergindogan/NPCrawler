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
			doc = Jsoup.connect("http://www.radikal.com.tr/yazarlar/tum_yazarlar/#kategori").get();
			
			Elements links = doc.getElementsByTag("a");
			
			//http://www.radikal.com.tr/arama/yazar=ahmet_m%C3%BCmtaz_taylan&siralama=tarihe_gore_azalan/
			for(Element element:links){
				String link = element.text();
				if(link.contains("arama")){
					link = link.replace(link, "/arama/yazar=");link.replace(link, "&siralama=tarihe_gore_azalan/");
					
					String yazarAdi = link.replace("_", "-");
					yazarAdi = yazarAdi.replaceAll("\\W", ""); 
				}
			}
			
			//<div class="authors-list lazy-load push_3">
			//http://www.radikal.com.tr/index/cengiz-candar/
//			Element element = doc.select("div.authors-list lazy-load push_3").first();
//			
//			Elements links = element.getElementsByTag("a");
			KoseYazari yazar = new KoseYazari();
			
			return yazarlar;
			
		} catch (IOException e) {
			e.printStackTrace();
			return yazarlar;
		}
	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int aySayisi){
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		String linkHref = "";
		
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
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + koseYazisi.getKoseYazisi() + "\n";
	}

}
