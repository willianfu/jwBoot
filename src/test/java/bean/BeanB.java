package bean;

import com.jiawei.jwboot.annotation.component.configuration.Configuration;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Configuration
public class BeanB implements BeanService{

    private String name;

    private Integer age;


    public BeanB() {
        System.out.println("BeanB 被加载了");
    }

    @Override
    public void hello() {
        System.out.println("BeanB 说 hello");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "BeanB{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
