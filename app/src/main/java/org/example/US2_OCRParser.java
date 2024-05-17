package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class US2_OCRParser {

    private static final Map<String, String> OCR_MAP = new HashMap<>();

    static {
        OCR_MAP.put(" _ | ||_|", "0");
        OCR_MAP.put("     |  |", "1");
        OCR_MAP.put(" _  _||_ ", "2");
        OCR_MAP.put(" _  _| _|", "3");
        OCR_MAP.put("   |_|  |", "4");
        OCR_MAP.put(" _ |_  _|", "5");
        OCR_MAP.put(" _ |_ |_|", "6");
        OCR_MAP.put(" _   |  |", "7");
        OCR_MAP.put(" _ |_||_|", "8");
        OCR_MAP.put(" _ |_| _|", "9");
    }

    public List<String> parseDocument(List<String> document) {
        List<String> accountNumbers = new ArrayList<>();
        for (int i = 0; i < document.size(); i += 4) {
            List<String> entryLines = document.subList(i, i + 3);
            accountNumbers.add(parseEntry(entryLines));
        }
        return accountNumbers;
    }

    private String parseEntry(List<String> entryLines) {
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 27; i += 3) {
            String ocrDigit = entryLines.get(0).substring(i, i + 3) +
                              entryLines.get(1).substring(i, i + 3) +
                              entryLines.get(2).substring(i, i + 3);
            accountNumber.append(OCR_MAP.getOrDefault(ocrDigit, "?"));
        }
        return accountNumber.toString();
    }

    public boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber.contains("?")) {
            return false;
        }
        int checksum = 0;
        for (int i = 0; i < accountNumber.length(); i++) {
            int digit = Character.getNumericValue(accountNumber.charAt(accountNumber.length() - 1 - i));
            checksum += (i + 1) * digit;
        }
        return checksum % 11 == 0;
    }
}

