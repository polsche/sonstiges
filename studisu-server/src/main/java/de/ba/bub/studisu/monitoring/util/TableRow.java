package de.ba.bub.studisu.monitoring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * as the model for HTMLUtils grows, we introduce some simple classes for this usecase
 */
public class TableRow {

    private List<String> tableData;

    /**
     * C-tor
     */
    public TableRow() {
        super();
    }

    /**
     * C-tor
     * with tableData
     * @param tableData List of Strings
     */
    public TableRow(List<String> tableData) {
        super();
        setTableData(tableData);
    }

    /**
     * C-tor
     * with two tableData strings
     * @param tableData1 String
     * @param tableData2 String
     */
    public TableRow(String tableData1, String tableData2) {
        super();
        List<String> td = new ArrayList<>();
        td.add(tableData1);
        td.add(tableData2);
        setTableData(td);
    }

    /**
     * set table data as list of strings
     * @param tableData table data as list of strings
     */
    public final void setTableData(List<String> tableData) {
        this.tableData = new ArrayList<>(tableData);
    }

    /**
     * get table data as list of strings
     * @return table data as list of strings
     */
    public final List<String> getTableData() {
        return Collections.unmodifiableList(tableData);
    }
}
