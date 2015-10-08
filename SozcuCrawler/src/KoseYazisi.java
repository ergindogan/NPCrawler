import java.io.IOException;
import java.text.ParseException;
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
	private String yazarAdi;
	
	
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
		Document doc = Jsoup.connect(getKoseYazisiLink()).timeout(SOZCU.timeout).get();
		Element a;
		Element date;
		String dateString;
		Date tarih = null;
		
		a = doc.select("div[class=content]").first();
		if(a == null){
			a = doc.select("article[class=hayat-single-post-content-in]").first();
		}
		
		date = doc.select("meta[itemprop=datePublished]").first();
		if(date == null){
			date = doc.select("meta[property=article:published_time]").first();
		}
		
		
		if(date != null && date.attr("content") != null){
			dateString = date.attr("content");
			
			tarih = Utils.parseDate(dateString);
		}else{
			
			dateString = "Date couldn't found";
			System.err.println("Date couldn't found for " + yazarAdi + " link : " + koseYazisiLink);
		}
		
		setTarih(tarih);
		
		if(a == null){
			setKoseYazisi("Yazi alinamadi." + getKoseYazisiLink());
		}else{
			setKoseYazisi(a.text());
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

}
