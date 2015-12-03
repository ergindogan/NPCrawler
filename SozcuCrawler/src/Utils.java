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
			doc = Jsoup.connect("http://www.sozcu.com.tr/kategori/yazarlar/").data("query", "Java")
					  .userAgent("Mozilla")
					  .timeout(SOZCU.timeout)
					  .post();;
			
			Element content = doc.select("div[class=media-list list cat authors _mbtm30]").first();
			
			Elements elements = content.select("div[class=item-link _flex _aic]");
			
			for (Element element : elements) {
				Element titleElement = element.select("div[class=item-media]").first();
				Element tumYazilarElement = element.select("div[class=item-after]").first();
				
				String tumYazilar = tumYazilarElement.baseUri() + tumYazilarElement.select("a").first().attr("href");
				String yazarAdi = titleElement.select("a").first().attr("title");
				
				KoseYazari koseYazari = new KoseYazari(yazarAdi, tumYazilar);
				yazarlar.add(koseYazari);
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
				doc = Jsoup.connect(koseYazari.getKoseYazariLink() + "?ay=" + dateToFetch.get(Calendar.MONTH) + "&Yil=" + dateToFetch.get(Calendar.YEAR) + "&yazi=Yaz%C4%B1lar%C4%B1+Getir").timeout(SOZCU.timeout).data("query", "Java")
						  .userAgent("Mozilla")
						  .timeout(SOZCU.timeout)
						  .post();;
				
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
