package com.github.anthogis.json_parser;

import java.util.Collection;
import java.util.Iterator;

/**
 * An attribute of a JSONObject.
 *
 * <p>An attribute of a JSONObject. This class holds the key/value pair of an attribute of a JSONObject, as well as
 * the <i>JS Object Notation</i> for the attribute (on a single line).</p>
 * @param <T> the type of the value of the JSONAttribute.
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 * @see JSONObject
 */
public class JSONAttribute<T> {
    /**
     * The key of the attribute.
     */
    private String keyWord;

    /**
     * The value of the attribute.
     */
    private T value;

    /**
     * The <i>JS Object Notation</i> of the attribute.
     */
    private String notation;

    /**
     * The constructor for JSONAttribute.
     *
     * <p>The constructor for JSONAttributes assigns the parameter values to the attributes of the class, and
     * calls for construction of the notation of the attribute.</p>
     * @param keyWord
     * @param value
     */
    public JSONAttribute(String keyWord, T value) {
        this.keyWord = keyWord;
        this.value = value;
        constructNotation();
    }

    /**
     * Constructs the notation of JSONAttribute according to the data type of <code>value</code>.
     */
    public void constructNotation() {
        notation = '\"' + keyWord + "\" : ";

        if (value instanceof Collection) {
            notation += '[';
            if (((Collection) value).isEmpty()) {
                notation += ' ';
            } else {
                Iterator iterator = ((Collection) value).iterator();
                while(iterator.hasNext()) {
                    notation += formatByType(iterator.next()) + ",";
                }
                notation = notation.substring(0, notation.length() - 1);
            }
            notation += ']';
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).formatObject();
            notation += ((JSONObject) value).getNotation();
        } else if (value == null) {
            notation += "null";
        } else if (value instanceof Number || value instanceof Boolean) {
            notation += value.toString();
        } else {
            notation += "\"" + value.toString() + "\"";
        }
    }


    /**
     * Formats an Object as JSON notation.
     *
     * <p>Formats an Object as JSON notation. It only deals with values, and not key/value pairs. Meant to be used
     * as a helper method for constructNotation when dealing with Collections.</p>
     * @param value the value to format.
     * @return the value formatted.
     */
    private String formatByType(Object value) {
        String formatted;

        if (value == null) {
            formatted = "null";
        } else if (value instanceof Number || value instanceof Boolean) {
            formatted = value.toString();
        } else {
            formatted = "\"" + value.toString() + "\"";
        }

        return formatted;
    }

    /**
     * Returns the notation of JSONAttribute.
     * @return the notation of JSONAttribute.
     */
    public String getNotation() {
        return notation;
    }

    /**
     * Constructs and returns the notation of JSONAttribute.
     * @return the notation of JSONAttribute.
     */
    @Override
    public String toString() {
        this.constructNotation();
        return notation;
    }
}
