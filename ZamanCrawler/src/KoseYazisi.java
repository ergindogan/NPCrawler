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
	
	public KoseYazisi(String koseYazisiLink, String baslik, String yazarAdi) throws IOException, ParseException{
		setKoseYazisiLink(koseYazisiLink);
		setBaslik(baslik);
		setYazarAdi(yazarAdi);
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
		this.koseYazisiLink = koseYazisiLink;
	}

	public String getKoseYazisi() {
		return koseYazisi;
	}

	public void downloadKoseYazisi() throws IOException, ParseException {
		setParagraphs(new ArrayList<String>());
		Document doc;
		Date contentDate = null;
		
		String koseYazisi = "Content couldn't fetched!";
		
		doc = Jsoup.connect(getKoseYazisiLink())
				.data("query", "Java")
				  .userAgent("Mozilla")
				  .timeout(ZAMAN.timeout)
				  .post();
		
		Element content = doc.select(".detaySpot").first();
		
		Element content2 = doc.select("span[itemprop=articleBody]").first();
		
		for(Element el:content.select("p")){
			getParagraphs().add(el.text());
		}
		for(Element el2:content2.select("p")){
			getParagraphs().add(el2.text());
		}
		
		Element date = doc.select("meta[itemprop=datePublished]").first();
		
		if(date != null){
			contentDate = Utils.parseDate(date.attr("content"));
		}else{
			System.err.println("Date couldn't fetched for yazar : " + getYazarAdi() + " Yazi link : " + getKoseYazisiLink());
		}
		
		if(content != null && content2 != null){
			koseYazisi = content.text() + " " + content2.text();
		}
		
		setKoseYazisi(koseYazisi);
		setTarih(contentDate);
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
