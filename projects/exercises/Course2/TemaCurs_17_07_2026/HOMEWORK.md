# Tema: Analize vânzări cu Colecții, Streams și `java.time`

Folosește dataset-ul `Sales.json` (modele BMW) și clasele `SalesResponseJson` și `SaleRecord` (cu `LocalDate transactionDate`). Poți folosi `SalesJsonReader.readSales("Sales.json")` pentru a încărca datele.

## Obiective
- Exersează utilizarea colecțiilor Java, Streams API și `java.time`.

## Cerințe
1. Încarcă `Sales.json` într-un `List<SaleRecord>` și afișează doar datele din 2023.
2. Calculează numărul total de vânzări per `salesPerson` (returnează `Map<String,Integer>`), ordonat descrescător după număr.
3. Calculează venitul total per a)`salesPerson` b)`year`.
4. Obține modelele distincte vândute și top 3 cele mai vândute modele (după număr).
5. Identifică cea mai scumpă și cea mai ieftină vânzare.
6. Vânzări pe lună pentru un an dat (ex: 2023, e la alegere) într-un `Map<Month,Integer>`; tratează lunile fără vânzări.
7. Grupează vânzările pe trimestre (Q1–Q4) pentru un an dat (ex: 2022, e la alegere).
8. Construiește o listă imuabilă cu vânzări peste un prag de preț (ex: > 70.000) și un set imuabil cu persoanele de vânzări.
9. Folosește un `Comparator` pentru a sorta vânzările după `transactionDate`, apoi `price` descrescător, într-o listă nouă (nu modifica lista originală).
10. Calculează venitul total folosind `reduce` fără collectors.
11. (Bonus) Top-N persoane de vânzări după venit.
12. (Bonus) Identifică vânzări cu preț mult peste medie: calculează deviația standard (SD) a prețurilor cu formula `SD = sqrt(sum((p - media)^2) / n)` și marchează vânzările cu preț > media + 2 * SD .
        - Deviația standard: cât de împrăștiate sunt valorile față de medie; `SD = sqrt(sum((p - media)^2) / n)`, unde `p` este fiecare preț, `media` este media prețurilor, `n` este numărul de prețuri.

## Livrabile
- Metode care implementează cerințele.