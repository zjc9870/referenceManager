package syuu.util;

import syuu.dataObject.Reference;

public class StringUtil {
    public static String  getKeywordsByDblpStr(String dblpStr){
        String authors = "";
        for(int i=0;i<dblpStr.length();i++){
            if(dblpStr.charAt(i)==':'){
                authors = dblpStr.substring(0,i);
                dblpStr = dblpStr.substring(i+1,dblpStr.length()-1).trim();
                System.out.println(authors);
                break;
            }
        }
        String title="";
        for(int i=0;i<dblpStr.length();i++){
            if(dblpStr.charAt(i)=='.'){
                title = dblpStr.substring(0,i);
                dblpStr = dblpStr.substring(i+1,dblpStr.length()-1).trim();
                System.out.println(title);
                System.out.println(dblpStr);
                break;
            }
        }

        String keywords = authors+": "+title;
        return keywords;
    }
}
