/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: jetty/9.2.11.v20150529
 * Generated at: 2016-04-07 14:00:41 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.ncsu.csc492.group17.cluster.ServerStatus;
import net.sf.json.JSONSerializer;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(4);
    _jspx_dependants.put("file:/C:/Users/sneha/.m2/repository/org/apache/taglibs/taglibs-standard-impl/1.2.1/taglibs-standard-impl-1.2.1.jar", Long.valueOf(1455941923462L));
    _jspx_dependants.put("file:/C:/Users/sneha/.m2/repository/org/springframework/spring-webmvc/4.2.2.RELEASE/spring-webmvc-4.2.2.RELEASE.jar", Long.valueOf(1455941822914L));
    _jspx_dependants.put("jar:file:/C:/Users/sneha/.m2/repository/org/springframework/spring-webmvc/4.2.2.RELEASE/spring-webmvc-4.2.2.RELEASE.jar!/META-INF/spring.tld", Long.valueOf(1444900464000L));
    _jspx_dependants.put("jar:file:/C:/Users/sneha/.m2/repository/org/apache/taglibs/taglibs-standard-impl/1.2.1/taglibs-standard-impl-1.2.1.jar!/META-INF/c.tld", Long.valueOf(1384386462000L));
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.release();
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, false, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_c_005furl_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("    <link rel=\"stylesheet\" href=\"resources/core/css/bootstrap.css\"/>\r\n");
      out.write("    <link rel=\"icon\" href=\"");
      if (_jspx_meth_c_005furl_005f1(_jspx_page_context))
        return;
      out.write("\" type=\"image/x-icon\"/>\r\n");
      out.write("    <link rel=\"shortcut icon\" href=\"");
      if (_jspx_meth_c_005furl_005f2(_jspx_page_context))
        return;
      out.write("\" type=\"image/x-icon\"/>\r\n");
      out.write("    <link rel=\"stylesheet\" href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css\">\r\n");
      out.write("    <title>ElasticQueue</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("<nav class=\"navbar navbar-inverse\">\r\n");
      out.write("    <div class=\"container-fluid\">\r\n");
      out.write("        <div class=\"navbar-header\">\r\n");
      out.write("            <a class=\"navbar-brand\" href=\"#\">ElasticQueue Dashboard</a>\r\n");
      out.write("        </div>\r\n");
      out.write("        <ul class=\"nav navbar-nav\">\r\n");
      out.write("            <li class=\"active\"><a href=\"#\">Home</a></li>\r\n");
      out.write("            <li class=\"dropdown\">\r\n");
      out.write("                <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">Servers\r\n");
      out.write("                    <span class=\"caret\"></span></a>\r\n");
      out.write("                <ul class=\"dropdown-menu\">\r\n");
      out.write("                    <li id=\"addOption\">Add a Server</li>\r\n");
      out.write("                </ul>\r\n");
      out.write("            </li>\r\n");
      out.write("        </ul>\r\n");
      out.write("    </div>\r\n");
      out.write("</nav>\r\n");
      out.write("\r\n");

    ServerStatus myServer = new ServerStatus();

      out.write("\r\n");
      out.write("\r\n");
      out.write("<!-- Latest compiled JavaScript -->\r\n");
      out.write("<script src=\"resources/core/js/jquery.min.js\"></script>\r\n");
      out.write("<script src=\"resources/core/js/bootstrap.min.js\"></script>\r\n");
      out.write("<script src=\"resources/core/js/konami.js\"></script>\r\n");
      out.write("<link href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/ui-darkness/jquery-ui.css\" rel=\"stylesheet\">\r\n");
      out.write("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\r\n");
      out.write("<script src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/dialog.js\"></script>\r\n");
      out.write("<script>\r\n");
      out.write("    var easter_egg = new Konami(function () {\r\n");
      out.write("        alert('Konami code!');\r\n");
      out.write("        document.getElementById('konami').style.display = \"inline-block\";\r\n");
      out.write("        var itemTable = document.getElementById('konamiTable');\r\n");
      out.write("\r\n");
      out.write("        var newRows = itemTable.getElementsByTagName(\"tbody\")[0];\r\n");
      out.write("        var picRow = newRows.insertRow(0);\r\n");
      out.write("        var onePic = picRow.insertCell(0);\r\n");
      out.write("        onePic.innerHTML = \"<img src=\\\"resources/pug1.gif\\\">\";\r\n");
      out.write("\r\n");
      out.write("        var textRow = newRows.insertRow(1);\r\n");
      out.write("        var oneText = textRow.insertCell(0);\r\n");
      out.write("        oneText.innerText = \"By Sneha Bhalodia, Kevin Commons, Michael Goff, and Laurel Timko\";\r\n");
      out.write("    });\r\n");
      out.write("    var selectedServer = \"\";\r\n");
      out.write("    var selectedQueue = \"\";\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div class=\"alert alert-info\" align=\"center\" id=\"loadingMessage\" style=\"display: none\">\r\n");
      out.write("    <p id=\"LoadText\"><strong>Loading.</strong> Please wait operation in progress.</p>\r\n");
      out.write("    <i class=\"fa fa-spinner fa-spin\" style=\"font-size:24px\"></i>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div class=\"container\">\r\n");
      out.write("    <div class=\"row\">\r\n");
      out.write("        <div class=\"col-md-5\" margin-bottom:30px;height:240px;width:100%;>\r\n");
      out.write("            <table id=\"serverTable\" class=\"table\" width=\"100%\">\r\n");
      out.write("                <thead>\r\n");
      out.write("                <tr>\r\n");
      out.write("                    <th>Status</th>\r\n");
      out.write("                    <th>Server Type</th>\r\n");
      out.write("                    <th>IP Address</th>\r\n");
      out.write("                    <th>Port Number</th>\r\n");
      out.write("                    <th></th>\r\n");
      out.write("                    <th>Master Port</th>\r\n");
      out.write("                    <th>Master Host</th>\r\n");
      out.write("                </tr>\r\n");
      out.write("                </thead>\r\n");
      out.write("                <tbody>\r\n");
      out.write("                </tbody>\r\n");
      out.write("            </table>\r\n");
      out.write("            <div class=\"loading\" id=\"serverLoadIcon\" align=\"center\">\r\n");
      out.write("                <i class=\"fa fa-spinner fa-spin\" style=\"font-size:24px\"></i>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("    <div class=\"row\">\r\n");
      out.write("        <div class=\"col-md-5\">\r\n");
      out.write("            <h5 id=\"progressHeading\" style=\"display:none\"></h5>\r\n");
      out.write("            <div class=\"progress\" id=\"progressInfo\" style=\"display:none\">\r\n");
      out.write("                <div class=\"progress-bar active\" id=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"70\"\r\n");
      out.write("                     aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width:0%\">\r\n");
      out.write("                    0%\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("    <div class=\"row\">\r\n");
      out.write("        <h4 id=\"queueHeading\" style=\"display:none\">Showing queues for: <span class=\"serverValue\"></span></h4>\r\n");
      out.write("        <div class=\"col-md-5\">\r\n");
      out.write("            <span style=\"display:none\" class=\"queueValue\"></span>\r\n");
      out.write("            <table style=\"display:none\" id=\"queueTable\" class=\"table\" width=\"100%\">\r\n");
      out.write("                <thead>\r\n");
      out.write("                <tr>\r\n");
      out.write("                    <th>Queues</th>\r\n");
      out.write("                    <th>Size</th>\r\n");
      out.write("                </tr>\r\n");
      out.write("                </thead>\r\n");
      out.write("                <tbody>\r\n");
      out.write("                </tbody>\r\n");
      out.write("            </table>\r\n");
      out.write("            <div class=\"loading\" id=\"queueLoadIcon\" align=\"center\" style=\"display: none\">\r\n");
      out.write("                <i class=\"fa fa-spinner fa-spin\" style=\"font-size:24px\"></i>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div id=\"konami\" style=\"display: none\">\r\n");
      out.write("    <table border=\"0\" id=\"konamiTable\">\r\n");
      out.write("        <tbody>\r\n");
      out.write("        </tbody>\r\n");
      out.write("    </table>\r\n");
      out.write("\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div id=\"dialog\" title=\"Dialog Form\">\r\n");
      out.write("    <label>New Host IP Address:</label>\r\n");
      out.write("    <input id=\"newHost\" name=\"newHost\" type=\"text\" style=\"color:black;\">\r\n");
      out.write("    <label>New Host Port Number:</label>\r\n");
      out.write("    <input id=\"newPort\" name=\"newPort\" type=\"text\" style=\"color:black;\">\r\n");
      out.write("    <label>Existing MASTER Host IP Address:</label>\r\n");
      out.write("    <input id=\"oldHost\" name=\"oldHost\" type=\"text\" style=\"color:black;\">\r\n");
      out.write("    <label>Existing MASTER Port Number:</label>\r\n");
      out.write("    <input id=\"oldPort\" name=\"oldPort\" type=\"text\" style=\"color:black;\">\r\n");
      out.write("    <input id=\"submit\" type=\"submit\" value=\"Submit\" style=\"color:red;\">\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("    //Toggles queue table off by default\r\n");
      out.write("    var qTable = document.getElementById(\"queueTable\");\r\n");
      out.write("    var sTable = document.getElementById(\"serverTable\");\r\n");
      out.write("    var serverLoadIcon = document.getElementById(\"serverLoadIcon\");\r\n");
      out.write("\r\n");
      out.write("    function toggleQueueTable() {\r\n");
      out.write("\r\n");
      out.write("        var heading = document.getElementById(\"progressHeading\");\r\n");
      out.write("        if (heading.style.display == \"none\")\r\n");
      out.write("            heading.style.display = \"inline\";\r\n");
      out.write("\r\n");
      out.write("        heading = document.getElementById(\"progressInfo\");\r\n");
      out.write("        if (heading.style.display == \"none\")\r\n");
      out.write("            heading.style.display = \"block\";\r\n");
      out.write("\r\n");
      out.write("        if (qTable.style.display == \"none\")\r\n");
      out.write("            qTable.style.display = \"table\";\r\n");
      out.write("\r\n");
      out.write("        var heading = document.getElementById(\"queueHeading\");\r\n");
      out.write("        if (heading.style.display == \"none\")\r\n");
      out.write("            heading.style.display = \"inline\";\r\n");
      out.write("\r\n");
      out.write("        var queueLoadIcon = document.getElementById(\"queueLoadIcon\");\r\n");
      out.write("        if (queueLoadIcon.style.display = \"none\") {\r\n");
      out.write("            queueLoadIcon.style.display = \"block\";\r\n");
      out.write("        }\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function getServerList() {\r\n");
      out.write("        //clears entire server table\r\n");
      out.write("        $('#serverTable > tbody').remove();\r\n");
      out.write("        $('#serverTable').append(\"<tbody></tbody>\");\r\n");
      out.write("\r\n");
      out.write("        var server = {};\r\n");
      out.write("        server[\"port\"] = \"30001\";\r\n");
      out.write("        server[\"host\"] = \"152.14.106.22\";\r\n");
      out.write("\r\n");
      out.write("        $.ajax({\r\n");
      out.write("            type: \"POST\",\r\n");
      out.write("            contentType: \"application/json\",\r\n");
      out.write("            url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("getServers\",\r\n");
      out.write("            data: JSON.stringify(server),\r\n");
      out.write("            dataType: 'json',\r\n");
      out.write("            success: function (data) {\r\n");
      out.write("                console.log(\"SUCCESS: \", data);\r\n");
      out.write("                initializeTable(data);\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function getQueues(data2) {\r\n");
      out.write("        console.log(data2.port);\r\n");
      out.write("        console.log(data2.host);\r\n");
      out.write("\r\n");
      out.write("        //clears entire table\r\n");
      out.write("        $('#queueTable > tbody').remove();\r\n");
      out.write("        $('#queueTable').append(\"<tbody></tbody>\");\r\n");
      out.write("\r\n");
      out.write("        $.ajax({\r\n");
      out.write("            type: \"POST\",\r\n");
      out.write("            contentType: \"application/json\",\r\n");
      out.write("            url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("getQueues\",\r\n");
      out.write("            data: JSON.stringify(data2),\r\n");
      out.write("            dataType: 'json',\r\n");
      out.write("            success: function (data) {\r\n");
      out.write("                console.log(\"Initial length: \", data.length);\r\n");
      out.write("                $(\"#queueHeading .serverValue\").html(data[data.length - 1]);\r\n");
      out.write("                cleanQueueData(data);\r\n");
      out.write("                console.log(\"SUCCESS: \", data, \"-> length: \", data.length);\r\n");
      out.write("                initializeQueueTable(data);\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function sortNumber(a,b) {\r\n");
      out.write("        return a - b;\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function cleanQueueData(data) {\r\n");
      out.write("        var arrayIndexes = [];\r\n");
      out.write("        for(var i = 0; i < ((data.length / 2) | 0); i++) {\r\n");
      out.write("            if(data[i].indexOf(\"_values\") != -1) {\r\n");
      out.write("                var start = data[i].indexOf(\"{\") + 1;\r\n");
      out.write("                var end = data[i].indexOf(\"}\");\r\n");
      out.write("                data[i] = data[i].substring(start, end);\r\n");
      out.write("            } else {\r\n");
      out.write("                arrayIndexes.push(i);\r\n");
      out.write("                arrayIndexes.push(i + data.length / 2);\r\n");
      out.write("            }\r\n");
      out.write("        }\r\n");
      out.write("        //removing elements\r\n");
      out.write("        arrayIndexes.sort(sortNumber);\r\n");
      out.write("        for(var i = arrayIndexes.length - 1; i >= 0; i--) {\r\n");
      out.write("            data.splice(arrayIndexes[i], 1);\r\n");
      out.write("        }\r\n");
      out.write("        data.splice(data.length - 1, 1);\r\n");
      out.write("        console.log(\"DATA POST REMOVE\", data);\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function softRemoveServer(data2) {\r\n");
      out.write("        console.log(data2.port);\r\n");
      out.write("        console.log(data2.host);\r\n");
      out.write("\r\n");
      out.write("        $.ajax({\r\n");
      out.write("           type: \"POST\",\r\n");
      out.write("            contentType: \"application/json\",\r\n");
      out.write("            url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("softRemoveServer\",\r\n");
      out.write("            data: JSON.stringify(data2),\r\n");
      out.write("            dataType: 'json',\r\n");
      out.write("            success: function (data) {\r\n");
      out.write("                console.log(\"SUCCESS: \", data);\r\n");
      out.write("                alert(data);\r\n");
      out.write("                if(data.contains(\"Successful Failover of \"))\r\n");
      out.write("                        getServerList();\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function serverMemoryUtil(data2) {\r\n");
      out.write("        console.log(data2.port);\r\n");
      out.write("        console.log(data2.host);\r\n");
      out.write("\r\n");
      out.write("        $.ajax({\r\n");
      out.write("            type: \"POST\",\r\n");
      out.write("            contentType: \"application/json\",\r\n");
      out.write("            url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("getMemory\",\r\n");
      out.write("            data: JSON.stringify(data2),\r\n");
      out.write("            dataType: 'json',\r\n");
      out.write("            success: function (data) {\r\n");
      out.write("                console.log(\"SUCCESS: \", data);\r\n");
      out.write("                updateMemoryBar(data.memory);\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    //Function to update the memory bar to display the utilization of current server.\r\n");
      out.write("    function updateMemoryBar(data) {\r\n");
      out.write("        data = data * 100;\r\n");
      out.write("        data = Math.ceil(data);\r\n");
      out.write("\r\n");
      out.write("        if(data > 66) {\r\n");
      out.write("            document.getElementById(\"progressHeading\").innerText = \"Load: High\";\r\n");
      out.write("        } else if (data < 33) {\r\n");
      out.write("            document.getElementById(\"progressHeading\").innerText = \"Load: Low\";\r\n");
      out.write("        } else {\r\n");
      out.write("            document.getElementById(\"progressHeading\").innerText = \"Load: Medium\";\r\n");
      out.write("        }\r\n");
      out.write("        $(\"#progress-bar\")\r\n");
      out.write("                .css(\"width\", data + \"%\")\r\n");
      out.write("                .attr(\"aria-valuenow\", data)\r\n");
      out.write("                .text(data + \"%\");\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function initializeQueueTable(queueData) {\r\n");
      out.write("        document.getElementById(\"queueLoadIcon\").style.display = \"inline\";\r\n");
      out.write("\r\n");
      out.write("        newQueueRows = qTable.getElementsByTagName(\"tbody\")[0];\r\n");
      out.write("        for (var l = 0; l < queueData.length / 2; l++) {\r\n");
      out.write("            var newRow = newQueueRows.insertRow(l);\r\n");
      out.write("            var newCell1 = newRow.insertCell(0);\r\n");
      out.write("            newCell1.innerText = queueData[l];\r\n");
      out.write("            var newCell2 = newRow.insertCell(1);\r\n");
      out.write("            newCell2.innerText = queueData[l + (queueData.length / 2) ];\r\n");
      out.write("        }\r\n");
      out.write("\r\n");
      out.write("        document.getElementById(\"queueLoadIcon\").style.display = \"none\";\r\n");
      out.write("\r\n");
      out.write("        $('#queueTable > tbody').find('tr').click(function () {\r\n");
      out.write("            var rows = qTable.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\");\r\n");
      out.write("            for (i = 0; i < rows.length; i++) {\r\n");
      out.write("                rows[i].style.backgroundColor = 'white';\r\n");
      out.write("            }\r\n");
      out.write("            rows[$(this).index()].style.backgroundColor = 'lightblue';\r\n");
      out.write("            var Cells = qTable.rows[$(this).index() + 1].getElementsByTagName(\"td\");\r\n");
      out.write("            //window.location.href = \"index.jsp?selectedServer=\" + Cells[0].innerText;\r\n");
      out.write("\r\n");
      out.write("            selectedQueue = Cells[0].innerText;\r\n");
      out.write("            $(\".queueValue\").html(selectedQueue);\r\n");
      out.write("        });\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function initializeTable(data) {\r\n");
      out.write("        console.log(data[0].host);\r\n");
      out.write("        console.log(data[0].port);\r\n");
      out.write("\r\n");
      out.write("        //Initialize list of servers\r\n");
      out.write("        newServerRows = sTable.getElementsByTagName(\"tbody\")[0];\r\n");
      out.write("        for (var i = 0; i < data.length; i++) {\r\n");
      out.write("            var newRow = newServerRows.insertRow(i);\r\n");
      out.write("            newRow.id = \"row\" + i;\r\n");
      out.write("            var newCell1 = newRow.insertCell(0);\r\n");
      out.write("            newCell1.innerText = data[i].status;\r\n");
      out.write("            var newCell2 = newRow.insertCell(1);\r\n");
      out.write("            newCell2.innerText = data[i].type;\r\n");
      out.write("            var newCell3 = newRow.insertCell(2);\r\n");
      out.write("            newCell3.innerText = data[i].host;\r\n");
      out.write("            var newCell4 = newRow.insertCell(3);\r\n");
      out.write("            newCell4.innerText = data[i].port;\r\n");
      out.write("            var newCell5 = newRow.insertCell(4);\r\n");
      out.write("            newCell5.innerHTML = \"<span class=\\\"glyphicon glyphicon-trash\\\"></span>\";\r\n");
      out.write("            newCell5.id = \"trash\";\r\n");
      out.write("            newCell5.onclick = (function (host, port) {\r\n");
      out.write("                return function () {\r\n");
      out.write("                    var r = confirm(\"Are you sure you want to remove \" + host + \":\" + port + \"?\");\r\n");
      out.write("                    if (r) {  //Say yes to remove server\r\n");
      out.write("                        document.getElementById(\"loadingMessage\").style.display = \"block\";\r\n");
      out.write("                        document.getElementById(\"LoadText\").innerText = \"Deleting \" + host + \":\" + port + \". Please wait.\";\r\n");
      out.write("                    } else {  //Say no to cancel request\r\n");
      out.write("                        return;\r\n");
      out.write("                    }\r\n");
      out.write("\r\n");
      out.write("                    var sendObject = {\r\n");
      out.write("                        host: host,\r\n");
      out.write("                        port: port\r\n");
      out.write("                    }\r\n");
      out.write("\r\n");
      out.write("                    $.ajax({\r\n");
      out.write("                        type: \"POST\",\r\n");
      out.write("                        contentType: \"application/json\",\r\n");
      out.write("                        url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("softRemoveServer\",\r\n");
      out.write("                        data: JSON.stringify(sendObject),\r\n");
      out.write("                        success: function (data) {\r\n");
      out.write("                            console.log(\"SUCCESS: \", data);\r\n");
      out.write("                            getServerList();\r\n");
      out.write("                        }\r\n");
      out.write("                    });\r\n");
      out.write("                };\r\n");
      out.write("            })(data[i].host, data[i].port);\r\n");
      out.write("\r\n");
      out.write("            var newCell6 = newRow.insertCell(5);\r\n");
      out.write("            newCell6.innerText = data[i].slaveOf;\r\n");
      out.write("            var newCell7 = newRow.insertCell(6);\r\n");
      out.write("            newCell7.innerText = data[i].slaveOf;\r\n");
      out.write("        }\r\n");
      out.write("\r\n");
      out.write("        document.getElementById(\"serverLoadIcon\").style.display = \"none\";\r\n");
      out.write("\r\n");
      out.write("        $('#serverTable > tbody').find('tr').click(function () {\r\n");
      out.write("            var rows = sTable.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\");\r\n");
      out.write("            for (i = 0; i < rows.length; i++) {\r\n");
      out.write("                rows[i].style.backgroundColor = 'white';\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            var queueRows = qTable.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\");\r\n");
      out.write("            for (i = 0; i < queueRows.length; i++) {\r\n");
      out.write("                queueRows[i].style.backgroundColor = 'white';\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            rows[$(this).index()].style.backgroundColor = 'lightblue';\r\n");
      out.write("            toggleQueueTable();\r\n");
      out.write("            var Cells = sTable.rows[$(this).index() + 1].getElementsByTagName(\"td\");\r\n");
      out.write("\r\n");
      out.write("            $(\"#queueHeading .serverValue\").html(Cells[2].innerText + \":\" + Cells[3].innerText);\r\n");
      out.write("\r\n");
      out.write("            //Get memory util\r\n");
      out.write("            serverMemoryUtil(data[$(this).index()]);\r\n");
      out.write("            //Initialize queue list\r\n");
      out.write("            getQueues(data[$(this).index()]);\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    //Adding a server function\r\n");
      out.write("\r\n");
      out.write("    $(document).ready(function () {\r\n");
      out.write("        $(function () {\r\n");
      out.write("            $(\"#dialog\").dialog({\r\n");
      out.write("                autoOpen: false\r\n");
      out.write("            });\r\n");
      out.write("            $(\"#addOption\").on(\"click\", function () {\r\n");
      out.write("                $(\"#dialog\").dialog(\"open\");\r\n");
      out.write("            });\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("        $(\"#submit\").click(function (e) {\r\n");
      out.write("            var newHost = $(\"#newHost\").val();\r\n");
      out.write("            var newPort = $(\"#newPort\").val();\r\n");
      out.write("            var oldHost = $(\"#oldHost\").val();\r\n");
      out.write("            var oldPort = $(\"#oldPort\").val();\r\n");
      out.write("            if (newHost === '' || newPort === '' || oldHost === '' || oldPort === '') {\r\n");
      out.write("                alert(\"Please fill all fields.\");\r\n");
      out.write("                e.preventDefault();\r\n");
      out.write("            } else {\r\n");
      out.write("                var yes = confirm(\"Do you want to add \" + newHost + \":\" + newPort + \"?\");\r\n");
      out.write("\r\n");
      out.write("                if (yes) {\r\n");
      out.write("                    document.getElementById(\"loadingMessage\").style.display = \"block\";\r\n");
      out.write("                    document.getElementById(\"LoadText\").innerText = \"Adding \" + newHost + \":\" + newPort + \". Please wait.\";\r\n");
      out.write("                } else {\r\n");
      out.write("                    return;\r\n");
      out.write("                }\r\n");
      out.write("                var sendObject = {\r\n");
      out.write("                    server: {host: oldHost, port: oldPort},\r\n");
      out.write("                    serverAdd: {host: newHost, port: newPort}\r\n");
      out.write("                }\r\n");
      out.write("\r\n");
      out.write("                $(\"#dialog\").dialog(\"close\");\r\n");
      out.write("\r\n");
      out.write("                $.ajax({\r\n");
      out.write("                    type: \"POST\",\r\n");
      out.write("                    contentType: \"application/json\",\r\n");
      out.write("                    url: \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${home}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("addServers\",\r\n");
      out.write("                    data: JSON.stringify(sendObject),\r\n");
      out.write("                    success: function (data) {\r\n");
      out.write("                        console.log(\"SUCCESS: \", data);\r\n");
      out.write("                        document.getElementById(\"loadingMessage\").style.display = \"none\";\r\n");
      out.write("                        alert(data);\r\n");
      out.write("                        if(!data.contains(\"error\"))\r\n");
      out.write("                            getServerList();\r\n");
      out.write("                    }\r\n");
      out.write("                });\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("    getServerList();\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_005furl_005f0(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:url
    org.apache.taglibs.standard.tag.rt.core.UrlTag _jspx_th_c_005furl_005f0 = (org.apache.taglibs.standard.tag.rt.core.UrlTag) _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.UrlTag.class);
    _jspx_th_c_005furl_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005furl_005f0.setParent(null);
    // /WEB-INF/views/jsp/index.jsp(13,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f0.setVar("home");
    // /WEB-INF/views/jsp/index.jsp(13,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f0.setValue("/");
    // /WEB-INF/views/jsp/index.jsp(13,0) name = scope type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f0.setScope("request");
    int _jspx_eval_c_005furl_005f0 = _jspx_th_c_005furl_005f0.doStartTag();
    if (_jspx_th_c_005furl_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody.reuse(_jspx_th_c_005furl_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvar_005fvalue_005fscope_005fnobody.reuse(_jspx_th_c_005furl_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005furl_005f1(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:url
    org.apache.taglibs.standard.tag.rt.core.UrlTag _jspx_th_c_005furl_005f1 = (org.apache.taglibs.standard.tag.rt.core.UrlTag) _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.UrlTag.class);
    _jspx_th_c_005furl_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005furl_005f1.setParent(null);
    // /WEB-INF/views/jsp/index.jsp(17,27) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f1.setValue("/resources/favicon.ico");
    int _jspx_eval_c_005furl_005f1 = _jspx_th_c_005furl_005f1.doStartTag();
    if (_jspx_th_c_005furl_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005furl_005f2(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:url
    org.apache.taglibs.standard.tag.rt.core.UrlTag _jspx_th_c_005furl_005f2 = (org.apache.taglibs.standard.tag.rt.core.UrlTag) _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.UrlTag.class);
    _jspx_th_c_005furl_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005furl_005f2.setParent(null);
    // /WEB-INF/views/jsp/index.jsp(18,36) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f2.setValue("/resources/favicon.ico");
    int _jspx_eval_c_005furl_005f2 = _jspx_th_c_005furl_005f2.doStartTag();
    if (_jspx_th_c_005furl_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f2);
    return false;
  }
}
