package util;

import lombok.Getter;

/**
 * Класс для получения ID сохраненного объявления из тела HTTP ответа сервера
 */
@Getter
public class Status {
    private String status;

    public String getId() {
        String[] tokens;
        if (status != null && status.contains("Сохранили объявление")) {
            tokens = status.split(" ");
            return tokens[3];
        } else throw new RuntimeException("invalid status");
    }
}
