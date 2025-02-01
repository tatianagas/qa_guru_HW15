package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Page2ResponseModel {
    String page, per_page, total, total_pages;
    private List<User> data;
    private Support support;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private int id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Support {
        private String url;
        private String text;
    }
}
