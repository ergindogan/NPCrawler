import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.i2i.common.utils.threadpool.base.ThreadPool;



/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class HURRIYET {
	
	public static final int fetcherCount = 12;
	public static int yearsToCrawl = 1;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 20000;
	
	public static void main(String args[]) throws ClientProtocolException, IOException{
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/HurriyetCrawler.txt"), false);
		
		List<HURRIYETFetcher> fetchers = new ArrayList<HURRIYETFetcher>();
		
		login();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 1; id <= fetcherCount; id++) {
			try {
				HURRIYETFetcher fetcher = new HURRIYETFetcher(id);
				fetchers.add(fetcher);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		ThreadPool fetcherPool = new ThreadPool("", fetchers);
		fetcherPool.setInterval(15);
		fetcherPool.start();
		
	}
	
	public static void login() throws ClientProtocolException, IOException{
		String url = "http://auth.hurriyet.com.tr/api/loginuser/yildizergindogan@gmail.com/?%3D%3E%3F89%3A&_=40558625487";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response1 = httpclient.execute(httpGet);


        try {
//            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();

            BufferedReader rd = new BufferedReader(new InputStreamReader(entity1.getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                        result.append(line);
                }

//                System.out.println(result.toString());

            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
	}

}
