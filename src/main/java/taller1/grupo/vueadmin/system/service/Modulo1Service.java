package taller1.grupo.vueadmin.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import taller1.grupo.vueadmin.system.entity.dto.Modulo1top10Dto;
import taller1.grupo.vueadmin.system.entity.Clientes;
import taller1.grupo.vueadmin.system.entity.dto.Modulo1top10DClientesDto;
import taller1.grupo.vueadmin.system.entity.dto.QueryDto;

public interface Modulo1Service {
    public IPage<Modulo1top10Dto> queryModulo1Table(QueryDto queryDto);

    public IPage<Modulo1top10DClientesDto> queryModulo1DClientesTable(QueryDto queryDto);

}
