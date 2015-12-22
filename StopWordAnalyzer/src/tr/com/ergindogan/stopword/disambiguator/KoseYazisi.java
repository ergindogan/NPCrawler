package tr.com.ergindogan.stopword.disambiguator;

import java.util.Date;
import java.util.List;


/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class KoseYazisi {
	
	private Date tarih;
	private String baslik;
	private String koseYazisi;
	private String yazarAdi;
	
	private List<String> paragraphs;
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(String yazarAdi, String baslik, String koseYazisi, Date tarih){
		setYazarAdi(yazarAdi);
		setBaslik(baslik);
		setKoseYazisi(koseYazisi);
		setTarih(tarih);
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

	public String getKoseYazisi() {
		return koseYazisi;
	}

	public void setKoseYazisi(String koseYazisi) {
		this.koseYazisi = koseYazisi;
	}

	public String getYazarAdi() {
		return yazarAdi;
	}

	public void setYazarAdi(String yazarAdi) {
		this.yazarAdi = yazarAdi;
	}
	
	public String getOneRecord(){
		return getYazarAdi() + " - " + getBaslik() + " - " + getTarih() + "\n" + ParagraphExtension.getParagraphString(getParagraphs(), getKoseYazisi())+ "\n";
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<String> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
