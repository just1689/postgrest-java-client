package za.co.captain;

import lombok.AllArgsConstructor;
import za.co.captain.generic.GenericRepository;
import za.co.captain.model.Database;
import za.co.captain.model.Table;

@AllArgsConstructor
public class UsefulRepository<T> extends GenericRepository<T> {

    private Database database;
    private Table table;


    public String getTableUrl() {
        return database.getUrl() + table.getTableName();
    }

    public Class getTableClass() {
        return table.getClass();
    }
}
