# jwBoot

#### 介绍
简易版web框架实现

本项目实现了一个demo版的web框架，注解配置形式及名称与springboot类似，内置tomcat，可直接打成jar包启动

已实现的功能注解有

@Component、@Configuration、@Bean

@Controller、@Service、 @RequestMapping、@GetMapping、@PostMapping、@PutMapping、@DeleteMapping

@Autowired、@Value

###创建应用

```

@JwBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Value("app.name")
    private String appName;


    public static void main(String[] args) {
        JwApplication.run(ApplicationTest.class, args);
    }

    /**
     * 应用启动完成后执行本方法
     *
     * @param args 方法参数
     */
    @Override
    public void run(String... args) {
        System.out.println("启动应用 " + appName);
    }

```java

###依赖注入

```
@Controller
@RequestMapping("/url")
public class TestController {

    @Autowired(Bean.class)
    private Bean beanA;

    @Autowired
    private XXXClass beanB;

    @RequestMapping(value = "/get", method = HttpMethod.POST)
    public Object controller1(){
        return null;
    }

    @GetMapping(value = "/get")
    public Object controller2(){
        return null;
    }

}

```java

