package javaCore.homeworks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> listCsv = parseCSV(columnMapping, fileName);
        String jsonCsv = listToJson(listCsv);
        writeString(jsonCsv, "data.json");

        List<Employee> listXml = parseXML("data.xml");
        String jsonXml = listToJson(listXml);
        writeString(jsonXml, "data2.json");

//        String json = readString("new_data.json");
    }

//    public static String readString(String s) {
//        String json = null;
//        JSONParser parser = new JSONParser();
//        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
//            Object obj = parser.parse(br);
//            JSONObject jsonObject = (JSONObject) obj;
//            json = jsonObject.toString();
//        } catch (IOException | ParseException e) {
//            System.out.println(e.getMessage());
//        }
//        return json;
//    }

    public static List<Employee> parseXML(String s) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(s));
            Node root = doc.getDocumentElement();
//            System.out.println("Корневой элемент: " + root.getNodeName());
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    System.out.println("Текущий узел: " + node.getNodeName());
                    NodeList employeeParameters = node.getChildNodes();
                    String[] parameters = new String[employeeParameters.getLength()];
                    int count = 0;
                    for (int a = 0; a < employeeParameters.getLength(); a++) {
                        Node node_ = employeeParameters.item(a);
                        if (Node.ELEMENT_NODE == node_.getNodeType()) {
                            parameters[count] = node_.getFirstChild().getNodeValue();
//                            System.out.println(parameters[count]);
                            count++;
                        }
                    }
                        employeeList.add(new Employee(Long.parseLong(parameters[0]),
                                parameters[1],
                                parameters[2],
                                parameters[3],
                                Integer.parseInt(parameters[4])));
                    }
                }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    public static void writeString(String json, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            return csv.parse();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}