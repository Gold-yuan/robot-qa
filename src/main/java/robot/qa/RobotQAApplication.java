package robot.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RobotQAApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RobotQAApplication.class, args);
    }

    /**
     * tomcat run: extends SpringBootServletInitializer
     * 
     * @Override configure
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RobotQAApplication.class);
    }
}
