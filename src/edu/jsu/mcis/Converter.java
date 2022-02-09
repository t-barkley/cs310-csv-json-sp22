package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following:
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        (Tabs and other whitespace have been added here for clarity.)  Note the
        curly braces, square brackets, and double-quotes!  These indicate which
        values should be encoded as strings, and which values should be encoded
        as integers!  The data files which contain this CSV and JSON data are
        given in the "resources" package of this project.
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity and readability.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            String[] headers = iterator.next();
            
            //JSONArray records = new JSONArray();
            
            JSONObject jsonObject = new JSONObject();
           
            String[] record;
            String jsonString;
            
            ArrayList<String> colArray = new ArrayList<>();
            ArrayList<String> rowArray = new ArrayList<>();
            
            List<Integer> innerData;
            List<List<Integer>> dataList = new ArrayList<>();
            
            
            while (iterator.hasNext()) {
                record = iterator.next();
                innerData = new ArrayList<>();
                for (int i = 0; i < headers.length; ++i) { 
                    if (i == 0) {
                        rowArray.add(record[i]);
                    }    
                    else {
                        int a = Integer.parseInt(record[i]);
                        innerData.add(a);
                    }
                }
                dataList.add(innerData);
            }
            
            for (String field : headers) {
                    colArray.add(field);
            }
            
            
            jsonObject.put("rowHeaders", rowArray);
            jsonObject.put("data", dataList);
            jsonObject.put("colHeaders", colArray);
            //records.add(jsonObject);
            
            
            results = JSONValue.toJSONString(jsonObject);
            
        }        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            ArrayList<String> colHeaders = (ArrayList) jsonObject.get("colHeaders");
            ArrayList<String> rowHeaders = (ArrayList) jsonObject.get("rowHeaders");
            ArrayList<String> data = new ArrayList<>();
            
            List<List<Integer>> dataList;
            dataList = (ArrayList) jsonObject.get("data");
            
            for (List innerlist: dataList) {
                for (Object j: innerlist) {
                    data.add(j.toString());
                }    
            }
            
            String[] col = new String[colHeaders.size()];
            
            for (int i = 0; i < colHeaders.size(); ++i) {
                col[i] = (String) colHeaders.get(i);
            }
            csvWriter.writeNext(col);
            
            
            for (int i = 0; i < rowHeaders.size(); ++i) {
                col[0] = (String) rowHeaders.get(i);
                
                for (int b = 0; b < (colHeaders.size() - 1); ++b) {
                    col[b + 1] = data.get(b);
                }
                
                for (int k = 1; k < colHeaders.size(); ++k) {
                    if (data.size() > 4) {
                        data.remove(0);
                    }
                }
                csvWriter.writeNext(col);
            }
            
            results = writer.toString();
            
        }
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }

}