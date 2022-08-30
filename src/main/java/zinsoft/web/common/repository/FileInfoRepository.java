package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    List<FileInfo> findByFileSeqInAndStatusCd(List<Long> fileSeqList, String statusCd);

    FileInfo findByFileSeqAndStatusCd(Long fileSeq, String statusCd);

}
