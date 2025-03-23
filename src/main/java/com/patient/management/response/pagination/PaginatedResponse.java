package com.patient.management.response.pagination;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class PaginatedResponse<T> {
    private List<T> result;
    private PaginationInfo paginationInfo;

}
