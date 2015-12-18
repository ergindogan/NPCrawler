import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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
	
	private List<String> paragraphs;
	
	private String yazarAdi;
	
	public KoseYazisi(){
		
	}
	
	public KoseYazisi(String baslik, String koseYazisiLink, String koseYaziriAdi) throws IOException, ParseException{
		setBaslik(baslik);
		setKoseYazisiLink(koseYazisiLink);
		setYazarAdi(koseYaziriAdi);
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
		try {
			setParagraphs(new ArrayList<String>());
			
			Document doc = Jsoup.connect(getKoseYazisiLink()).timeout(HURRIYET.timeout).get();
			
			Elements contents = doc.select("script[type=text/javascript]");
			
			Element content = contents.get(44);
			
			String wholeText = content.childNode(0).toString();
			
			String myContentText = wholeText.substring(wholeText.indexOf("aContent") + 12, wholeText.indexOf("addDate") - 9);
			
			String date = wholeText.substring(wholeText.indexOf("addDate") + 11, wholeText.indexOf("addDate") + 30);
			
			Document contetimDoc = Jsoup.parse(myContentText);
			
			setKoseYazisi(contetimDoc.text());
			
			Elements elements = contetimDoc.select("p");
			
			for(Element el:elements){
				getParagraphs().add(el.text());
			}
			
			setTarih(DateUtils.getTarih(date, getKoseYazisiLink()));
		} catch (Exception e) {
			setKoseYazisi("Couldn't fetched!");
			
			setTarih(null);
		}
		
	}

	public String getYazarAdi() {
		return yazarAdi;
	}

	@Override
	public String toString() {
		return "KoseYazisi [tarih=" + tarih + ", baslik=" + baslik
				+ ", koseYazisiLink=" + koseYazisiLink + ", koseYazisi="
				+ koseYazisi + ", yazarAdi=" + yazarAdi + "]";
	}

	public void setYazarAdi(String yazarAdi) {
		this.yazarAdi = yazarAdi;
	}

	public void setKoseYazisi(String koseYazisi) {
		this.koseYazisi = koseYazisi;
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<String> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
