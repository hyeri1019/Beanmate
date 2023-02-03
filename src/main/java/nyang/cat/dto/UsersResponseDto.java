package nyang.cat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nyang.cat.entity.SecurityUsers;
import nyang.cat.entity.Users;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponseDto {
    private String email;

    public static UsersResponseDto of(Users users) {
        return new UsersResponseDto(users.getEmail());
    }

}

