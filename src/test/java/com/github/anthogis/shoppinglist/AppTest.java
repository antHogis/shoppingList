package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;
import org.junit.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AppTest extends TestCase {

    public AppTest( String testName ) {
        super( testName );
    }


    public static Test suite() {
        return new TestSuite( AppTest.class );
    }

    public void testApp() {
        assertTrue( true );
    }
}
/*
public class AppTest {
    @Test
    public void testJSONAttribute() {
        JSONAttribute<Double> dblattrbt = new JSONAttribute<Double>("Test", 1.2);
        Assert.assertTrue(dblattrbt.getNotation().equals("no quotes"));
    }
}*/
