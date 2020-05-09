package bean;

import com.jiawei.jwboot.annotation.component.Component;
import com.jiawei.jwboot.annotation.component.di.Autowired;
import controller.TestController;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Component
public class BeanA implements BeanService {

    @Autowired
    private TestController controller;

    public BeanA() {
        System.out.println("BeanA 被加载了");
    }

    @Override
    public void hello() {
        System.out.println("BeanA 说 hello" + controller.toString());
    }
}
