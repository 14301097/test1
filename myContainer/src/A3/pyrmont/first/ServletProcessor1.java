package A3.pyrmont.first;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.xpath.XPathExpressionException;

import A3.pyrmont.Constants;
import A3.pyrmont.Request;
import A3.pyrmont.Response;
import A3.pyrmont.DOMParser;

public class ServletProcessor1 {

    public void process(Request request, Response response) {

        String uri = request.getUri();
        //String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        //loadclass 需要含包名的全名
        //servletName = "s.PrimitiveServlet";
        //String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        //servletName = "s.PrimitiveServlet";
        String servletName = "/"+uri.substring(uri.lastIndexOf("/") + 1);
        int loc = servletName.indexOf("?");
        if(loc > 0){
        	servletName = servletName.substring(0, loc);
        }
        String servletClass = null;
        
        //解析webxml
        DOMParser webxmlparser = new DOMParser();
        try {
        	servletClass = webxmlparser.parserWebxml(servletName);
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //类加载器，用于从指定JAR文件或目录加载类
        URLClassLoader loader = null;
        try {
        	//test: path; webxml;
            System.out.println(Constants.WEB_SERVLET_ROOT);
            System.out.println(Constants.WEB_ROOT);
            
            URLStreamHandler streamHandler = null;
            //创建类加载器
            loader = new URLClassLoader(new URL[]{new URL(null, "file:" + Constants.WEB_SERVLET_ROOT, streamHandler)});
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        Class<?> myClass = null;
        try {
            //加载对应的servlet类
            myClass = loader.loadClass(servletClass);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            //生产servlet实例
            servlet = (Servlet) myClass.newInstance();
            //执行ervlet的service方法
            servlet.service((ServletRequest) request,(ServletResponse) response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

    }
}
