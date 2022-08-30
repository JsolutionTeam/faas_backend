package zinsoft.web.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.MenuRoleDto;
import zinsoft.web.common.entity.MenuRole;
import zinsoft.web.common.repository.MenuRoleRepository;
import zinsoft.web.common.service.MenuRoleHistService;
import zinsoft.web.common.service.MenuRoleService;

@Service("menuRoleService")
@Transactional
public class MenuRoleServiceImpl extends EgovAbstractServiceImpl implements MenuRoleService {

    //private Type dtoListType = new TypeToken<List<MenuRoleDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    MenuRoleRepository menuRoleRepository;

    @Resource
    MenuRoleHistService menuRoleHistService;

    @Override
    public void insert(String workerId, MenuRoleDto dto) {
        menuRoleRepository.save(modelMapper.map(dto, MenuRole.class));
        menuRoleHistService.insert(workerId, Constants.WORK_CD_INSERT, dto);
    }

    @Override
    public void insert(String workerId, String menuId, String roleId, String actCd) {
        insert(workerId, new MenuRoleDto(menuId, roleId, actCd));
    }

    @Override
    public void insert(String workerId, String roleId, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds) {
        if (listMenuIds != null) {
            for (String menuId : listMenuIds) {
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_LIST);
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DETAIL);
            }
        }

        if (insertMenuIds != null) {
            for (String menuId : insertMenuIds) {
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_INSERT);
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_UPDATE);
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DELETE);
            }
        }

        if (exprMenuIds != null) {
            for (String menuId : exprMenuIds) {
                insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_EXPR);
            }
        }
    }

    @Override
    public List<MenuRoleDto> listByRoleId(String roleId) {
        MenuRoleDto dto = new MenuRoleDto();

        dto.setRoleId(roleId);

        return menuRoleRepository.list(dto);
    }

    @Override
    public void update(String workerId, String roleId, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds) {
        List<MenuRoleDto> list = listByRoleId(roleId);
        List<String> listMenuIdList = new ArrayList<>();
        List<String> insertMenuIdList = new ArrayList<>();
        List<String> exprMenuIdList = new ArrayList<>();

        // 삭제
        for (MenuRoleDto dto : list) {
            String menuId = dto.getMenuId();

            if (dto.getActCd().equals(MenuRoleDto.ACT_CD_LIST)) {
                listMenuIdList.add(menuId);
                if (listMenuIds == null || !ArrayUtils.contains(listMenuIds, menuId)) {
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_LIST);
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DETAIL);
                }
            } else if (dto.getActCd().equals(MenuRoleDto.ACT_CD_INSERT)) {
                insertMenuIdList.add(menuId);
                if (insertMenuIds == null || !ArrayUtils.contains(insertMenuIds, menuId)) {
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_INSERT);
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_UPDATE);
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DELETE);
                }
            } else if (dto.getActCd().equals(MenuRoleDto.ACT_CD_EXPR)) {
                exprMenuIdList.add(menuId);
                if (exprMenuIds == null || !ArrayUtils.contains(exprMenuIds, menuId)) {
                    delete(workerId, menuId, roleId, MenuRoleDto.ACT_CD_EXPR);
                }
            }
        }

        // 추가
        if (listMenuIds != null) {
            for (String menuId : listMenuIds) {
                if (!listMenuIdList.contains(menuId)) {
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_LIST);
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DETAIL);
                }
            }
        }

        if (insertMenuIds != null) {
            for (String menuId : insertMenuIds) {
                if (!insertMenuIdList.contains(menuId)) {
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_INSERT);
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_UPDATE);
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_DELETE);
                }
            }
        }

        if (exprMenuIds != null) {
            for (String menuId : exprMenuIds) {
                if (!exprMenuIdList.contains(menuId)) {
                    insert(workerId, menuId, roleId, MenuRoleDto.ACT_CD_EXPR);
                }
            }
        }
    }

    @Override
    public void delete(String workerId, MenuRoleDto dto) {
        menuRoleHistService.insert(workerId, Constants.WORK_CD_DELETE, dto);
        menuRoleRepository.deleteById(modelMapper.map(dto, MenuRole.class));
    }

    @Override
    public void delete(String workerId, String menuId, String roleId, String actCd) {
        delete(workerId, new MenuRoleDto(menuId, roleId, actCd));
    }

    @Override
    public void deleteByRoleId(String workerId, String roleId) {
        menuRoleHistService.insert(workerId, Constants.WORK_CD_DELETE, roleId);
        menuRoleRepository.deleteByRoleId(roleId);
    }

}
