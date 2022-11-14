package zinsoft.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.security.WebUser;

@Slf4j
public class UserInfoUtil {

    private UserInfoUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static UserInfoDto getUserInfo() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("auth : {}", auth.toString());
            Object principal = auth.getPrincipal();
            log.info("principal : {}", principal.toString());

            if (principal instanceof WebUser) {

                UserInfoDto user = ((WebUser) principal).getUser();
                log.info("user : {}", user.toString());
                return user;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getCause().toString());
            return null;
        }
        return null;
    }

    public static String getUserId() {
        UserInfoDto dto = getUserInfo();
        return dto != null ? dto.getUserId() : null;
    }

    public static String getMainRoleId() {
        UserInfoDto dto = getUserInfo();

        if (dto == null) {
            return RoleDto.ROLE_ANONYMOUS;
        }

        return getMainRoleId(dto);
    }

    public static String getMainRoleId(UserInfoDto dto) {
        if (dto == null) {
            return RoleDto.ROLE_ANONYMOUS;
        }

        List<String> userRoleIdList = dto.getUserRoleIdList();

        if (userRoleIdList != null && !userRoleIdList.isEmpty()) {
            return userRoleIdList.get(0);
        }

        return null;
    }

    public static boolean isAdmin() {
        return isAdmin(getUserInfo());
    }

    public static boolean isAdmin(UserInfoDto dto) {
        if (dto == null) {
            return false;
        }

        List<String> userRoleIdList = dto.getUserRoleIdList();

        if (userRoleIdList == null) {
            return false;
        }

        return userRoleIdList.contains(RoleDto.ROLE_ADMIN);
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole(RoleDto.ROLE_ADMIN);
    }

    public static boolean isManager() {
        return isManager(getUserInfo());
    }

    public static boolean isManager(UserInfoDto dto) {
        if (dto == null) {
            return false;
        }

        List<String> userRoleIdList = dto.getUserRoleIdList();

        if (userRoleIdList == null) {
            return false;
        }

        return /*userRoleIdList.contains(RoleDto.ROLE_ADMIN) ||*/ userRoleIdList.contains(RoleDto.ROLE_MANAGER);
    }

    public static boolean isManager(HttpServletRequest request) {
        return /*request.isUserInRole(RoleDto.ROLE_ADMIN) ||*/ request.isUserInRole(RoleDto.ROLE_MANAGER);
    }

    public static boolean isPermitUserResource(String userId) {
        UserInfoDto userInfo = getUserInfo();
        return userInfo != null && (isManager(userInfo) || userId.equals(userInfo.getUserId()));
    }

    public static void updateUserInfo(UserInfoDto newDto) {
        UserInfoDto dto = getUserInfo();
        dto.setUserNm(newDto.getUserNm());
        dto.setEmailAddr(newDto.getEmailAddr());
    }

    public static void updateUserRole(List<UserRoleDto> userRoleList) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (principal instanceof WebUser) {
                UserInfoDto dto = ((WebUser) principal).getUser();
                List<GrantedAuthority> authList = new ArrayList<>();
                List<String> userRoleIdList = new ArrayList<>();

                for (UserRoleDto userRoleDto : userRoleList) {
                    userRoleIdList.add(userRoleDto.getRoleId());
                }

                dto.setUserRoleList(userRoleList);
                dto.setUserRoleIdList(userRoleIdList);

                for (UserRoleDto userRole : userRoleList) {
                    authList.add(new SimpleGrantedAuthority(userRole.getRoleId()));
                }

                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authList);
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public static UserInfoDto getFarmerInfo() {
        try {
            ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            log.info("requestAttr : {}", requestAttr.toString());
            HttpSession session = requestAttr.getRequest().getSession();
            log.info("session : {}", session.toString());
//            Enumeration<String> names = session.getAttributeNames();
            UserInfoDto farmerInfoDto = (UserInfoDto) session.getAttribute(Constants.SESSION_FARMER_INFO);
            log.info("farmerInfoDto : {}", farmerInfoDto.toString());

            if (farmerInfoDto != null) {
                return farmerInfoDto;
            }
        } catch (Exception e) {
            // ignore
            log.error(e.getMessage());
            log.error(e.getCause().toString());
        }

        return getUserInfo();
    }

}
