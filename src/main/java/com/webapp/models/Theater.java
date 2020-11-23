package com.webapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Theater {

    @Data
    @NoArgsConstructor
    public static class Address {
        @NotNull
        @NotBlank
        private	String city;

        @NotNull
        @NotBlank
        private	String state;
        private	String area;

        @NotNull
        @NotBlank
        private	String pincode;
    }

    @Data
    @NoArgsConstructor
    public static class ShortMovie {

        private String id;
        private String name;
    }

    private	String theaterId;
    private String cityId;

    @NotNull
    @NotBlank
    private	String theaterName;

    @NotNull
    private Address address;

    @NotNull
    @Min(1)
    @Max(5)
    private Double rating;
    private List<ShortMovie> movies;

}
