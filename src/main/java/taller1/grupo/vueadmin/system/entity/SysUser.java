package taller1.grupo.vueadmin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import taller1.grupo.vueadmin.common.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: tarija
 * @description: this is a class
 * @author: richard sivila
 * @create: 2024
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser extends BaseEntity {

    // @Id 
    // @GeneratedValue   en Jakarta
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    private String username;

    private String nickName;

    private String password;

    private String email;

    private boolean enabled;
}
