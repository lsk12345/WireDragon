import com.lsk.util.Utils;

import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class testMain {
    public static void parseHttpRequest(String httpRequest) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("字段名称");
        model.addColumn("内容含义");

        String[] lines = httpRequest.split("\r\n");

        if (lines.length < 1) {
            // Invalid HTTP request
            return;
        }

        // Parse the request line
        String requestLine = lines[0];
        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length != 3) {
            // Invalid request line
            return;
        }

        String requestModel = requestLineParts[0];
        String requestUrl = requestLineParts[1];
        String requestVersion = requestLineParts[2];

        // Parse the request URL and parameters
        String requestUrlPath = requestUrl;
        Map<String, String> requestUrlParams = new HashMap<String, String>();
        if (requestUrl.contains("?")) {
            String[] urlParts = requestUrl.split("\\?", 2);
            requestUrlPath = urlParts[0];
            String queryString = urlParts[1];
            String[] paramPairs = queryString.split("&");
            for (String paramPair : paramPairs) {
                String[] paramParts = paramPair.split("=");
                if (paramParts.length == 2) {
                    String key = paramParts[0];
                    String value = paramParts[1];
                    requestUrlParams.put(key, value);
                }
            }
        }

        // Parse other headers
        Map<String, String> headers = new HashMap<String, String>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.length() == 0) {
                // Headers end, exit the loop
                break;
            }
            String[] headerParts = line.split(": ", 2);
            if (headerParts.length == 2) {
                String headerName = headerParts[0];
                String headerValue = headerParts[1];
                headers.put(headerName, headerValue);
            }
        }

        // Parse and split Cookie header into CookiePairs
        String cookieHeader = headers.get("Cookie");
        Map<String, String> cookiePairs = new HashMap<String, String>();
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                String[] cookieParts = cookie.split("=", 2);
                if (cookieParts.length == 2) {
                    String key = cookieParts[0];
                    String value = cookieParts[1];
                    cookiePairs.put(key, value);
                }
            }
        }
        headers.remove("Cookie");
        // Now you have the parsed data, and you can use it as needed

        model.addRow(new Object[]{"requestModel",requestModel});

        model.addRow(new Object[]{"requestUrl",requestUrl});

        Utils.mapToModel(model,requestUrlParams,"requestUrlParam");
        model.addRow(new Object[]{"requestVersion",requestVersion});
        Utils.mapToModel(model,headers,"");
        Utils.mapToModel(model,cookiePairs,"cookie");
        System.out.println(model);
    }

    public static void main(String[] args) {
        String httpRequest = "GET /example?param1=value1&param2=value2 HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "Cookie: key1=value1; key2=value2; key3=value3\r\n" +
                "\r\n";

        parseHttpRequest(httpRequest);
    }
}