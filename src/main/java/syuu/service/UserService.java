package syuu.service;

import org.hibernate.Session;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import syuu.configuration.WebSecurityConfig;
import syuu.dataObject.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import syuu.repository.UserRepository;
import syuu.service.VO.UserVo;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession session;
    @Autowired
    FriendService friendService;
    /**
     * 获取所有
     * @return
     */
     public String verifyLogin(String username, String password){
         List<User> userList = userRepository.findAll();
         String result = "用户不存在";
         if(userList.size()>0){
             for(User user:userList){
                 if(user.getLoginname().equals(username)){
                     if(user.getPassword().equals(password)){
                         result = String.valueOf(user.getId());
                     }else{
                         result = "密码错误";
                     }
                 }
             }
         }
         return result;
     }

    public UserVo getLoginUser() {
         String userid = (String) session.getAttribute(WebSecurityConfig.SESSION_KEY);
         UserVo uservo = getUserById(userRepository.findOne(Integer.valueOf(userid)).getId());
         return uservo;
    }

    public List<UserVo> searchByUsername(String username) {
         List<User>userList = userRepository.findByUsername(username);
         List<UserVo> userVoList = new ArrayList<UserVo>();
         for(User user:userList){
             userVoList.add(new UserVo(user));
         }
         return userVoList;
    }

    public UserVo getUserById(String id) {
        UserVo userVo = new UserVo(userRepository.findOne(Integer.valueOf(id)));
        userVo.setFriendList(friendService.getFriendByUser(userVo));
        return userVo;
    }

    public UserVo getUserById(int id) {
        UserVo userVo = new UserVo(userRepository.findOne(id));
        userVo.setFriendList(friendService.getFriendByUser(userVo));
        return userVo;
    }
}
