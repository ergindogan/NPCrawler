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
			doc = Jsoup.connect("http://www.zaman.com.tr/search.action").data("query", "Java")
					  .userAgent("Mozilla")
					  .cookie("auth", "token")
					  .timeout(ZAMAN.timeout)
					  .post();
			
			Element content = doc.select("#columnistSuggestionBox").first();
			
			Elements elements = content.getAllElements();
			
			for (Element element : elements) {
				String idString = element.attr("value");
				String koseYazariAdi = element.text();
				
				if(!idString.isEmpty()){
					KoseYazari yazar = new KoseYazari(Integer.parseInt(idString), koseYazariAdi);
					
					yazarlar.add(yazar);
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return yazarlar;

	}
	
	public static List<KoseYazisi> getKoseYazisi(KoseYazari koseYazari, int id){
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
		
//		Calendar fromDate = Calendar.getInstance();
//		fromDate.setTime(from);
//		
//		Calendar toDate = Calendar.getInstance();
//		toDate.setTime(to);
//		
		int yazarId = koseYazari.getId();
		
//		String fromString = parseDateToString(fromDate);
//		String toString = parseDateToString(toDate);
		
		Document doc;
		
		int pageNumber = id;
		int pageCount = -1;
		try {
			do{
				doc = Jsoup.connect("http://www.zaman.com.tr/search.action?pageNo=" + pageNumber + "&category=0&dt=0&"
						+ "columnists=" + yazarId + "&firstDate=" + ZAMAN.fromString + "&lastDate=" + ZAMAN.toString + "&sort_type=date_sort&media_type=allType&words=").data("query", "Java")
						  .userAgent("Mozilla")
						  .cookie("auth", "token")
						  .timeout(ZAMAN.timeout)
						  .post();
				
				Element element = doc.select("div[class=aramaMSayi]").first();
				             
				if(pageCount == -1){
					pageCount = getPageCount(element.text());
					if(pageCount < 0){
						continue;
					}
				}
				
				Element content = doc.select("div[class=aramaBody]").first();
				
				Elements links = content.getElementsByTag("a");
				
				int i = 0;
				
				for (Element link : links) {
					if(i%2==0){
						String linkHref = link.attr("href");
						String linkText = link.text();
						
						KoseYazisi koseYazisi = new KoseYazisi(CommonLinks.BASE_URL + linkHref, linkText, koseYazari.getKoseYazariAdi());
						
						koseYazilari.add(koseYazisi);
					}
					
					i++;
				}
				
				pageNumber = pageNumber + ZAMAN.fetcherCount;
			}while(pageNumber < pageCount);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		return koseYazilari;
	}

	public static String getOneRecord(KoseYazisi koseYazisi){
		return koseYazisi.getYazarAdi() + " - " + koseYazisi.getBaslik() + " - " + koseYazisi.getTarih() + "\n" + ParagraphExtension.getParagraphString(koseYazisi.getParagraphs(), koseYazisi.getKoseYazisi()) + "\n";
	}
	
	public static Date parseDate(String dateString) throws ParseException{
		
		SimpleDateFormat sdf;
		
		if(!dateString.contains("T")){
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		
		return sdf.parse(dateString);
	}
	
	public static String parseDateToString(Calendar cal){
		String result = "";
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		result = year + "-" + month + "-" + day;
		
		return result;
	}
	
	private static int getPageCount(String pageCountString){
		
		String wholeText = pageCountString;
		
		String array[] = wholeText.split(" ");
		
		String forth = array[4];
		
		String countString = forth.substring(7);
		
		int resultCount = -1000;
		
		if(!countString.isEmpty()){
			resultCount = Integer.parseInt(countString);
		}
		
		return (resultCount / 20) + 1;
		
	}

}
