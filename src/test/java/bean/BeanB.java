package bean;

import com.jiawei.jwboot.annotation.component.configuration.Configuration;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Configuration
public class BeanB {
    public BeanB() {
        System.out.println("BeanB 被加载了");
    }
}