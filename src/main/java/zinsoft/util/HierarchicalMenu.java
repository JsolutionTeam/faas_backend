package zinsoft.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import zinsoft.web.common.dto.MenuDto;

@Data
public class HierarchicalMenu {

    private MenuDto menu;
    private List<HierarchicalMenu> subMenu;

    public HierarchicalMenu() {
    }

    public HierarchicalMenu(MenuDto menu) {
        this.menu = menu;
        this.subMenu = new ArrayList<>();
    }

}
