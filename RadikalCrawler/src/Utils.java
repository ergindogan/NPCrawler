import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
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
	
	public static Map<Integer,KoseYazari> getKoseYazarlari(){
		Map<Integer,KoseYazari> yazarlar = new HashMap<Integer,KoseYazari>();
		int i = 0;
		Document doc;
		try {
			doc = Jsoup.connect("http://www.radikal.com.tr/yazarlar/tum_yazarlar/#kategori").get();
			
			Elements links = doc.select("a[href]");
			
			for(Element element:links){
				String link = element.attr("abs:href");
				if(link.contains("/arama/yazar")){
					link = link.replace("http://www.radikal.com.tr/arama/yazar=", "");
					link = link.replace("&siralama=tarihe_gore_azalan/", "");
					
					String okunurYazarAdi = getOkunurYazarAdi(link);
					
					String linkYazarAdi = deAccent(link);
					if(linkYazarAdi.contains("serdar")){
						okunurYazarAdi = "M." + okunurYazarAdi;
						linkYazarAdi = "m" + linkYazarAdi;
					} else if (linkYazarAdi.contains("therap")){
						continue;
					}
					
					KoseYazari yazar = new KoseYazari(okunurYazarAdi, linkYazarAdi);
					yazarlar.put(i, yazar);
					i++;
				}
			}
			
			return yazarlar;
			
		} catch (IOException e) {
			e.printStackTrace();
			return yazarlar;
		}
	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int sayfaSayisi){
		Document doc;
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
		String sayfaLink = koseYazari.getTumYazilariLink().substring(0, koseYazari.getTumYazilariLink().length() - 2);
		Set<String> tumLinkler = new LinkedHashSet<String>();
		
		for(int i = 1; i <= sayfaSayisi; i++){
			
			try {
				doc = Jsoup.connect(sayfaLink + i + "/").timeout(RADIKAL.timeout).get();
				
				Elements links = doc.select("a[href]");
				Set<String> linkler = new LinkedHashSet<String>();
				
				for(Element element:links){
					String link = element.attr("abs:href");
					
					if(link.contains(CommonLinks.BASE_URL + "/yazarlar/" + koseYazari.getLinkYazarAdi() + "/")){
						if(!tumLinkler.contains(link)){
							tumLinkler.add(link);
							linkler.add(link);
						}
					}
				}
				
				for(String myLink:linkler){
					KoseYazisi koseYazisi = new KoseYazisi(myLink, koseYazari.getKoseYazariAdi());
					koseYazilari.add(koseYazisi);
				}
				
			} catch (IOException e) {
//				e.printStackTrace();
				System.err.println("Yazar : " + koseYazari.getKoseYazariAdi() + " yazilari cekilemedi...");
			}

		}
		
		return koseYazilari;
	}
	
	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getKoseYazariAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + koseYazisi.getKoseYazisi() + "\n";
	}
	
	public static String deAccent(String str) {
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	private static String getOkunurYazarAdi(String rawYazarAdi){
		String okunurYazarAdi = rawYazarAdi.replace("_", " ");
		okunurYazarAdi = WordUtils.capitalize(okunurYazarAdi);
		
		return okunurYazarAdi;
	}

}
