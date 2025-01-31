package controller.orderItem;

import net.sf.jasperreports.components.items.Item;

import java.util.List;

public interface OrderItemService {
    boolean addItem();
    boolean updateItem();
    boolean deleteItem();
    Item searchItem(String code);
    List<Item> getAll();
}
