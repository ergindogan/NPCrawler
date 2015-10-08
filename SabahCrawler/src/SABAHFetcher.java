import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


public class SABAHFetcher extends PooledMessageWorker {
	
	private int id;

	private static final Logger logger = Logger.getLogger(SABAHFetcher.class);
	
	public SABAHFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		try {
			for (KoseYazari koseYazari : SABAH.yazarlar) {
				List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, SABAH.pagesToCrawl / SABAH.fetcherCount, id);
				
				for (KoseYazisi koseYazisi : koseYazilari) {
					System.out.println("Id : " + id + " Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
					String oneRecord = Utils.getOneRecord(koseYazisi);
					logger.info(oneRecord);
					logger.info("------------------------------------------------------------------------------------------ \n");
				}
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
