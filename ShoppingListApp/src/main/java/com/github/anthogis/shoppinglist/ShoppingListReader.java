package com.github.anthogis.shoppinglist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListReader {
    private List<ShoppingListItem> shoppingList;
    private List<String> fileLines;

    public ShoppingListReader(File file) throws IOException, ShoppingListMalformedException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        fileLines = reader.lines().collect(Collectors.toList());
        reader.close();

        shoppingList = new ArrayList<>(fileLines.size() / 4);
        parseLines();
    }

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
                    System.out.println(line);

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
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new ShoppingListMalformedException();
        }
    }

    public List<ShoppingListItem> getShoppingList() {
        return shoppingList;
    }
}
