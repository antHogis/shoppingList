package com.github.anthogis.shoppinglist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListReader {
    private List<ShoppingListItem> shoppingList;
    private List<String> fileLines;

    public ShoppingListReader(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        fileLines = reader.lines().collect(Collectors.toList());
        reader.close();
        parseLines();
    }

    private void parseLines() {

    }
}
