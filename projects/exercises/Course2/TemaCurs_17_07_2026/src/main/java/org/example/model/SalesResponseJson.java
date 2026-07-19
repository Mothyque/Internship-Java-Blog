package org.example.model;

import lombok.Data;

import java.util.List;

@Data
public class SalesResponseJson {
    private List<SaleRecord> sales;
}
