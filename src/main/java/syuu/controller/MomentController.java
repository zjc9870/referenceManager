package syuu.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import syuu.service.MomentService;
import syuu.service.ReferenceService;
import syuu.service.UserService;
import syuu.service.VO.CommentVo;
import syuu.service.VO.MomentVo;
import syuu.service.VO.ReferenceVo;
import syuu.service.VO.UserVo;

import java.util.*;

@Controller
@RequestMapping("/moment")
public class MomentController {
    @Autowired
    UserService userService;
    @Autowired
    MomentService momentService;
    @Autowired
    ReferenceService referenceService;

    @RequestMapping("/manager")
    public ModelAndView manager(String tabId){
        ModelAndView mv = new ModelAndView("/moment/momentManager");
        List<MomentVo> momentVoList = momentService.getMoments(userService.getLoginUser());
        mv.addObject("tabId",tabId);
        mv.addObject("momentVoList",momentVoList);
        return mv;
    }

    @RequestMapping("/getBlockUserList")
    @ResponseBody
    public Map<String,Object> getBlockUserList(String[] blockIdList){
        List<UserVo> blockUserList = new ArrayList<UserVo>();
        if(blockIdList!=null){
            for(int i=0;i<blockIdList.length;i++){
                blockUserList.add(userService.getUserById(blockIdList[i]));
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("blockUserList",blockUserList);
        return map;
    }

    @RequestMapping("/saveMoment")
    @ResponseBody
    public Map<String,Object> saveMoment(String content,String[] blockIdList,String[] referenceIdList){
        UserVo user = userService.getLoginUser();
        List<UserVo> blockList = new ArrayList<UserVo>();
        List<ReferenceVo> referenceVoList = new ArrayList<ReferenceVo>();
        if(blockIdList!=null){
            for(int i=0;i<blockIdList.length;i++){
                String userId = blockIdList[i].replaceAll("friend","");
                blockList.add(userService.getUserById(userId));
            }
        }
        if(referenceIdList!=null){
            for(int i=0;i<referenceIdList.length;i++){
                String userId = referenceIdList[i].replaceAll("reference","");
                referenceVoList.add(referenceService.getReferenceById(userId));
            }
        }
        MomentVo momentVo = new MomentVo(0,content,new Date(),user,blockList,referenceVoList,new ArrayList<CommentVo>());
        momentService.savenNewMoment(momentVo);
        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }
}
