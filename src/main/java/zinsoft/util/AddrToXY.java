package zinsoft.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddrToXY {
    
    private static String SERVICE_ID = "e59dbcc1457d42b7a143";
    private static String SERVICE_KEY = "fc7aeb2723d54e8bab57";
    private static String ACCESS_TOKEN = "";
    
    public static void main(String[] args) throws IOException, ParseException {
        HashMap<String, Object> resultMap = getXY("경상북도 구미시 신평1동");
        
        Distance dt = new Distance((double)resultMap.get("posX"), (double)resultMap.get("posY"), 127.907686111, 36.445352778);
        
        System.out.println("두점사이 거리 : " + dt.getDistance());
    }
    
    public static HashMap<String, Object> getXY(String addr) throws IOException, ParseException {
        if (addr == null || addr.isEmpty()) {
            return null;
        }

        // 신주소 입력 예 >> 경북 경주시 감포읍 가곡남길 31-2 (대본리)
        // 구주소 입력 예 >> 경상북도 포항시남구 구룡포읍
        String url = "http://211.34.86.51/OpenAPI3/addr/geocode.json?address=%s&pagenum=0&resultcount=5&accessToken=";
        //String url = "http://sgisapi.kostat.go.kr/OpenAPI3/addr/geocode.json?address=%s&pagenum=0&resultcount=5&accessToken=";
        
        String makURL = String.format(url, URLEncoder.encode(addr,"UTF-8")) + getToken();
        HashMap<String, Object> jsonMap = getSGIS(makURL);
        if (jsonMap == null)
        {
            String[] array = addr.split(" ");
            String newAddr = array[1] + " " + array[2];
            makURL = String.format(url, URLEncoder.encode(newAddr,"UTF-8")) + getToken();
            jsonMap = getSGIS(makURL);
            if(jsonMap == null)
            {
                array = addr.split(" ");
                newAddr = array[0] + " " + array[1];
                makURL = String.format(url, URLEncoder.encode(newAddr,"UTF-8")) + getToken();
                jsonMap = getSGIS(makURL);
                if(jsonMap == null)
                {
                    System.out.println("좌표 조회 실패");
                    return null;
                }
            }
            
        }
        JSONArray msgList =(JSONArray) jsonMap.get("resultdata");
        JSONObject result = (JSONObject) msgList.get(0);
        
        HashMap<String, Object> jsonResultMap  = chageXY(result);  // 좌표계 변환 UTM-K -> WGS84
        
        return jsonResultMap;
    }
    
    public static String getToken() throws IOException {
        // JSON 데이터
        //String tokenURL ="http://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json" +
        //        "?consumer_key="+SERVICE_ID+"&consumer_secret="+SERVICE_KEY;
        String tokenURL ="http://211.34.86.51/OpenAPI3/auth/authentication.json" +
                "?consumer_key="+SERVICE_ID+"&consumer_secret="+SERVICE_KEY;
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(tokenURL);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        
        HashMap<String, Object> jsonMap = getJSONObject(response.toString());
        ACCESS_TOKEN = (String) jsonMap.get("accessToken");
        
        return ACCESS_TOKEN;
    }
    
    public static HashMap<String, Object> getSGIS(String url) throws IOException {
        // JSON 데이터
        int timeout = 100;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 10000)
                .setConnectionRequestTimeout(timeout * 10000)
                .setSocketTimeout(timeout * 10000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = new HttpGet(url);
        
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return getJSONObject(response.toString());
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getJSONObject(String result) throws IOException {
        // JSON 데이터
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            return (HashMap<String, Object>)jsonObject.get("result") ;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static HashMap<String, Object> chageXY(JSONObject result) throws IOException, ParseException {
        //String xyURL = "http://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.json?src=%s&dst=%s&posX=%s&posY=%s&accessToken=%s";
        String xyURL = "http://211.34.86.51/OpenAPI3/transformation/transcoord.json?src=%s&dst=%s&posX=%s&posY=%s&accessToken=%s";
        
        String src = "5179"; // 현재 좌표계 코드 (UTM-K)
        String dst = "4166"; // 변환 좌표계 코드 (WGS84)
        String posX = (String) result.get("x");
        String posY = (String) result.get("y");
        
        String makURL = String.format(xyURL, src,dst,posX,posY,ACCESS_TOKEN);   // getSGIS에서 사용한 접근키값 사용
        return getSGIS(makURL);
    }
}
