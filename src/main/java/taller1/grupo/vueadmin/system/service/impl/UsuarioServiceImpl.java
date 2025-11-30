// Archivo: src/main/java/taller1/grupo/vueadmin/system/service/impl/UsuarioServiceImpl.java

package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import taller1.grupo.vueadmin.system.entity.Personal;
import taller1.grupo.vueadmin.system.entity.Datos; 
import taller1.grupo.vueadmin.system.entity.SysUser;
import taller1.grupo.vueadmin.system.entity.dto.QueryUsuarioDto;
import taller1.grupo.vueadmin.system.entity.dto.UsuarioDto; 
import taller1.grupo.vueadmin.system.mapper.UsuarioMapper;
import taller1.grupo.vueadmin.system.mapper.DatosMapper; // Asumo la existencia
import taller1.grupo.vueadmin.system.mapper.SysUserMapper; // Asumo la existencia (aunque usamos el método en UsuarioMapper)
import taller1.grupo.vueadmin.system.service.UsuarioService;
import taller1.grupo.vueadmin.common.exception.BadRequestException; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioMapper usuarioMapper;
    private final DatosMapper datosMapper; // Necesario para gestionar 'academico.datos'
    private final SysUserMapper sysUserMapper; // Necesario para selectById del SysUser

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    // ===============================================
    // Consulta y Paginación
    // ===============================================
    @Override
    public IPage<Personal> queryUsuarioTable(QueryUsuarioDto queryDto) {
        Page<Personal> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return usuarioMapper.queryUsuarioTable(page, queryDto); 
    }
    
    // ===============================================
    // Obtener Usuarios sin Asignar
    // ===============================================
    @Override
    public List<SysUser> getUnassignedSysUsers() {
        return usuarioMapper.findUnassignedSysUsers();
    }

    // ===============================================
    // Obtener Detalles para Edición
    // ===============================================
    @Override
    public UsuarioDto getUsuarioDetails(Long idusuario) {
        // 1. Obtener Personal
        Personal personal = usuarioMapper.selectById(idusuario);
        if (personal == null) {
            throw new BadRequestException("Registro de Personal no encontrado.");
        }
        
        UsuarioDto dto = new UsuarioDto();
        BeanUtils.copyProperties(personal, dto); // Copia campos de Personal a DTO
        
        // 2. Obtener Datos (Cédula)
        Datos datos = datosMapper.selectById(idusuario); // 'cod' = 'idusuario'
        if (datos != null) {
            dto.setCedula(datos.getCedula());
        }
        
        // 3. Obtener Username (opcional, para visualización en el frontend)
        SysUser sysUser = sysUserMapper.selectById(idusuario);
        if (sysUser != null) {
            dto.setUsername(sysUser.getUsername());
        }

        return dto;
    }

    // ===============================================
    // Edición/Inserción (UPSERT LÓGICA)
    // ===============================================
    @Override
    public void editUsuario(UsuarioDto usuarioDto) {
        Long idusuario = usuarioDto.getIdusuario();
        
        // --- 1. VALIDAR EXISTENCIA EN sys_user ---
        if (sysUserMapper.selectById(idusuario) == null) {
            logger.error("Error: El idusuario {} no existe en la tabla public.sys_user.", idusuario);
            throw new BadRequestException("El ID de usuario proporcionado no existe en el sistema base.");
        }
        
        // --- 2. UPSERT en 'academico.personal' ---
        Personal personal = new Personal();
        BeanUtils.copyProperties(usuarioDto, personal); 
        personal.setIdusuario(idusuario); 
        
        if (usuarioMapper.selectById(idusuario) != null) {
            // Existe en Personal: Actualizar
            usuarioMapper.updateById(personal);
            logger.info("Registro PERSONAL ID: {} modificado.", idusuario);
        } else {
            // No existe en Personal: Insertar
            personal.setEstado(1); // Opcional: Establecer estado por defecto en 1 (Activo)
            usuarioMapper.insert(personal);
            logger.info("Registro PERSONAL ID: {} insertado.", idusuario);
        }

        // --- 3. UPSERT en 'academico.datos' (solo para Cédula) ---
        Datos datos = new Datos();
        datos.setCod(idusuario); 
        datos.setCedula(usuarioDto.getCedula());
        
        if (datosMapper.selectById(idusuario) != null) {
            // Existe en Datos: Actualizar
            datosMapper.updateById(datos); 
            logger.info("Registro DATOS ID: {} modificado.", idusuario);
        } else {
            // No existe en Datos: Insertar
            datosMapper.insert(datos); 
            logger.info("Registro DATOS ID: {} insertado.", idusuario);
        }
    }
}