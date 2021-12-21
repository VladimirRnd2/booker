package com.zuzex.booker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {

    private Long id;

    @JsonIgnore
    private Date created;

    @JsonIgnore
    private Date updated;

    @JsonIgnore
    private Status status;
}
