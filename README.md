# jwBoot

#### 介绍
简易版web框架实现

本项目实现了一个demo版的web框架，注解配置形式及名称与springboot类似，内置tomcat，可直接打成jar包启动

已实现的功能注解有

@Component、@Configuration、@Bean

@Controller、@RestController、@Service、 @RequestMapping、@GetMapping、@PostMapping、@PutMapping、@DeleteMapping

@Autowired、@Value、@RequestBody、@RequestParam、@ResponseBody

### 创建应用

```java

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

```

### 依赖注入

```java
@Controller
@RequestMapping("/url")
public class TestController {

    @Autowired(Bean.class)
    private Bean beanA;

    @Autowired
    private XXXClass beanB;
    
    @RequestMapping(value = "/test", method = HttpMethod.GET)
    public Object controller1(@RequestParam("name") String name,
                              @RequestParam("age") Integer age){
        System.out.println("收到GET请求数据 => name=" + name + " age="+age);
        return "收到GET请求数据 => name=" + name + " age="+age;
    }

    @ResponseBody
    @PostMapping(value = "/test")
    public Object controller2(@RequestBody BeanB beanB){
        System.out.println("收到POST请求数据 => " + beanB.toString());
        return beanB;
    }
}

```

