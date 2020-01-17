# 2장 테스트
토비의 스프링 3.1 Vol.1 스프링 이해와 원리

## 기초 셋팅
[1장 오브젝트와 의존관계](https://github.com/JuJin1324/tobi-spring3-chapter1)의 기초 셋팅을 모두 가져옴(Maven 포함)

### 추가 Maven 
```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.7</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${org.springframework-version}</version>
    <scope>test</scope>
</dependency>
```

### DB(MySQL) 스키마 및 테이블 생성
```mysql
-- 스키마 생성 : testdb
create database testdb;

-- testdb 스키마에 users 테이블 생성 
create table testdb.users(
    id varchar(10) primary key ,
    name varchar(20) not null ,
    password varchar(10) not null
);
```
## 테스트
테스트의 작성은 스프링의 다양한 기술을 활용하는 방법을 이해하고 검증하고, 실전에 적용하는 방법을 익히는 데 효과적으로 사용될 수 있다.

## 2.1 UserDaoTest 다시 보기

### 2.1.1 테스트의 유용성
테스트란 결국 내가 예상하고 의도했던 대로 코드가 정확히 동작하는지를 확인해서, 만든 코드를 확신할 수 있게 해주는 작업.   
테스트의 결과가 원하는 대로 나오지 않는 경우에는 코드나 설계에 결함이 있을음 알 수 있다.

### 2.1.2 UserDaoTest의 특징
* 자바에서 가장 손쉽게 실행 가능한 main() 메서드 이용
* 테스트할 대상인 UserDao를 직접 호출해서 사용

<b>웹을 통한 DAO 테스트 방법의 문제점</b>
* 기존 웹 프로그램에서 DAO 테스트 방식은 다음과 같았다.   
    
    DAO를 만든다 -> 서비스 계층 및 MVC 프레젠테이션(View) 계층까지 모든 입출력 기능을 대충이라도 만든다. 
    -> 웹 애플리케이션을 서버에 구동한다. -> 웹 화면을 띄우고 폼을 열어 값을 입력한 뒤 버튼을 눌러 등록(Submit) 한다.
    (앞의 부분은 폼의 값을 받아 알맞게 파싱 후에 User 오브젝트를 만들고 UserDao를 호출해주는 기능이 이미 만들어져 있어야 가능하다.)
    -> UserDao를 통해 DB에 insert 하는 작업이면 여기서 끝나지만 select, update를 해야하는 경우 화면에 결과를 출력해야하기 때문에 
    해당 화면(View)를 만들어놔야한다.

* 앞의 테스트 방식에서는 우리가 원하는 것은 UserDao가 재대로 동작하는지 테스트를 하는 것이였지만 선행되어야할 작업이 너무 많다.
(서비스 클래스, 컨트롤러, JSP 뷰 등..)

* 또한 앞의 테스트 방식에서 테스트가 실패한 경우 UserDao를 제외한 다른 부분에서 오류가 발생했을 수 있기 때문에 UserDao가 아닌 다른 부분의 테스트도 필요하다.

<b>작은 단위의 테스트</b>
* 테스트는 가능하면 작은 단위로 쪼개서 집중해서 할 수 있어야 한다.

* 1장에서의 관심사의 분리라는 원리가 여기(테스트)에도 적용된다. 테스트의 관심이 다르다면 테스트할 대상을 분리하고 집중해서 접근해야 한다.

* UserDaoTest로 테스트를 수행할 때는 웹 인터페이스나, 그것을 위한 MVC 클래스, 서비스 오브젝트 등이 <b>필요 없다.</b> 서버에 배포할 필요도 없다. 
즉 에러가 발생했을 때 UserDao 및 데이터베이스 연결에 관한 에러를 제외한 부분(웹 인터페이스나, 그것을 위한 MVC 클래스, 서비스 오브젝트 등)은 
신경 쓸 필요가 없어진다.

이렇게 작은 단위의 코드에 대해 테스트를 수행하는 것을 <b>단위 테스트(Unit Test)</b>라고 한다.

충분히 하나의 관심에 집중해서 효율적으로 테스트할 만한 범위의 단위를 <b>단위</b>로 보면 된다.

엄밀히 DB를 사용하는 것은 단위테스트라기 보다는 통합 테스트에 가깝다. DB의 상태(테이블의 속성 및 테이블 내용 등)가 매번 달라지고, 테스트를 
위해 DB를 특정 상태로 만들어줄 수 없다면 그때는 단위테스트로서 가치가 없어진다.

즉 단위테스트는 외부 환경과 상관없이 매번 테스트 결과가 같아져야한다.
단위테스트를 하는 이유는 개발자가 설계하고 만든 코드가 원래 의도한 대로 동작하는지를 스스로 빨르게 확인받기 위해서이다. 이때 확인 대상과 조건이 간단하고 명확할수록 좋다. 

<b>자동수행 테스트 코드</b>   
위에서 언급한 DAO를 테스트하기 위해서 서비스부터 웹 화면까지 만들어 테스트하는 방식의 경우 개발자 스스로가 테스트마다 매번 웹 화면을 띄우고 
User의 등록 값을 일일히 입력하고 버튼을 누르는 작업을 해야한다. 하지만 UserDaoTest의 경우 User 객체를 만들어 적절한 값을 넣고,
이미 DB 연결 준비까지 다 되어 있는 UserDao 오브젝트를 스프링 컨테이너에서 가져와서 add() 메서드를 호출하고, 그 키 값으로 get()을 호출하는 것까지
자동으로 진행된다.

기존에 만들어진 코드를 수정하면서 무서운 것이 해당 기능의 수정이 다른 기능에 영향을 미쳐서 프로그램에 오류가 나지 않을까이다. 
수정하는 기능에 대한 단위테스트가 있다면 수정 후 빠르게 전체 테스트를 수행해서 수정 때문에 다른 기능에 문제가 발생하지 않았는지 빠른 확인이 가능해진다.

<b>지속적인 개선과 점진적인 개발을 위한 테스트</b>
* 일단 가장 단순한 등록과 조회 기능을 만들고, 이를 테스트로 검증해서 만든 코드에 대한 확신을 갖는다.

* 그리고 거기에 조금씩 기능을 더 추가해가면서 그에 대한 테스트도 함께 추가하는 식으로 점진적인 개발이 가능해진다.

### 2.1.3 UserDaoTest의 문제점
<b>수동 확인 작업의 번거로움</b>  
* add() 메서드를 통해 User 정보를 DB에 등록하고, 이를 다시 get() 메서드를 이용해 가져왔을 때 입력한 값과 가져온 값이 일치하는지를 테스트 코드는
확인해주지 않는다. 단지 콘솔에 값만 출력해줄 뿐이며 사람이 직접 눈으로 확인해야하는 번거로움이 있다.

<b>실행 작업의 번거로움</b>   
* DAO가 한 두개라면 모를까 각 DAO 마다 각 서비스 마다 main 메서드를 만들어서 테스트하기에는 각 main 메서드를 실행시켜주는 작업 및 각각의 
테스트 결과를 눈으로 확인하는 작업이 너무 번거롭다.

## 2.2 UserDaoTest 개선
### 2.2.1 테스트 검증의 자동화
모든 테스트는 <b>성공</b>과 <b>실패</b>의 두 가지 결과를 가짐.

<b>테스트 실패의 경우</b>  
* 에러 : 테스트가 진행되는 동안에 에러가 발생해서 실패하는 경우

* 테스트 실패 : 테스트 작업 중에 에러가 발생하진 않았지만 그 결과가 기대한 것과 다르게 나오는 경우

앞으로의 JUnit을 이용해서 포괄적인 테스트를 만들고나면, 개발한 애플리케이션은 이후에 어떤 과감한 수정을 하고 나서도 테스트를 통해 그 변경에
영향을 받는 부분을 정확히 확인하여 빠르게 조치를 취할 수 있다.

이렇게 개발 과정에서, 또는 유지보수를 하면서 기존 코드에 수정을 할 때 마음의 평안을 얻고, 자신이 만지는 코드에 대해 자신감을 가질 수 있으며,
새로 도입한 기술의 적용에 문제가 없는지 확인이 가능해진다.

### 2.2.2 테스트의 효율적인 수행과 결과 관리
<b>JUnit 테스트로 전환</b> 
* JUnit 은 프레임워크이다. 프레임워크의 기본 동작 원리는 IoC(제어의 역전)이며 프레임워크는 개발자가 만든 클래스에 대한 제어 권한을 넘겨받아서 
    주도적으로 애플리케이션 흐름을 제어한다.  

<b>테스트 메서드 전환</b>
* 테스트가 main() 메서드로 만들어졌다는 건 제어권을 직접 갖는다는 의미이기 때문에 프레임워크에 적용하기에는 적합하지 않다.

* JUnit 에는 main 메서드가 필요 없으며, 테스트를 실행할 메서드는 public 선언 및 위에 @Test 애노테이션이 필요하다.

* main 이름 대신에 테스트의 의도가 무엇인지 알 수 있는 이름으로 적절하게 붙여준다.

<b>검증 코드 전환</b>
* main 메서드에서 만들었던 기존 if/else 문장 -> assertThat() / is() 메서드로 변경

<b>JUnit 테스트 실행</b>
* 책에서 보면 JUnit도 따로 main 메서드를 만들어서 실행시켜주지만 요즘은 IDE를 통해서 따로 main을 사용자가 직접 만들어서 JUnit을 실행시키도록 
하지 않고 IDE를 통해서 JUnit 테스트를 실행한다.

* assertThat()의 조건을 만족하지 못하면 테스트는 더 이상 진행되지 않고 JUnit은 테스트가 실패했음을 알린다. 

## 2.3 개발자를 위한 테스팅 프레임워크 JUnit
### 2.3.1 JUnit 테스트 실행 방법
<b>IDE</b> 
* IDE를 통해서 JUnit 테스트를 직접 실행하게 할 수 있다.

<b>빌드 툴</b> 
* Maven을 통해서 JUnit을 통해 만든 테스트 클래스들을 한꺼번에 돌려볼 수 있으며 결과를 HTML이나 텍스트 파일 형태로 보기 좋게 만들 수도 있다.

### 2.3.2 테스트 결과의 일관성
현재 테스트는 매번 UserDaoTest 실행시에 DB에서 이전 테스트 때 넣어줬던 User 정보를 모두 수동으로 삭제해야한다. 
이렇게 외부 상태에 따라 성공하기도 하고 실패하기도 하는 테스트는 좋은 테스트라고 할 수가 없다.

해당 문제 해결을 위해서 테스트가 끝나면 등록했던 User 정보를 삭제해서, 테스트를 수항하기 이전 상태로 만들어주는 것이다.

deleteAll 메서드 및 getCount() 메서드를 추가한다.
```java
public void deleteAll() throws SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement("delete from users");
    ps.executeUpdate();

    ps.close();
    c.close();
}

public int getCount() throws SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement("select count(*) from users");

    ResultSet rs = ps.executeQuery();
    rs.next();
    int count = rs.getInt(1);

    rs.close();
    ps.close();
    c.close();

    return count;
}
```
Intellij IDEA 사용시에 사용 중인 데이터베이스를 등록해주면 preparedStatement에 쿼리 문법을 자동으로 체크해준다.(기초적 문법)

테스트 이전에 DB에 있던 데이터를 삭제해서 테스트 하는 방법과 테스트가 끝난 이후에 데이터를 삭제하는 방법이 있다. 테스트 후에 데이터를 삭제하였을 때 문제점
은 테스트가 시작하기 이전에 현재 프로젝트 외에 다른 프로젝트에서 DB에 데이터를 남겨놨을 경우에 문제가 될 수 있기 때문에 테스트 시작 전에 데이터를 삭제하는
방식이 더 나아 보인다.

단위테스트는 항상 일관성 있는 결과가 보장돼야 한다는 점을 잊어선 안 된다. 그렇기 위해서는 DB에 남아있는 데이터와 같은 외부 환경에 영향을 받아선 안됨은
물론이고 테스트를 실행하는 순서를 바꿔도 동일한 결과가 보장되도록 만들어야한다. 

### 2.3.3 포괄적인 테스트
테스트를 안 만드는 것도 위험한 일이지만, 성의 없이 테스트를 만드는 바람에 문제가 있는 코드인데도 테스트가 성공하게 만드는 건 더 위험한다.
특히 한 가지 결과만 검증하고 마는 것은 상당히 위험하다.  

<b>get() 예외조건에 대한 테스트</b>

get() 메서드에 전달된 id 값에 해당하는 사용자 정보가 없는 경우 처리 방법
1. null 과 같은 특별한 값 리턴 

2. 정보를 찾을 수 없다고 예외를 던진다.

여기서는 후자인 예외를 던지는 방법으로 처리해본다.

일반적인 JUnit 테스트 중에 예외가 던져지면 테스트 메서드의 실행이 중단되고 테스트는 실패한다. assertThat()을 통한 검증실패가 아닌 테스트 자체 에러라고
볼 수 있다. 즉 assertThat() 메서드로 예외 발생을 검증할 수가 없다.

현재 우리는 get() 메서드에서 id 값에 해당하는 사용자 정보가 없을 경우 제대로 예외가 발생하는지 테스트를 하고 싶으며 메서드 도중 예외가 발생하면 
테스트가 도중 종료되는 것이 아닌 테스트에 성공한 것으로 처리하고싶다.   
=> @Test 애노테이션에서 파라미터 expected 에 발생해야하는 exception 클래스를
넣어서 해당 exception이 발생하면 테스트가 성공한 것으로 처리되도록, exception이 발생하지 않고 정상 처리되면 테스트가 실패한 것으로 처리되도록 한다.   

예제   
```java
@Test(expected=EmptyResultDataAccessException.class)
public void getUserFailure() throws SQLException {
    ...
    
    UserDao dao = context.getBean("userDao", UserDao.class);
    dao.get("unknown_id");      // DB 상에 존재하지 않는 아이디를 넣었다.
}
```

개발자는 빨리 테스트를 만들어 성공하는 것을 보고 다음 기능으로 나아가고 싶어하기 때문에, 긍정적인 경우를 골라서 성공할 만한 테스트를 먼저 작성하게 되기가 쉽다. 
테스트 작성할 때 부정적인 케이스를 먼저 만드는 습관을 들이는 게 좋다.  

### 2.3.4 테스트가 이끄는 개발
<b>기능설계를 위한 테스트</b>
* 기능을 먼저 만들어놓고 테스트를 진행한 것이 아니라 추가하고 싶은 기능을 테스트로 만들고(getUserFailure) 실제 코드를 수정하였다.

<b>테스트 주도 개발, Test Driven Development(TDD)</b>
* 실행 코드를 먼저 만들고 테스트를 만들면 솔직히 만들기 너무 귀찮다.

* TDD는 테스트를 먼저 들고 그 테스트가 성공하도록 하는 코드만 만드는 식으로 진행하기 때문에 테스트를 놓칠 일이 없다.

* 테스트는 코드를 작성한 후 가능한 빨리 실행할 수 있어야 한다. 그러려면 테스트없이 한 번에 너무 많은 코드를 만드는 것은 좋지 않다.

### 2.3.5 테스트 코드 개선
리팩토링 : 중복되는 코드를 메서드로 따로 추출
```java
ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
UserDao dao = context.getBean("userDao", UserDao.class);
```
위의 중복된 코드를 별로의 메서드로 추출하는 것이 가장 일반적인 방법이지만 JUnit에서 제공하는 <b>@Before</b> 애노테이션이 붙은 <b>setUp()</b>메서드를 추가한다.

<b>@Before</b> 애노테이션 메서드는 테스트 클래스를 실행할 때 가장 먼저 실행되는 메서드로 테스트 메서드에서 공통으로 사용할 변수를 멤버변수로 선언하여 <b>@Before</b> 애노테이션 메서드에서 초기화한다. 

<b>JUnit 프레임워크 흐름</b>
1. 테스트 클래스에서 @Test가 붙은 public 이고 void형이며 파라미터가 없는 테스트 메서드를 모두 찾는다.
2. 테스트 클래스의 오브젝트를 하나 만든다.
3. @Before 가 붙은 메서드가 있으면 실행.
4. @Test 가 붙은 메서드를 하나 호출하고 테스트 결과를 저장해둔다.
5. @After 가 붙은 메서드가 있으면 실행.
6. 2~5 계속 반복
7. 모든 테스트 결과를 종합해서 돌려준다.

JUnit 은 @Test 메서드 마다 오브젝트 생성을 하여 @Before -> @Test -> @After 메서드 순으로 각 오브젝트가 실행을 한다.  

@Before나 @After 메서드를 테스트 메서드에서 직접 호출하지 않기 때문에 서로 주고받을 정보나 오브젝트가 있는 경우는 멤버 변수를 이용해야한다.

JUnit 개발자는 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 확실히 보장해주기 위해 매번 새로운 오브젝트를 만들게 했다.

픽스처(fixture) : 테스트를 수행하는 데 필요한 정보나 오브젝트  
* 예시 : UserDaoTest 에서의 dao(UserDao)

* 예시 : add() 메서드에 전달하는 User 오브젝트들도 픽스처라고 볼 수 있다.   

일반적으로 픽스처는 여러 테스트에서 반복적으로 사용되기 때문에 @Before 메서드를 이용해 생성.

## 2.4 스프링 테스트 적용
@Before 메서드가 테스트 메서드 갯수만큼 반복되기 때문에 애플리케이션 컨텍스트도 세 번 만들어짐.   

→ 애플리케이션 컨텍스트를 만드는 작업은 생각보다 자원 및 시간 소요가되며 어떤 빈은 독립적인 스레드를 띄우기도 한다.   
이런 경우 테스트를 마칠 때마다 애플리케이션 컨텍스트 내의 빈이 할당한 리소스를 깔끔하게 정리해주지 않으면 
다음 테스트에서 새로운 애플리케이션 컨텍스트가 만들어지면서 문제를 일으킬 수도 있다.   

→ JUnit 은 테스트 클래스 전체에 걸쳐 딱 한 번만 실행되는 @BeforeClass 스태틱 메서드를 지원한다.   
이 메서드로 애플리케이션 컨텍스트를 메서드 갯수와 상관없이 한번만 만들어서 모든 테스트 메서드에서 사용할 수 있도록 해보자.

### 2.4.1 테스트를 위한 애플리케이션 컨텍스트 관리
Spring 에서 제공하는 테스트를 위한 애플리케이션 컨택스트 관리
```java
@RunWith(SpringJUnit4ClassRunner.class)
/* locations -> classes 
   SpringBoot로 넘어가면서 XML 설정파일보다는 Java 클래스를 사용 */
//@ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {

    @Autowired
    private ApplicationContext context; 
}
```

JUnit 프레임워크의 테스트 실행 확장기능 지정 
* @RunWith(SpringJUnit4ClassRunner.class)

위와 같이 지정해주면 JUnit이 테스트를 진행하는 중에 테스트가 사용할 애플리케이션 컨텍스트를 만들고 관리하는 작업을 진행해준다.

* @ContextConfiguration(classes = DaoFactory.class)

위의 애노테이션을 통해 빈이 등록되어있는 configuration(설정) 자바 클래스의 위치를 지정한다.

<b>테스트 메서드의 컨텍스트 공유</b>
* 스프링 JUnit 확장기능은 테스트가 실행되기 전에 딱 한 번만 애플리케이션 컨택스트를 만들어두고, 
테스트 오브젝트가 만들어질 때마다 특별한 방법을 이용해 애플리케이션 컨텍스트 자신을 테스트 오브젝트의 특정 필드에 주입해주는 것이다.    
일종의 DI라고 볼 수 있다. (스프링 애플리케이션 컨택스트는 초기화할 때 자기 자신도 빈으로 등록하기 때문에 테스트 클래스에서 다음과 같이 사용이 가능하다.)
```java
@Autowired
private ApplicationContext context;
```

스프링 테스트 컨텍스트 프레임워크는 하나의 테스트 클래스 안에서 ApplicationContext를 공유하는 것이 전부가 아니라 설정파일 
<b>@ContextConfiguration(classes = DaoFactory.class)</b>을 여러 개의 테스트 클래스에서 같게 설정해주면 
여러개의 테스트 클래스에서 하나의 ApplicationContext 를 공유하게 된다.

<b>@Autowired</b>
* @Autowired 가 붙은 인스턴스 변수가 있으면, 테스트 컨텍스트 프레임워크는 <b>변수 타입</b>과 일치하는 컨텍스트 내의 빈을 찾아서 주입해준다.
* DaoFactory 클래스에는 ApplicationContext 클래스 빈이 없지만 <b>@Autowired</b> 를 통한 DI가 가능했다. 
이유는 스프링 애플리케이션 컨택스트는 초기화할 때 자기 자신도 빈으로 등록하기 때문이다.

@Autowired 가 붙은 인스턴스 변수가 있으면, 테스트 컨텍스트 프레임워크는 <b>변수 타입</b>과 일치하는 컨텍스트 내의 빈을 찾는다. 빈 주입 기준은 다음과 같다.
* 먼저 <b>변수 타입</b> 일치에 따라 주입

* 변수 타입이 같은 빈이 여러개인 경우 <b>변수 이름과 빈 이름 매칭</b>하여 주입

* 변수 이름으로도 빈을 찾을 수 없는 경우에는 예외가 발생한다.

테스트시 DI를 할 때 구현 클래스를 선언하는 것이 좋을까 인터페이스를 선언하여 느슨하게 연결하는 것이 좋을까?
* 구현 클래스 오브젝트 자체에 관심이 있는 경우가 있을 수 있다.
구현 클래스의 메서드를 직접 이용해서 테스트를 해야할 경우 등. 
이럴 때는 구현 클래스를 선언하여 DI를 실행한다.(아직 구체적으로 어느 상황인지는 모르겠음)

* 테스트는 필요하다면 얼마든지 애플리케이션 클래스와 밀접한 관계를 맺고 있어도 상관없다. 코드 내부구조와 설정 등을 알고 있고 의도적으로 그 내용을
검증해야 할 필요가 있기 때문에.

* 하지만, 꼭 필요하지 않다면 일단 코드처럼 인터페이스를 사용해서 느슨한 연결을 하는 편이 좋다.

### 2.4.2 DI와 테스트
하나의 정해진 클래스만 사용할 건데도 인터페이스를 두고 DI를 해야하는 이유
* 첫번째. 개발에서 절대 바뀌지 않는 것은 없다. => 클래스가 변경될 가능성은 0프로가 아니다.

* 두번째. 다른 차원의 서비스 기능을 도입할 수 있다. => CountingConnectionMaker처럼 기능 + 알파를 추가할 수 있다.

<b>테스트 코드에 의한 DI</b> 
* 현재 DaoFactory 클래스에서 설정한 DataSource의 DB가 운영 DB라고 가정해보자. 
테스트할 시에 운영 DB를 가지고 더미 데이터를 생성, 수정, 삭제하는 짓은 미친짓임으로 해당 테스트 작업은 테스트 DB를 연결하여 테스트해보자.
(DataSource 변경)

@DirtiesContext : 스프링의 테스트 컨텍스트 프레임워크에게 해당 클래스의 테스트에서만 애플리케이션 컨텍스트의 상태를 변경
(이미 @Autowired 한 UserDao에 새로운 dataSource를 주입)한다는 것을 알려준다.  

예시
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
@DirtiesContext
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Before
    public void setUp() {
        /* application context 상태 변경 : xml 에 빈 등록한 dao(UserDao)의 DI dataSource를 임의로 변경 */
        DataSource dataSource = new SingleConnectionDataSource(
                        "jdbc:mysql://localhost/testdb", 
                        "spring", 
                        "book" , 
                        true);
        dao.setDataSource(dataSource);      
    }
}
```

※ 해당 코드의 문제점 : 애플리케이션 컨텍스트가 관리하는 빈(Bean)인 UserDao의 기존 의존관계를 무시하고 강제로 DI 하였다.(심지어 빈 객체도 아님)
모든 테스트 클래스는 단 하나의 애플리케이션 컨택스트를 사용함으로 UserDao의 바뀐 의존관계는 다음 테스트하는 클래스에도 영향을 주게된다.

@RunWith 를 통해서 스프링 컨텍스트는 모든 테스트 클래스에서 공유되지만 @DirtiesContext를 선언한 클래스와는 공유하지 않는다.
(@DirtiesContext를 선언한 클래스에서는 컨텍스트가 따로 만들어지며 해당 테스트 클래스에서만 사용된다.)

<b>테스트를 위한 별도의 DI 설정</b>
* 애플리케이션 컨텍스트를 해당 클래스 때문에 따로 만들어야하고 빈 객체도 아닌 오브젝트를 주입하는 것도 단점이 된다.

* 차라리 DaoFactory에서 DB연결 부분(스키마 spring3 -> testdb)을 변경한 TestDaoFactory를 Configuration으로 추가하는 것이 바람직하다. 

* 컨테이너 없는 DI 테스트 : 현재 테스트는 스프링 자체 기능은 사용하지 않고 있다.(DI는 스프링 자체 기능이 아님)
또한 스프링 컨테이너로 테스트를 하기에는 스프링 컨테이너를 로드하는 시간이 오래걸리기 때문에 단위 테스트의 경우에는 스프링 컨테이너없이 자체적으로 DI 하여 
테스트를 진행하는 것이 바람직하다.  

<b>DI를 이용한 테스트 방법 선택</b>
* 항상 스프링 컨테이너 <b>없이</b> 테스트할 수 있는 방법을 가장 우선적으로 고려한다.

* 의존관계가 복잡한 오브젝트에 대해서 테스트할 시에 스프링 컨테이너의 도움을 받으면 편하다. 대신 테스트 전용 설정파일(자바 클래스 혹은 XML)을 따로 만들어서 테스트한다.

### 2.5 학습 테스트로 배우는 스프링
학습테스트 : 자신이 만들지 않은 프레임워크나 다른 개발팀에서 만들어서 제공하는 라이브러리 등에 대한 테스트  

학습 테스트의 목적은 자신이 사용할 API나 프레임워크의 기능을 테스트 해보면서 사용 방법을 익히는 것 및 내가 가진 해당 기술에 대한 지식 검증

### 2.5.1 학습 테스트의 장점
다양한 조건에 따른 기능을 손쉽게 확인할 수 있다.  
* UI 및 서비스를 만들어서 다양한 조건에 따라 어떻게 기능이 다르게 동작하는지 수동으로 값을 입력하는 것이 아닌 
자동화된 테스트 코드로 다양한 조건에 따라 기능이 어떻게 동작하는지 빠르게 확인할 수 있다.
    
학습 테스트 코드를 개발 중에 참고할 수 있다.
* 수동으로 예제를 만드는 방법은 계속해서 수정해가면서 기능을 확인하기 때문에 결국 최종 수정한 예제 코드만 남아있다.
반면에 학습 테스트는 다양한 기능과 조건에 대한 테스트 코드를 개별적으로 만들고 남겨둘 수 있다.
    
프레임워크나 제품을 업그레이드할 때 호환성 검증을 도와준다.
* 프레임워크나 상용 제품 등을 새로운 버전으로 업그레이드할 때 API 사용법에 미묘한 변화가 생긴다거나, 기존에 잘 동작하던 기능에 문제가 발생할 수도 있다. 
학습 테스트에 애플리케이션에서 자주 사용하는 기능에 대한 테스트를 만들어놓았다면 새로운 버전의 프레임워크나 제품을 학습 테스트에만 먼저 적용해볼 수 있다.
   
### 2.5.2 학습 테스트 예제
JUnit 자체 테스트 클래스인 JUnitTest.java 생성
* 각 메서드마다 다른 테스트 객체(JUnitTest Object)가 생성되어서 유닛테스트를 진행하는 것을 검증해본다.

<b>스프링 테스트 컨텍스트 테스트</b>
* JUnit과 반대로 스프링의 테스트용 애플리케이션 컨텍스트는 테스트 갯수에 상관없이 한 개만 만들어지는 것을 검증해본다.

### 테스트 검증 메서드 정리
JUnit static 메서드

<b>assertThat()</b>
* `assertThat(T actual, Matcher<? super T> matcher)`의 형태로 메서드를 사용하여 두 값을 비교할 수 있다. 
* 첫번째 파라미터에는 비교대상 값을, 두번째 파라미터로는 비교로직이 담긴 Matcher가 사용된다.
```java
int result = calculator.add(4, 6);
assertThat(result, is(10));
```

<b>assertTrue()</b>
```java 
assertThat() + is(true)
```

<b>either().or()</b>
* either A or B 형식으로 matcher를 사용할 수 있게 해 준다.  
A, B 매쳐 둘중 하나가 성공할 경우 테스트가 성공한다.  
```java
assertThat("fan", either(containsString(“a”)).or(containsString(“b”)))
```

Package : org.hamcrest.CoreMatchers 

<b>is()</b>   
is는 equals() 비교를 해서 같으면 성공 처리하며, 두가지 용도로 사용할 수 있다.
* A is B와 같이 비교값이 서로 같은지 여부를 확인할 경우  
```java
assertThat("Simple Text", is("Simple Text"));
```   
이경우 `assertThat("Simple Text", is(equalTo("Simple Text")))`와 동일하게 사용할 수 있다.   

* 다른 매쳐를 꾸며주는 용도로 사용. 매쳐에는 영향을 끼치지 않으며, 조금 더 표현력이 있도록 변경하여 준다.  
```
assertThat("Simple Text", is(not("simpleText")));
```   
위의 경우 is가 빠지더라도 문제없이 작동된다. 하지만 is가 있음으로써 자연스럽게 읽혀지는 테스트 코드가 된다.
    
<b>not()</b>   
is와 동일하게 두가지 경우로 사용할 수 있다.
* 내부에 매쳐를 선언할 경우 내부 매쳐의결과를 뒤집는다.  
`assertThat(cheese, is(not(equalTo(smelly))))`

* not뒤에 값이 나올 경우, 같지 않을 경우 테스트가 통과한다.(확실히 is not이 아닌 not만 쓰니까 허전하다.)
`assertThat("Test", not("tEST"));`

<b>sameInstance()</b>   
비교매쳐의 값과 같은 인스턴스일 경우 테스트가 통과한다. theInstance 와 동일  
```java 
assertThat("Test", not(sameInstance("not Same Instance")));
```
    
<b>hasItem()</b>   
배열에서 매쳐가 통과하는 값이 하나 이상이 있는지 여부를 검사한다.
```java 
assertThat(Arrays.asList("foo", "bar"), hasItem("bar"));
```
    
<b>equalTo()</b>    
is() 와 동일   
※ 주의 : JUnitTest 예제에서 다음 부분은 문제가 있음.
```java
@Autowired
static ApplicationContext context;
```
@Autowired 애노테이션 사용시에 사용 변수에 static을 붙이면 해당 변수 사용 불가능.
다음과 같은 warning이 로그에 남는다.
```log
경고: Autowired annotation is not supported on static fields: static org.springframework.context.ApplicationContext study.tobi.spring3.chapter2.JUnitTest.context
```

### 2.5.3 버그 테스트
버그 테스트란 코드에 오류가 있을 때 그 오류를 가장 잘 드러낼줄 수 있는 테스트를 말한다.

버그 테스트는 실패하도록 만들어야 한다. 버그가 원인이 되서 테스트가 실패하는 코드를 만드는 것이다. 
그러고 나서 버그 테스트가 성공할 수 있도록 애플리케이션 코드를 수정하고 테스트가 성공하면 버그 해결.   

버그 테스트의 장점들은 다음과 같다.
* 테스트의 완성도를 높여준다 : 테스트는 잘 동작하는 상태에 대해서만 하는 것이 아닌 오류가 났을 때 대처에 및 오류 발생의 가능성 또한 테스트하는 것이 좋다.

* 버그의 내용을 명확하게 분석하게 해준다 : 버그를 테스트로 만들어 발생하게 하려면 해당 버그의 내용을 명확하게 분석해야한다.

* 기술적인 문제를 해결하는 데 도움이 된다 : 문제 발생 시 해당 문제가 다시 발생할 수 있도록 코드로 재현을 해놓으면 해당 문제를 고치는데 도움이 된다.

## 참조 자료 링크
* [assertThat](http://sejong-wiki.appspot.com/assertThat)
