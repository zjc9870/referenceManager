package syuu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import syuu.dataObject.Moment;
import syuu.dataObject.Reference;
import syuu.dataObject.User;
import syuu.repository.MomentRepository;
import syuu.repository.ReferenceRepository;
import syuu.repository.UserRepository;
import syuu.service.VO.CommentVo;
import syuu.service.VO.MomentVo;
import syuu.service.VO.ReferenceVo;
import syuu.service.VO.UserVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MomentService {

    @Autowired
    MomentRepository momentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReferenceRepository referenceRepository;
    public List<MomentVo> getMomentByUser(UserVo user) {
        List<Moment> momentList = momentRepository.findMomentByUser(userRepository.findOne(user.getId()));
        List<MomentVo> momentVoList = new ArrayList<MomentVo>();
        if(momentList!=null){
            for(Moment moment:momentList){
                int id = moment.getId();
                String content = moment.getContent();
                Date time = moment.getTime();
                List<UserVo> blockList = new ArrayList<UserVo>();
                if(moment.getBlockList()!=null){
                    for(User blockUser:moment.getBlockList()){
                        blockList.add(new UserVo(blockUser));
                    }
                }
                List<ReferenceVo> referenceVoList = new ArrayList<ReferenceVo>();
                if(moment.getReference()!=null){
                    for(Reference reference:moment.getReference()){
                        referenceVoList.add(new ReferenceVo(reference));
                    }
                }
                List<CommentVo> commentVoList = new ArrayList<CommentVo>();
                UserVo userVo = new UserVo(userRepository.findOne(user.getId()));
                MomentVo momentVo = new MomentVo(id,content,time,user,blockList,referenceVoList,commentVoList);
                momentVoList.add(momentVo);
            }
        }
        return momentVoList;
    }
    //获取该用户应该看到的全部朋友圈
    public List<MomentVo> getMoments(UserVo user){
        List<MomentVo> momentVoList = new ArrayList<MomentVo>();
        if(user.getFriendList()!=null){
            for(UserVo friendVo:user.getFriendList()){
                momentVoList.addAll(getMomentByUser(friendVo));
            }
        }
        momentVoList.addAll(getMomentByUser(user));
        return momentVoList;
    }

    public void savenNewMoment(MomentVo momentVo) {
        Moment moment = new Moment();
        moment.setId(momentVo.getId());
        moment.setContent(momentVo.getContent());
        moment.setTime(new Date());
        List<User> blockList = new ArrayList<User>();
        List<Reference> referenceList = new ArrayList<Reference>();
        for(UserVo userVo:momentVo.getBlockList()){
            blockList.add(userRepository.findOne(userVo.getId()));
        }
        for(ReferenceVo referenceVo:momentVo.getReferenceVoList()){
            referenceList.add(referenceRepository.findOne(referenceVo.getId()));
        }
        moment.setBlockList(blockList);
        moment.setReference(referenceList);
        moment.setUser(userRepository.findOne(momentVo.getUser().getId()));
        momentRepository.save(moment);
    }
}
