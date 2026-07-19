package org.example;

import org.example.model.SaleRecord;
import org.example.model.SalesResponseJson;
import org.example.util.SalesJsonReader;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        SalesResponseJson salesResponse = SalesJsonReader.readSales("Sales.json").orElse(null);
        if (salesResponse == null || salesResponse.getSales() == null) {
            System.err.println("Nu s-au putut încărca vânzările.");
        }
        List<SaleRecord> saleRecords = SalesJsonReader.readSales("Sales.json")
                .map(SalesResponseJson::getSales)
                .orElse(Collections.emptyList());
        Set<String> models = getModels(saleRecords);
//        System.out.println(showSalesFrom2023(saleRecords));
//        System.out.println(salesCountPerPerson(saleRecords));
//        System.out.println(salesAmountPerPerson(saleRecords, "Eve Martin"));
//        System.out.println(salesAmountPerYear(saleRecords, 2023));
//        System.out.println(models);
//        System.out.println(top3Models(saleRecords));
//        System.out.println(getMostExpensiveSale(saleRecords));
//        System.out.println(getLeastExpensiveSale(saleRecords));
//        System.out.println(salesPerMonth(saleRecords, 2023));
//        System.out.println(salesPerQuarter(saleRecords, 2023));
//        System.out.println(salesOverPrice(saleRecords, 100000.0));
//        System.out.println(getSalesPersons(saleRecords));
//        System.out.println(sortByDateAndPrice(saleRecords));
//        System.out.println(totalSales(saleRecords));
        System.out.println(topNSalesPersons(saleRecords, 3));
    }

    public static List<SaleRecord> showSalesFrom2023(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .filter(saleRecord -> saleRecord.getTransactionDate().getYear() == 2023)
                .toList();
    }

    public static Map<String, Integer> salesCountPerPerson(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .collect(Collectors.groupingBy(SaleRecord::getSalesPerson, Collectors.summingInt(sale -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    public static Double salesAmountPerPerson(List<SaleRecord> saleRecords, String salesPerson)
    {
        return saleRecords.stream()
                .filter(saleRecord -> saleRecord.getSalesPerson().equals(salesPerson))
                .mapToDouble(SaleRecord::getPrice)
                .sum();
    }

    public static Double salesAmountPerYear(List<SaleRecord> saleRecords, Integer year)
    {
        return saleRecords.stream()
                .filter(saleYear -> saleYear.getTransactionDate().getYear() == year)
                .mapToDouble(SaleRecord::getPrice)
                .sum();
    }

    public static Set<String> getModels(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .map(SaleRecord::getProduct)
                .collect(Collectors.toSet());
    }

    public static Map<String, Integer> top3Models(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .collect(Collectors.groupingBy(SaleRecord::getProduct, Collectors.summingInt(saleRecord -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    public static String getMostExpensiveSale(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .max(Comparator.comparing(SaleRecord::getPrice))
                .map(saleRecord -> saleRecord.getProduct() + " - " + saleRecord.getPrice())
                .orElse(null);
    }

    public static String getLeastExpensiveSale(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .min(Comparator.comparing(SaleRecord::getPrice))
                .map(saleRecord -> saleRecord.getProduct() + " - " + saleRecord.getPrice())
                .orElse(null);
    }

    public static Map<Month, Integer> salesPerMonth(List<SaleRecord> saleRecords, Integer year)
    {
        return saleRecords.stream()
                .filter(saleRecord -> saleRecord.getTransactionDate().getYear() == year)
                .collect(Collectors.groupingBy(saleRecord -> saleRecord.getTransactionDate().getMonth(), Collectors.summingInt(saleRecord -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Month, Integer>comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    public static Map<String, Integer> salesPerQuarter(List<SaleRecord> saleRecords, Integer year)
    {
        return saleRecords.stream()
                .filter(saleRecord -> saleRecord.getTransactionDate().getYear() == year)
                .collect(Collectors.groupingBy(saleRecord -> {
                    int month = saleRecord.getTransactionDate().getMonth().getValue();
                    if (month <= 3) {
                        return "Q1";
                    } else if (month <= 6) {
                        return "Q2";
                    } else if (month <= 9) {
                        return "Q3";
                    } else {
                        return "Q4";
                    }
                }, Collectors.summingInt(saleRecord -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    public static List<SaleRecord> salesOverPrice(List<SaleRecord> saleRecords, Double threshold)
    {
        return saleRecords.stream()
                .filter(saleRecord -> saleRecord.getPrice() > threshold)
                .toList();
    }

    public static Set<String> getSalesPersons(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .map(SaleRecord::getSalesPerson)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static List<SaleRecord> sortByDateAndPrice(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .sorted(Comparator.comparing(SaleRecord::getTransactionDate)
                        .thenComparing(SaleRecord::getPrice).reversed())
                .toList();
    }

    public static Double totalSales(List<SaleRecord> saleRecords)
    {
        return saleRecords.stream()
                .map(SaleRecord::getPrice)
                .reduce(0.0, Double::sum);
    }

    public static Map<String, Double> topNSalesPersons(List<SaleRecord> saleRecords, int n)
    {
        return saleRecords.stream()
                .collect(Collectors.groupingBy(SaleRecord::getSalesPerson, Collectors.summingDouble(SaleRecord::getPrice)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }
}
