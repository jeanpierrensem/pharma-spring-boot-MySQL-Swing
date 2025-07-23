package com.officine.losto.ui.shared.model;

import java.util.List;
import java.util.Set;

public abstract class DefaultAbstractComboBoxModel<T> extends javax.swing.DefaultComboBoxModel<T> {

    @Override
    public T getSelectedItem() {
        return (T) super.getSelectedItem();
    }

    public void addElements(List<T> elements) {
        elements.forEach(this::addElement);
    }

    public void addElements(Set<T> elements) {
        elements.forEach(this::addElement);
    }
    
    public void clear() {
        removeAllElements();
    }
}
