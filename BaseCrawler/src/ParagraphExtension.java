import java.util.ArrayList;
import java.util.List;


/**
 * @author ergindoganyildiz
 * 
 * Dec 18, 2015
 */
public class ParagraphExtension {
	
	public static final String delimeter = "_______";

	public static String getParagraphString(List<String> paragraphs, String passage){
		String text = "";
		
		if(!paragraphs.isEmpty()){
			for(int i = 0; i < paragraphs.size(); i++){
				String paragraph = paragraphs.get(i);
				
				if(!paragraph.startsWith("*") && !paragraph.isEmpty()){
					text = text + paragraph + delimeter;
				}
			}
			
			if(!text.isEmpty()){
				text = text.substring(0, text.length() - delimeter.length());
			}
			
		}else{
			text = passage;
		}
		
		return text;
	}
	
	
	public static void main(String args[]){
		String passage1 = "asdhbaksdvasd";
		
		List<String> paragraphs = new ArrayList<String>();
		paragraphs.add(passage1);
		
		System.out.println();
		
		System.out.println(getParagraphString(paragraphs, passage1).split(delimeter)[0]);
	}

}
