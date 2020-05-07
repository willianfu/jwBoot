package bean;

import com.jiawei.jwboot.annotation.component.Component;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class BeanA {
    public BeanA() {
        System.out.println("BeanA 被加载了");
    }
}
