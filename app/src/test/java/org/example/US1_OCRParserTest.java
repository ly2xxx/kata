package org.example;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class US1_OCRParserTest {

    @Test
    public void testParseSingleEntry() {
        US1_OCRParser parser = new US1_OCRParser();
        List<String> entry = Arrays.asList(
            "    _  _     _  _  _  _  _ ",
            "  | _| _||_||_ |_   ||_||_|",
            "  ||_  _|  | _||_|  ||_| _|",
            ""
        );
        String accountNumber = parser.parseDocument(entry).get(0);
        assertEquals("123456789", accountNumber);
    }

    @Test
    public void testParseMultipleEntries() {
        US1_OCRParser parser = new US1_OCRParser();
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
    }
}

