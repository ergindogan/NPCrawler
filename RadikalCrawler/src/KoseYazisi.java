import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class KoseYazisi {
	
	private Date tarih;
	private String baslik;
	private String koseYazisiLink;
	private String koseYazisi;
	
	private String koseYazariAdi;
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(String koseYazisiLink, String koseYazariAdi) throws IOException{
		setKoseYazisiLink(koseYazisiLink);
		setKoseYazariAdi(koseYazariAdi);
		downloadKoseYazisi();
	}

	public Date getTarih() {
		return tarih;
	}

	public void setTarih(Date tarih) {
		this.tarih = tarih;
	}

	public String getBaslik() {
		return baslik;
	}

	public void setBaslik(String baslik) {
		this.baslik = baslik;
	}

	public String getKoseYazisiLink() {
		return koseYazisiLink;
	}

	public void setKoseYazisiLink(String koseYazisiLink) {
		if(!koseYazisiLink.contains(CommonLinks.BASE_URL)){
			koseYazisiLink = CommonLinks.BASE_URL + koseYazisiLink;
		}
		this.koseYazisiLink = koseYazisiLink;
	}

	public String getKoseYazisi() {
		return koseYazisi;
	}

	public void downloadKoseYazisi() throws IOException {
		Document doc;
		
		try {
			doc = Jsoup.connect(getKoseYazisiLink()).timeout(RADIKAL.timeout).get();
			
			Element headerElement = doc.select("[itemprop=headline]").first();
			String header = headerElement.text();
			
			Element subHeaderElement = doc.select("[itemprop=articleSection]").first();
			String subHeader = subHeaderElement.text();
			
			Element articleElement = doc.select("[itemprop=articleBody]").first();
			String article = articleElement.text();
			
			Element dateElement = doc.select("[itemprop=datePublished]").first();
			String dateString = dateElement.text();
			
			setBaslik(header);
			setKoseYazisi(subHeader + "\n" + article);
			setTarih(DateUtils.getDate(dateString));

		} catch (Exception e) {
			setKoseYazisi("Read timed out");
		}
		
	}

	public void setKoseYazisi(String koseYazisi) {
		this.koseYazisi = koseYazisi;
	}

	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}

	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}

}
