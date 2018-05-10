package syuu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import syuu.dataObject.*;
import syuu.repository.AttachmentReferenceRepository;
import syuu.repository.ReferenceRepository;
import syuu.repository.ResearchRepository;
import syuu.repository.UserRepository;
import syuu.service.VO.AttachmentVo;
import syuu.service.VO.ReferenceVo;
import syuu.service.VO.ResearchVo;
import syuu.service.VO.UserVo;
import syuu.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResearchService {

    @Autowired
    ResearchRepository researchRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReferenceRepository referenceRepository;

    @Autowired
    ReferenceService referenceService;
    @Autowired
    SearchService searchService;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    AttachmentReferenceRepository attachmentReferenceRepository;
    @Autowired
    StyleService styleService;

    public List<ResearchVo> getReseachByUser(int userId){
        User user = userRepository.findOne(userId);
        List<Research> result = researchRepository.getResearchByUser(user);
        List<ResearchVo> researchVo = convert(result);
        return researchVo;

    }

    private List<ResearchVo> convert(List<Research> result) {
        ArrayList<ResearchVo> voList = new ArrayList<ResearchVo>();
        for(Research research:result){
            ResearchVo vo = new ResearchVo(research);
            vo.setReferenceVoList(referenceService.getReferenceOfResearch(vo.getId()));
            voList.add(vo);
        }
        return voList;
    }

    public ResearchVo getReseachById(int id) {
        Research research = researchRepository.findOne(id);
        ResearchVo researchVo = new ResearchVo(research);
        return researchVo;
    }

    public String saveNewResearch(String researchName, String[] referenceIdList, UserVo loginUser) {
        Research research = new Research();
        research.setName(researchName);
        research.setUser(userRepository.findOne(loginUser.getId()));
        research = researchRepository.save(research);
        for(int i=0;i<referenceIdList.length;i++){
            Reference reference = referenceRepository.findOne(Integer.valueOf(referenceIdList[i]));
            copyReferenceToResearch(reference,research);
        }
        return String.valueOf(research.getId());
    }

    public void copyReferenceToResearch(Reference reference,Research research){
        Reference reference1 = new Reference(new ReferenceVo(reference));
        reference1.setId(0);
        reference1.setResearch(research);
        List<AttachmentVo> attachmentList = attachmentService.getAttachmentByXgId(String.valueOf(reference.getId()));

        reference1 = referenceRepository.save(reference1);

        for(AttachmentVo attachment:attachmentList){
            AttachmentReference attachmentReference = new AttachmentReference();
            attachmentReference.setAttachmentId(attachment.getId());
            attachmentReference.setReferenceId(reference1.getId());
            attachmentReferenceRepository.save(attachmentReference);
        }
    }

    public void deleteResearch(String researchId) {
        researchRepository.delete(Integer.valueOf(researchId));
    }

    public void saveReferenceToResearch(String researchId, String[] referenceIdList) {
        Research research = researchRepository.findOne(Integer.valueOf(researchId));
        for(int i=0;i<referenceIdList.length;i++){
            Reference reference = referenceRepository.findOne(Integer.valueOf(referenceIdList[i]));
            copyReferenceToResearch(reference,research);
        }
    }

    public boolean saveNewReferenceToResearch(String researchId, String dblpStr) throws IOException {
        String keywords = StringUtil.getKeywordsByDblpStr(dblpStr);
        ReferenceVo referenceVo = new ReferenceVo();
        boolean result = false;
        List<ReferenceVo> referenceVoList = searchService.getResultByKeywords(keywords,0);
        if(referenceVoList.size()!=0){
            referenceVo = referenceVoList.get(0);
            referenceVo.setId(0);
            referenceVo.setResearchId(Integer.valueOf(researchId));
            Reference reference = referenceService.saveReference(referenceVo);
            result = true;
        }else{
            styleService.convertDblpStr(dblpStr);
        }
        return result;
    }
}
