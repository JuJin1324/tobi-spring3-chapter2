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

