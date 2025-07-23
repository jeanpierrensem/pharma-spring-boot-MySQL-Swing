package com.officine.losto.ui.shared.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultTableModel<T> extends AbstractTableModel {

    protected final List<T> entities = new ArrayList<>();

    public abstract String[] getColumnLabels();

    @Override
    public int getRowCount() {
        return entities.size();
    }

    @Override
    public int getColumnCount() {
        return getColumnLabels().length;
    }

    @Override
    public String getColumnName(int column) {
        return getColumnLabels()[column];
    }

    public void addEntity(T entity) {
        entities.add(entity);
        fireTableDataChanged();
    }
   
    public void updateEntity(int index, T entity) {
        entities.set(index, entity); 
        fireTableDataChanged();
    }
   

    public void addEntities(List<T> entities) {
        this.entities.addAll(entities);
        fireTableDataChanged();
    }
    
    public void addEntities(Object [][] data) {
    	for (int i = 0; i< data.length; i++)
        this.entities.add((T) data[i]);
        fireTableDataChanged();
    }
    
   

    public T getEntityByRow(int rowIndex) {
        return entities.get(rowIndex);
    }

    public void removeRow(int row) {
        entities.remove(row);
        fireTableDataChanged();
    }

    public void clear() {
        entities.clear();
    }
}
