package za.co.captain.generic;

import za.co.captain.exception.CaptainException;
import za.co.captain.util.HttpUtil;
import za.co.captain.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericRepository<T> {

    public abstract String getTableUrl();

    public abstract Class getTableClass();

    public void post(T t) throws CaptainException {
        try {
            String response = HttpUtil.post(getTableUrl(), JsonUtil.objectToJson(t));
            //TODO: check it went well!
        } catch (Exception e) {
            throw new CaptainException(e, "Failed to post");
        }
    }

    public void put(T t) throws CaptainException {
        try {
            String response = HttpUtil.put(getTableUrl(), JsonUtil.objectToJson(t));
            //TODO: check it went well!
        } catch (Exception e) {
            throw new CaptainException(e, "Failed to put");
        }
    }

    public T get(String query) throws CaptainException {
        try {
            String json = HttpUtil.get(getTableUrl() + query);
            return (T) JsonUtil.jsonToObject(json, getTableClass());
        } catch (CaptainException e) {
            throw new CaptainException(e, "Failed to get");
        }
    }

    public ArrayList<T> getAll() throws CaptainException {
        return getMany("");
    }

    public ArrayList<T> getMany(String query) throws CaptainException {
        try {
            String json = HttpUtil.get(getTableUrl() + query);
            List<Object> list = JsonUtil.jsonToObjects(json, getTableClass());
            return castList(list);
        } catch (CaptainException e) {
            throw new CaptainException(e, "Failed to getMany");
        }
    }

    public void delete(String query) throws CaptainException {
        try {
            String response = HttpUtil.delete(getTableUrl() + query);
            //TODO: check it went well!
        } catch (Exception e) {
            e.printStackTrace();
            throw new CaptainException(e, "Failed to delete");
        }
    }

    private ArrayList<T> castList(List<Object> list) {
        ArrayList<T> results = new ArrayList();
        for (Object o : list) {
            results.add((T) o);
        }
        return results;
    }


}
