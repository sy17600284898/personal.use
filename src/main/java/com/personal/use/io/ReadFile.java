package com.personal.use.io;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;

import java.io.*;
import java.util.Map;

/**
 * IOS
 *
 * @author: shiyan
 * @version: 2019-10-23 16:35
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class ReadFile {
    public static void main(String[] args) {
        File file = new File("D:\\abcde.txt");
        String str = readFile(file);
        System.out.println(str);
    }

    private static String readFile(File file) {
        Map<String, Object> map = Maps.newHashMap();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = bf.readLine()) != null) {
                String[] each = str.split(":");
                map.put(each[0], each[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONUtils.toJSONString(map);
    }
}
