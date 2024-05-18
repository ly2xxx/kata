package org.example;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class US4_OCRParserTest {

    @Test
    public void testParseSingleEntry() {
        US4_OCRParser parser = new US4_OCRParser();
        List<String> entry = Arrays.asList(
       "    _  _     _  _  _  _  _ ",
            "  | _| _||_||_ |_   ||_||_|",
            "  ||_  _|  | _||_|  ||_| _|",
            ""
        );
        String accountNumber = parser.parseDocument(entry).get(0);
        assertEquals("123456789", accountNumber);
        assertEquals(true, parser.isValidAccountNumber(accountNumber));
    }

    // @Test
    // public void testParseMultipleEntries() throws IOException {
    //     US4_OCRParser parser = new US4_OCRParser();
    //     List<String> document = Arrays.asList(
    //    "    _  _     _  _  _  _  _ ",
    //         "  | _| _||_||_ |_   ||_||_|",
    //         "  ||_  _|  | _||_|  ||_| _|",
    //         "",
    //         " _  _  _  _  _  _  _  _  _ ",
    //         "| || || || || || || || || |",
    //         "|_||_||_||_||_||_||_||_||_|",
    //         "",
    //         "    _  _  _  _  _  _  _  _ ",
    //         "|_||_|| || ||_   |  |  |  |",
    //         "  | _| _|  | _||_|  |  |  |",
    //         ""
    //     );
    //     List<String> accountNumbers = parser.parseDocument(document);
    //     String outputFilename = "output.txt";
    //     parser.writeResultsToFile(accountNumbers, outputFilename);

    //     try (BufferedReader reader = new BufferedReader(new FileReader(outputFilename))) {
    //         assertEquals("123456789", reader.readLine());
    //         assertEquals("000000000", reader.readLine());
    //         assertEquals("49??5?777 ILL", reader.readLine());
    //     }
    // }

    @Test
    public void testInvalidAccountNumber() {
        US4_OCRParser parser = new US4_OCRParser();
        String invalidAccountNumber = "123456788"; // Invalid checksum
        assertEquals(false, parser.isValidAccountNumber(invalidAccountNumber));
    }

    @Test
    public void testAccountNumberWithUnrecognizedDigits() {
        US4_OCRParser parser = new US4_OCRParser();
        String accountNumberWithUnknown = "12345?789";
        assertEquals(false, parser.isValidAccountNumber(accountNumberWithUnknown));
    }
}

