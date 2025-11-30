package taller1.grupo.vueadmin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import taller1.grupo.vueadmin.common.exception.BadRequestException;
import taller1.grupo.vueadmin.common.utils.StringUtil;
import taller1.grupo.vueadmin.constant.CommonConstants;
import taller1.grupo.vueadmin.system.entity.Clientes;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;
import taller1.grupo.vueadmin.system.entity.dto.ClientesDto;
import taller1.grupo.vueadmin.system.entity.dto.ClientesProductosDto;
import taller1.grupo.vueadmin.system.entity.dto.Clientes2Dto;
import taller1.grupo.vueadmin.system.mapper.ClientesMapper;
import taller1.grupo.vueadmin.system.service.ClientesService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ClientesServiceImpl implements ClientesService {
    private final ClientesMapper clientesMapper;
    private static final Logger logger = LoggerFactory.getLogger(ClientesService.class);

    @Override
    public List<Clientes> getClientesList(String blurry) {
        LambdaQueryWrapper<Clientes> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(blurry)) {
            wrapper.like(Clientes::getNombre, blurry);
            wrapper.or();
            wrapper.like(Clientes::getApellidos, blurry);
        }
        wrapper.ne(Clientes::getNombre, CommonConstants.ROLE_ADMIN);

        return clientesMapper.selectList(wrapper);
    }
    @Override
    public IPage<ClientesDto> queryClientesTable(QueryDto queryDto) {
        Page<ClientesDto> page = new Page<>();
        page.setCurrent(queryDto.getCurrentPage());
        page.setSize(queryDto.getSize());
        return clientesMapper.queryClientesTable(page, queryDto.getBlurry());
    }

    private void checkClientes(ClientesDto clientesDto) {
        LambdaQueryWrapper<Clientes> wrapper = new LambdaQueryWrapper<>();

        if (clientesDto.getId() != null) {
            wrapper.ne(Clientes::getIdcliente, clientesDto.getId());
        }
        if (StringUtil.isNotBlank(clientesDto.getNombre()) && StringUtil.isNotBlank(clientesDto.getApellidos())) {
            wrapper.and(w -> w.eq(Clientes::getNombre, clientesDto.getNombre()).or().eq(Clientes::getApellidos,
                    clientesDto.getApellidos()));
        }
        long count = clientesMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BadRequestException("El nombre de cliente Por favor vuelve a entrar");
        }

    }

    private void checkClientes(Long id) {
        if (clientesMapper.selectById(id).getIdcliente() != null)
            System.out.println("existe el cliente");
        else
            System.out.println("no existe el cliente");

    }

    private void checkClientes2(Clientes2Dto clientes2Dto) {
        LambdaQueryWrapper<Clientes> wrapper = new LambdaQueryWrapper<>();

        if (clientes2Dto.getIdcliente() != null) {
            wrapper.ne(Clientes::getIdcliente, clientes2Dto.getIdcliente());
        }
        /*
         * if (StringUtil.isNotBlank(clientes2Dto.getNombre()) &&
         * StringUtil.isNotBlank(clientes2Dto.getApellidos())) {
         * wrapper.and(w -> w.eq(Clientes::getNombre,
         * clientes2Dto.getNombre()).or().eq(Clientes::getApellidos,
         * clientes2Dto.getApellidos()));
         * }
         */
        /*
         * long count = clientesMapper.selectCount(wrapper);
         * if (count > 0) {
         * throw new
         * BadRequestException("El nombre de cliente Por favor vuelve a entrar");
         * }
         */

    }

    @Override
    public void editClientes(Clientes2Dto clientes2Dto) {
        checkClientes2(clientes2Dto);
        Clientes clientes = new Clientes();
        clientes.setIdcliente(clientes2Dto.getIdcliente());
        clientes.setCompania(clientes2Dto.getCompania());
        clientes.setApellidos(clientes2Dto.getApellidos());
        clientes.setNombre(clientes2Dto.getNombre());
        clientes.setDirecciónDeCorreoElectrónico(clientes2Dto.getDirecciónDeCorreoElectrónico());
        clientes.setCargo(clientes2Dto.getCargo());
        clientes.setTeléfonoDelTrabajo(clientes2Dto.getTelefonoDelTrabajo());
        clientes.setTeléfonoParticular(clientes2Dto.getTelefonoParticular());
        clientes.setTeléfonoMóvil(clientes2Dto.getTelefonoMóvil());
        clientes.setNúmeroDeFax(clientes2Dto.getNumeroDeFax());
        clientes.setDirección(clientes2Dto.getDireccinn());
        clientes.setCiudad(clientes2Dto.getCiudad());
        clientes.setEstadoOProvincia(clientes2Dto.getEstadoOProvincia());
        clientes.setCPostal(clientes2Dto.getCPostal());
        clientes.setPaísORegión(clientes2Dto.getPaisORegion());
        clientes.setPáginaWeb(clientes2Dto.getPaginaWeb());
        clientes.setNotas(clientes2Dto.getNotas());
        clientes.setDatosAdjuntos(clientes2Dto.getDatosAdjuntos());
        if (clientes.getIdcliente() != null) {

            clientesMapper.updateById(clientes);
        } else {
            QueryWrapper<Clientes> query = new QueryWrapper<>();
           
            clientes.setIdcliente(clientesMapper.selectCount(query));
             System.out.print("total cliente:"+clientesMapper.selectCount(query).toString());
             System.out.print("insertar cliente:"+clientes.toString());
             clientesMapper.insert(clientes);
        }
    }

    @Override
    public List<Clientes> getClientesByClientesId(Long clientesId) {
        return clientesMapper.getClientesByClientesId(clientesId);
    }

    @Override
    public void deleteByClientesId(Long id) {
        LambdaQueryWrapper<Clientes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Clientes::getIdcliente, id);
        clientesMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public void delClientes(Long id) {
        // Verificar si el rol ha estado vinculado al usuario
        checkClientes(id);
        // Primero elimine el menú vinculado al personaje.
        this.deleteByClientesId(id);
        // Eliminar el rol nuevamente
        clientesMapper.deleteById(id);
    }

    public List<ClientesProductosDto> queryModulo1TableClientesPedido(Long id) {
        return clientesMapper.queryModulo1TableClientesPedido(id);

    }
}
