package controller;

import bean.BeanA;
import bean.BeanService;
import com.jiawei.jwboot.annotation.component.configuration.Bean;
import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.PostMapping;
import com.jiawei.jwboot.annotation.component.di.Autowired;


/**
 * @author : willian fu
 * @version : 1.0
 */
@Controller
@RequestMapping("/url")
public class TestController {

    @Autowired(BeanA.class)
    private BeanService beanA;

    @RequestMapping(value = "/get", method = HttpMethod.GET)
    public Object controller1(){
        return null;
    }

    @PostMapping(value = "/get")
    public Object controller2(){
        return null;
    }

    public void test(){
        beanA.hello();
    }

}
