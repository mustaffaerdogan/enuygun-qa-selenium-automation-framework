package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
import com.mustafa.pages.HomePage;
import com.mustafa.pages.SearchResultsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DataExtractionTest - Veri çıkarma ve analiz testleri
 * Case 4: Data Extraction and Analysis
 */
@Epic("Data Analysis")
@Feature("Data Extraction")
public class DataExtractionTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 4 - Data Extraction and Analysis")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Veri çıkarma ve analiz testi")
    @Story("As a tester, I should be able to extract and analyze flight data")
    public void testDataExtractionAndAnalysis() throws InterruptedException {
        logger.info("TEST: Case 4 - Data Extraction and Analysis Started");
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("data.extraction.origin");
        String destination = ConfigReader.getProperty("data.extraction.destination");
        String departureDate = ConfigReader.getProperty("data.extraction.departure.date");
        String returnDate = ConfigReader.getProperty("data.extraction.return.date");
        
        logger.info("Test Data - Origin: " + origin + ", Destination: " + destination);
        logger.info("Test Data - Departure: " + departureDate + ", Return: " + returnDate);
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // Act - Fill flight search form
        homePage.selectRoundTrip();
        
        // Verify search form is visible
        Assert.assertTrue(homePage.isFlightSearchFormVisible(),
            "Flight search form should be visible");
        
        // Enter origin and destination
        homePage.enterOrigin(origin)
                .enterDestination(destination);
        
        logger.info("TEST: Origin and destination entered successfully");
        
        // Select departure and return dates
        homePage.selectDepartureDate(departureDate)
                .selectReturnDate(returnDate);
        
        logger.info("TEST: Departure and return dates selected successfully");
        
        // Ensure hotel checkbox is unchecked (otel listesi istemiyoruz)
        homePage.ensureHotelCheckboxUnchecked();
        
        logger.info("TEST: Hotel checkbox verified as unchecked");
        
        // Click search button to find flights
        homePage.clickSearchButton();
        
        logger.info("TEST: Search button clicked - Navigating to results page");
        
        // Search Results Page'e geç
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        
        // Verify results page is loaded
        Assert.assertTrue(resultsPage.isResultsPageLoaded(),
            "Search results page should be loaded");
        
        logger.info("TEST: Search results page loaded successfully");
        
        // Extract flight data from all search results
        logger.info("TEST: Extracting flight data from all search results");
        List<SearchResultsPage.FlightData> flightDataList = resultsPage.extractAllFlightData();
        
        Assert.assertFalse(flightDataList.isEmpty(), 
            "Flight data list should not be empty");
        
        logger.info("TEST: Successfully extracted data from " + flightDataList.size() + " flights");
        
        // Save extracted data to CSV file
        String csvFilePath = saveFlightDataToCSV(flightDataList);
        logger.info("TEST: Flight data saved to CSV file: " + csvFilePath);
        
        Assert.assertNotNull(csvFilePath, "CSV file path should not be null");
        Assert.assertTrue(Files.exists(Paths.get(csvFilePath)), 
            "CSV file should exist at: " + csvFilePath);
        
        logger.info("TEST: Data extraction and CSV creation completed successfully");
        
        // Analyze CSV data
        logger.info("TEST: Starting data analysis from CSV file");
        analyzeFlightData(csvFilePath, flightDataList);
        logger.info("TEST: Data analysis completed successfully");
        
        logger.info("TEST: Case 4 - Data Extraction and Analysis Completed");
    }
    
    /**
     * Uçuş verilerini CSV dosyasına kaydet (UTF-8 BOM ile Excel uyumlu)
     */
    private String saveFlightDataToCSV(List<SearchResultsPage.FlightData> flightDataList) {
        String csvDirectory = "target/test-data";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "flight_data_" + timestamp + ".csv";
        String csvFilePath = csvDirectory + "/" + fileName;
        
        try {
            // Dizini oluştur (yoksa)
            Files.createDirectories(Paths.get(csvDirectory));
            
            // CSV dosyasına UTF-8 BOM ile yaz (Excel için gerekli)
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(csvFilePath), 
                        StandardCharsets.UTF_8))) {
                
                // UTF-8 BOM (Byte Order Mark) yaz - Excel için gerekli
                writer.write('\ufeff');
                
                // CSV başlıklarını yaz
                writer.write("Departure Time,Arrival Time,Airline Name,Price,Connection Info,Flight Duration\n");
                
                // Her uçuş verisini yaz
                for (SearchResultsPage.FlightData flightData : flightDataList) {
                    writer.write(flightData.toCsvRow());
                    writer.write("\n");
                }
                
                logger.info("Successfully wrote " + flightDataList.size() + " flight records to CSV with UTF-8 BOM encoding");
            }
            
        } catch (IOException e) {
            logger.error("Error writing CSV file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return csvFilePath;
    }
    
    /**
     * CSV'den veri analizi yap ve görselleştirme oluştur
     */
    private void analyzeFlightData(String csvFilePath, List<SearchResultsPage.FlightData> flightDataList) {
        try {
            logger.info("=== STARTING DATA ANALYSIS ===");
            
            // Havayolu bazında fiyat analizi
            Map<String, List<Double>> pricesByAirline = new HashMap<>();
            Map<String, Integer> directFlightsByAirline = new HashMap<>();
            Map<String, Integer> transferFlightsByAirline = new HashMap<>();
            
            // Zaman dilimi bazında fiyat analizi (sabah, öğle, akşam)
            Map<String, List<Double>> pricesByTimeSlot = new HashMap<>();
            pricesByTimeSlot.put("Morning (06:00-12:00)", new ArrayList<>());
            pricesByTimeSlot.put("Afternoon (12:00-18:00)", new ArrayList<>());
            pricesByTimeSlot.put("Evening (18:00-24:00)", new ArrayList<>());
            pricesByTimeSlot.put("Night (00:00-06:00)", new ArrayList<>());
            
            // En uygun maliyetli uçuşlar için liste
            List<FlightAnalysis> flightAnalyses = new ArrayList<>();
            
            for (SearchResultsPage.FlightData flight : flightDataList) {
                String airline = flight.getAirlineName();
                String priceStr = flight.getPrice();
                String departureTime = flight.getDepartureTime();
                String connectionInfo = flight.getConnectionInfo();
                String duration = flight.getFlightDuration();
                
                // Fiyatı parse et
                double price = parsePrice(priceStr);
                if (price > 0) {
                    // Havayolu bazında fiyat toplama
                    pricesByAirline.computeIfAbsent(airline, k -> new ArrayList<>()).add(price);
                    
                    // Zaman dilimi bazında fiyat toplama
                    String timeSlot = getTimeSlot(departureTime);
                    if (timeSlot != null) {
                        pricesByTimeSlot.get(timeSlot).add(price);
                    }
                    
                    // Direkt/Aktarmalı uçuş sayıları
                    if (connectionInfo.contains("Direkt")) {
                        directFlightsByAirline.merge(airline, 1, Integer::sum);
                    } else {
                        transferFlightsByAirline.merge(airline, 1, Integer::sum);
                    }
                    
                    // Maliyet etkinliği için analiz
                    FlightAnalysis analysis = new FlightAnalysis(
                        airline, price, departureTime, flight.getArrivalTime(),
                        connectionInfo, duration
                    );
                    flightAnalyses.add(analysis);
                }
            }
            
            // İstatistikleri hesapla
            Map<String, PriceStatistics> statistics = calculateStatistics(pricesByAirline);
            
            // En uygun maliyetli uçuşları belirle
            List<FlightAnalysis> costEffectiveFlights = identifyMostCostEffectiveFlights(flightAnalyses);
            
            // Konsola yazdır
            printAnalysisToConsole(statistics, pricesByTimeSlot, costEffectiveFlights);
            
            // HTML rapor oluştur
            String htmlReportPath = generateHtmlReport(
                statistics, pricesByTimeSlot, costEffectiveFlights, 
                directFlightsByAirline, transferFlightsByAirline, csvFilePath
            );
            
            logger.info("=== DATA ANALYSIS COMPLETED ===");
            logger.info("HTML Report generated at: " + htmlReportPath);
            
        } catch (Exception e) {
            logger.error("Error during data analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Fiyat string'ini double'a çevir
     */
    private double parsePrice(String priceStr) {
        try {
            if (priceStr == null || priceStr.equals("N/A")) return 0;
            // "1649.99 TRY" -> "1649.99"
            String numericPart = priceStr.split(" ")[0];
            return Double.parseDouble(numericPart);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Kalkış saatinden zaman dilimi belirle
     */
    private String getTimeSlot(String departureTime) {
        try {
            if (departureTime == null || departureTime.equals("N/A")) return null;
            String[] parts = departureTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            
            if (hour >= 6 && hour < 12) return "Morning (06:00-12:00)";
            if (hour >= 12 && hour < 18) return "Afternoon (12:00-18:00)";
            if (hour >= 18 && hour < 24) return "Evening (18:00-24:00)";
            return "Night (00:00-06:00)";
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Havayolu bazında istatistikleri hesapla
     */
    private Map<String, PriceStatistics> calculateStatistics(Map<String, List<Double>> pricesByAirline) {
        Map<String, PriceStatistics> statistics = new HashMap<>();
        
        for (Map.Entry<String, List<Double>> entry : pricesByAirline.entrySet()) {
            String airline = entry.getKey();
            List<Double> prices = entry.getValue();
            
            if (prices.isEmpty()) continue;
            
            double min = prices.stream().min(Double::compare).orElse(0.0);
            double max = prices.stream().max(Double::compare).orElse(0.0);
            double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            statistics.put(airline, new PriceStatistics(min, max, avg, prices.size()));
        }
        
        return statistics;
    }
    
    /**
     * En uygun maliyetli uçuşları belirle (algoritma)
     */
    private List<FlightAnalysis> identifyMostCostEffectiveFlights(List<FlightAnalysis> flights) {
        // Maliyet etkinliği skoru hesapla
        for (FlightAnalysis flight : flights) {
            flight.calculateCostEffectivenessScore();
        }
        
        // Skora göre sırala ve en iyi 5'i al
        return flights.stream()
            .sorted(Comparator.comparingDouble(FlightAnalysis::getCostEffectivenessScore).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
    
    /**
     * Konsola analiz sonuçlarını yazdır
     */
    private void printAnalysisToConsole(
        Map<String, PriceStatistics> statistics,
        Map<String, List<Double>> pricesByTimeSlot,
        List<FlightAnalysis> costEffectiveFlights
    ) {
        logger.info("\n========================================");
        logger.info("PRICE ANALYSIS BY AIRLINE");
        logger.info("========================================");
        
        for (Map.Entry<String, PriceStatistics> entry : statistics.entrySet()) {
            PriceStatistics stats = entry.getValue();
            logger.info(String.format("%s: Min=%.2f TL, Max=%.2f TL, Avg=%.2f TL, Count=%d",
                entry.getKey(), stats.min, stats.max, stats.avg, stats.count));
        }
        
        logger.info("\n========================================");
        logger.info("PRICE DISTRIBUTION BY TIME SLOT");
        logger.info("========================================");
        
        for (Map.Entry<String, List<Double>> entry : pricesByTimeSlot.entrySet()) {
            List<Double> prices = entry.getValue();
            if (!prices.isEmpty()) {
                double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                logger.info(String.format("%s: Avg=%.2f TL, Count=%d",
                    entry.getKey(), avg, prices.size()));
            }
        }
        
        logger.info("\n========================================");
        logger.info("TOP 5 MOST COST-EFFECTIVE FLIGHTS");
        logger.info("========================================");
        
        for (int i = 0; i < costEffectiveFlights.size(); i++) {
            FlightAnalysis flight = costEffectiveFlights.get(i);
            logger.info(String.format("%d. %s | %.2f TL | %s-%s | %s | Score: %.2f",
                i + 1, flight.airline, flight.price, flight.departureTime, 
                flight.arrivalTime, flight.connectionInfo, flight.costEffectivenessScore));
        }
    }
    
    /**
     * HTML rapor oluştur 
     */
    private String generateHtmlReport(
        Map<String, PriceStatistics> statistics,
        Map<String, List<Double>> pricesByTimeSlot,
        List<FlightAnalysis> costEffectiveFlights,
        Map<String, Integer> directFlightsByAirline,
        Map<String, Integer> transferFlightsByAirline,
        String csvFilePath
    ) throws IOException {
        String reportDirectory = "target/test-reports";
        Files.createDirectories(Paths.get(reportDirectory));
        
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = reportDirectory + "/flight_analysis_report_" + timestamp + ".html";
        
        // Calculate key metrics
        int totalFlights = statistics.values().stream().mapToInt(s -> s.count).sum();
        double overallAvgPrice = statistics.values().stream()
            .mapToDouble(s -> s.avg * s.count).sum() / totalFlights;
        double minPrice = statistics.values().stream().mapToDouble(s -> s.min).min().orElse(0);
        double maxPrice = statistics.values().stream().mapToDouble(s -> s.max).max().orElse(0);
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(reportPath), 
                    StandardCharsets.UTF_8))) {
            
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang='en'>\n<head>\n");
            writer.write("<meta charset='UTF-8'>\n");
            writer.write("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
            writer.write("<title>Flight Market Analysis - Executive Summary</title>\n");
            writer.write("<style>\n");
            
            // Modern, professional CSS
            writer.write("* { margin: 0; padding: 0; box-sizing: border-box; }\n");
            writer.write("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f7fa; padding: 40px 20px; line-height: 1.6; }\n");
            writer.write(".container { max-width: 1400px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }\n");
            
            // Header
            writer.write(".header { background: #1e3c72; color: white; padding: 50px 60px; }\n");
            writer.write(".header h1 { font-size: 42px; font-weight: 300; letter-spacing: -1px; margin-bottom: 10px; }\n");
            writer.write(".header .subtitle { font-size: 18px; opacity: 0.9; font-weight: 300; }\n");
            writer.write(".header .meta { margin-top: 25px; font-size: 14px; opacity: 0.8; }\n");
            
            // Content
            writer.write(".content { padding: 60px; }\n");
            
            // Executive Summary
            writer.write(".executive-summary { background: #f5f7fa; padding: 40px; border-radius: 8px; margin-bottom: 50px; border-left: 5px solid #1e3c72; }\n");
            writer.write(".executive-summary h2 { color: #1e3c72; font-size: 28px; margin-bottom: 20px; font-weight: 600; }\n");
            writer.write(".executive-summary p { color: #2c3e50; font-size: 16px; line-height: 1.8; }\n");
            
            // KPI Cards
            writer.write(".kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 30px; margin-bottom: 50px; }\n");
            writer.write(".kpi-card { background: #667eea; padding: 30px; border-radius: 8px; color: white; box-shadow: 0 4px 15px rgba(0,0,0,0.1); transition: transform 0.3s; }\n");
            writer.write(".kpi-card:hover { transform: translateY(-5px); box-shadow: 0 8px 25px rgba(0,0,0,0.15); }\n");
            writer.write(".kpi-card .label { font-size: 14px; opacity: 0.9; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; }\n");
            writer.write(".kpi-card .value { font-size: 36px; font-weight: 700; margin-bottom: 5px; }\n");
            writer.write(".kpi-card .unit { font-size: 16px; opacity: 0.85; }\n");
            
            // Section headers
            writer.write(".section { margin-bottom: 50px; }\n");
            writer.write(".section h2 { color: #1e3c72; font-size: 28px; margin-bottom: 25px; padding-bottom: 15px; border-bottom: 3px solid #667eea; font-weight: 600; }\n");
            
            // Tables
            writer.write("table { width: 100%; border-collapse: separate; border-spacing: 0; margin: 25px 0; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }\n");
            writer.write("th { background: #1e3c72; color: white; padding: 18px; text-align: left; font-weight: 600; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px; }\n");
            writer.write("td { padding: 16px 18px; border-bottom: 1px solid #e9ecef; color: #2c3e50; font-size: 15px; }\n");
            writer.write("tr:last-child td { border-bottom: none; }\n");
            writer.write("tr:hover { background: #f8f9fa; }\n");
            writer.write(".rank-badge { background: #667eea; color: white; padding: 5px 12px; border-radius: 20px; font-weight: 600; font-size: 13px; display: inline-block; }\n");
            
            // Charts
            writer.write(".chart-container { background: #f8f9fa; padding: 30px; border-radius: 8px; margin: 25px 0; }\n");
            writer.write(".bar-chart { display: flex; flex-direction: column; gap: 15px; }\n");
            writer.write(".bar-item { display: flex; align-items: center; }\n");
            writer.write(".bar-label { min-width: 150px; font-weight: 500; color: #2c3e50; font-size: 14px; }\n");
            writer.write(".bar-visual { flex: 1; background: #e9ecef; border-radius: 20px; height: 35px; position: relative; overflow: hidden; }\n");
            writer.write(".bar-fill { height: 100%; background: #667eea; border-radius: 20px; display: flex; align-items: center; padding: 0 15px; color: white; font-weight: 600; font-size: 13px; transition: width 0.8s ease; }\n");
            
            // Heatmap
            writer.write(".heatmap { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin: 25px 0; }\n");
            writer.write(".heatmap-item { padding: 25px; border-radius: 8px; color: white; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }\n");
            writer.write(".heatmap-item .time-label { font-size: 14px; font-weight: 600; margin-bottom: 12px; opacity: 0.95; }\n");
            writer.write(".heatmap-item .price-value { font-size: 28px; font-weight: 700; margin-bottom: 8px; }\n");
            writer.write(".heatmap-item .flight-count { font-size: 13px; opacity: 0.85; }\n");
            writer.write(".heat-high { background: #e74c3c; }\n");
            writer.write(".heat-medium-high { background: #e67e22; }\n");
            writer.write(".heat-medium { background: #f39c12; }\n");
            writer.write(".heat-low { background: #27ae60; }\n");
            
            // Footer
            writer.write(".footer { background: #2c3e50; color: white; padding: 30px 60px; text-align: center; font-size: 13px; }\n");
            writer.write(".footer .disclaimer { opacity: 0.7; line-height: 1.6; }\n");
            
            // Responsive
            writer.write("@media (max-width: 768px) { .content { padding: 30px; } .header { padding: 30px; } .kpi-grid { grid-template-columns: 1fr; } }\n");
            
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            
            writer.write("<div class='container'>\n");
            
            // Header
            writer.write("<div class='header'>\n");
            writer.write("<h1>Flight Market Analysis Report</h1>\n");
            writer.write("<div class='subtitle'>Executive Summary & Strategic Insights</div>\n");
            writer.write("<div class='meta'>\n");
            writer.write("<div>Report Generated: " + new SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm").format(new Date()) + "</div>\n");
            writer.write("<div>Analysis Period: " + new SimpleDateFormat("yyyy").format(new Date()) + "</div>\n");
            writer.write("</div>\n");
            writer.write("</div>\n");
            
            writer.write("<div class='content'>\n");
            
            // Executive Summary
            writer.write("<div class='executive-summary'>\n");
            writer.write("<h2>Executive Summary</h2>\n");
            writer.write("<p>This comprehensive analysis examines " + totalFlights + " flight options across multiple airlines, ");
            writer.write("providing detailed pricing insights and cost-effectiveness metrics. The data reveals significant pricing ");
            writer.write("variations across carriers and time periods, with prices ranging from " + String.format("%.2f", minPrice) + " TL to ");
            writer.write(String.format("%.2f", maxPrice) + " TL. Our cost-optimization algorithm has identified the most economically ");
            writer.write("advantageous flight options based on price, connection efficiency, and duration.</p>\n");
            writer.write("</div>\n");
            
            // KPI Cards
            writer.write("<div class='kpi-grid'>\n");
            writer.write("<div class='kpi-card'>\n");
            writer.write("<div class='label'>Total Flights Analyzed</div>\n");
            writer.write("<div class='value'>" + totalFlights + "</div>\n");
            writer.write("<div class='unit'>Flight Options</div>\n");
            writer.write("</div>\n");
            
            writer.write("<div class='kpi-card'>\n");
            writer.write("<div class='label'>Average Market Price</div>\n");
            writer.write("<div class='value'>" + String.format("%.0f", overallAvgPrice) + "</div>\n");
            writer.write("<div class='unit'>TRY</div>\n");
            writer.write("</div>\n");
            
            writer.write("<div class='kpi-card'>\n");
            writer.write("<div class='label'>Minimum Price</div>\n");
            writer.write("<div class='value'>" + String.format("%.0f", minPrice) + "</div>\n");
            writer.write("<div class='unit'>TRY</div>\n");
            writer.write("</div>\n");
            
            writer.write("<div class='kpi-card'>\n");
            writer.write("<div class='label'>Price Range</div>\n");
            writer.write("<div class='value'>" + String.format("%.0f", maxPrice - minPrice) + "</div>\n");
            writer.write("<div class='unit'>TRY Spread</div>\n");
            writer.write("</div>\n");
            writer.write("</div>\n");
            
            // Price statistics table
            writer.write("<div class='section'>\n");
            writer.write("<h2>Airline Price Statistics</h2>\n");
            writer.write("<table>\n");
            writer.write("<thead><tr><th>Airline</th><th>Minimum Price</th><th>Maximum Price</th><th>Average Price</th><th>Flight Count</th><th>Price Variance</th></tr></thead>\n");
            writer.write("<tbody>\n");
            for (Map.Entry<String, PriceStatistics> entry : statistics.entrySet()) {
                PriceStatistics stats = entry.getValue();
                double variance = stats.max - stats.min;
                writer.write(String.format("<tr><td><strong>%s</strong></td><td>%,.2f TL</td><td>%,.2f TL</td><td><strong>%,.2f TL</strong></td><td>%d</td><td>%,.2f TL</td></tr>\n",
                    entry.getKey(), stats.min, stats.max, stats.avg, stats.count, variance));
            }
            writer.write("</tbody></table>\n");
            writer.write("</div>\n");
            
            // Price comparison chart
            writer.write("<div class='section'>\n");
            writer.write("<h2>Average Price Comparison by Airline</h2>\n");
            writer.write("<div class='chart-container'>\n");
            writer.write("<div class='bar-chart'>\n");
            double maxAvg = statistics.values().stream().mapToDouble(s -> s.avg).max().orElse(1.0);
            for (Map.Entry<String, PriceStatistics> entry : statistics.entrySet()) {
                double widthPercent = (entry.getValue().avg / maxAvg) * 100;
                writer.write("<div class='bar-item'>\n");
                writer.write("<div class='bar-label'>" + entry.getKey() + "</div>\n");
                writer.write("<div class='bar-visual'>\n");
                writer.write(String.format("<div class='bar-fill' style='width: %.1f%%;'>%,.2f TL</div>\n", widthPercent, entry.getValue().avg));
                writer.write("</div>\n");
                writer.write("</div>\n");
            }
            writer.write("</div>\n");
            writer.write("</div>\n");
            writer.write("</div>\n");
            
            // Time slot heatmap
            writer.write("<div class='section'>\n");
            writer.write("<h2>Price Distribution by Time Period</h2>\n");
            writer.write("<div class='heatmap'>\n");
            List<Double> allTimeSlotPrices = pricesByTimeSlot.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
            double avgOverall = allTimeSlotPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            String[] timeSlotOrder = {"Morning (06:00-12:00)", "Afternoon (12:00-18:00)", "Evening (18:00-24:00)", "Night (00:00-06:00)"};
            for (String timeSlot : timeSlotOrder) {
                List<Double> prices = pricesByTimeSlot.get(timeSlot);
                if (prices != null && !prices.isEmpty()) {
                    double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    String heatClass = getHeatClassProfessional(avg, avgOverall);
                    writer.write("<div class='heatmap-item " + heatClass + "'>\n");
                    writer.write("<div class='time-label'>" + timeSlot + "</div>\n");
                    writer.write(String.format("<div class='price-value'>%,.0f TL</div>\n", avg));
                    writer.write("<div class='flight-count'>" + prices.size() + " flights</div>\n");
                    writer.write("</div>\n");
                }
            }
            writer.write("</div>\n");
            writer.write("</div>\n");
            
            // Cost-effective flights
            writer.write("<div class='section'>\n");
            writer.write("<h2>Top 5 Cost-Optimized Flight Recommendations</h2>\n");
            writer.write("<table>\n");
            writer.write("<thead><tr><th>Rank</th><th>Airline</th><th>Price</th><th>Departure</th><th>Arrival</th><th>Type</th><th>Duration</th><th>Efficiency Score</th></tr></thead>\n");
            writer.write("<tbody>\n");
            for (int i = 0; i < costEffectiveFlights.size(); i++) {
                FlightAnalysis flight = costEffectiveFlights.get(i);
                writer.write(String.format("<tr><td><span class='rank-badge'>%d</span></td><td><strong>%s</strong></td><td>%,.2f TL</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td><strong>%.2f</strong></td></tr>\n",
                    i + 1, flight.airline, flight.price, flight.departureTime, 
                    flight.arrivalTime, flight.connectionInfo, flight.duration, flight.costEffectivenessScore));
            }
            writer.write("</tbody></table>\n");
            writer.write("</div>\n");
            
            writer.write("</div>\n"); // content
            
            // Footer
            writer.write("<div class='footer'>\n");
            writer.write("<div class='disclaimer'>\n");
            writer.write("This report is generated through automated analysis of flight data. ");
            writer.write("All prices are in Turkish Lira (TRY). Cost-effectiveness scores are calculated based on ");
            writer.write("price optimization, connection efficiency, and flight duration. ");
            writer.write("Data source: " + csvFilePath + "\n");
            writer.write("</div>\n");
            writer.write("</div>\n");
            
            writer.write("</div>\n"); // container
            writer.write("</body>\n</html>");
        }
        
        return reportPath;
    }
    
    /**
     * Profesyonel heatmap sınıfı
     */
    private String getHeatClassProfessional(double avg, double overall) {
        if (avg > overall * 1.2) return "heat-high";
        if (avg > overall * 1.05) return "heat-medium-high";
        if (avg > overall * 0.95) return "heat-medium";
        return "heat-low";
    }
    
    // İç sınıflar
    private static class PriceStatistics {
        double min, max, avg;
        int count;
        
        PriceStatistics(double min, double max, double avg, int count) {
            this.min = min;
            this.max = max;
            this.avg = avg;
            this.count = count;
        }
    }
    
    private static class FlightAnalysis {
        String airline, departureTime, arrivalTime, connectionInfo, duration;
        double price;
        double costEffectivenessScore;
        
        FlightAnalysis(String airline, double price, String departureTime, 
                      String arrivalTime, String connectionInfo, String duration) {
            this.airline = airline;
            this.price = price;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.connectionInfo = connectionInfo;
            this.duration = duration;
        }
        
        void calculateCostEffectivenessScore() {
            // Algoritma: Düşük fiyat + Direkt uçuş + Kısa süre = Yüksek skor
            double priceScore = 10000.0 / Math.max(price, 1.0); // Düşük fiyat = yüksek skor
            double directBonus = connectionInfo.contains("Direkt") ? 50 : 0;
            double durationScore = 0;
            
            try {
                // "1sa 5dk" -> 65 dakika
                String[] parts = duration.replaceAll("[^0-9]", " ").trim().split("\\s+");
                int totalMinutes = 0;
                if (parts.length >= 2) {
                    totalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                } else if (parts.length == 1) {
                    totalMinutes = Integer.parseInt(parts[0]) * 60;
                }
                durationScore = 500.0 / Math.max(totalMinutes, 1); // Kısa süre = yüksek skor
            } catch (Exception e) {
                // Hata durumunda varsayılan
            }
            
            this.costEffectivenessScore = priceScore + directBonus + durationScore;
        }
        
        double getCostEffectivenessScore() {
            return costEffectivenessScore;
        }
    }
}
