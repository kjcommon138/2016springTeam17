<%--
  Created by IntelliJ IDEA.
  User: michael
  Date: 1/26/16
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ncsu.csc492.group17.cluster.ServerStatus, net.sf.json.JSONSerializer" %>
<html>
<head>
    <link rel="stylesheet" href="resources/core/css/bootstrap.css"/>
    <title>ElasticQueue</title>
</head>
<body>

<c:url var="home" value="/" scope="request"/>

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

<!-- Latest compiled JavaScript -->
<script src="resources/core/js/jquery.min.js"></script>
<script src="resources/core/js/bootstrap.min.js"></script>
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
    //Toggles queue table off by default
    var qTable = document.getElementById("queueTable");
    var rTable = document.getElementById("redisTable");
    var sTable = document.getElementById("serverTable");

    function toggleQueueTable() {

        var heading = document.getElementById("progressHeading");
        if (heading.style.display == "none")
            heading.style.display = "inline";

        heading = document.getElementById("progressInfo");
        if (heading.style.display == "none")
            heading.style.display = "block";

        if (qTable.style.display == "none")
            qTable.style.display = "table";

        if (rTable.style.display == "table")
            rTable.style.display = "none";

        var heading = document.getElementById("queueHeading");
        if (heading.style.display == "none")
            heading.style.display = "inline";
    }

    function toggleRedisTable() {
        if (rTable.style.display == "none")
            rTable.style.display = "table";
    }

    //clears entire server table
    $('#serverTable > tbody').remove();
    $('#serverTable').append("<tbody></tbody>");


    function getServerList() {
        var server = {};
        server["port"] = "30001";
        server["host"] = "152.14.106.22";

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}getServers",
            data: JSON.stringify(server),
            dataType: 'json',
            timeout: 4500,
            success: function (data) {
                console.log("SUCCESS: ", data);
                initializeTable(data);
            }
        });

    }

    function getQueues(data2) {
        console.log(data2.port);
        console.log(data2.host);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}getQueues",
            data: JSON.stringify(data2),
            dataType: 'json',
            timeout: 4500,
            success: function (data) {
                console.log("SUCCESS: ", data);
                initializeQueueTable(data, data2);
            }
        });
    }

    function getItems(key, sData) {
        sData.key = key;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}getItems",
            data: JSON.stringify(sData),
            dataType: 'json',
            timeout: 4500,
            success: function (data) {
                console.log("SUCCESS: ", data);
                initializeItemsTable(data);
            }
        });
    }

    function initializeQueueTable(queueData, serverData) {
        //clears entire table
        $('#queueTable > tbody').remove();
        $('#queueTable').append("<tbody></tbody>");

        newQueueRows = qTable.getElementsByTagName("tbody")[0];
        for (var l = 0; l < queueData.length / 2; l++) {
            var newRow = newQueueRows.insertRow(l);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = queueData[l];
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = queueData[l+(queueData.length/2)];
        }

        $('#queueTable > tbody').find('tr').click(function () {
            var rows = qTable.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
            for (i = 0; i < rows.length; i++) {
                rows[i].style.backgroundColor = 'white';
            }
            rows[$(this).index()].style.backgroundColor = 'lightblue';
            var Cells = qTable.rows[$(this).index() + 1].getElementsByTagName("td");
            //window.location.href = "index.jsp?selectedServer=" + Cells[0].innerText;

            selectedQueue = Cells[0].innerText;
            $(".queueValue").html(selectedQueue);

            toggleRedisTable();

            //output table of values inside of queue
            getItems(selectedQueue, serverData);
        });
    }

    function initializeItemsTable(listData) {
        //clears entire table
        $('#redisTable > tbody').remove();
        $('#redisTable').append("<tbody></tbody>");
        newRedisRows = rTable.getElementsByTagName("tbody")[0];

        for (l = 0; l < listData.length; l++) {
            var newRow = newRedisRows.insertRow(l);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerHTML = l + 1;
            var newCell2 = newRow.insertCell(1);
            newCell2.innerHTML = listData[l];
        }
    }

    function initializeTable(data) {
        console.log(data[0].host);
        console.log(data[0].port);

        //Initialize list of servers
        newServerRows = sTable.getElementsByTagName("tbody")[0];
        for (var i = 0; i < data.length; i++) {
            var newRow = newServerRows.insertRow(i);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = data[i].nodeID;
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = data[i].status;
            var newCell3 = newRow.insertCell(2);
            newCell3.innerText = data[i].type;
            var newCell4 = newRow.insertCell(3);
            newCell4.innerText = data[i].host;
            var newCell5 = newRow.insertCell(4);
            newCell5.innerText = data[i].port;
        }

        $('#serverTable > tbody').find('tr').click(function () {
            var rows = sTable.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
            for (i = 0; i < rows.length; i++) {
                rows[i].style.backgroundColor = 'white';
            }

            var queueRows = qTable.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
            for (i = 0; i < queueRows.length; i++) {
                queueRows[i].style.backgroundColor = 'white';
            }

            rows[$(this).index()].style.backgroundColor = 'lightblue';
            toggleQueueTable();
            var Cells = sTable.rows[$(this).index() + 1].getElementsByTagName("td");

            selectedServer = Cells[0].innerText;

            $("#queueHeading .serverValue").html(selectedServer);

            //Initialize queue list
            getQueues(data[$(this).index()]);
        });
    }

    getServerList();
</script>

</body>
</html>