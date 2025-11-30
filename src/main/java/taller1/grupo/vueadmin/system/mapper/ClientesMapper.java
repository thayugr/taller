package taller1.grupo.vueadmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import taller1.grupo.vueadmin.system.entity.*;
import taller1.grupo.vueadmin.system.entity.dto.ClientesDto;
import taller1.grupo.vueadmin.system.entity.dto.ClientesProductosDto;

@Repository
public interface ClientesMapper extends BaseMapper<Clientes> {

    IPage<ClientesDto> queryClientesTable(Page<?> page, @Param("blurry") String blurry);

    List<Clientes> getClientesByClientesId(@Param("clientesId") Long clientesId);

    @Select("SELECT * FROM northwind.\"Resumen de pedidos\" where iddecliente=#{id}")
    List<ClientesProductosDto> queryModulo1TableClientesPedido(@Param("id") Long id);
}