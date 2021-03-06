import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private String yazarAdi;
	
	private List<String> paragraphs;
	
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(String koseYazisiLink, String baslik, String yazarAdi, String tarih) throws IOException, ParseException{
		setKoseYazisiLink(koseYazisiLink);
		setBaslik(baslik);
		setYazarAdi(yazarAdi);
		downloadKoseYazisi();
		setTarih(DateUtils.getDate(tarih));
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
		String result = CommonLinks.BASE_URL + koseYazisiLink;
		
		this.koseYazisiLink = result;
	}

	public String getKoseYazisi() {
		return koseYazisi;
	}

	public void downloadKoseYazisi() throws IOException, ParseException {
		Document doc;
		try {
			setParagraphs(new ArrayList<String>());
			doc = Jsoup.connect(getKoseYazisiLink()).timeout(YENISAFAK.timeout).get();
			
			Element content = doc.select("div[class=content]").first();
			
			for(Element el:content.select("p")){
				getParagraphs().add(el.text());
			}
			
			String text = content.text();
			
			setKoseYazisi(text);
		} catch (Exception e) {
			setKoseYazisi("Read timed out");
		}
		
	}

	public String getYazarAdi() {
		return yazarAdi;
	}

	public void setYazarAdi(String yazarAdi) {
		this.yazarAdi = yazarAdi;
	}

	public void setKoseYazisi(String koseYazisi) {
		this.koseYazisi = koseYazisi;
	}

	@Override
	public String toString() {
		return "KoseYazisi [tarih=" + tarih + ", baslik=" + baslik
				+ ", koseYazisiLink=" + koseYazisiLink + ", koseYazisi="
				+ koseYazisi + ", yazarAdi=" + yazarAdi + "]";
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<String> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
