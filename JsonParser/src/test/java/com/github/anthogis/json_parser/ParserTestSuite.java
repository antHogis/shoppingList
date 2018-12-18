package com.github.anthogis.json_parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        JSONAttributeTest.class,
        JSONFormatterTest.class,
        JSONObjectTest.class,
        JSONParserTest.class,
        JSONTokenizerTest.class,
        JSONWriterTest.class
        })
public class ParserTestSuite {
}
