package my.demo.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

@SpringBootApplication(scanBasePackages = {"my.demo.service.user", "my.demo.utils"})
@MapperScan(basePackages = { "my.demo.dao.user" })
@EnableDubbo(scanBasePackages = { "my.demo.service.user" })
public class Application {
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
	}
}