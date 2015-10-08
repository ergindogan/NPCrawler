
/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class KoseYazari {
	
	private int id;
	private String koseYazariAdi;
	private String tumYazilariLink;
	private String sonYaziLink;
	private String sonYaziBaslik;

	public KoseYazari(){
		
	}
	
	public KoseYazari(int id, String koseYazariAdi, String tumYazilariLink, String sonYaziLink){
		setId(id);
		setKoseYazariAdi(koseYazariAdi);
		setTumYazilariLink(tumYazilariLink);
		setSonYaziLink(sonYaziLink);
	}

	@Override
	public String toString() {
		return "KoseYazari [id=" + id + ", koseYazariAdi=" + koseYazariAdi
				+ ", tumYazilariLink=" + tumYazilariLink + ", sonYaziLink="
				+ sonYaziLink + ", sonYaziBaslik=" + sonYaziBaslik + "]";
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}


	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}


	public String getTumYazilariLink() {
		return tumYazilariLink;
	}


	public void setTumYazilariLink(String tumYazilariLink) {
		if(!tumYazilariLink.contains(CommonLinks.BASE_URL)){
			tumYazilariLink = CommonLinks.BASE_URL + tumYazilariLink;
		}
		this.tumYazilariLink = tumYazilariLink;
	}


	public String getSonYaziLink() {
		return sonYaziLink;
	}


	public void setSonYaziLink(String sonYaziLink) {
		if(!sonYaziLink.contains(CommonLinks.BASE_URL)){
			sonYaziLink = CommonLinks.BASE_URL + sonYaziLink;
		}
		this.sonYaziLink = sonYaziLink;
	}

	public String getSonYaziBaslik() {
		return sonYaziBaslik;
	}

	public void setSonYaziBaslik(String sonYaziBaslik) {
		this.sonYaziBaslik = sonYaziBaslik;
	}
	
}
