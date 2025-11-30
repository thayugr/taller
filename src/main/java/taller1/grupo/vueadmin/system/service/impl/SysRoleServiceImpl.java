package taller1.grupo.vueadmin.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import taller1.grupo.vueadmin.constant.CommonConstants;
import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.system.entity.SysRole;
import taller1.grupo.vueadmin.system.entity.SysRoleUser;
import taller1.grupo.vueadmin.system.mapper.SysRoleMapper;
import taller1.grupo.vueadmin.system.service.SysRoleMenuService;
import taller1.grupo.vueadmin.system.service.SysRoleService;
import taller1.grupo.vueadmin.system.service.SysRoleUserService;
import lombok.RequiredArgsConstructor;
import taller1.grupo.vueadmin.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;

    private final SysRoleUserService roleUserService;

    private final SysRoleMenuService roleMenuService;

    /**
     * @param blurry
     * @Description:Obtener lista de roles
     * @Param: [blurry]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRole>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysRole> getRoleList(String blurry) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(SysRole::getRoleName, blurry);
            wrapper.or();
            wrapper.like(SysRole::getRoleCode, blurry);
            wrapper.or();
            wrapper.like(SysRole::getDescription, blurry);
        }
        wrapper.ne(SysRole::getRoleCode, CommonConstants.ROLE_ADMIN);
        return roleMapper.selectList(wrapper);
    }

    /**
     * @param role
     * @Description: Editar rol
     * @Param: [role]
     * @return: void
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public void editRole(SysRole role) {
        // Verificar el código y el nombre del rol
        checkRole(role);
        if (role.getId() != null) {
            roleMapper.updateById(role);
        } else {
            roleMapper.insert(role);
        }
    }

    /**
     * @param id
     * @Description: 删除角色
     * @Param: [id]
     * @return: void
     * @Author: richard sivila
     * @Date: 2021/11/27
     */
    @Override
    @Transactional
    public void delRole(Long id) {
        // Verificar si el rol ha estado vinculado al usuario
        checkRoleUser(id);
        // Primero elimine el menú vinculado al personaje.
        roleMenuService.deleteByRoleId(id);
        // Eliminar el rol nuevamente
        roleMapper.deleteById(id);
    }

    /**
     * @param userId
     * @Description: Lista de selección de roles
     * @Param: [userId]
     * @return: com.alibaba.fastjson.JSONArray
     * @Author: starao
     * @Date: 2021/11/27
     */
    @Override
    public JSONArray getAllRoleForXm(Long userId) {
        // Consultar todos los roles
        List<SysRole> list = roleMapper.selectList(null);
        // Consultar el rol de usuario actual
        List<SysRoleUser> roleUserList = roleUserService.getRoleUserByUserId(userId);
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isEmpty(list)) {
            for (SysRole role : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", role.getRoleName());
                jsonObject.put("id", role.getId());

                if (!CollectionUtils.isEmpty(roleUserList) && roleUserList.get(0).getRoleId().equals(role.getId())) {
                    jsonObject.put("selected", true);
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    /**
     * @param userId
     * @Description: Obtener el rol de usuario actual por ID de usuario
     * @Param: [userId]
     * @return: java.util.List<taller1.grupo.vueadmin.system.entity.SysRole>
     * @Author: richard sivila
     * @Date: 2024
     */
    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        return roleMapper.getRoleByUserId(userId);

    }

    /**
     * @param userId
     * @Description: Obtener la lista de funciones actuales del usuario
     * @Param: [id]
     * @return: java.util.List<org.springframework.security.core.GrantedAuthority>
     * @Author: starao
     * @Date: 2024
     */
    @Override
    public List<GrantedAuthority> getRolesByUser(Long userId) {
        Set<String> permissions = new HashSet<>();
        List<SysRole> roleList = roleMapper.getRoleByUserId(userId);
        if (!CollectionUtils.isEmpty(roleList)) {
            for (SysRole sysRole : roleList) {
                permissions.add(sysRole.getRoleCode());
            }
        }
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * @Description: Verificar el nombre y el código del rol
     * @Param: [role]
     * @return: void
     * @Author: starao
     * @Date: 2024
     */
    private void checkRole(SysRole role) {
        List<SysRole> list;
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        if (role.getId() != null) {
            wrapper.ne("id", role.getId());
        }
        wrapper.and(w -> w.eq("role_name", role.getRoleName()).or().eq("role_code", role.getRoleCode()));
        list = roleMapper.selectList(wrapper);
        if (list != null && !list.isEmpty()) {
            throw new BadRequestException("El código o nombre del rol ya existe，Por favor vuelve a entrar");
        }
    }

    /**
     * @Description:Verificar si el rol ha estado vinculado al usuario
     * @Param: [roleId]
     * @return: void
     * @Author: starao
     * @Date: 2021/11/27
     */
    private void checkRoleUser(Long roleId) {
        List<SysRoleUser> list = roleUserService.getRoleUserByRoleId(roleId);
        if (!CollectionUtils.isEmpty(list)) {
            throw new BadRequestException("Este rol ha estado vinculado al usuario.，Desvincula y luego elimina");
        }
    }
}
