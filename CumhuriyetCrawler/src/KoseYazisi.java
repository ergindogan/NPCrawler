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
	private int id;
	
	private String yazarAdi;
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(int id, String tarih, String baslik, String koseYazisiLink) throws IOException{
		setId(id);
		setTarih(DateUtils.getDate(tarih));
		setBaslik(baslik);
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
		if(!koseYazisiLink.contains(CommonLinks.BASE_URL)){
			koseYazisiLink = CommonLinks.BASE_URL + koseYazisiLink;
		}
		this.koseYazisiLink = koseYazisiLink;
	}

	public String getKoseYazisi() {
		return koseYazisi;
	}

	public void downloadKoseYazisi() throws IOException {
		try {
			Document doc = Jsoup.connect(getKoseYazisiLink()).timeout(CUMHURIYET.timeout).get();
			
			Element a = doc.select("div#article-body").first();
			
			setKoseYazisi(a.text());
		} catch (Exception e) {
			setKoseYazisi("Read timed out");
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
