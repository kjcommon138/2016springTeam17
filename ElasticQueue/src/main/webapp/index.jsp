<%--
  Created by IntelliJ IDEA.
  User: michael
  Date: 1/26/16
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cluster.ServerStatus, java.util.*" %>
<html>
<head>
    <link rel="stylesheet" href="css/bootstrap.css"/>
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
    String selectedServer = request.getParameter("selectedServer");
%>

<div class="container">
    <% if(myServer.getRedisConnection().equalsIgnoreCase("Disabled")) { %>
    <div class="alert alert-danger">
        <strong>Server down!</strong> One or more servers are down.
    </div>
    <% } %>

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
                    <td>Server1</td>
                    <td>Active</td>
                    <td>Master</td>
                </tr>
                <tr>
                    <td>Server2</td>
                    <td>Active</td>
                    <td>Slave</td>
                </tr>
            </table>
        </div>
        <div class="col-md-5">
            <h5>Load: High</h5>
            <div class="progress">
                <div class="progress-bar" role="progressbar" aria-valuenow="70"
                     aria-valuemin="0" aria-valuemax="100" style="width:70%">
                    <span class="sr-only">70% Complete</span>
                </div>
            </div>
            <%
                if(selectedServer != null) { %>
                  <h4>Showing queues for: <%= selectedServer%></h4>
                <% }%>
            <table style="display:none" id="queueTable" class="table" width="100%">
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

            <div class="col-md-5" style="overflow:scroll;height:80px;width:100%;overflow:auto">

                <table style="display:none" class="table" width:100% id="redisTable">
                    <tr>
                        <th>ID</th>
                        <th>Item</th>
                    </tr>
                </table>

            </div>

        </div>
    </div>
</div>


<!-- Latest compiled JavaScript -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
    //Toggles queue table off by default
    var qTable = document.getElementById("queueTable");
    var rTable = document.getElementById("redisTable");

    function toggleQueueTable() {
        var qTable = document.getElementById("queueTable");
        if(qTable.style.display == "none")
            qTable.style.display = "table";

        var rTable = document.getElementById("redisTable");
        if(rTable.style.display == "table")
            rTable.style.display = "none";
    }

    function toggleRedisTable() {
        var rTable = document.getElementById("redisTable");
        if(rTable.style.display == "none")
            rTable.style.display = "table";
    }


    $('#queueTable').find('tr').click(function queueClick() {
        if ($(this).index() != 0) {
            var table = document.getElementById("queueTable");
            var rows = table.getElementsByTagName("tr");
            for (i = 0; i < rows.length; i++) {
                table.rows[i].style.backgroundColor = 'white';
            }
            table.rows[$(this).index()].style.backgroundColor = 'lightblue';
            toggleRedisTable();

            //output table of values inside of queue
            var redisTable = document.getElementById("redisTable");
            //clears entire table
            for (j = redisTable.rows.length; j > 1; j--) {
                redisTable.deleteRow(j-1);
            }
            <% String[] strList = myServer.getList(); %>
            var redisSet = [];
            redisSet.push(<% for(int k = 0; k < strList.length; k++) { %>"<%= strList[k] %>"<%= k + 1 < strList.length ? ",":"" %><% } %> );
            console.log("Length of redisSet: " + redisSet.length);
            console.log(redisSet);
            for (l = 1; l < redisSet.length + 1; l++) {
                var newRow = redisTable.insertRow(l);
                var newCell1 = newRow.insertCell(0);
                newCell1.innerHTML=l;
                var newCell2 = newRow.insertCell(1);
                newCell2.innerHTML=redisSet[l-1];
            }
        }
    });
</script>
<script>
    $('#serverTable').find('tr').click(function () {
        if ($(this).index() != 0) {
            var table = document.getElementById("serverTable");
            var rows = table.getElementsByTagName("tr");
            for (i = 0; i < rows.length; i++) {
                table.rows[i].style.backgroundColor = 'white';
            }
            var Cells = table.rows[$(this).index()].getElementsByTagName("td");
            window.location.href = "index.jsp?selectedServer=" + Cells[0].innerText;

            /**
            var queueTable = document.getElementById("queueTable");
            //clears entire table
            for (j = queueTable.rows.length; j > 1; j--) {
                queueTable.deleteRow(j-1);
            }
            var queueList = ["hi","bye"];
            <%
              String[] arr = myServer.getQueueList();
              for(int i = 0; i < arr.length; i++) { %>
            queueList[<%= i%>] = "<%=arr[i]%>";
            <% }%>
            for (var l = 1; l < queueList.length + 1; l++) {
                var newRow = queueTable.insertRow(l);
                var newCell1 = newRow.insertCell(0);
                newCell1.innerText = queueList[l-1];
                var newCell2 = newRow.insertCell(1);
                newCell2.innerText = "50 items";
            }
             **/
        }
    });

</script>
<script>
window.onload = function colorTableRow() {
    var url = window.location.href;
    var dist = url.indexOf("selectedServer=");
    if(dist != -1) {
        var serverName = url.substring(dist + 15);
        var table = document.getElementById("serverTable");
        var rows = table.getElementsByTagName("tr");
        var done = false;
        for (i = 0; i < rows.length; i++) {
            var Cells = rows[i].getElementsByTagName("td");
            for(j = 0; j < Cells.length; j++) {
                if(Cells[j].innerHTML === serverName) {
                    done = true;
                    toggleQueueTable();
                }
            }
            if(done == true) {
                table.rows[i].style.backgroundColor = 'lightgreen';
                break;
            }
        }
    }
}
</script>

</body>
</html>