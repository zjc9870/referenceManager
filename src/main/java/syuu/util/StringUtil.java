package syuu.util;

import syuu.dataObject.Reference;

public class StringUtil {
    public static String  getKeywordsByDblpStr(String dblpStr){
        String authors = getAuthorByDblpStr(dblpStr);

        String title=getTitleByDblpStr(dblpStr);

        String keywords = authors+": "+title;
        return keywords;
    }

    public static String getAuthorByDblpStr(String dblpStr) {
        String authors = "";
        for(int i=0;i<dblpStr.length();i++){
            if(dblpStr.charAt(i)==':'){
                authors = dblpStr.substring(0,i);
                dblpStr = dblpStr.substring(i+1,dblpStr.length()-1).trim();
                System.out.println(authors);
                break;
            }
        }
        return authors;
    }

    public static String getTitleByDblpStr(String dblpStr){
        String authors = getAuthorByDblpStr(dblpStr);
        dblpStr = dblpStr.substring(authors.length(),dblpStr.length()-1).trim();
        String title = "";
        for(int i=0;i<dblpStr.length();i++){
            if(dblpStr.charAt(i)=='.'){
                title = dblpStr.substring(0,i);
                dblpStr = dblpStr.substring(i+1,dblpStr.length()-1).trim();
                System.out.println(title);
                System.out.println(dblpStr);
                break;
            }
        }
        return title;
    }

    public static String getYearByDblpStr(String dblpStr) {
        String year = "";
        if(dblpStr.charAt(dblpStr.length()-1)==')'&&dblpStr.charAt(dblpStr.length()-6)=='('){
            year = dblpStr.substring(dblpStr.length()-5,dblpStr.length()-1);
        }else{
            return "年份无法导入";
        }
        return year;
    }

    public static String[] getPaginationByDblpStr(String dblpStr) {
        String[] result = {"1","2"};
        return result;
    }
}
