<%--
  Created by IntelliJ IDEA.
  User: michael
  Date: 1/26/16
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cluster.ServerStatus" %>
<html>
  <head>
    <link rel="stylesheet" href="css/bootstrap.css" />
    <title>$Title$</title>
  </head>
  <body>

    <nav class="navbar navbar-inverse">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">ElasticQueue Dashboard</a>
        </div>
        <ul class="nav navbar-nav">
          <li class="active"><a href="#">Home</a></li>
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Servers
            <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li><a href="#">Add a Server</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
    <%
      ServerStatus myServer = new ServerStatus();
    %>


    <div class ="container">
      <h5>All Servers are <%= myServer.getRedisConnection()%></h5>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-md-5">
          <table id="serverTable" class="table" width="100%">
            <tr>
              <th>Server</th>
              <th>Status</th>
              <th>Server Type</th>
            </tr>
            <tr>
              <td>Server 1</td>
              <td>Active</td>
              <td>Master</td>
            </tr>
            <tr>
              <td>Server 2</td>
              <td>Active</td>
              <td>Slave</td>
            </tr>
          </table>
        </div>
        <div class="col-md-5">
          <div class="progress">
            <div class="progress-bar" role="progressbar" aria-valuenow="70"
                 aria-valuemin="0" aria-valuemax="100" style="width:70%">
              70%
            </div>
          </div>
          <table id="tableId" class="table" width="100%">
            <tr>
              <th>Queues</th>
              <th>Size</th>
            </tr>
            <tr>
              <td>Q1</td>
              <td>50 Items</td>
            </tr>
            <tr>
              <td>Q2</td>
              <td>94 Items</td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  Hello Again. Please work.


  </body>
</html>
<!-- Latest compiled JavaScript -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
  $('#serverTable').find('tr').click( function(){
    if($(this).index() != 0) {
      var table = document.getElementById("serverTable");
      var rows = table.getElementsByTagName("tr");
      for(i=0; i <rows.length; i++) {
        table.rows[i].style.backgroundColor = 'white';
      }
      table.rows[$(this).index()].style.backgroundColor = 'green';
    }
  });
</script>
