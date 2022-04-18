package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

/**
 * Class that handles any GET request to the /loadKanban endpoint.
 */
public class LoadKanBan implements Route {
    /**
     * Creates a new GSON to create Json String from Object.
     */
    private static final Gson GSON = new Gson();

    /**
     * Handles a request to spark backend server and this route.
     * @param request spark request (handled in typescript)
     * @param response spark response (handled in typescript)
     * @return String representing JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String tableName = "block";
//        JSONObject values = null;
//
//        try {
//            values = new JSONObject(request.body());
//            tableName = values.getString("name");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        // TODO: Handle errors on the frontend
        try {
            System.out.println(TableCommander.getDb().getTable(tableName));
            return GSON.toJson(TableCommander.getDb().getTable(tableName));
            // returns table
        } catch (IllegalArgumentException e) {
            return GSON.toJson(e.getMessage());
            // returns error message
        } catch (SQLException e) {
            return GSON.toJson(e.getMessage());
            // returns error message
        }
    }
}
