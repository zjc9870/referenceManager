package syuu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import syuu.service.SearchService;
import syuu.service.VO.ReferenceVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;
    @RequestMapping("/searchPage")
    public ModelAndView searchPage(){
        ModelAndView mv = new ModelAndView("/search/search");
        return mv;
    }

    @RequestMapping("/getSearchResult")
    @ResponseBody
    public Map<String,Object> getSearchResult(String keywords) throws IOException {
        List<ReferenceVo> resultList = searchService.getResultByKeywords(keywords,0);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("resultList",resultList);
        return map;
    }

    @RequestMapping("/getNextPage")
    @ResponseBody
    public Map<String,Object> getSearchResult(String keywords,String currentPage) throws IOException {
        List<ReferenceVo> resultList = searchService.getResultByKeywords(keywords,Integer.valueOf(currentPage));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("resultList",resultList);
        map.put("currentPage",currentPage);
        return map;
    }
}
