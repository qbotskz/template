package qbots.mektep.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Role {

    ROLE_ADMIN("Админ"),
    ROLE_CLASSROOM_TEACHER("Классный руководитель"),
    ROLE_PARENT("Родитель"),
    ROLE_STUDENT("Ученик"),
    ROLE_PSYCHOLOGIST("Психолог");

    private final String strName;
}
