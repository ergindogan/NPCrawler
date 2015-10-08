import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HURRIYET_ESKI {

	private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String args[]) throws Exception{

    	HURRIYET_ESKI crawler = new HURRIYET_ESKI();
    	crawler.sendGetRequest();
    }


    private void sendGetRequest() throws Exception{
            String url = "http://auth.hurriyet.com.tr/api/loginuser/yildizergindogan@gmail.com/?%3D%3E%3F89%3A&_=40558625487";

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response1 = httpclient.execute(httpGet);


            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();

                BufferedReader rd = new BufferedReader(new InputStreamReader(entity1.getContent()));

                    StringBuffer result = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                            result.append(line);
                    }

                    System.out.println(result.toString());

                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }
            
//            http://hurarsiv.hurriyet.com.tr/yazarlar/
            
            Document doc;
    		try {
    			
    			//--------------------
//    			doc = Jsoup.connect("http://hurarsiv.hurriyet.com.tr/yazarlar/default.aspx?ID=131").get();
//    			
////    			Element content = doc.select("div[class=hsa-articles row h-shadow clearfix]").first();
////    			
////    			Element href = content.getElementsByAttribute("href").first();
//    			
////    			System.out.println(doc.text());
//    			
//    			Element content = doc.select("ul[class=hsaca-list h-archive-article-list]").first();
//    			
//    			Elements links = content.getElementsByTag("a");
//    			for (Element link : links) {
//    				String value = link.attr("href");
//    				String linkText = link.text();
//    				if(!value.equals("#")){
//    					System.out.println("Baslik : " + linkText + " Link : " + value );
//    				}
//    				
//    			}
    			//-------------------------
    			
//    			doc = Jsoup.connect("http://hurarsiv.hurriyet.com.tr/Yazarlar/Default.aspx?ID=131&ay=7&yil=2015&p=1").get();
//    			
//    			Element content = doc.select("ul[class=hsaca-list h-archive-article-list]").first();
//    			
//    			Elements links = content.getElementsByTag("a");
//    			for (Element link : links) {
//    				String value = link.attr("href");
//    				String linkText = link.text();
//    				if(!value.equals("#")){
//    					System.out.println("Baslik : " + linkText + " Link : " + value );
//    				}
//    				
//    			}

        			

    			
    			doc = Jsoup.connect("http://sosyal.hurriyet.com.tr/yazar/ahmet-hakan_131/cubbeli-ye-yedi-tavsiye_29588057").get();
    			
    			Elements contents = doc.select("script[type=text/javascript]");
    			
    			Element content = contents.get(43);
    			
    			String textContent = content.childNode(0).toString();
    			
    			String title = textContent.substring(textContent.indexOf("aTitle") + 10, textContent.indexOf("aTitleShort") - 9);
    			
    			String contentim = textContent.substring(textContent.indexOf("aContent") + 12, textContent.indexOf("addDate") - 9);
    			
    			Document contetimDoc = Jsoup.parse(contentim);
    			
//    			System.out.println(contetimDoc.text());
    			
    			String date = textContent.substring(textContent.indexOf("addDate") + 11, textContent.indexOf("addDate") + 30);
    			
    			System.out.println(date);
    			
//    			System.out.println(textContent);
    			
    			
    			
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}

            
    }

}

