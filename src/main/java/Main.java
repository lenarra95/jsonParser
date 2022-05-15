import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main (String[] args) {
        List<Employee> employeeList = parseXML("data.xml");
        String employeesString = listToJson(employeeList);
        writeString(employeesString);
    }

    private static List<Employee> parseXML (String file) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(file));
            NodeList nodeEmployeeList = document.getElementsByTagName("employee");
            for (int i = 0; i < nodeEmployeeList.getLength(); i++) {
                Employee employee = new Employee();
                Element employeeElements = (Element) nodeEmployeeList.item(i);
                employee.setId(Long.parseLong(employeeElements.getElementsByTagName("id").item(0).getTextContent()));
                employee.setFirstName(employeeElements.getElementsByTagName("firstName").item(0).getTextContent());
                employee.setLastName(employeeElements.getElementsByTagName("lastName").item(0).getTextContent());
                employee.setCountry(employeeElements.getElementsByTagName("country").item(0).getTextContent());
                employee.setAge(Integer.parseInt(employeeElements.getElementsByTagName("age").item(0).getTextContent()));
                employeeList.add(employee);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    private static String listToJson (List<Employee> employeeList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(employeeList, listType);
    }

    private static void writeString (String json) {
        try {
            FileWriter fileWriter = new FileWriter("json.json");
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
