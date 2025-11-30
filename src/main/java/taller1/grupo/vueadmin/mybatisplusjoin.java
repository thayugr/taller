package taller1.grupo.vueadmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")*/
public class mybatisplusjoin {

    public static void main(String[] args) {
        SampleTest prueba = new SampleTest();
        prueba.testSelect();

        
        SpringApplication.run(mybatisplusjoin.class, args);
    }

}