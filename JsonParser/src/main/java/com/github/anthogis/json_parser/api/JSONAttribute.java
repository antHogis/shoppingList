package com.github.anthogis.json_parser.api;

import java.util.Collection;

/**
 * An attribute of a JSONObject.
 *
 * <p>An attribute of a JSONObject. This class holds the key/value pair of an attribute of a JSONObject, as well as
 * the <i>JS Object Notation</i> for the attribute (on a single line).</p>
 * @param <T> the type of the value of the JSONAttribute.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 * @see JSONObject
 */
public class JSONAttribute<T> {
    /**
     * The value of the attribute.
     */
    private final T value;

    /**
     * The <i>JS Object Notation</i> of the attribute.
     */
    private String notation;

    /**
     * The constructor for JSONAttribute.
     *
     * <p>The constructor for JSONAttributes assigns the parameter values to the attributes of the class, and
     * calls for construction of the notation of the attribute.</p>
     * @param keyWord the key of the JSONAttribute.
     * @param value the value of the JSONAttribute.
     */
    public JSONAttribute(String keyWord, T value) {
        this.value = value;
        notation = '"' + keyWord + "\" : " + constructNotation(value);
    }

    /**
     * Constructs the notation of JSONAttribute according to the data type of <code>value</code>.
     */
    private String constructNotation(Object value) {
        StringBuilder notationBuilder = new StringBuilder();

        if (value instanceof Collection) {
            notationBuilder.append('[');
            if (((Collection) value).isEmpty()) {
                notationBuilder.append(' ');
            } else {
                for (Object o : ((Collection) value)) {
                    notationBuilder.append(constructNotation(o)).append(',');
                }
                if (notationBuilder.charAt(notationBuilder.length() - 1) == ',') {
                    notationBuilder.deleteCharAt(notationBuilder.length() - 1);
                }
            }
            notationBuilder.append(']');
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).formatObject();
            notationBuilder.append(((JSONObject) value).getNotation());
        } else if (value == null) {
            notationBuilder.append("null");
        } else if (value instanceof Number || value instanceof Boolean) {
            notationBuilder.append(value.toString());
        } else {
            notationBuilder.append('"').append(value.toString()).append('"');
        }

        return notationBuilder.toString();
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
        notation = constructNotation(value);
        return notation;
    }
}
