import com.jiawei.jwboot.CommandLineRunner;
import com.jiawei.jwboot.JwApplication;
import com.jiawei.jwboot.annotation.JwBootApplication;
import com.jiawei.jwboot.annotation.component.di.Value;
import com.jiawei.jwboot.mvc.ioc.IocContainerContext;
import controller.TestController;

/**
 * @author : willian fu
 * @version : 1.0
 */
@JwBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Value("app.name")
    private String appName;

    @Value("app.author")
    private String author;

    public static void main(String[] args) {
        JwApplication.run(ApplicationTest.class, args);
        //IocContainerContext.getInstance().getBeanByClass(TestController.class).test();
    }

    /**
     * 应用启动完成后执行本方法
     *
     * @param args 方法参数
     */
    @Override
    public void run(String... args) {
        System.out.println("启动应用 " + appName + " => " + author);
    }
}
