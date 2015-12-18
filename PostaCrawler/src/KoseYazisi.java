import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class KoseYazisi {
	private Date tarih;
	private String baslik;
	private String koseYazisiLink;
	private String koseYazisi;
	
	private List<String> paragraphs;
	
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
			setParagraphs(new ArrayList<String>());
			doc = Jsoup.connect(getKoseYazisiLink()).timeout(POSTA.timeout).get();
			
			Element titleElement = doc.select("head[id=Head1]").first();
			
			Element contentParent = doc.select("div[class=yazarDetay]").first();
			
			Element dateElementChild = contentParent.select("span[class=date]").first();
			Element contentChild = contentParent.select("div[class=yazarHaberTxt]").first();
			
			String titleText = titleElement.select("title").text();
			String dateText = dateElementChild.text();
			String contentText = contentChild.text();
			
			for(Element el:contentChild.select("p")){
				getParagraphs().add(el.text());
			}
			
			setTarih(DateUtils.getDate(dateText));
			setBaslik(titleText);
			setKoseYazisi(contentText);

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

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<String> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
