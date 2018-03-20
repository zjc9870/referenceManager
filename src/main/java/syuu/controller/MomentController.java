package syuu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/moment")
public class MomentController {
    @RequestMapping("/manager")
    public ModelAndView manager(){
        ModelAndView mv = new ModelAndView("/moment/momentManager");
        return mv;
    }
}
