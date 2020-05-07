package controller;

import com.jiawei.jwboot.annotation.component.controller.Controller;
import com.jiawei.jwboot.annotation.component.controller.mapping.HttpMethod;
import com.jiawei.jwboot.annotation.component.controller.mapping.RequestMapping;
import com.jiawei.jwboot.annotation.component.controller.mapping.subinterface.GetMapping;


/**
 * @author : willian fu
 * @version : 1.0
 */
@Controller
@RequestMapping("/url")
public class TestController {


    @RequestMapping(value = "/get", method = HttpMethod.GET)
    public Object controller1(){
        return null;
    }

    @GetMapping(value = "/get")
    public Object controller2(){
        return null;
    }
}
