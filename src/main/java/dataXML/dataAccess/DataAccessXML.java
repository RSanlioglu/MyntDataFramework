package dataXML.dataAccess;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataAccessXML implements IDataAccessXML {
    public List<Object> o_list = new ArrayList<>();
    private final String fileName;
    private Class type;


    public DataAccessXML(String fileName, Class type) {
        this.fileName = fileName;
        this.type = type;
        createXML();

        /*try {
            read(type, path);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    private void write(List<Object> obj) {
        XmlMapper objectMapper = new XmlMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void read(Class type) throws IOException {

        List<Object> objects = new ArrayList<>();
        XmlMapper objectMapper = new XmlMapper();
        MappingIterator<Object> mappingIterator;

         mappingIterator = objectMapper.readerFor(type).readValues(fileName);

        while (mappingIterator.hasNext()) {
            objects.add(mappingIterator.next());
        }
        o_list.addAll(objects);
    }

    private void assertNotNull(String xml) {
    }




   /* public void writeToDataFile(Object o) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(o);
        assertNotNull(xml);
    }*/


    @Override
    public void createXML() {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("Success");
            } else {
                System.out.println("This file already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Object> getAllObjects() {
        return null;
    }

    @Override
    public void writeObject(Object o) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writeValue(new File(fileName), o);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void writeList(List<Object> l_o, String rootName) {
        XmlMapper xmlMapper = new XmlMapper();
        for (Object o : l_o) {
            try {
                xmlMapper.writerWithDefaultPrettyPrinter().withRootName(rootName).writeValue(new File(fileName), o);
                System.out.println("2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*Add one object to the datafile. The object must be of the same
     * type as the data inside the json-file.
     * The previous data will not be removed*/
    public void appendObject(Object o) {
        try {
            read(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        o_list.add(o);
        write(o_list);

    }

    /*Add one list of objects to the datafile. The list must contain
     * OBJECTS in order to append to the datafile.
     * The previous data will not be removed*/
    public void appendList(List<Object> l_o) {
        o_list.addAll(l_o);
        write(o_list);
    }

    /*Used to check if an object does exist in the collection of data
     * gotten from the data-file. The objects are compared by the .toString()
     * functions from the objects. Therefore
     * */
    public boolean doesExist(Object o) {
        boolean exists = false;
        for(Object x : o_list) {
            if(x.toString().equals(o.toString())) {
                exists = true;
            }
        }
        return exists;
    }
    /*Deletes an object from the file. The object must first
     * be created by the client and the framework will find it and
     * remove it*/
    public void deleteObject(Object o) {
        Iterator i = o_list.iterator();
        while(i.hasNext()) {
            Object x = i.next();
            if(x.toString().equals(o.toString())) {
                i.remove();
            }
        }
        write(o_list);
    }

    /*Finds the object to be updated and deletes it and replaces it
     * with the new object with new data. The new object is placed
     * last in the file*/
    public void updateObject(Object oldObject, Object newObject) {
        Object objectToBeDeleted = null;

        for(Object x : o_list) {
            if(x.toString().equals(oldObject.toString())) {
                objectToBeDeleted = x;
            }
        }
        deleteObject(objectToBeDeleted);
        appendObject(newObject);
    }
}
