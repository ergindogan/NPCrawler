import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.i2i.common.utils.threadpool.base.ThreadPool;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class TAKVIM {
	
	public static int fetcherCount = 1;
	public static int sayfaSayisi = 40;
	public static Map<Integer,KoseYazari> yazarlar = new HashMap<Integer,KoseYazari>();
	public static int timeout = 30000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/TakvimCrawler.txt"), false);
		
		List<TAKVIMFetcher> fetchers = new ArrayList<TAKVIMFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		fetcherCount = yazarlar.size();
		
		for (int id = 0; id < fetcherCount; id++) {
			try {
				TAKVIMFetcher fetcher = new TAKVIMFetcher(id);
				fetchers.add(fetcher);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		ThreadPool fetcherPool = new ThreadPool("", fetchers);
		fetcherPool.setInterval(2000);
		fetcherPool.start();
	}

}
