# jwBoot

#### 介绍
简易版web框架实现

本项目实现了一个demo版的web框架，注解配置形式及名称与springboot类似，内置tomcat，可直接打成jar包启动

已实现的功能注解有

@Component、@Configuration、@Bean

@Controller、@RestController、@Service、 @RequestMapping、@GetMapping、@PostMapping、@PutMapping、@DeleteMapping

@Autowired、@Value、@RequestBody、@RequestParam、@ResponseBody

### 启动测试用例

1、 拉取项目  

2、 打开jw-boot-test模块下 com.jiawei.jwBoot.JwBootApplicationTest   

3、 点击Main方法启动  
![启动日志](https://images.gitee.com/uploads/images/2020/0512/105352_c1c20b3d_4928216.png "日志.png")  

4、 访问 [http://localhost:8080/jwboot/hello](http://localhost:8080/jwboot/hello) 查看欢迎页  
![jwboot欢迎页](https://images.gitee.com/uploads/images/2020/0512/105156_759eecf5_4928216.png "hello.png")  
 


### 创建应用

```java

@JwBootApplication
public class JwBootApplicationTest implements CommandLineRunner {


    public static void main(String[] args) {
        JwApplication.run(JwBootApplicationTest.class, args);
    }

    /**
     * 应用启动完成后执行本方法
     *
     * @param args 方法参数
     */
    @Override
    public void run(String... args) {
        System.out.println("------------JwBoot应用已启动----------");
    }
}
```

### Bean托管
> 被@Component、@Configuration、@Controller、@RestController、@Service、@JwBootApplication注解的类将会被框架扫描并托管到IOC容器中
> 如果想获取某个被托管的Bean有2种方式
* 使用@Autowired自动注入
* IocContainerContext.getInstance().getBeanByClass(xxBean.class)获取


### Controller
> URI映射路径必须以/开头分隔

```java
@Controller
@RequestMapping("/jwboot")
public class TestController {

    @Autowired
    private TestService testService;

    //没有其他声明，返回值将默认解析为视图
    @GetMapping("/hello")
    public String helloGet(){
        return testService.helloGet();
    }

    //ResponseBody注解的方法将默认返回application/json
    @ResponseBody
    @PostMapping("/hello")
    public Object helloPost(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam("name") String name,
                            @RequestParam(value = "sex", required = false) String sex,
                            @RequestParam(value = "age", defaultValue = "20") Integer age){
        return testService.helloPost(request, response, name, sex, age);
    }

    @ResponseBody
    @PutMapping("/hello")
    public Object helloPut(@RequestBody UserInfoVo userInfo){
        return testService.helloPut(userInfo);
    }

    @ResponseBody
    @DeleteMapping("/hello")
    public Object helloDelete(@RequestBody UserInfoVo userInfo){
        return testService.helloDelete(userInfo);
    }
}
```
也可以全部使用@RequestMapping，但是必须有一个方法声明请求方法
```java
    @RequestMapping(value="/hello", method=HttpMethod.POST)
```
 
也可以使用@RestController直接注解Controller
> 被@RestController注解的类其中所有的方法返回值将默认为application/json

  
#### 依赖注入
> 可以声明为接口/抽象类/类
```java
    //默认注入，当只有一个被IOC托管的实现类时不用声明,可以注入私有属性
    @Autowired
    private TestService testService;

    //声明式注入，当被IOC托管的bean有多个实例时必须声明，否则将报错
    @Autowired(ServiceImpl.class)
    private TestService testService;
```
#### 配置文件注入
> 默认读取resource/application.properties配置文件      
> 只能对基础类型/基础类型的包装类进行配置注入(String Integer Long Short Boolean...)
 ```java
     //注入string
     @Value("app.name")
     private String appName;
 
     //注入Inteager
     @Value("server.port")
     private Integer serverPort;
 ```

#### 配置启动端口
> 在application.properties配置文件中添加 server.port配置即可
-----
