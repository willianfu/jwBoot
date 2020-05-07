package bean;

import com.jiawei.jwboot.annotation.component.Service;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Service
public class BeanC {
    public BeanC() {
        System.out.println("BeanC 被加载了");
    }
}
