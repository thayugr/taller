package taller1.grupo.vueadmin.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.SecurityUtil;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.system.entity.SysMenu;
import taller1.grupo.vueadmin.system.entity.SysRoleMenu;
import taller1.grupo.vueadmin.system.mapper.SysMenuMapper;
import taller1.grupo.vueadmin.system.mapper.SysRoleMenuMapper;
import taller1.grupo.vueadmin.system.service.SysMenuService;
import taller1.grupo.vueadmin.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import taller1.grupo.vueadmin.constant.CaptchaConstants;
import taller1.grupo.vueadmin.constant.CommonConstants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2021-11-27 14:33
 **/
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper menuMapper;

    private final SysRoleMenuMapper roleMenuMapper;

    /**
     * @param roles
     * @Description: Obtener árbol de menú
     * @Param: [roles]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public JSONArray getMenuTree(List<String> roles) {
        List<SysMenu> menuListAll;
        // Si el rol contiene administrador, consulte directamente todos los menús que
        // no sean botones
        if (roles.contains(CommonConstants.ROLE_ADMIN)) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.ne(SysMenu::getType, "3");
            wrapper.orderByAsc(SysMenu::getSort);
            menuListAll = menuMapper.selectList(wrapper);
            // de lo contrario
        } else {
            // Obtener el menú autorizado por el usuario actual
            menuListAll = menuMapper.getMenuTree(roles);
            if (!CollectionUtils.isEmpty(menuListAll)) {
                menuListAll = menuListAll.stream().filter(item -> !item.getType().equals("3"))
                        .collect(Collectors.toList());
                Set<SysMenu> menuSet = new HashSet<>();
                List<SysMenu> list = new ArrayList<>();
                // Pasar por todos los menús
                for (SysMenu sysMenu : menuListAll) {
                    list.add(sysMenu);
                    // Obtener todos los menús superiores del menú actual
                    getAllMenusByChildId(sysMenu.getParentId(), list);
                    for (SysMenu menu : list) {
                        if (menuSet.stream().noneMatch(item -> item.getId().equals(menu.getId()))) {
                            menuSet.add(menu);
                        }
                    }
                }
                menuListAll = menuSet.stream().sorted(Comparator.comparing(SysMenu::getId))
                        .collect(Collectors.toList());
            }
        }
        JSONArray jsonArray = new JSONArray();
        // Obtener el menú superior
        List<SysMenu> topList = menuListAll.stream().filter(item -> item.getParentId() == 0L)
                .collect(Collectors.toList());
        // Si el menú de nivel superior no está vacío
        return getObjects(menuListAll, jsonArray, topList);
    }

    /**
     * @Description: Métodos públicos para ensamblar árboles.
     * @Param: [menuListAll, jsonArray, topList]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    private JSONArray getObjects(List<SysMenu> menuListAll, JSONArray jsonArray, List<SysMenu> topList) {
        if (!CollectionUtils.isEmpty(topList)) {
            // Montaje del árbol de menús
            for (SysMenu sysMenu : topList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", sysMenu.getId());
                jsonObject.put("parentId", sysMenu.getParentId());
                jsonObject.put("name", sysMenu.getName());
                jsonObject.put("path", sysMenu.getPath());
                jsonObject.put("icon", sysMenu.getIcon());
                jsonObject.put("sort", sysMenu.getSort());
                jsonObject.put("component", sysMenu.getComponent());
                jsonObject.put("permission", sysMenu.getPermission());
                jsonObject.put("type", sysMenu.getType());
                if (!CollectionUtils.isEmpty(getChildById(menuListAll, sysMenu.getId()))) {
                    jsonObject.put("children", getChildById(menuListAll, sysMenu.getId()));
                }

                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    /**
     * @Description: Obtener submenú por ID
     * @Param: [menuList, parentId]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    private JSONArray getChildById(List<SysMenu> menuList, long parentId) {
        JSONArray jsonArray = new JSONArray();
        List<SysMenu> children = menuList.stream().filter(item -> item.getParentId().equals(parentId))
                .collect(Collectors.toList());
        return getObjects(menuList, jsonArray, children);
    }

    /**
     * @param sysMenu
     * @Description: Editar menú
     * @Param: [sysMenu]
     * @return: void
     * @Author: richard sivila
     * @Date: 2021/11/27
     */
    @Override
    public void editMenu(SysMenu sysMenu) {
        if (sysMenu.getParentId() == null) {
            throw new BadRequestException("Falta el directorio superior, la edición falló");
        }
        if (sysMenu.getId() != null) {
            menuMapper.updateById(sysMenu);
        } else {
            menuMapper.insert(sysMenu);
        }
    }

    /**
     * @param id
     * @Description: eliminar menú
     * @Param: [id]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void delMenu(Long id) {
        // Verificar si el menú ha sido vinculado a un rol
        checkMenuRole(id);
        menuMapper.deleteById(id);
    }

    /**
     * @param roles
     * @Description: Consultar todos los menús de permisos del usuario actual.
     * @Param: [roles]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysMenu>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysMenu> queryAllMenus(List<String> roles) {
        if (roles.contains(CommonConstants.ROLE_ADMIN)) {
            return menuMapper.selectList(null);
        }
        return menuMapper.getMenuTree(roles);
    }

    /**
     * @param blurry
     * @Description: Obtener lista de menú
     * @Param: [blurry]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public JSONArray getMenuTable(String blurry) {
        JSONArray jsonArray = new JSONArray();
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(blurry)) {
            wrapper.like(SysMenu::getName, blurry);
            wrapper.or();
            wrapper.like(SysMenu::getPath, blurry);
        }
        wrapper.orderByAsc(SysMenu::getSort);
        List<SysMenu> list = menuMapper.selectList(wrapper);
        List<SysMenu> topList = list.stream().filter(item -> item.getParentId() == 0L).collect(Collectors.toList());
        return getObjects(list, jsonArray, topList);
    }

    /**
     * @param roles
     * @Description: Obtenga todos los menús de autorización por rol
     * @Param: [currentRoles]
     * @return: java.util.List<java.lang.String>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<String> getUrlsByRoles(List<String> roles) {
        return menuMapper.getMenuUrlByRole(roles);
    }

    /**
     * @Description: Obtener lista de permisos
     * @Param: []
     * @return: java.util.List<java.lang.String>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<String> getPermission() {
        List<String> roles = SecurityUtil.getCurrentRoles();
        List<String> permissions;
        if (roles.contains(CommonConstants.ROLE_ADMIN)) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(SysMenu::getPermission);
            wrapper.eq(SysMenu::getType, "3");
            permissions = menuMapper.selectObjs(wrapper).stream().map(o -> (String) o).collect(Collectors.toList());
        } else {
            permissions = menuMapper.getPermission(roles);
        }
        return permissions;
    }

    /**
     * @Description: Obtener árbol desplegable del menú
     * @Param: []
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: starao
     * @Date: 2022/11/9
     */
    @Override
    public JSONArray getMenuTreeSelect() {
        JSONArray menuArray = getMenuTree(SecurityUtil.getCurrentRoles());
        JSONArray children = new JSONArray();
        if (!CollectionUtils.isEmpty(menuArray)) {
            for (int i = 0; i < menuArray.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", menuArray.getJSONObject(i).getLongValue("id"));
                jsonObject.put("label", menuArray.getJSONObject(i).getString("name"));
                if (menuArray.getJSONObject(i).getJSONArray("children") != null) {
                    jsonObject.put("children", getTreeChildren(menuArray.getJSONObject(i).getJSONArray("children")));
                }
                children.add(jsonObject);
            }
        }
        return children;
    }

    /**
     * @Description: Verificar si el menú ha sido vinculado a un rol
     * @Param: [menuId]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    private void checkMenuRole(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getMenuId, menuId);
        long count = roleMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("Este menú tiene una función asociada y no se puede eliminar.");
        }
    }

    /**
     * @Description: Obtenga todos los menús de nivel superior por ID de menú
     * @Param: [menuId]
     * @Author: richard sivila
     * @Date: 2024
     */
    private void getAllMenusByChildId(Long menuId, List<SysMenu> list) {
        SysMenu sysMenu = menuMapper.selectById(menuId);
        // Si el menú actual no es el menú de nivel superior
        if (sysMenu != null) {
            list.add(sysMenu);
            // Si el menú encima del menú actual no es el menú de nivel superior
            if (!sysMenu.getParentId().equals(0L)) {
                getAllMenusByChildId(sysMenu.getParentId(), list);
            }
        }
    }

    /**
     * @Description: Obtener subconjunto de árbol desplegable
     * @Param: [jsonArray]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: richard sivila
     * @Date: 2024
     */
    private JSONArray getTreeChildren(JSONArray jsonArray) {
        JSONArray children = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", jsonArray.getJSONObject(i).getLongValue("id"));
            jsonObject.put("label", jsonArray.getJSONObject(i).getString("name"));
            if (jsonArray.getJSONObject(i).getJSONArray("children") != null) {
                jsonObject.put("children", jsonArray.getJSONObject(i).getJSONArray("children"));
            }
            children.add(jsonObject);
        }
        return children;
    }
}
