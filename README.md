# 2장 테스트
토비의 스프링 3.1 Vol.1 스프링 이해와 원리

## 기초 셋팅
* [1장 오브젝트와 의존관계](https://github.com/JuJin1324/tobi-spring3-chapter1)의
기초 셋팅을 모두 가져옴(Maven 포함)

### 추가 Maven 
```xml
<!-- https://mvnrepository.com/artifact/org.junit/com.springsource.org.junit -->
<dependency>
    <groupId>org.junit</groupId>
    <artifactId>com.springsource.org.junit</artifactId>
    <version>4.7.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${org.springframework-version}</version>
    <scope>test</scope>
</dependency>

```

## 테스트 검증의 자동화
### 테스트의 결과
* 모든 테스트는 <b>성공</b>과 <b>실패</b>의 두 가지 결과를 가짐.
* 테스트 실패의 경우  
    - 에러 : 테스트가 진행되는 동안에 에러가 발생해서 실패하는 경우
    - 테스트 실패 : 테스트 작업 중에 에러가 발생하진 않았지만 그 결과가 기대한 것과 다르게 나오는 경우
 
### JUnit
* JUnit 은 프레임워크이다. 프레임워크의 기본 동작 원리는 IoC(제어의 역전)이며  
프레임워크는 개발자가 만든 클래스에 대한 제어 권한을 넘겨받아서 주도적으로 애플리케이션 흐름을 제어한다.  

* JUnit 에는 main 메서드가 필요 없으며, 테스트를 실행할 메서드는 public 선언 및 위에 @Test 애노테이션이 필요하다.

### 예외조건에 대한 테스트
* 테스트 진행 중에 특정 예외가 던져지면 테스트가 성공한 것이고, 예외가 던져지지 않으면  
테스트 에러를 검증하기 위해서는 @Test 애노테이션에서 파라미터 expected 에 발생해야하는  
exception 클래스를 넣는다.
예제)  
```java
@Test(expected=EmptyResultDataAccessException.class)
public void getUserFailure() throws SQLException {
    ...
    
    UserDao dao = context.getBean("userDao", UserDao.class);
    dao.get("unknown_id");      // DB 상에 존재하지 않는 아이디를 넣었다.
}
```

* 개발자는 빨리 테스트를 만들어 성공하는 것을 보고 다음 기능으로 나아가고 싶어하기 때문에,  
긍정적인 경우를 골라서 성공할 만한 테스트를 먼저 작성하게 되기가 쉽다.  
테스트 작성할 때 부정적인 케이스를 먼저 만드는 습관을 들이는 게 좋다.  

## 테스트가 이끄는 개발
### 테스트 주도 개발, Test Driven Development(TDD)
* TDD는 테스트를 먼저 들고 그 테스트가 성공하도록 하는 코드만 만드는 식으로 진행  

* 테스트는 코드를 작성한 후 가능한 빨리 실행할 수 있어야 한다. 그러려면 테스트없이 한 번에  
너무 많은 코드를 만드는 것은 좋지 않다.

### JUnit 프레임워크 흐름
1. 테스트 클래스에서 @Test가 붙은 public 이고 void형이며 파라미터가 없는 테스트 메서드를 모두 찾는다.
2. 테스트 클래스의 오브젝트를 하나 만든다.
3. @Before 가 붙은 메서드가 있으면 실행.
4. @Test 가 붙은 메서드를 하나 호출하고 테스트 결과를 저장해둔다.
5. @After 가 붙은 메서드가 있으면 실행.
6. 2~5 계속 반복
7. 모든 테스트 결과를 종합해서 돌려준다.

* JUnit 은 @Test 메서드 마다 오브젝트 생성을 하여 @Before -> @Test -> @After 메서드 순으로
각 오브젝트가 실행을 한다.  

* JUnit 개발자는 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 확실히 보장해주기 위해  
매번 새로운 오브젝트를 만들게 했다.

### 픽스처(fixture)
* 픽스처 : 테스트를 수행하는 데 필요한 정보나 오브젝트  
예) UserDaoTest 에서의 dao(UserDao)

* 일반적으로 픽스처는 여러 테스트에서 반복적으로 사용되기 때문에 @Before 메서드를 이용해 생성.

* 빈이 복잡해지고 많아지면 애플리케이션 컨텍스트 생성에 시간이 많이 소요되기 때문에 @Test 메서드마다  
애플리케이션 컨택스트를 생성하는 것은 시간이 많이 걸린다.

* JUnit 은 테스트 클래스 전체에 걸쳐 딱 한 번만 실행되는 @BeforeClass 스태틱 메서드를 지원한다.

### Spring 에서 제공하는 테스트를 위한 애플리케이션 컨택스트 관리
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;
    
}
```
* 스프링 테스트 컨택스트 프레임워크의 JUnit 확장기능 지정
    - @RunWith(SpringJUnit4ClassRunner.class)

* @ContextConfiguration(locations = "/applicationContext.xml")  
를 통해서 빈이 등록되어있는 컨텍스트 configuration(설정) xml 의 위치를 지정한다.

* 스프링 JUnit 확장기능은 테스트가 실행되기 전에 딱 한 번만 애플리케이션 컨택스트를 만들어두고,  
테스트 오브젝트가 만들어질 때마다 특별한 방법을 이용해 애플리케이션 컨텍스트 자신을 테스트 오브젝트의  
특정 필드에 주입해주는 것이다. 일종의 DI라고 볼 수 있다.

      @Autowired
      private ApplicationContext context;

* 스프링 테스트 컨텍스트 프레임워크는 하나의 테스트 클래스 안에서 ApplicationContext 를 공유하는 것이  
전부가 아니라 여러 개의 테스트 클래스에서도 하나의 ApplicationContext 를 공유한다.

* ApplicationContext.xml 에는 ApplicationContext 클래스 빈이 없지만 @Autowired 를 통한  
DI가 가능했다. 이유는 스프링 애플리케이션 컨택스트는 초기화할 때 자기 자신도 빈으로 등록하기 때문이다.

* @Autowired 가 붙은 인스턴스 변수가 있으면, 테스트 컨텍스트 프레임워크는 <b>변수 타입</b>과 일치하는  
컨텍스트 내의 빈을 찾는다. 빈 주입 기준은 다음과 같다.
    - 먼저 변수 타입 일치에 따라 주입
    - 변수 타입이 같은 빈이 여러개인 경우 변수 이름과 빈 이름 매칭하여 주입
    
* @DirtiesContext : 스프링의 테스트 컨텍스트 프레임워크에게 해당 클래스의 테스트에서  
애플리케이션 컨텍스트의 상태를 변경한다는 것을 알려준다.  
예시)
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
@DirtiesContext
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Before
    public void setUp() {
        /* application context 상태 변경 : xml 에 등록된 datasource 임의로 변경 */
        DataSource dataSource = new SingleConnectionDataSource(
                        "jdbc:mysql://localhost/testdb", 
                        "spring", 
                        "book" , 
                        true);
        dao.setDataSource(dataSource);      
    }
}
```

* @RunWith 를 통해서 스프링 컨텍스트는 모든 테스트 클래스에서 공유되지만 @DirtiesContext를 선언한  
클래스와는 공유하지 않는다.(@DirtiesContext를 선언한 클래스에서는 컨텍스트가 따로 만들어지며 해당 테스트 클래스에서만  
사용된다.)

* 스프링 컨테이너로 테스트를 하기에는 스프링 컨테이너를 로드하는 시간이 오래걸리기 때문에 단위 테스트의 경우에는  
스프링 컨테이너없이 자체적으로 DI 하여 테스트를 진행하는 것이 바람직하다.  

## 학습 테스트
* 자신이 만들지 않은 프레임워크나 다른 개발팀에서 만들어서 제공하는 라이브러리 등에 대한 테스트  

* 학습 테스트의 목적은 자신이 사용할 API나 프레임워크의 기능을 테스트 해보면서 사용 방법을 익히는 것 및  
내가 가진 해당 기술에 대한 지식 검증

### 학습 테스트의 장점
* 다양한 조건에 따른 기능을 손쉽게 확인할 수 있다.  
    * 예제를 만들어서 다양한 조건에 따라 어떻게 기능이 다르게 동작하는지 수동으로 값을 입력하는 것이 아닌  
    자동화된 테스트 코드로 다양한 조건에 따라 기능이 어떻게 동작하는지 빠르게 확인할 수 있다.
    
* 학습 테스트 코드를 개발 중에 참고할 수 있다.
    * 예제 코드는 계속해서 수정해가면서 기능을 확인하기 때문에 결국 최종 수정한 예제 코드만 남아있다.  
    반면에 학습 테스트는 다양한 기능과 조건에 대한 테스트 코드를 개별적으로 만들고 남겨둘 수 있다.
    
* 프레임워크나 제품을 업그레이드할 때 호환성 검증을 도와준다.
    * 프레임워크나 상용 제품 등을 새로운 버전으로 업그레이드할 때 API 사용법에 미묘한 변화가 생긴다거나,  
    기존에 잘 동작하던 기능에 문제가 발생할 수도 있다.  
    학습 테스트에 애플리케이션에서 자주 사용하는 기능에 대한 테스트를 만들어놓았다면 새로운 버전의 프레임워크나  
    제품을 학습 테스트에만 먼저 적용해볼 수 있다.
   

    




