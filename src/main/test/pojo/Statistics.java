package pojo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс для маппинга JSON объекта статистики из тела HTTP ответа сервера
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Statistics {
    private Integer contacts;
    private Integer likes;
    private Integer viewCount;

    public Statistics() {
        this.contacts = 0;
        this.likes = 0;
        this.viewCount = 0;
    }
}
