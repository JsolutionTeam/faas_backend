package zinsoft.web.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import zinsoft.util.AppPropertyUtil;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.MenuDto;
import zinsoft.web.common.dto.MenuRoleDto;
import zinsoft.web.common.dto.RestfulApiAccessDto;
import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.service.MenuApiService;
import zinsoft.web.common.service.MenuService;

@Slf4j
public class ReloadableFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    @Value("${api.prefix:}")
    private String apiPrefix;

    @Resource
    private MenuService menuService;

    @Resource
    private MenuApiService menuApiService;

    @PostConstruct
    public void init() {
        reload();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Collection<ConfigAttribute> result = null;

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                result = entry.getValue();
                break;
            }
        }

        return result;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void reload() {
        requestMap.clear();

        log.info("ReloadableFilterInvocationSecurityMetadataSource - reload()");

        // 관리자
        Collection<ConfigAttribute> admin = SecurityConfig.createList(RoleDto.ROLE_ADMIN);

        // 로그인한 사용자
        ArrayList<ConfigAttribute> authenticated = new ArrayList<>();
        authenticated.add(new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_FULLY));
        authenticated.add(new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED));

        // 로그인하지 않은 사용자
        ArrayList<ConfigAttribute> anonymously = new ArrayList<>();
        anonymously.add(new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY));

        RequestMatcher matcher = null;
        List<ConfigAttribute> attributes = null;

        // static resources
        matcher = new AntPathRequestMatcher("/static/**");
        requestMap.put(matcher, anonymously);
        matcher = new AntPathRequestMatcher("/favicon.ico");
        requestMap.put(matcher, anonymously);

        // 회원, 메뉴 등 기본 정보
        matcher = new AntPathRequestMatcher(apiPrefix + "/session");
        requestMap.put(matcher, anonymously);

        // 회원가입
        matcher = new AntPathRequestMatcher(apiPrefix + "/user", "POST");
        requestMap.put(matcher, anonymously);

        // 회원조회/수정/탈퇴
        matcher = new AntPathRequestMatcher(apiPrefix + "/user/item");
        requestMap.put(matcher, authenticated);

        // 아이디 찾기
        matcher = new AntPathRequestMatcher(apiPrefix + "/user-id", "POST");
        requestMap.put(matcher, anonymously);

        // 비번 찾기
        matcher = new AntPathRequestMatcher(apiPrefix + "/user-pwd", "POST");
        requestMap.put(matcher, anonymously);

        // 게시판 (tb_board 테이블에서 세부 권한 설정)
        matcher = new AntPathRequestMatcher(apiPrefix + "/boardcat/*", "GET");
        requestMap.put(matcher, anonymously);
        matcher = new AntPathRequestMatcher(apiPrefix + "/board/**");
        requestMap.put(matcher, anonymously);

        // 파일다운로드
        matcher = new AntPathRequestMatcher(apiPrefix + "/file/*", "GET");
        requestMap.put(matcher, anonymously);

        // code
        matcher = new AntPathRequestMatcher(apiPrefix + "/code/*", "GET");
        requestMap.put(matcher, anonymously);

        // 메뉴에서 사용하는 API 허용
        List<RestfulApiAccessDto> apiAccessList = menuApiService.listRestfulApiAccess();
        for (RestfulApiAccessDto access : apiAccessList) {
            List<String> roleIdList = access.getRoleIdList();
            if (roleIdList == null || roleIdList.isEmpty()) {
                continue;
            }

            matcher = new AntPathRequestMatcher(access.getPathPattern(), access.getMethod());

            if (roleIdList.contains(RoleDto.ROLE_ANONYMOUS)) {
                requestMap.put(matcher, anonymously);
                continue;
            }

            attributes = new ArrayList<>(roleIdList.size());
            for (String roleId : roleIdList) {
                attributes.add(new SecurityConfig(roleId));
            }

            requestMap.put(matcher, attributes);
        }

        if (Constants.BOOLEAN_TRUE.equals(AppPropertyUtil.get(Constants.PROJECT_WITH_FRONTEND))) {
            // 메뉴 페이지 허용
            List<MenuDto> menuList = menuService.list(Constants.SITE_CD);
            List<String> exprAccessList = new ArrayList<>();
            List<String> parentMenuIdList = new ArrayList<>(Arrays.asList(""));
            int prevDepth = 0;

            for (MenuDto menu : menuList) {
                List<MenuRoleDto> menuRoleList = menu.getExprMenuRoleList();
                String menuId = menu.getMenuId();
                int depth = menu.getDepth();

                exprAccessList.clear();
                if (menuRoleList != null) {
                    for (MenuRoleDto mr : menuRoleList) {
                        exprAccessList.add(mr.getRoleId());
                    }
                }

                if (prevDepth == depth) {
                    parentMenuIdList.set(parentMenuIdList.size() - 1, menuId);
                } else if (prevDepth < depth) {
                    parentMenuIdList.add(menuId);
                } else {
                    parentMenuIdList = parentMenuIdList.subList(0, depth);
                    parentMenuIdList.add(menuId);
                }

                prevDepth = depth;

                if (exprAccessList.size() > 0) {
                    attributes = SecurityConfig.createListFromCommaDelimitedString(StringUtils.join(exprAccessList, ","));
                    matcher = new AntPathRequestMatcher("/content/" + String.join("/", parentMenuIdList));
                    requestMap.put(matcher, attributes);
                }
            }
        }

        matcher = new AntPathRequestMatcher("/content/login");
        requestMap.put(matcher, anonymously);
        matcher = new AntPathRequestMatcher("/content/error");
        requestMap.put(matcher, anonymously);
        matcher = new AntPathRequestMatcher("/");
        requestMap.put(matcher, anonymously);

        // swagger login은 아무나 가능하도록
        matcher = new AntPathRequestMatcher("/swagger", "GET");
        requestMap.put(matcher, anonymously);

        // 그외 지정되지 않은 것은 관리자만...
        //requestMap.put(AnyRequestMatcher.INSTANCE, admin);
        requestMap.put(AnyRequestMatcher.INSTANCE, authenticated); // TODO
    }

}
