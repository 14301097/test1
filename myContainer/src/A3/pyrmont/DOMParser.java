package A3.pyrmont;

import A3.pyrmont.Constants;

import java.io.File; 
import java.io.IOException; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException; 

public class DOMParser { 
	String pathWebxml = Constants.WEB_XML;
	private static XPath xpath;
	
  DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
  //Load and parse XML file into DOM 
  public Document parse(String filePath) { 
     Document document = null; 
     try { 
        //DOM parser instance 
        DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
        //parse an XML file into a DOM tree 
        document = builder.parse(new File(filePath)); 
        //XPath
        XPathFactory XPfactory = XPathFactory.newInstance();
        xpath = XPfactory.newXPath();
     } catch (ParserConfigurationException e) { 
        e.printStackTrace();  
     } catch (SAXException e) { 
        e.printStackTrace(); 
     } catch (IOException e) { 
        e.printStackTrace(); 
     } 
     return document; 
  } 
	
  public String parserWebxml(String uri) throws XPathExpressionException { 
        DOMParser parser = new DOMParser(); 
        Document document = parser.parse(pathWebxml);
        
        /*xPath 遍历找到与uri相同的	<servlet-mapping><url-pattern>的<servlet-name>,
         * 遍历与<servlet-mapping><servlet-name>相同的	<servlet>的<servlet-class>
         */
        String servletName=null;
        String servletClass=null;
        NodeList nodeListUrl = (NodeList) xpath.evaluate(
        		"//url-pattern[text()='"+uri+"']/preceding-sibling::*[1]", document, XPathConstants.NODESET);
        NodeList nodeListClass;
        for(int i=0; i<nodeListUrl.getLength(); i++){
        	servletName = nodeListUrl.item(i).getTextContent();
        }
        document = parser.parse(pathWebxml);
        nodeListClass = (NodeList) xpath.evaluate(
        		"//servlet/servlet-name[text()='"+servletName+"']/following::*[1]", document, XPathConstants.NODESET);
        for(int i=0; i<nodeListClass.getLength(); i++){
        	servletClass = nodeListClass.item(i).getTextContent();
        }
        System.out.println(servletClass+"/n"+servletName);
        return servletClass;
  } 
}
