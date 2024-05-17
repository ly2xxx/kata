package org.example;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class US2_OCRParserTest {

    @Test
    public void testParseSingleEntry() {
        US2_OCRParser parser = new US2_OCRParser();
        List<String> entry = Arrays.asList(
            "    _  _     _  _  _  _  _ ",
            "  | _| _||_||_ |_   ||_||_|",
            "  ||_  _|  | _||_|  ||_| _|",
            ""
        );
        String accountNumber = parser.parseDocument(entry).get(0);
        assertEquals("123456789", accountNumber);
        assertTrue(parser.isValidAccountNumber(accountNumber));
    }

    @Test
    public void testParseMultipleEntries() {
        US2_OCRParser parser = new US2_OCRParser();
        List<String> document = Arrays.asList(
            "    _  _     _  _  _  _  _ ",
            "  | _| _||_||_ |_   ||_||_|",
            "  ||_  _|  | _||_|  ||_| _|",
            "",
            " _  _  _  _  _  _  _  _  _ ",
            "| || || || || || || || || |",
            "|_||_||_||_||_||_||_||_||_|",
            ""
        );
        List<String> accountNumbers = parser.parseDocument(document);
        assertEquals(Arrays.asList("123456789", "000000000"), accountNumbers);
        assertTrue(parser.isValidAccountNumber(accountNumbers.get(0)));
        assertTrue(parser.isValidAccountNumber(accountNumbers.get(1)));
    }

    @Test
    public void testInvalidAccountNumber() {
        US2_OCRParser parser = new US2_OCRParser();
        String invalidAccountNumber = "123456788"; // Invalid checksum
        assertFalse(parser.isValidAccountNumber(invalidAccountNumber));
    }

    @Test
    public void testAccountNumberWithUnrecognizedDigits() {
        US2_OCRParser parser = new US2_OCRParser();
        String accountNumberWithUnknown = "12345?789";
        assertFalse(parser.isValidAccountNumber(accountNumberWithUnknown));
    }
}

