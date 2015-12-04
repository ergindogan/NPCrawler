import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
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
	
	public static List<KoseYazisi> getKoseYazisi(HttpClient httpClient, KoseYazari koseYazari, Calendar dateToFetch) {
		List<KoseYazisi> koseYazilari = new ArrayList<KoseYazisi>();
			
		Document doc;
		
		try {
			doc = makePostRequest(httpClient, koseYazari.getKoseYazariLink(), dateToFetch.get(Calendar.MONTH), dateToFetch.get(Calendar.YEAR));
			
			Element content = doc.select("div[class=popular-news _mbtm20]").first();
			if(content != null){
				Elements links = content.select("a[href^=http://www.sozcu.com.tr]");
				
				for (Element link : links) {
					String yaziLink = link.attr("href");
					String title = link.select("div[class=item-text]").first().text();
					String dateString  = link.select("div[class=txt-date]").first().text();
						
					KoseYazisi koseYazisi = new KoseYazisi(yaziLink, title, koseYazari.getKoseYazariAdi(), dateString);
					koseYazilari.add(koseYazisi);
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
	
	private static Document makePostRequest(HttpClient httpClient, String postUrl, int month, int year) throws ClientProtocolException, IOException{
		
		HttpPost httppost = new HttpPost(postUrl);
		Document doc = null;

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("months",Integer.toString(month)));
		params.add(new BasicNameValuePair("years", Integer.toString(year)));
		params.add(new BasicNameValuePair("send", "Getir"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		//Execute and get the response.
		HttpResponse response = httpClient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		    	StringWriter writer = new StringWriter();
		    	IOUtils.copy(instream, writer, "UTF-8");
		    	String theString = writer.toString();
		    	
		    	doc = Jsoup.parse(theString);
		    } finally {
		        instream.close();
		    }
		}
		return doc;
	}

}
