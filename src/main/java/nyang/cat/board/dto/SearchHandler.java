package nyang.cat.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@ToString
public class SearchHandler {

    private Integer page;
    private String option;
    private String keyword;

    public SearchHandler(Integer page, String option, String keyword) {
        this.page = page;
        this.option = option;
        this.keyword = keyword;
    }

    public SearchHandler() {}

    public String getQueryString(Integer page) {
        // ?page=10&option=option&keyword=keyword
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("option", option)
                .queryParam("keyword", keyword)
                .build().toString();
    }

}
