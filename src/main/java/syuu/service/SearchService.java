package syuu.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import syuu.service.VO.ReferenceVo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SearchService {
    public List<ReferenceVo> getResultByKeywords(String keywords,int pageCount) throws IOException {
        String url = getUrl(keywords);
        List<ReferenceVo> referenceVoList = run(url,pageCount);
        return referenceVoList;
    }

    public List<ReferenceVo> run(String url,int pageCount) throws IOException {
        List<ReferenceVo> referenceVoList = new ArrayList<ReferenceVo>();
        Elements elements = getAllContent(url,pageCount);
        for(Element element:elements){
            ReferenceVo referenceVo = new ReferenceVo();
            //添加下载部分
            Elements hrefs = element.select("a:matches((?i)electronic edition)");
            String downloadUrl = "";
            downloadUrl += hrefs.get(0).attr("href");
            //添加字段部分
            Elements datas = element.select("div.data");
            if(datas.size()>=1){
                referenceVo = parse(datas.get(0));
                referenceVo.setDblpStr(datas.get(0).text());
            }
            referenceVo.setDownloadUrl(downloadUrl);
            referenceVoList.add(referenceVo);
        }
        System.out.println(referenceVoList.size());
        return referenceVoList;
    }

    private Elements getAllContent(String url,int pageCount) {
        Elements allElements = new Elements();
        int pageSize = 30;
        String newUrl = url+"&h="+pageSize+"&f="+pageCount*pageSize+"&s=yvpc";
        System.out.println(newUrl);
        try{
            Document document =  Jsoup.parse(new URL(newUrl),60000);
            Elements elements = document.select("li.entry");
            allElements.addAll(elements);
//            int size = elements.size();
//            if(size==pageSize){
//                pageCount++;
//            }
        }catch(Exception e){
            return allElements;
        }
        return allElements;
    }


    public static ReferenceVo parse(Element element){
        //从前遍历找:
        ReferenceVo referenceVo = new ReferenceVo();
        String[] fullText = element.text().split(" ");
        referenceVo.setAuthors("");
        for(Element authorNode:element.select("[itemprop='author']")){
            referenceVo.setAuthors(referenceVo.getAuthors()+authorNode.select("[itemprop='name']").text()+",");
        }
        if(!referenceVo.getAuthors().equals("")){
            if(referenceVo.getAuthors().charAt(referenceVo.getAuthors().length()-1)==','){
                referenceVo.setAuthors(referenceVo.getAuthors().substring(0,referenceVo.getAuthors().length()-1));
            }
        }
        referenceVo.setName(element.select("[class='title']").text());
        referenceVo.setYear(Integer.valueOf(element.select("[itemprop='datePublished']").text()));
        String pagination = element.select("[itemprop='pagination']").text();
        if(pageValidation(pagination)){
            String[] pages = pagination.split("-");
            referenceVo.setBeginPage(Integer.valueOf(pages[0]));
            referenceVo.setEndPage(Integer.valueOf(pages[1]));
        }else{
            for(int i = 1;i<fullText.length;i++){
                if(fullText[i].contains("-")){
                    if(!fullText[i-1].contains("ISBN")){
                        String[] numbers = fullText[i].split("-");
                        if(numbers.length>=2){
                            if(isNumeric(numbers[0])&&isNumeric(numbers[1])){
                                pagination=numbers[0]+"-"+numbers[1];
                                break;
                            }
                        }
                    }
                }
            }
            if(pageValidation(pagination)){
                String[] pages = pagination.split("-");
                referenceVo.setBeginPage(Integer.valueOf(pages[0]));
                referenceVo.setEndPage(Integer.valueOf(pages[1]));
            }
        }
        referenceVo.setIsPartOf(element.select("[itemprop='isPartOf']").text());
        if(referenceVo.getIsPartOf()==null){
            String isbn=element.select("[itemprop='isbn']").text();
            referenceVo.setIsPartOf(isbn);
            String publisher=element.select("[itemprop='publisher']").text();
            if(!publisher.equals("")){
                referenceVo.setIsPartOf(publisher);
            }
        }

        return referenceVo;
    }

    private static boolean pageValidation(String pagination) {
        if(pagination.equals("")){
            return false;
        }else if(!pagination.contains("-")){
            return false;
        }else{
            boolean result = true;
            String[] numbers = pagination.split("-");
            if(numbers.length<2){
                return false;
            }
            result = isNumeric(numbers[0].trim())&&isNumeric(numbers[1].trim());
            return result;
        }
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }



    //获取url
    private String getUrl(String keywords){
        String[] keywordsList = keywords.split(" ");
        String searchString = "";
        for(int i=0;i<keywordsList.length;i++){
            searchString += keywordsList[i];
            if(i!=keywordsList.length-1){
                searchString+="+";
            }
        }
        String url = "http://dblp.uni-trier.de/search/publ/inc?q="+searchString;
        return url;
    }
}


