package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.List;
import entity.GasStation;
import org.json.JSONArray;
import gasStation.GooglePlaceClient;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet({ "/search" })
public class SearchGasStation extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final double lat = Double.parseDouble(request.getParameter("lat"));
        final double lng = Double.parseDouble(request.getParameter("lng"));
        final double radius = Double.parseDouble(request.getParameter("radius"));
        final String keyword = request.getParameter("query");
        final GooglePlaceClient client = new GooglePlaceClient();
        try {
            final List<GasStation> stations = client.search(lat, lng, radius, keyword);
            final JSONArray array = new JSONArray();
            for (final GasStation station : stations) {
                final JSONObject obj = station.toJSONObject();
                array.put((Object)obj);
            }
            RpcHelper.writeJsonArray(response, array);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("this is post: ").append(request.getContextPath());
    }
}