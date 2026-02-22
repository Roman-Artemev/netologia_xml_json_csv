import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");


        List<Employee> listFromXML = parseXML("data.xml");
        String jsonFromXML = listToJson(listFromXML);
        writeString(jsonFromXML, "data2.json");

        String json1 = readString("new_data.json");
        List<Employee> listFromJSON = jsonToList(json1);

        // Выводим в консоль
        for (Employee employee : listFromJSON) {
            System.out.println(employee);
        }

    }

    private static String readString(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return content.toString();
    }

    /**
     * Парсит JSON-строку в список объектов Employee
     */
    private static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();

        try {
            // Парсим JSON в массив
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(json).getAsJsonArray();

            // Создаём экземпляр Gson для конвертации
            Gson gson = new Gson();

            // Проходим по всем элементам массива
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                // Конвертируем JSON-объект в Employee
                Employee employee = gson.fromJson(jsonObject, Employee.class);
                employees.add(employee);
            }
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON: " + e.getMessage());
        }

        return employees;
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();

        try {
            // Создаём фабрику построителей документов
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Парсим XML-файл в объект Document
            Document document = builder.parse(new File(fileName));

            // Получаем корневой элемент
            Element root = document.getDocumentElement();

            // Получаем список дочерних узлов (employee)
            NodeList employeeNodes = root.getChildNodes();

            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Node node = employeeNodes.item(i);

                // Обрабатываем только элементы типа Element (пропускаем текстовые узлы и комментарии)
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element employeeElement = (Element) node;

                    // Извлекаем значения из XML-элементов
                    long id = Long.parseLong(employeeElement.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = employeeElement.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employeeElement.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employeeElement.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(employeeElement.getElementsByTagName("age").item(0).getTextContent());

                    // Создаём объект Employee и добавляем в список
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    employees.add(employee);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка парсинга XML: " + e.getMessage());
        }

        return employees;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).
                    withMappingStrategy(strategy).
                    build();
            staff = csv.parse();
        } catch (IOException e) {
            System.out.println("Чтение файла не выполнено!");
        }
        return staff;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            System.out.println("JSON успешно записан в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
