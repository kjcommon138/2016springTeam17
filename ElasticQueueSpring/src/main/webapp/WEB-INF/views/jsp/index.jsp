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
<c:url var="home" value="/" scope="request"/>
<html>
<head>
    <link rel="stylesheet" href="resources/core/css/bootstrap.css"/>
    <link rel="icon" href="<c:url value="/resources/favicon.ico"/>" type="image/x-icon" />
    <link rel="shortcut icon" href="<c:url value="/resources/favicon.ico"/>" type="image/x-icon" />

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
                    <li id="addOption">Add a Server</li>
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
<script src="resources/core/js/konami.js"></script>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/ui-darkness/jquery-ui.css" rel="stylesheet">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<script>
    var easter_egg = new Konami(function() {
        alert('Konami code!');
        document.getElementById('konami').style.display="inline-block";
        var itemTable = document.getElementById('konamiTable');

        var newRows = itemTable.getElementsByTagName("tbody")[0];
        var picRow = newRows.insertRow(0);
        var onePic = picRow.insertCell(0);
        onePic.innerHTML = "<img src=\"resources/pug1.gif\">";

        var textRow = newRows.insertRow(1);
        var oneText = textRow.insertCell(0);
        oneText.innerText = "By Sneha Bhalodia, Kevin Commons, Michael Goff, and Laurel Timko";
    });
    var selectedServer = "";
    var selectedQueue = "";
</script>

</div>
<div class="container">
    <div class="row">
        <div class="col-md-5" style="overflow:scroll;margin-bottom:30px;height:240px;width:100%;overflow:auto">
            <table id="serverTable" class="table" width="100%">
                <thead>
                <tr>
                    <th>Status</th>
                    <th>Server Type</th>
                    <th>IP Address</th>
                    <th>Port Number</th>
                    <th></th>
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
    </div>
</div>

<div id="konami" style="display: none">
    <table border="0" id="konamiTable">
        <tbody>
        </tbody>
    </table>

</div>

<div id="dialog" title="Dialog Form">
    <label>New Host IP Address:</label>
    <input id="newHost" name="newHost" type="text" style="color:black;">
    <label>New Host Port Number:</label>
    <input id="newPort" name="newPort" type="text" style="color:black;">
    <label>Existing MASTER Host IP Address:</label>
    <input id="oldHost" name="oldHost" type="text" style="color:black;">
    <label>Existing MASTER Port Number:</label>
    <input id="oldPort" name="oldPort" type="text" style="color:black;">
    <input id="submit" type="submit" value="Submit" style="color:red;">
</div>

<script>
    //Toggles queue table off by default
    var qTable = document.getElementById("queueTable");
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

        var heading = document.getElementById("queueHeading");
        if (heading.style.display == "none")
            heading.style.display = "inline";
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
            success: function (data) {
                console.log("SUCCESS: ", data);
                initializeQueueTable(data);
            }
        });
    }

    function initializeQueueTable(queueData) {
        //clears entire table
        $('#queueTable > tbody').remove();
        $('#queueTable').append("<tbody></tbody>");

        newQueueRows = qTable.getElementsByTagName("tbody")[0];
        for (var l = 0; l < queueData.length / 2; l++) {
            var newRow = newQueueRows.insertRow(l);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = queueData[l];
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = queueData[l + (queueData.length / 2)];
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
        });
    }

    function initializeTable(data) {
        console.log(data[0].host);
        console.log(data[0].port);

        //Initialize list of servers
        newServerRows = sTable.getElementsByTagName("tbody")[0];
        for (var i = 0; i < data.length; i++) {
            var newRow = newServerRows.insertRow(i);
            newRow.id = "row" + i;
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = data[i].status;
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = data[i].type;
            var newCell3 = newRow.insertCell(2);
            newCell3.innerText = data[i].host;
            var newCell4 = newRow.insertCell(3);
            newCell4.innerText = data[i].port;
            var newCell5 = newRow.insertCell(4);
            newCell5.innerHTML = "<span class=\"glyphicon glyphicon-trash\"></span>";
            newCell5.id = "trash";
            newCell5.onclick = (function(host, port) {
                return function () {
                    alert('hello');
                    var sendObject = {
                        host: host,
                        port: port
                    }

                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "${home}removeServers",
                        data: JSON.stringify(sendObject),
                        success: function (data) {
                            console.log("SUCCESS: ", data);
                            window.location.reload(true);
                        }
                    });
                };
            }) (data[i].host, data[i].port);
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

            $("#queueHeading .serverValue").html(Cells[2].innerText + ":" + Cells[3].innerText);

            //Initialize queue list
            getQueues(data[$(this).index()]);
        });

    }

    //Adding a server function

    $(document).ready(function() {
        $(function() {
            $("#dialog2").dialog({
                autoOpen: false
            });
            $("#addOption").on("click", function() {
                $("#dialog").dialog("open");
            });
        });

        $("#submit").click(function(e) {
            var newHost = $("#newHost").val();
            var newPort = $("#newPort").val();
            var oldHost = $("#oldHost").val();
            var oldPort = $("#oldPort").val();
            if (newHost === '' || newPort === '' || oldHost === '' || oldPort === '') {
                alert("Please fill all fields.");
                e.preventDefault();
            } else {
                alert("Adding " + newHost + ":" + newPort + ".");
                var sendObject = {
                    server: {host:oldHost, port:oldPort},
                    serverAdd: {host:newHost, port:newPort}
                }

                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "${home}addServers",
                    data: JSON.stringify(sendObject),
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                        window.location.reload(true);
                    }
                });

                $("#dialog").dialog("close");
            }
        });
    });

    getServerList();
</script>

</body>
</html>