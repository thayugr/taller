package taller1.grupo.vueadmin.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import taller1.grupo.vueadmin.system.entity.dto.Modulo1top10Dto;
import taller1.grupo.vueadmin.system.entity.dto.Modulo1top10DClientesDto;

@Repository
public interface Modulo1Mapper extends BaseMapper<Modulo1top10Dto> {
    @Select("SELECT distinct * FROM northwind.\"Diez mejores pedidos por importe de ventas\"")
    IPage<Modulo1top10Dto> queryModulo1Table(Page<?> page, @Param("blurry") String blurry);

    @Select("SELECT  distinct * FROM northwind.\"Clientes ampliados\" order by idcliente asc ")
    IPage<Modulo1top10DClientesDto> queryModulo1DClientesTable(Page<?> page, @Param("blurry") String blurry);
}
