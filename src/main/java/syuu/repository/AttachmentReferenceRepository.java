package syuu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syuu.dataObject.Attachment;
import syuu.dataObject.AttachmentReference;

import java.util.List;

public interface AttachmentReferenceRepository extends JpaRepository<AttachmentReference,Integer> {
    List<AttachmentReference> findByAttachmentId(int attachmentId);
    List<AttachmentReference> findByReferenceId(int referenceId);
}
