package qbots.project.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Role {

    ROLE_ADMIN("Админ"),
    ROLE_PATIENT("Пациент"),
    ROLE_NURSE("Медсестра");

    private final String strName;
}
