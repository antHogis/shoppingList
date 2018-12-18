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
     * The keyword of the attribute.
     */
    private final String keyWord;

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
        this.keyWord = escapeQuotes(keyWord);
        notation = '"' + keyWord + "\" : " + constructNotation(value);
    }

    /**
     * Constructs the notation of JSONAttribute according to the data type of <code>value</code>.
     */
    private String constructNotation(Object value) {
        StringBuilder notationBuilder = new StringBuilder();

        if (value instanceof Collection) {
            notationBuilder.append('[');
            if (!((Collection) value).isEmpty()) {
                for (Object o : ((Collection) value)) {
                    notationBuilder.append(constructNotation(o)).append(',');
                }
                if (notationBuilder.charAt(notationBuilder.length() - 1) == ',') {
                    notationBuilder.deleteCharAt(notationBuilder.length() - 1);
                }
            }
            notationBuilder.append(']');
        } else if (value instanceof JSONObject) {
            notationBuilder.append(((JSONObject) value).getNotation());
        } else if (value == null) {
            notationBuilder.append("null");
        } else if (value instanceof Number || value instanceof Boolean) {
            notationBuilder.append(value);
        } else if (value instanceof JSONAttribute) {
            notationBuilder.append(constructNotation(((JSONAttribute) value).getValue()));
        } else {
            notationBuilder.append('"').append(escapeQuotes(value.toString())).append('"');
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

    /**
     * Returns the value of the attribute.
     * @return the value of the attribute.
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns the keyword of the attribute.
     * @return the keyword of the attribute.
     */
    public String getKeyWord() {
        return keyWord;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;

        if (obj instanceof JSONAttribute) {
            equals = this.value.equals(((JSONAttribute) obj).getValue())
                    && this.keyWord.equals(((JSONAttribute) obj).getKeyWord());
        } else {
            equals = super.equals(obj);
        }

        return equals;
    }

    /**
     * Adds escape chars in front of chars in a string that need to be escaped for JSON.
     *
     * @param input the String to inspect for escape characters.
     * @return the input string with characters escaped as needed.
     */
    private String escapeQuotes(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '"' ||input.charAt(i) == '\\') {
                input = input.substring(0, i) + '\\' + input.substring(i);
                i++;
            }
        }

        return input;
    }
}
