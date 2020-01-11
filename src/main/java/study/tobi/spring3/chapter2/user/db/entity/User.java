package study.tobi.spring3.chapter2.user.db.entity;

import lombok.*;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 08/09/2019
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String id;
    private String name;
    private String password;
}
