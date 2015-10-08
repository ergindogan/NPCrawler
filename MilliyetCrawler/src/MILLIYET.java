import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.i2i.common.utils.threadpool.base.ThreadPool;



/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class MILLIYET {
	
	public static int fetcherCount = 10;
	public static int pagesToCrawl = 20;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 20000;
	
	public static void main(String args[]){
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/MilliyetCrawler.txt"), false);
		
		List<MILLIYETFetcher> fetchers = new ArrayList<MILLIYETFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 0; id < fetcherCount; id++) {
			try {
				MILLIYETFetcher fetcher = new MILLIYETFetcher(id);
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
