import java.util.Calendar;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;



public class SOZCUFetcher extends PooledMessageWorker {
	
	private int id;
	private Calendar dateToFetch = Calendar.getInstance();
	private HttpClient httpClient;

	private static final Logger logger = Logger.getLogger(SOZCUFetcher.class);
	
	public SOZCUFetcher(int id, HttpClient httpClient){
		setId(id);
		setHttpClient(httpClient);
	}
	
	@Override
	public void run() {
		
		
		
		for(int i = 0; i < SOZCU.to.get(Calendar.YEAR) - SOZCU.from.get(Calendar.YEAR); i++){
			int year = SOZCU.to.get(Calendar.YEAR);
			
			if(id > 12 * i && id <= 12 * (i+1) ){
				dateToFetch.set(year - i, id - (i * 12), 1,0,0,0);
			}
		}
		
		
		for (KoseYazari koseYazari : SOZCU.yazarlar) {
			List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(getHttpClient(), koseYazari, dateToFetch);
			
			for (KoseYazisi koseYazisi : koseYazilari) {
				System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
				String oneRecord = Utils.getOneRecord(koseYazisi);
				logger.warn(oneRecord);
				logger.warn("------------------------------------------------------------------------------------------ \n");
			}
			
		}
			
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
