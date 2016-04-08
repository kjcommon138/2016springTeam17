<%--
  Created by IntelliJ IDEA.
  User: michael
  Date: 1/26/16
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cluster.ServerStatus, net.sf.json.JSONSerializer" %>
<html>
<head>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <title>ElasticQueue</title>
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
    String selectedServer;
%>

<!-- Latest compiled JavaScript -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
    var selectedServer = "";
    var selectedQueue = "";
</script>

<div class="container">
    <% if (myServer.getRedisConnection().equalsIgnoreCase("Disabled")) { %>
    <div class="alert alert-danger">
        <strong>Server down!</strong> One or more servers are down.
    </div>
    <% } %>

</div>
<div class="container">
    <div class="row">
        <div class="col-md-5" style="overflow:scroll;margin-bottom:30px;height:240px;width:100%;overflow:auto">
            <table id="serverTable" class="table" width="100%">
                <thead>
                <tr>
                    <th>Server</th>
                    <th>Status</th>
                    <th>Server Type</th>
                    <th>IP Address</th>
                    <th>Port Number</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-md-5">
            <h5 id="progressHeading" style="display:none">Load: High</h5>
            <div class="progress" id="progressInfo" style="display:none">
                <div class="progress-bar" role="progressbar" aria-valuenow="70"
                     aria-valuemin="0" aria-valuemax="100" style="width:70%">
                    70%
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <h4 id="queueHeading" style="display:none">Showing queues for: <span class="serverValue"></span></h4>
        <div class="col-md-5">
            <span style="display:none" class="queueValue"></span>
            <table style="display:none" id="queueTable" class="table" width="100%">
                <thead>
                <tr>
                    <th>Queues</th>
                    <th>Size</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Q1</td>
                    <td>50 Items</td>
                </tr>
                <tr>
                    <td>Q2</td>
                    <td>94 Items</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="col-md-5" style="overflow:scroll;margin-left:100px;height:100px;overflow:auto">
            <table style="display:none" class="table" width:100% id="redisTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Item</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>

    console.log(<%=myServer.getList()%>);

    //Toggles queue table off by default
    var qTable = document.getElementById("queueTable");
    var rTable = document.getElementById("redisTable");
    var sTable = document.getElementById("serverTable");

    function toggleQueueTable() {

        var heading = document.getElementById("progressHeading");
        if (heading.style.display == "none")
            heading.style.display = "inline";

        var heading = document.getElementById("progressInfo");
        if (heading.style.display == "none")
            heading.style.display = "block";

        var qTable = document.getElementById("queueTable");
        if (qTable.style.display == "none")
            qTable.style.display = "table";

        var rTable = document.getElementById("redisTable");
        if (rTable.style.display == "table")
            rTable.style.display = "none";

        var heading = document.getElementById("queueHeading");
        if (heading.style.display == "none")
            heading.style.display = "inline";
    }

    function toggleRedisTable() {
        var rTable = document.getElementById("redisTable");
        if (rTable.style.display == "none")
            rTable.style.display = "table";
    }


    <%
      String[][] serverList = myServer.getServerList();
      %>
    var numberServers = <%=serverList.length%>;
    var serverList = [numberServers];

    //clears entire server table
    $('#serverTable > tbody').remove();
    $('#serverTable').append("<tbody></tbody>");

    //Initialize list of servers
    var serverSet = new Array(<%=serverList.length%>);
    <%
      for(int j = 0; j < serverList.length; j++) {
    %>
    serverList[<%=j%>] = <%=JSONSerializer.toJSON(serverList[j])%>;
    <%}%>

    newServerRows = sTable.getElementsByTagName("tbody")[0];
    for (var l = 0; l < serverList.length; l++) {
        var newRow = newServerRows.insertRow(l);
        var newCell1 = newRow.insertCell(0);
        newCell1.innerText = serverList[l][0];
        var newCell2 = newRow.insertCell(1);
        newCell2.innerText = serverList[l][1];
        var newCell3 = newRow.insertCell(2);
        newCell3.innerText = serverList[l][2];
        var newCell4 = newRow.insertCell(3);
        newCell4.innerText = serverList[l][3];
        var newCell5 = newRow.insertCell(4);
        newCell5.innerText = serverList[l][4];
    }

    $('#serverTable > tbody').find('tr').click(function () {
        var table = document.getElementById("serverTable");
        var rows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
        for (i = 0; i < rows.length; i++) {
            rows[i].style.backgroundColor = 'white';
        }

        var queueTable = document.getElementById("queueTable");
        var queueRows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
        for (i = 0; i < queueRows.length; i++) {
            queueRows[i].style.backgroundColor = 'white';
        }

        rows[$(this).index()].style.backgroundColor = 'lightblue';
        toggleQueueTable();
        var Cells = table.rows[$(this).index() + 1].getElementsByTagName("td");

        selectedServer = Cells[0].innerText;

        $("#queueHeading .serverValue").html(selectedServer);

        var queueTable = document.getElementById("queueTable");
        //clears entire table
        $('#queueTable > tbody').remove();
        $('#queueTable').append("<tbody></tbody>");

        //Initializes javascript arrays for series of lists
        var queueList = [];
        var itemLength = [];

        //Initialize java arrays for queue and server list
        <%
          String[] arr = myServer.getQueueList();
          %>

        //Initialize list of queues
        var redisSet = new Array(<%=arr.length%>);
        <%
          for(int i = 0; i < arr.length; i++) {
           %>
        queueList.push("<%=arr[i]%>");
        redisSet[<%=i%>] = <%=JSONSerializer.toJSON((myServer.getList(arr[i])))%>;
        itemLength.push("<%=myServer.getList(arr[i]).length + " items"%>")
        <% } %>

        newQueueRows = queueTable.getElementsByTagName("tbody")[0];
        for (var l = 0; l < queueList.length; l++) {
            var newRow = newQueueRows.insertRow(l);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = queueList[l];
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = itemLength[l];
        }

        $('#queueTable > tbody').find('tr').click(function () {
            var table = document.getElementById("queueTable");
            var rows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
            for (i = 0; i < rows.length; i++) {
                rows[i].style.backgroundColor = 'white';
            }
            rows[$(this).index()].style.backgroundColor = 'lightblue';
            var Cells = table.rows[$(this).index() + 1].getElementsByTagName("td");
            //window.location.href = "index.jsp?selectedServer=" + Cells[0].innerText;

            selectedQueue = Cells[0].innerText;
            $(".queueValue").html(selectedQueue);

            toggleRedisTable();

            //output table of values inside of queue
            var redisTable = document.getElementById("redisTable");
            //clears entire table
            $('#redisTable > tbody').remove();
            $('#redisTable').append("<tbody></tbody>");
            newRedisRows = redisTable.getElementsByTagName("tbody")[0];

            for (l = 0; l < redisSet[$(this).index()].length; l++) {
                var newRow = newRedisRows.insertRow(l);
                var newCell1 = newRow.insertCell(0);
                newCell1.innerHTML = l + 1;
                var newCell2 = newRow.insertCell(1);
                newCell2.innerHTML = redisSet[$(this).index()][l];
            }
        });
    });
</script>

</body>
</html>