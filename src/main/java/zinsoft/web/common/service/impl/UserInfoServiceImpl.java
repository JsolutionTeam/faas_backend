package zinsoft.web.common.service.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sillasystem.common.EncryptUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.common.entity.UserInfo;
import zinsoft.web.common.repository.UserInfoRepository;
import zinsoft.web.common.service.EmailService;
import zinsoft.web.common.service.UserAccessLogService;
import zinsoft.web.common.service.UserInfoExtService;
import zinsoft.web.common.service.UserInfoHistService;
import zinsoft.web.common.service.UserInfoService;
import zinsoft.web.common.service.UserRoleService;
import zinsoft.web.exception.CodeMessageException;
import zinsoft.web.security.WebUser;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl extends EgovAbstractServiceImpl implements UserInfoService {

    //private Type dtoListType = new TypeToken<List<UserInfoDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserInfoRepository userInfoRepository;

    @Resource
    UserInfoExtService userInfoExtService;

    @Resource
    EmailService emailService;

    @Resource
    UserInfoHistService userInfoHistService;

    @Resource
    UserRoleService userRoleService;

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void insert(String workerId, UserInfoDto dto) throws CodeMessageException {
        /*
        if (dto.getStatusCd() == null || dto.getStatusCd().isEmpty()) {
            dto.setStatusCd(UserInfoDto.STATUS_CD_NORMAL);
        }

        if (StringUtils.isNotBlank(dto.getUserPwd())) {
            if (!CommonUtil.isValidPassword(dto.getUserPwd())) {
                throw new CodeMessageException(Result.INVALID_PASSWORD);
            }

            dto.setUserPwd(CommonUtil.sha256(dto.getUserPwd()));
        }

        if (StringUtils.isNotBlank(dto.getMobileNum())) {
            dto.setMobileNum(dto.getMobileNum().replaceAll("\\D", ""));
        }

        userInfoRepository.save(modelMapper.map(dto, UserInfo.class));
        userInfoHistService.insert(workerId, Constants.WORK_CD_INSERT, dto.getUserId());

        if (dto.getUserRoleList() != null && !dto.getUserRoleList().isEmpty()) {
            userRoleService.insert(workerId, dto.getUserId(), dto.getUserRoleList());
        } else {
            userRoleService.insert(workerId, dto.getUserId(), AppPropertyUtil.get(Constants.DEFAULT_ROLE_ID));
        }

        userInfoExtService.insert(dto);
        */
    }

    @Override
    public void insert(String workerId, UserInfoDto dto, String baseUri) throws CodeMessageException {
        insert(workerId, dto);

        if (baseUri != null) {
            emailService.send(dto.getEmailAddr(), "join.html", AppPropertyUtil.get(Constants.MAIL_SUBJECT_JOIN), dto, baseUri);
        }
    }

    @Override
    public DataTablesResponse<UserInfoDto> page(Map<String, Object> search, Pageable pageable) {
        DataTablesResponse<UserInfoDto> page = DataTablesResponse.of(userInfoRepository.page(search, pageable));
        List<UserInfoDto> list = page.getItems();

        for (UserInfoDto dto : list) {
            // 신라시스템 암호화 데이터 복호화
            try {
                EncryptUtil decryptUtil = new EncryptUtil();
                dto.setEmailAddr(decryptUtil.decrypt(dto.getEmailAddr()));
                dto.setMobileNum(decryptUtil.decrypt(dto.getMobileNum()));
                dto.setTelNum(decryptUtil.decrypt(dto.getTelNum()));
            } catch (Exception e) {
            }
        }
        return page;
    }

    @Override
    public List<UserInfoDto> list(Map<String, Object> search) {
        List<UserInfoDto> list = userInfoRepository.list(search);

        for (UserInfoDto dto : list) {
            // 신라시스템 암호화 데이터 복호화
            try {
                EncryptUtil decryptUtil = new EncryptUtil();
                dto.setEmailAddr(decryptUtil.decrypt(dto.getEmailAddr()));
                dto.setMobileNum(decryptUtil.decrypt(dto.getMobileNum()));
                dto.setTelNum(decryptUtil.decrypt(dto.getTelNum()));
            } catch (Exception e) {
            }
        }

        return list;
    }

    @Override
    public List<String> listUserId(Map<String, Object> search) {
        return userInfoRepository.listUserId(search);
    }

    @Override
    public List<UserInfoDto> listByRoleId(String roleId) {
        Map<String, Object> search = new HashMap<>();
        search.put("roleId", roleId);
        return list(search);
    }

    @Override
    public UserInfoDto get(String userId) {
        UserInfo userInfo = null;

        try {
            userInfo = getEntity(userId);
        } catch (CodeMessageException cme) {
            return null;
        }

        UserInfoDto dto = modelMapper.map(userInfo, UserInfoDto.class);

        // 신라시스템 암호화 데이터 복호화
        try {
            EncryptUtil decryptUtil = new EncryptUtil();
            dto.setEmailAddr(decryptUtil.decrypt(dto.getEmailAddr()));
            dto.setMobileNum(decryptUtil.decrypt(dto.getMobileNum()));
            dto.setTelNum(decryptUtil.decrypt(dto.getTelNum()));
        } catch (Exception e) {
        }

        List<UserRoleDto> userRoleList = dto.getUserRoleList();
        List<String> userRoleIdList = new ArrayList<>();

        for (UserRoleDto userRole : userRoleList) {
            userRoleIdList.add(userRole.getRoleId());
        }

        dto.setUserRoleIdList(userRoleIdList);
        dto = userInfoExtService.get(dto);

        return dto;
    }

    @Override
    public boolean findPwd(String userId, String userNm, String mobileNum, String baseUri) throws CodeMessageException {
        UserInfoDto dto = get(userId);

        if (!dto.getUserNm().equals(userNm) || !dto.getMobileNum().replaceAll("\\D", "").equals(mobileNum.replaceAll("\\D", ""))) {
            return false;
        }

        String tempPwd = UUID.randomUUID().toString().substring(0, 9);

        dto.setUserPwd(tempPwd);
        dto.setUserRoleList(null);
        update("*findPwd", dto, false);

        dto.setUserPwd(tempPwd); // 위 update 메소드 실행시 비밀번호가 암호화 되므로 다시 셋팅
        emailService.send(dto.getEmailAddr(), "find_pwd", AppPropertyUtil.get(Constants.MAIL_SUBJECT_FIND_PWD), dto, baseUri);
        return true;
    }

    @Override
    public void login(String userId, HttpSession session, String remoteAddr) {
        UserInfo userInfo = getEntity(userId);

        // 최종 로그인 시간 업데이트
        userInfo.setLastLoginDtm(new Date());
        userInfoRepository.save(userInfo);

        // 로그인 로그 추가
        userAccessLogService.login(userId);

        UserInfoDto dto = modelMapper.map(userInfo, UserInfoDto.class);

        dto.setRemoteAddr(remoteAddr);

        List<UserRoleDto> userRoleList = dto.getUserRoleList();
        List<GrantedAuthority> authList = new ArrayList<>();

        for (UserRoleDto userRole : userRoleList) {
            authList.add(new SimpleGrantedAuthority(userRole.getRoleId()));
        }

        UserDetails userDetails = new WebUser(dto, authList);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

    @Override
    public void update(String workerId, UserInfoDto dto) throws CodeMessageException {
        update(workerId, dto, true);
    }

    private int update(String workerId, UserInfoDto dto, boolean checkValidPassword) throws CodeMessageException {
        /*
        UserInfo userInfo = getEntity(dto.getUserId());

        if (dto.getCurUserPwd() != null && !userInfo.getUserPwd().equals(CommonUtil.sha256(dto.getCurUserPwd()))) {
            throw new CodeMessageException(Result.CURRENT_PASSWORD_MISMATCH);
        }

        if (dto.getUserPwd() != null && !dto.getUserPwd().isEmpty()) {
            if (checkValidPassword && !CommonUtil.isValidPassword(dto.getUserPwd())) {
                throw new CodeMessageException(Result.INVALID_PASSWORD);
            }

            dto.setUserPwd(CommonUtil.sha256(dto.getUserPwd()));
        }

        if (StringUtils.isNotBlank(dto.getMobileNum())) {
            dto.setMobileNum(dto.getMobileNum().replaceAll("\\D", ""));
        }

        dto.setUpdateDtm(new Date());

        userInfoRepository.save(modelMapper.map(dto, UserInfo.class));
        userInfoHistService.insert(workerId, Constants.WORK_CD_UPDATE, dto.getUserId());
        if (dto.getUserRoleList() != null && !dto.getUserRoleList().isEmpty()) {
            userRoleService.update(workerId, dto.getUserId(), dto.getUserRoleList());
        }

        if (workerId.equals(dto.getUserId())) {
            UserInfoUtil.updateUserInfo(dto);
        }

        return 1;
        */
        return 0;
    }

    @Override
    public void updatePwd(String workerId, UserInfoDto dto) throws CodeMessageException {
        /*
        UserInfo userInfo = getEntity(dto.getUserId());

        if (!CommonUtil.isValidPassword(dto.getUserPwd())) {
            throw new CodeMessageException(Result.INVALID_PASSWORD);
        }

        if (!userInfo.getUserPwd().equals(CommonUtil.sha256(dto.getCurUserPwd()))) {
            throw new CodeMessageException(Result.CURRENT_PASSWORD_MISMATCH);
        }

        Date now = new Date();
        dto.setUpdateDtm(now);
        dto.setUserPwd(CommonUtil.sha256(dto.getUserPwd()));
        dto.setUserPwdDtm(now);
        dto.setUserPwdNotiDtm(now);

        userInfoRepository.save(modelMapper.map(dto, UserInfo.class));
        userInfoHistService.insert(workerId, Constants.WORK_CD_UPDATE, dto.getUserId());
        */
    }

    @Override
    public String resetPwd(String workerId, String userId) throws CodeMessageException {
        /*
        UserInfo userInfo = getEntity(userId);
        String tempPwd = UUID.randomUUID().toString().substring(0, 9);

        userInfo.setUpdateDtm(new Date());
        userInfo.setStatusCd(UserInfoDto.STATUS_CD_NORMAL);
        userInfo.setUserPwd(CommonUtil.sha256(tempPwd));
        userInfo.setLoginFailCnt((short) 0);
        userInfoRepository.save(userInfo);

        return tempPwd;
        */
        return "NOT SUPPORT";
    }

    @Override
    public void updateStatusCd(UserInfoDto dto) {
        updateStatusCd(dto.getUserId(), dto.getStatusCd());
    }

    @Override
    public void updateStatusCd(String userId, String statusCd) {
        /*
        UserInfo userInfo = getEntity(userId);

        userInfo.setUpdateDtm(new Date());
        userInfo.setStatusCd(statusCd);
        */
    }

    @Override
    public void updateStatusCd(String[] userIds, String statusCd) {
        for (String userId : userIds) {
            updateStatusCd(userId, statusCd);
        }
    }

    @Override
    public void updateApproval(String workerId, String[] userIds, String roleId, String approval, String baseUri) {
        /*
        UserInfoDto dto = null;

        updateStatusCd(userIds, Constants.YN_YES.equals(approval) ? UserInfoDto.STATUS_CD_NORMAL : UserInfoDto.STATUS_CD_DELETE);

        for (String userId : userIds) {
            userRoleService.update(workerId, userId, roleId);
            userInfoHistService.insert(workerId, Constants.WORK_CD_UPDATE, userId);

            try {
                dto = get(userId);
                emailService.send(dto.getEmailAddr(), "approval.html", AppPropertyUtil.get(Constants.MAIL_SUBJECT_APPROVAL), dto, baseUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }

    @Override
    public void updateLastLoginDtm(String userId) {
        /*
        UserInfo userInfo = getEntity(userId);

        userInfo.setLastLoginDtm(new Date());
        userInfo.setLoginFailCnt((short) 0);
        */
    }

    @Override
    public void delete(String workerId, String userId) {
        /*
        UserInfo userInfo = getEntity(userId);

        userInfo.setLastLoginDtm(new Date());
        userInfo.setStatusCd(UserInfoDto.STATUS_CD_DELETE);
        userInfoRepository.saveAndFlush(userInfo);

        userInfoHistService.insert(workerId, Constants.WORK_CD_DELETE, userId);
        userInfoExtService.delete(userId);
        userRoleService.deleteByUserId(workerId, userId);
        */
    }

    @Override
    public void delete(String workerId, String[] userIds) {
        for (String userId : userIds) {
            delete(workerId, userId);
        }
    }

    private UserInfo getEntity(String userId) {
        // 더아이엠씨 성영주 본부장님의
        // vf_user_info view 테이블에서 결과가 여러개 나올 수 있다는 답변이 있었음.
        List<UserInfo> data = userInfoRepository.findAllById(Collections.singletonList(userId));

        if (data.isEmpty()) {
            throw new CodeMessageException(Result.NO_DATA);
        }
//        Optional<UserInfo> data = userInfoRepository.findById(userId);

//        if (!data.isPresent()) {
//            throw new CodeMessageException(Result.NO_DATA);
//        }

        return data.get(0);
    }

}
