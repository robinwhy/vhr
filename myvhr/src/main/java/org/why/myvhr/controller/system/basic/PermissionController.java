package org.why.myvhr.controller.system.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.why.myvhr.model.Menu;
import org.why.myvhr.model.RespBean;
import org.why.myvhr.model.Role;
import org.why.myvhr.service.MenuService;
import org.why.myvhr.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    @Autowired
    MenuService menuService;
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return permissionService.getAllRoles();
    }
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping("/mids/{rid}")
    public List<Integer> getMidsByRid(@PathVariable Integer rid) {
        return menuService.getMidsByRid(rid);
    }

    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        if (menuService.updateMenuRole(rid, mids)) {
            return RespBean.ok("update success!");
        }
        return RespBean.error("update failure!");
    }

    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        if (permissionService.addRole(role) == 1) {
            return RespBean.ok("add success!");
        }
        return RespBean.error("add failure!");
    }

    @DeleteMapping("/role/{rid}")
    public RespBean deleteRoleById(@PathVariable Integer rid) {
        if (permissionService.deleteRoleById(rid) == 1) {
            return RespBean.ok("delete success!");
        }
        return RespBean.error("delete failure!");
    }
}
