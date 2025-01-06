package org.example.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class DepotWorker {
    private static final Log logger = Log.getInstance();
    private List<Parcel> parcelList = new LinkedList<>();
    private Queue<Customer> customerQueue = new LinkedList<>();
    private List<Parcel> collectedParcels = new ArrayList<>();
    private List<Parcel> waitingParcels = new ArrayList<>();
    private String fileName = "Parcels (2).csv";
    private String fileName2 = "Custs (2).csv";

    // Helper method to validate CSV parts
    private boolean validateCsvParts(String[] parts, int expectedLength, String source) {
        if (parts == null || parts.length < expectedLength) {
            String errorMessage = "Invalid CSV format in " + source + ". Expected " + expectedLength +
                    " parts but got " + (parts == null ? "null" : parts.length);
            System.err.println(errorMessage);
            logger.error(errorMessage);  // Log the error
            return false;
        }
        return true;
    }

    // Initialize parcel list from file
    public void initializeParcel() {
        logger.info("Initializing parcels...");  // Log the start of parcel initialization
        try (InputStream byteData = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(byteData))) {

            if (byteData == null) {
                String errorMessage = "File not found in resources: " + fileName;
                System.err.println(errorMessage);
                logger.error(errorMessage);  // Log the error
                throw new IllegalArgumentException(errorMessage);
            }

            String line;
            boolean firstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (!validateCsvParts(parts, 6, "Parcels file line " + lineNumber)) {
                    continue;
                }

                try {
                    String normalizedParcelId = parts[0].trim();
                    if (normalizedParcelId.isEmpty()) {
                        String errorMessage = "Empty parcel ID found at line " + lineNumber;
                        System.err.println(errorMessage);
                        logger.error(errorMessage);  // Log the error
                        continue;
                    }

                    Parcel parcel = new Parcel(
                            normalizedParcelId,
                            parts[1].trim(),
                            Float.parseFloat(parts[2].trim()),
                            Integer.parseInt(parts[3].trim()),
                            "processing"
                    );
                    parcelList.add(parcel);
                    String successMessage = "Successfully loaded parcel: '" + parcel.getParcelID() + "'";
                    System.out.println(successMessage);
                    logger.info(successMessage);  // Log the successful parcel load

                } catch (Exception e) {
                    String errorMessage = "Error processing line " + lineNumber + ": " + e.getMessage();
                    System.err.println(errorMessage);
                    logger.error(errorMessage);  // Log the error
                }
            }
            String totalMessage = "Total parcels loaded: " + parcelList.size();
            System.out.println(totalMessage);
            logger.info(totalMessage);  // Log the total parcels loaded
        } catch (Exception e) {
            String errorMessage = "Error initializing parcels: " + e.getMessage();
            System.err.println(errorMessage);
            logger.error(errorMessage);  // Log the error
            e.printStackTrace();
        }
    }

    // Initialize customer queue from file
    public void initializeCustomers() {
        logger.info("Initializing customers...");  // Log the start of customer initialization
        try (InputStream data = getClass().getClassLoader().getResourceAsStream(fileName2);
             BufferedReader read = new BufferedReader(new InputStreamReader(data))) {

            if (data == null) {
                String errorMessage = "File not found in resources: " + fileName2;
                System.err.println(errorMessage);
                logger.error(errorMessage);  // Log the error
                throw new IllegalArgumentException(errorMessage);
            }

            String line;
            int lineNumber = 0;

            while ((line = read.readLine()) != null) {
                lineNumber++;

                String[] parts = line.split(",");
                if (!validateCsvParts(parts, 2, "Customers file line " + lineNumber)) {
                    continue;
                }

                String customerName = parts[0].trim();
                String parcelId = parts[1].trim();

                if (customerName.isEmpty() || parcelId.isEmpty()) {
                    String errorMessage = "Empty customer name or parcel ID at line " + lineNumber;
                    System.err.println(errorMessage);
                    logger.error(errorMessage);  // Log the error
                    continue;
                }

                Customer customer = new Customer(customerName, parcelId);
                customerQueue.offer(customer);
                String successMessage = "Added customer: '" + customerName + "' with parcel ID: '" + parcelId + "'";
                System.out.println(successMessage);
                logger.info(successMessage);  // Log the successful customer addition
            }
            String totalMessage = "Total customers loaded: " + customerQueue.size();
            System.out.println(totalMessage);
            logger.info(totalMessage);  // Log the total customers loaded
        } catch (Exception e) {
            String errorMessage = "Error initializing customers: " + e.getMessage();
            System.err.println(errorMessage);
            logger.error(errorMessage);  // Log the error
            e.printStackTrace();
        }
    }

    // Process the first customer in the queue without requiring Stage
    public String processCustomer() {
        logger.info("\n=== Processing New Customer ===");

        Customer customer = customerQueue.poll();
        if (customer == null) {
            String message = "No customers in the queue.";
            logger.info(message);  // Log the info
            return message;
        }

        String customerName = customer.getName();
        String customerParcelId = customer.getParcelId();

        if (customerParcelId == null || customerParcelId.isEmpty()) {
            String message = "Invalid parcel ID for customer " + customerName;
            logger.error(message);  // Log the error
            return message;
        }

        // Find matching parcel
        Parcel matchedParcel = null;
        for (Parcel parcel : parcelList) {
            if (parcel.getParcelID().equals(customerParcelId)) {
                matchedParcel = parcel;
                break;
            }
        }

        if (matchedParcel == null) {
            String message = "Parcel not found for customer " + customerName;
            logger.error(message);  // Log the error
            return message;
        }

        // Calculate the fee for the matched parcel
        matchedParcel.calculateFee();

        // Now that the fee is calculated, retrieve it
        String fee = String.format("%.2f", matchedParcel.getFee());

        // Update the status of the parcel
        String successMessage = "Customer " + customerName + " processed. Parcel ID: " + customerParcelId + ", Fee: $" + fee;
        logger.info(successMessage);  // Log the success message
        return successMessage;
    }

    // Handle collect action, accepting a Parcel object
    public void handleCollect(Parcel parcel) {
        if (parcel != null) {
            // Ensure fee is calculated before updating status and collecting
            parcel.calculateFee();  // Calculate the fee before adding the parcel to collected list

            // Update the status of the parcel to "collected"
            parcel.updateStatus("collected");

            // Add the parcel to the collected parcels list
            collectedParcels.add(parcel);

            // Remove the collected parcel from the parcel list
            parcelList.remove(parcel);

            // Log the successful collection
            String message = "Parcel collected: " + parcel.getParcelID() + ", Fee: $" + String.format("%.2f", parcel.getFee());
            System.out.println(message);
            logger.info(message);  // Log the info
        } else {
            String message = "Parcel not found.";
            System.out.println(message);
            logger.error(message);  // Log the error
        }
    }


    // Generate collected parcels report
    public String generateCollectedReport(List<Parcel> collectedParcels) {
        StringBuilder report = new StringBuilder("Collected Parcels Report\n");
        report.append("Parcel ID\tDimension\tWeight\tDays in Depot\tStatus\tFee\n");
        for (Parcel parcel : collectedParcels) {
            report.append(parcel.getParcelID()).append("\t")
                    .append(parcel.getDimension()).append("\t")
                    .append(parcel.getWeight()).append("\t")
                    .append(parcel.getDaysInDepot()).append("\t")
                    .append(parcel.getStatus()).append("\t")
                    .append(parcel.getFee()).append("\n");
        }

        String reportMessage = "Generated Collected Parcels Report.";
        logger.info(reportMessage);  // Log the info
        return report.toString();
    }

    // Generate uncollected parcels report
    public String generateUncollectedReport(List<Parcel> uncollectedParcels) {
        StringBuilder report = new StringBuilder("Uncollected Parcels Report\n");
        report.append("Parcel ID\tDimension\tWeight\tDays in Depot\tStatus\tFee\n");
        for (Parcel parcel : uncollectedParcels) {
            report.append(parcel.getParcelID()).append("\t")
                    .append(parcel.getDimension()).append("\t")
                    .append(parcel.getWeight()).append("\t")
                    .append(parcel.getDaysInDepot()).append("\t")
                    .append(parcel.getStatus()).append("\t")
                    .append(parcel.getFee()).append("\n");
        }

        String reportMessage = "Generated Uncollected Parcels Report.";
        logger.info(reportMessage);  // Log the info
        return report.toString();
    }

    // Generate revenue report
    public String generateRevenueReport(double revenue) {
        String revenueReport = "Total Revenue Report\nRevenue: $" + String.format("%.2f", revenue) + "\n";
        logger.info("Generated Revenue Report: $" + revenue);  // Log the info
        return revenueReport;
    }

    // Method to calculate the total revenue from collected parcels
    public double calculateTotalRevenue() {
        double totalRevenue = collectedParcels.stream()
                .mapToDouble(Parcel::getFee)
                .sum();
        String message = "Total revenue calculated: $" + String.format("%.2f", totalRevenue);
        logger.info(message);  // Log the info
        return totalRevenue;
    }

    // Handle wait action, accepting a Parcel object
    public void handleWait(Parcel parcel) {
        if (parcel != null) {
            // Update the status of the parcel to "waiting"
            parcel.updateStatus("waiting");

            // Remove the parcel from parcelList
            parcelList.remove(parcel);

            // Remove any customers associated with this parcel
            customerQueue.removeIf(customer -> customer.getParcelId().equals(parcel.getParcelID()));

            // Add the parcel to the waiting list
            waitingParcels.add(parcel);

            String message = "Parcel moved to waiting list: " + parcel.getParcelID();
            System.out.println(message);
            logger.info(message);  // Log the info
        } else {
            String message = "Parcel not found.";
            System.out.println(message);
            logger.error(message);  // Log the error
        }
    }


    // Getter methods
    public List<Parcel> getParcelList() {
        return parcelList;
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public List<Parcel> getCollectedParcels() {
        return collectedParcels;
    }

    public List<Parcel> getWaitingParcels() {
        return waitingParcels;
    }
}
