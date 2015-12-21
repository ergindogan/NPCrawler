import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


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
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(String yazarAdi, String baslik, Date tarih, String koseYazisi){
		setYazarAdi(yazarAdi);
		setBaslik(baslik);
		setTarih(tarih);
		setKoseYazisi(koseYazisi);
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

}
