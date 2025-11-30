package taller1.grupo.vueadmin;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.List;
import taller1.grupo.vueadmin.system.mapper.UserMapper;
import taller1.grupo.vueadmin.system.entity.dto.User;
import taller1.grupo.vueadmin.system.entity.dto.Address;
import taller1.grupo.vueadmin.system.entity.dto.UserDTO2;


public class SampleTest {

    @Autowired
    private UserMapper userMapper;

  
    
    public void testSelect() {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)//查询user表全部字段
                .select(Address::getCity, Address::getAddress)
                .leftJoin(Address.class, Address::getUserId, User::getId);

        List<UserDTO2> userList = userMapper.selectJoinList(UserDTO2.class, wrapper);

        userList.forEach(System.out::println);
    }

}