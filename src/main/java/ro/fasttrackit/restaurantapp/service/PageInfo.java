package ro.fasttrackit.restaurantapp.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PageInfo {
    int totalPages;
    int totalElements;
    int crtPage;
    int pageSize;
}
