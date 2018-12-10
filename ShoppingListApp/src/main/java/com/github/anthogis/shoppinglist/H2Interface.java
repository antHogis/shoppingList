package com.github.anthogis.shoppinglist;

import java.util.List;

public class H2Interface {

    private String createShoppingListTable(String tableName) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("CREATE TABLE ")
                .append(tableName)
                .append('(')
                .append("ID INT PRIMARY KEY")
                .append(",ITEM_NAME VARCHAR(255)")
                .append(",ITEM_AMOUNT INT)");

        return commandBuilder.toString();
    }

    private String insertItems(List<ShoppingListItem> listItems, String tableName)
            throws NumberFormatException {
        StringBuilder commandBuilder = new StringBuilder();

        for (int i = 0; i < listItems.size(); i++) {
            String itemName = listItems.get(i).getItemName();
            if (itemName.length() > 255) {
                itemName = itemName.substring(0, 255);
            }

            int itemAmount = Integer.parseInt(listItems.get(i).getItemAmount());

            commandBuilder.append("INSERT INTO ")
                    .append(tableName)
                    .append(" VALUES(")
                    .append(i)
                    .append(',')
                    .append('\'' + itemName + '\'')
                    .append(',')
                    .append(itemAmount)
                    .append(')');
        }

        return commandBuilder.toString();
    }
}
