package data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserData {

    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;

}
