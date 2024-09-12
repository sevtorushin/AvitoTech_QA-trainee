package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Класс для маппинга JSON объекта объявления из тела HTTP ответа сервера
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Advert {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    private String createdAt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    private String id;
    private String name;
    private Integer price;
    private Integer sellerId;
    private Statistics statistics;

    public Advert(String name, Integer price, Integer sellerId, Statistics statistics) {
        this.name = name;
        this.price = price;
        this.sellerId = sellerId;
        this.statistics = statistics;
    }
}
