package com.craft;

import java.util.Arrays;
import java.util.Map;
import java.io.FileReader;

import com.opencsv.CSVReader;

import java.util.HashMap;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

public class CraftDemo {

    public static void main(String[] args) {
        String csvFile = "/path_to_csv_file"; // Path to the CSV file
        CSVReader reader = null;

        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            long row = 0;
            while ((line = reader.readNext()) != null) {
                if (row == 0) {
                    System.out.println("Headers = " + Arrays.toString(line));
                    row++;
                } else {
                    Map<String, Object> map = parseCSVLine(line);
                    JsonObject result = applyBusinessRules(map);
                    System.out.println("Decision: " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> parseCSVLine(String[] line) {
        if (line.length > 6) {

        }
        Map<String, Object> merchantData = new HashMap<>();
        // Assuming the CSV columns: IdentityScore, EAScore, OwnerPhoneType, etc.
        merchantData.put("IdentityScore", StringUtils.isEmpty(line[0]) ? null : Double.parseDouble(line[0]));
        merchantData.put("EAScore", StringUtils.isEmpty(line[1]) ? null : Double.parseDouble(line[1]));
        merchantData.put("OwnerPhoneTypeIndicator", line[2]);
        merchantData.put("OwnerMVNOIndicator", line[3]);
        merchantData.put("OwnerVerifiedComponents", StringUtils.isEmpty(line[4]) ? null : Integer.parseInt(line[4]));
        return merchantData;
    }

    public static JsonObject applyBusinessRules(Map<String, Object> merchantData) {
        JsonObject result = new JsonObject();

        // Example Rule 1: Decline if IdentityScore > 450 and other conditions
        if ((double) merchantData.get("IdentityScore") > 450 &&
                "W".equals(merchantData.get("OwnerPhoneTypeIndicator")) &&
                ("Y".equals(merchantData.get("OwnerMVNOIndicator")) || merchantData.get("OwnerMVNOIndicator") == null)) {
            result.addProperty("decision", "Decline");
            return result;
        }

        // Example Rule 2: Manual review if IdentityScore > 350 and EAScore >= 80
        if ((double) merchantData.get("IdentityScore") > 350 &&
                (double) merchantData.get("EAScore") >= 80 &&
                "W".equals(merchantData.get("OwnerPhoneTypeIndicator")) &&
                "Y".equals(merchantData.get("OwnerMVNOIndicator")) &&
                (int) merchantData.get("OwnerVerifiedComponents") == 1) {
            result.addProperty("decision", "Manual Review");
            return result;
        }

        // Example Rule 3: Approve if IdentityScore < 100 and other conditions
        if ((double) merchantData.get("IdentityScore") < 100 &&
                (double) merchantData.get("EAScore") <= 50) {
            result.addProperty("decision", "Approve");
            return result;
        }

        result.addProperty("decision", "Undetermined");
        return result;
    }
}
