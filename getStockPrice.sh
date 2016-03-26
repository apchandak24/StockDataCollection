#!/bin/bash
echo "Fetching Stock Information"
mvn exec:java -Dexec.mainClass="StockService.GetStockPrice.App"

