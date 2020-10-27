package com.ekalife.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Utility Class to Retrieve Droplist value from XML file
 *
 * @author Isyak
 */
public class DroplistManager {

    /**
     * Logger Instance
     */
    private static final Log logger = LogFactory.getLog(DroplistManager.class);

    /**
     * Droplist Manager Singleton Instance
     */
    private static DroplistManager droplistManager;

    /**
     * Droplist Cache Data
     */
    private Map<String, List<Map<String,String>>> droplistCache;

    static {

        logger.debug("Initializing Droplist Manager");
        droplistManager = new DroplistManager();
    }

    private DroplistManager() {

        droplistCache = new HashMap<String, List<Map<String,String>>>();
    }

    /**
     * Return Droplist Manager Instance
     *
     * @return droplistManager
     */
    public static DroplistManager getInstance() {
        return droplistManager;
    }

    /**
     * Return droplist value base on the name provided.
     *
     * @param name droplistName
     * @param sortColumn Sorting column base on the XML field
     * @param request HttpServletRequest
     *
     * @return droplist value
     */
    public List<Map<String, String>> get(String name, String sortColumn, HttpServletRequest request) {

        if (droplistCache.containsKey(name)) {

            logger.debug("Returning Droplist Cache Instance: " + name);
            return droplistCache.get(name);
        }
        else {

            logger.debug("Cache Not Found, Try to Parse XML: " + name);
            List<Map<String, String>> droplist = parseXML(name, sortColumn, request);
            droplistCache.put(name, droplist);

            return droplist;
        }
    }

    private List<Map<String, String>> parseXML(String name, String sortColumn, HttpServletRequest request) {

        List<Map<String, String>> droplist = new ArrayList<Map<String, String>>();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(request.getSession().getServletContext().getResource("/xml/" + name).toString());
            Element rootElement = document.getRootElement();

            List<Element> positionList = rootElement.elements("Position");
            for (Element positionElement : positionList) {

                Map<String, String> droplistValue = new HashMap<String, String>();
                List<Element> positionDataList = positionElement.elements();
                for (Element positionDataElement : positionDataList) {

                    droplistValue.put(positionDataElement.getName(), positionDataElement.getText());
                }

                droplist.add(droplistValue);
            }

            Collections.sort(droplist, new FieldComparator(sortColumn));
        }
        catch (Exception e) {
            logger.error("Unable to Parse Droplist: " + name, e);
        }

        return droplist;
    }
}
