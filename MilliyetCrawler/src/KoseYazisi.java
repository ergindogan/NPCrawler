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
	private int id;
	
	private String yazarAdi;
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(int id, String tarih, String koseYazisiLink) throws IOException, ParseException{
		setId(id);
		setTarih(DateUtils.getTarih(tarih, koseYazisiLink));
		setKoseYazisiLink(koseYazisiLink);
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

	public void downloadKoseYazisi() throws IOException {
		Document doc = Jsoup.connect(getKoseYazisiLink()).timeout(MILLIYET.timeout).get();
		Element a;
		String baslik = "";
		
		Element bigTitle = doc.select("strong[class=bigTitle]").first();
		Element header1 = doc.select("h1").first();
		Element header2 = doc.select("h2").first();
		Element header3 = doc.select("h3").first();
		
		if(bigTitle != null){
			baslik += bigTitle.text() + " ";
		}
		
		if(header1 != null){
			baslik += header1.text() + " ";
		}
		
		if(header2 != null){
			baslik += header2.text() + " ";
		}
		
		if(header3 != null){
			baslik = baslik + " ";
			baslik += header3.text();
		}
		 
		if(getKoseYazisiLink().contains("skorer")){
		   a = doc.select("p").first();
		}else{
		   a = doc.select("div#divAdnetKeyword3").first();
		}
		
		setBaslik(baslik);
		
		if(a == null){
			setKoseYazisi("Yazi alinamadi." + getKoseYazisiLink());
		}else{
			setKoseYazisi(a.text());
		}
		
		
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "KoseYazisi [tarih=" + tarih + ", baslik=" + baslik
				+ ", koseYazisiLink=" + koseYazisiLink + ", koseYazisi="
				+ koseYazisi + ", id=" + id + "]";
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

}
