import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class TAKVIMFetcher extends PooledMessageWorker {
	
	private int id;
	private int retreivedPassages = 0;

	private static final Logger logger = Logger.getLogger(TAKVIMFetcher.class);
	
	public TAKVIMFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		
		List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(TAKVIM.yazarlar.get(id), TAKVIM.sayfaSayisi);
		for (KoseYazisi koseYazisi : koseYazilari) {
			String oneRecord = Utils.getOneRecord(koseYazisi);
			logger.warn(oneRecord);
			logger.warn("------------------------------------------------------------------------------------------ \n");
			retreivedPassages++;
		}
			
		System.out.println(TAKVIM.yazarlar.get(id).getKoseYazariAdi() + " fetched..." + retreivedPassages + " passages retrieved!.");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
