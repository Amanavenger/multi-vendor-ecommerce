package com.multivendor.Product.Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkUploadResponse {
    Integer created;
    Integer updated;
    Integer errors;
}
