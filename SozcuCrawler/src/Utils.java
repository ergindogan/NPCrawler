import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
			doc = Jsoup.connect("http://www.sozcu.com.tr/kategori/yazarlar/").get();
			
			Element content = doc.select("div#left").first();
			
			Elements links = content.getElementsByTag("a");
			
			for (Element link : links) {
				String linkHref = "";
				String title = link.attr("title");
				
				if(!title.isEmpty()){
					linkHref = link.attr("href");
					KoseYazari koseYazari = new KoseYazari(title, linkHref);
					yazarlar.add(koseYazari);
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return yazarlar;

	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, Calendar dateToFetch) {
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
//		Calendar fromDate = Calendar.getInstance();
//		fromDate.setTime(from);
//		
//		Calendar toDate = Calendar.getInstance();
//		toDate.setTime(to);
//		
//		while(fromDate.get(Calendar.YEAR) <= toDate.get(Calendar.YEAR) && fromDate.get(Calendar.MONTH) < toDate.get(Calendar.MONTH)){
//			
//			int month = fromDate.get(Calendar.MONTH) + 1;
//			int year = fromDate.get(Calendar.YEAR);
			
			Document doc;
			try {
				doc = Jsoup.connect(koseYazari.getKoseYazariLink() + "?ay=" + dateToFetch.get(Calendar.MONTH) + "&Yil=" + dateToFetch.get(Calendar.YEAR) + "&yazi=Yaz%C4%B1lar%C4%B1+Getir").timeout(SOZCU.timeout).get();
				
				Element content = doc.select("div#left").first();
				if(content != null){
					Elements links = content.getElementsByTag("a");
					
					for (Element link : links) {
						String yaziLink = link.attr("href");
						String title = link.text();
						
						KoseYazisi koseYazisi = new KoseYazisi(yaziLink, title, koseYazari.getKoseYazariAdi());
						koseYazilari.add(koseYazisi);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
//			fromDate.add(Calendar.MONTH, 1);
//		}
		
		return koseYazilari;
	}

	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + koseYazisi.getKoseYazisi() + "\n";
	}
	
	public static Date parseDate(String dateString) throws ParseException{
		
		SimpleDateFormat sdf;
		
		if(!dateString.contains("T")){
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		}
		
		return sdf.parse(dateString);
	}

}
