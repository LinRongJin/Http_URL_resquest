import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class URLoption {


//    public void urlOption(){
//        try {
//            URL openUrl = new URL(url);
//            URLConnection connection = openUrl.openConnection();
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            connection.connect();
//            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
public static JSONObject getXpath(String url1){
    String res="";
    JSONObject object = null;
    StringBuffer buffer = new StringBuffer();
    try{
        URL url = new URL(url1);
        HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
        if(200==urlCon.getResponseCode()){
            InputStream is = urlCon.getInputStream();
            InputStreamReader isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);

            String str = null;
            while((str = br.readLine())!=null){
                buffer.append(str);
            }
            br.close();
            isr.close();
            is.close();
            res = buffer.toString();
            object = JSONObject.parseObject(res);
        }
    }catch(IOException e){
        e.printStackTrace();
    }
    return object;
}

    public static void main(String[] args) {
        List<Integer> numListbefore = new ArrayList<Integer>();
        Map<Integer,Integer> paixuBF = new HashMap<Integer,Integer>();
        List<Integer> numListafter = new ArrayList<Integer>();
        Map<Integer,Integer> paixuAF = new HashMap<Integer,Integer>();

        for (int i = 1; i < 31; i++) {
            int pageSize = 30;
            BufferedReader bufferedReader = null;
            PrintWriter printWriter = null;
            String url1 = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?" +
                    "gameNo=85&provinceId=0&" +
                    "pageSize=" + pageSize +
                    "&isVerify=1&pageNo=" + i;
            JSONObject jsonObject = JSONObject.parseObject(getXpath(url1).get("value").toString());
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("list").toString());
            for (Object json : jsonArray) {
                JSONObject jsonObject1 = JSONObject.parseObject(json.toString());
                System.out.println("中奖号码是:  " + jsonObject1.get("lotteryDrawResult"));
                String[] newStringList= jsonObject1.get("lotteryDrawResult").toString().split(" ");
                for (int j = 0; j < newStringList.length-2; j++) {
                    numListbefore.add(new Integer(newStringList[j]));
                }
                for (int j = newStringList.length-3; j < newStringList.length; j++) {
                    numListafter.add(new Integer(newStringList[j]));
                }

            }
        }

        Set<Integer> integerSetBF = new HashSet(numListbefore);
        Set<Integer> BFint = new HashSet<>();
        for (Integer i : integerSetBF) {
            BFint.add(Collections.frequency(numListbefore, i));
            paixuBF.put(Collections.frequency(numListbefore, i),i);
            //System.out.println(i+"出现的次数是："+Collections.frequency(numListbefore, i));
        }

        TreeSet sortset = new TreeSet(BFint);
        sortset.comparator();
        for (Object in : sortset) {
            System.out.println("前区数字出现次数排序（从少到多）："+paixuBF.get(new Integer(in.toString())));
        }
        System.out.println(sortset);

//        System.out.println("前区数组 : "+numListbefore.toString());
//        System.out.println("后区数组 ： "+numListafter.toString());
        Set<Integer> integerSetAF = new HashSet(numListafter);
        Set<Integer> AFint = new HashSet<>();
        for (Integer i : integerSetAF) {
            AFint.add(Collections.frequency(numListafter, i));
            paixuAF.put(Collections.frequency(numListafter, i),i);
            //System.out.println(i+"出现的次数是："+Collections.frequency(numListafter, i));
        }

        TreeSet sortsetAF = new TreeSet(AFint);
        sortsetAF.comparator();
        for (Object in : sortsetAF) {
            System.out.println("后区数字出现次数排序（从少到多）："+paixuAF.get(new Integer(in.toString())));
        }
    }
}
