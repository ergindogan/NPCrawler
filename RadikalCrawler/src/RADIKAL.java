import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.i2i.common.utils.threadpool.base.ThreadPool;


/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class RADIKAL {
	
	public static int fetcherCount = 30;
	public static int sayfaSayisi = 50;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 30000;
	
	public static void main(String args[]){
		
//		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/RadikalCrawl.txt"), false);
		
		List<RADIKALFetcher> fetchers = new ArrayList<RADIKALFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 0; id < fetcherCount; id++) {
			try {
				RADIKALFetcher fetcher = new RADIKALFetcher(id);
				fetchers.add(fetcher);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		ThreadPool fetcherPool = new ThreadPool("", fetchers);
		fetcherPool.setInterval(15);
		fetcherPool.start();
		
	}

}
