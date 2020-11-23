package com.webapp.models;

import com.webapp.enums.Dimension;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Movie {
    private String movieId;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String genre;

    @NotBlank
    @NotNull
    private String duration;

    @NotNull
    private Dimension movieDimension;

    @Min(1)
    @Max(10)
    @NotNull
    private Double rating;

    @NotNull
    private Date dateReleased;


    private List<String> casts;
    private List<String> languages;
}
