package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserResponseModel {
    private User data;
    private Support support;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String id;
        private String name;
        private String year;
        private String color;
        private String pantone_value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Support {
        private String url;
        private String text;
    }
}
