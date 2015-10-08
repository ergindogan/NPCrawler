import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;



public class SOZCUFetcher extends PooledMessageWorker {
	
	private int id;
	private Calendar dateToFetch = Calendar.getInstance();

	private static final Logger logger = Logger.getLogger(SOZCUFetcher.class);
	
	public SOZCUFetcher(int id){
		setId(id);
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
			List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, dateToFetch);
			
			for (KoseYazisi koseYazisi : koseYazilari) {
				System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
				String oneRecord = Utils.getOneRecord(koseYazisi);
				logger.info(oneRecord);
				logger.info("------------------------------------------------------------------------------------------ \n");
			}
			
		}
			
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
