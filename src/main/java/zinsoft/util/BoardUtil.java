package zinsoft.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import zinsoft.web.common.dto.BoardDto;
import zinsoft.web.common.dto.UserInfoDto;

public class BoardUtil {

    public enum BoardAct {
        INSERT, LIST, DETAIL, UPDATE, DELETE
    };

    public static BoardDto getBoard(String boardId) {
        ApplicationContext ctx = ApplicationContextProvider.applicationContext();
        @SuppressWarnings("unchecked")
        Map<String, BoardDto> boardMap = (Map<String, BoardDto>) ctx.getBean("boardMap");
        return boardMap.get(boardId);
    }

    public static boolean isPermitRole(String boardId, BoardAct act) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (UserInfoUtil.isAdmin(userInfo)) {
            return true;
        }

        BoardDto board = getBoard(boardId);
        boolean isPermit = false;
        String roles = null;

        switch (act) {
            case INSERT:
                roles = board.getRolesInsert();
                break;
            case LIST:
                roles = board.getRolesList();
                break;
            case DETAIL:
                roles = board.getRolesDetail();
                break;
            case UPDATE:
                roles = board.getRolesUpdate();
                break;
            case DELETE:
                roles = board.getRolesDelete();
                break;
        }

        if (StringUtils.isNotBlank(roles)) {
            String[] r = roles.split(",");
            List<String> userRoleIdList = userInfo.getUserRoleIdList();

            for (String roleId : r) {
                if (userRoleIdList.contains(roleId)) {
                    isPermit = true;
                    break;
                }
            }
        } else {
            isPermit = true;
        }

        return isPermit;
    }

}
