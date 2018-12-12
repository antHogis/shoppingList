package com.github.anthogis.shoppinglist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reader for json files created by ShoppingListApp
 *
 * ShoppingListReader reads proprietary files created by utilizing ParserInterface, which utilizes the JsonParser API.
 * Meant to be used efficiently to parse a File nad return a list of ShoppingListItems by initializing the ShoppingListReader,
 * and calling the method getShoppingList.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class ShoppingListReader {
    /**
     * The list of ShoppingListItems parsed from fileLines.
     */
    private List<ShoppingListItem> shoppingList;

    /**
     * The lines of the file given to the constructor.
     */
    private List<String> fileLines;

    /**
     * The constructor for ShoppingListReader.
     *
     * <p>The constructor for ShoppingListReader reads a given file, and calls method <code>parseLines</code> to
     * parse ShoppingListItems from the file</p>
     *
     * @param file the file from which ShoppingListItems are parsed.
     * @throws IOException if file could not be read.
     * @throws ShoppingListMalformedException if file is not parsable.
     */
    public ShoppingListReader(File file) throws IOException, ShoppingListMalformedException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        fileLines = reader.lines().collect(Collectors.toList());
        reader.close();

        shoppingList = new ArrayList<>(fileLines.size() / 4);
        parseLines();
    }

    /**
     * Parses ShoppingListItems from fileLines and stores them in shoppingList.
     */
    private void parseLines() throws ShoppingListMalformedException {
        String productNameRegex = "\\s*\"productName\" : ";
        String productAmountRegex = "\\s*\"productAmount\" : ";

        try {
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);

                if (line.matches(productNameRegex + ".*")) {
                    //Get the rest of the line
                    line = line.split(productNameRegex)[1];
                    //Remove quotes and comma from end
                    String productName = line.substring(1, line.length() - 2);

                    line = fileLines.get(i + 1);

                    if (line.matches(productAmountRegex + ".*")) {
                        line = line.split(productAmountRegex)[1];
                        String productAmount = line;
                        shoppingList.add(new ShoppingListItem(productName, productAmount));
                        i += 2;
                    } else {
                        throw new ShoppingListMalformedException();
                    }
                }
            }

            if (shoppingList.isEmpty()) {
                throw new ShoppingListMalformedException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new ShoppingListMalformedException();
        }
    }

    /**
     * Returns the list of ShoppingListItems
     *
     * @return the list of ShoppingListItems
     */
    public List<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }
}
