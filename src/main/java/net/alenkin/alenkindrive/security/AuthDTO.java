package net.alenkin.alenkindrive.security;

import lombok.Data;
import org.springframework.http.ResponseEntity;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Data
public class AuthDTO {
    private String name;
    private String password;
}
