package zinsoft.web.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontEndController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        //String name = request.getServletContext().getServletContextName(); // web.xml의 display-name
        return "index";
    }

    @GetMapping("/content/{menuId}")
    public String content(@PathVariable String menuId) {
        return "content/" + menuId + "/" + menuId;
    }

    @GetMapping("/content/{parentMenuId}/{menuId}")
    public String content(@PathVariable String parentMenuId, @PathVariable String menuId) {
        return "content/" + parentMenuId + "/" + menuId + "/" + menuId;
    }

    @GetMapping("/content/{parentMenuId}/{menuId}/{act}")
    public String content(@PathVariable String parentMenuId, @PathVariable String menuId, @PathVariable String act) {
        return "content/" + parentMenuId + "/" + menuId + "/" + act;
    }

    @GetMapping("/swagger")
    public String content() {
        return "swagger/login";
    }

}
