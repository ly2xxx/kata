package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class US4_OCRParser {

    private static final Map<String, String> OCR_MAP = new HashMap<>();
    private static final Map<String, String> REVERSE_OCR_MAP = new HashMap<>();

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

        for (Map.Entry<String, String> entry : OCR_MAP.entrySet()) {
            REVERSE_OCR_MAP.put(entry.getValue(), entry.getKey());
        }
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

    public void writeResultsToFile(List<String> accountNumbers, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String accountNumber : accountNumbers) {
                String status = getStatus(accountNumber);
                writer.write(accountNumber + (status.isEmpty() ? "" : " " + status) + "\n");
            }
        }
    }

    private String getStatus(String accountNumber) {
        if (accountNumber.contains("?")) {
            String correctedNumber = correctAccountNumber(accountNumber);
            if (correctedNumber.equals(accountNumber)) {
                return "ILL";
            }
            return correctedNumber;
        }
        if (!isValidAccountNumber(accountNumber)) {
            String correctedNumber = correctAccountNumber(accountNumber);
            if (correctedNumber.equals(accountNumber)) {
                return "ERR";
            }
            return correctedNumber;
        }
        return "";
    }

    private String correctAccountNumber(String accountNumber) {
        List<String> possibleNumbers = new ArrayList<>();
        for (int i = 0; i < accountNumber.length(); i++) {
            if (accountNumber.charAt(i) == '?') {
                possibleNumbers.addAll(getPossibleDigits(accountNumber, i));
            }
        }
        if (possibleNumbers.isEmpty()) {
            return accountNumber;
        }
        List<String> validNumbers = new ArrayList<>();
        for (String number : possibleNumbers) {
            if (isValidAccountNumber(number)) {
                validNumbers.add(number);
            }
        }
        if (validNumbers.size() == 1) {
            return validNumbers.get(0);
        } else if (validNumbers.size() > 1) {
            return accountNumber + " AMB";
        }
        return accountNumber + " ILL";
    }

    private List<String> getPossibleDigits(String accountNumber, int position) {
        List<String> possibleNumbers = new ArrayList<>();
        String partialOCR = REVERSE_OCR_MAP.get(accountNumber.substring(position, position + 1));
        for (int i = 0; i < partialOCR.length(); i++) {
            char[] chars = partialOCR.toCharArray();
            chars[i] = chars[i] == ' ' ? '_' : ' ';
            String newDigit = new String(chars);
            if (OCR_MAP.containsKey(newDigit)) {
                possibleNumbers.add(accountNumber.substring(0, position) + OCR_MAP.get(newDigit) + accountNumber.substring(position + 1));
            }
        }
        return possibleNumbers;
    }
}

