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
    <link rel="icon" href="<c:url value="/resources/favicon.ico"/>" type="image/x-icon"/>
    <link rel="shortcut icon" href="<c:url value="/resources/favicon.ico"/>" type="image/x-icon"/>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
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
    var easter_egg = new Konami(function () {
        alert('Konami code!');
        document.getElementById('konami').style.display = "inline-block";
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

<div class="alert alert-info" align="center" id="loadingMessage" style="display: none">
    <p id="LoadText"><strong>Loading.</strong> Please wait operation in progress.</p>
    <i class="fa fa-spinner fa-spin" style="font-size:24px"></i>
</div>

<div class="alert alert-danger" align="center" id="failedServerMessage" style="display: none">
    <strong>Server Down!</strong> A server is in a failed state.
</div>

<div class="container">
    <div class="row">
        <div class="col-md-5" margin-bottom:30px;height:240px;width:100%;>
            <table id="serverTable" class="table" width="100%">
                <thead>
                <tr>
                    <th>Status</th>
                    <th>Server Type</th>
                    <th>IP Address</th>
                    <th>Port Number</th>
                    <th></th>
                    <th>Master Host & Port</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="loading" id="serverLoadIcon" align="center">
                <i class="fa fa-spinner fa-spin" style="font-size:24px"></i>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-5">
            <h5 id="progressHeading" style="display:none"></h5>
            <div class="progress" id="progressInfo" style="display:none">
                <div class="progress-bar active" id="progress-bar" role="progressbar" aria-valuenow="70"
                     aria-valuemin="0" aria-valuemax="100" style="width:0%">
                    0%
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
            <div class="loading" id="queueLoadIcon" align="center" style="display: none">
                <i class="fa fa-spinner fa-spin" style="font-size:24px"></i>
            </div>
        </div>
    </div>
</div>

<div id="konami" style="display: none">
    <table border="0" id="konamiTable">
        <tbody>
        </tbody>
    </table>

</div>

<div id="dialog" title="Add New Master Server">
    <label>New Host IP Address:</label>
    <input id="newHost" class="form-control" name="newHost" type="text" style="color:black;">
    <label>New Host Port Number:</label>
    <input id="newPort" class="form-control" name="newPort" type="text" style="color:black;">
    <label>Select Master Server to meet with and receive hash slots from:</label>
    <select class="form-control" id="currentMasterList"></select>
    <br>
    <input id="submit" type="submit" class="btn btn-info" value="Submit">
</div>

<script>
    //Toggles queue table off by default
    var qTable = document.getElementById("queueTable");
    var sTable = document.getElementById("serverTable");
    var serverLoadIcon = document.getElementById("serverLoadIcon");

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

        var queueLoadIcon = document.getElementById("queueLoadIcon");
        if (queueLoadIcon.style.display = "none") {
            queueLoadIcon.style.display = "block";
        }
    }

    function toggleFailedAlert(data) {

        var alertMessage = document.getElementById("failedServerMessage");

        for(var i = 0; i < data.length; j++) {
            if(data[i].status == "Disabled") {
                if (alertMessage.style.display = "none")
                    alertMessage.style.display = "block";
            }
        }
    }

    function getServerList() {
        //clears entire server table
        $('#serverTable > tbody').remove();
        $('#serverTable').append("<tbody></tbody>");

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
                toggleFailedAlert(data);
            }
        });

    }

    function getQueues(data2) {
        console.log(data2.port);
        console.log(data2.host);

        //clears entire table
        $('#queueTable > tbody').remove();
        $('#queueTable').append("<tbody></tbody>");

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}getQueues",
            data: JSON.stringify(data2),
            dataType: 'json',
            success: function (data) {
                console.log("Initial length: ", data.length);
                $("#queueHeading .serverValue").html(data[data.length - 1]);
                cleanQueueData(data);
                console.log("SUCCESS: ", data, "-> length: ", data.length);
                initializeQueueTable(data);
            }
        });
    }

    function sortNumber(a,b) {
        return a - b;
    }

    function cleanQueueData(data) {
        var arrayIndexes = [];
        for(var i = 0; i < ((data.length / 2) | 0); i++) {
            if(data[i].indexOf("_values") != -1) {
                var start = data[i].indexOf("{") + 1;
                var end = data[i].indexOf("}");
                data[i] = data[i].substring(start, end);
            } else {
                arrayIndexes.push(i);
                arrayIndexes.push(i + data.length / 2);
            }
        }
        //removing elements
        arrayIndexes.sort(sortNumber);
        for(var i = arrayIndexes.length - 1; i >= 0; i--) {
            data.splice(arrayIndexes[i], 1);
        }
        data.splice(data.length - 1, 1);
        console.log("DATA POST REMOVE", data);
    }

    function softRemoveServer(data2) {
        console.log(data2.port);
        console.log(data2.host);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}softRemoveServer",
            data: JSON.stringify(data2),
            dataType: 'json',
            success: function (data) {
                console.log("SUCCESS: ", data);
                document.getElementById("loadingMessage").style.display = "none";
                alert(data);
                if(data.contains("Successful Failover of "))
                    getServerList();
            }
        });
    }

    function serverMemoryUtil(data2) {
        console.log(data2.port);
        console.log(data2.host);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}getMemory",
            data: JSON.stringify(data2),
            dataType: 'json',
            success: function (data) {
                console.log("SUCCESS: ", data);
                updateMemoryBar(data.memory);
            }
        });
    }

    //Function to update the memory bar to display the utilization of current server.
    function updateMemoryBar(data) {
        data = data * 100;
        data = Math.ceil(data);

        if(data > 66) {
            document.getElementById("progressHeading").innerText = "Memory Load: High";
        } else if (data < 33) {
            document.getElementById("progressHeading").innerText = "Memory Load: Low";
        } else {
            document.getElementById("progressHeading").innerText = "Memory Load: Medium";
        }
        $("#progress-bar")
                .css("width", data + "%")
                .attr("aria-valuenow", data)
                .text(data + "%");
    }

    function initializeQueueTable(queueData) {
        document.getElementById("queueLoadIcon").style.display = "inline";

        newQueueRows = qTable.getElementsByTagName("tbody")[0];
        for (var l = 0; l < queueData.length / 2; l++) {
            var newRow = newQueueRows.insertRow(l);
            var newCell1 = newRow.insertCell(0);
            newCell1.innerText = queueData[l];
            var newCell2 = newRow.insertCell(1);
            newCell2.innerText = queueData[l + (queueData.length / 2) ];
        }

        document.getElementById("queueLoadIcon").style.display = "none";

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

        var selectBox = document.getElementById("currentMasterList");
        //Clear the list options first.
        var length = selectBox.options.length;
        for (i = 0; i < length; i++) {
            selectBox.options[i] = null;
        }

        //Now add all current options.
        var option;
        for(var j = 0; j < data.length; j++) {
            if(data[j].type == "Master") {
                option = document.createElement("option");
                option.value = data[j].host + ":" + data[j].port;
                option.innerHTML = data[j].host + ":" + data[j].port;
                selectBox.appendChild(option);
            }
        }

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
            newCell5.onclick = (function (host, port) {
                return function () {
                    var r = confirm("Are you sure you want to remove " + host + ":" + port + "?");
                    if (r) {  //Say yes to remove server
                        document.getElementById("loadingMessage").style.display = "block";
                        document.getElementById("LoadText").innerText = "Deleting " + host + ":" + port + ". Please wait.";
                    } else {  //Say no to cancel request
                        return;
                    }

                    var sendObject = {
                        host: host,
                        port: port
                    }

                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "${home}softRemoveServer2",
                        data: JSON.stringify(sendObject),
                        success: function (data) {
                            console.log("SUCCESS: ", data);
                            document.getElementById("loadingMessage").style.display = "none";
                            alert("Done");
                            location.reload();
                        }
                    });
                };
            })(data[i].host, data[i].port);

            var newCell6 = newRow.insertCell(5);
            if(data[i].masterHost != null && data[i].masterPort != null)
                newCell6.innerText = data[i].masterHost + ":" + data[i].masterPort;
        }

        document.getElementById("serverLoadIcon").style.display = "none";

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

            //Get memory util
            serverMemoryUtil(data[$(this).index()]);
            //Initialize queue list
            getQueues(data[$(this).index()]);
        });

    }

    //Adding a server function

    $(document).ready(function () {
        $(function () {
            $("#dialog").dialog({
                autoOpen: false
            });
            $("#addOption").on("click", function () {
                $("#dialog").dialog("open");
            });
        });

        $("#submit").click(function (e) {
            var newHost = $("#newHost").val();
            var newPort = $("#newPort").val();
            var currentMasterList = $("#currentMasterList").val();
            var separateStr = currentMasterList.split(":");
            var oldHost = separateStr[0];
            var oldPort = separateStr[1];
            if (newHost === '' || newPort === '' || oldHost === '' || oldPort === '') {
                alert("Please fill all fields.");
                e.preventDefault();
            } else {
                var yes = confirm("Do you want to add " + newHost + ":" + newPort + "?");

                if (yes) {
                    document.getElementById("loadingMessage").style.display = "block";
                    document.getElementById("LoadText").innerText = "Adding " + newHost + ":" + newPort + ". Please wait.";
                } else {
                    return;
                }
                var sendObject = {
                    server: {host: oldHost, port: oldPort},
                    serverAdd: {host: newHost, port: newPort}
                }

                $("#dialog").dialog("close");

                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "${home}addServers",
                    data: JSON.stringify(sendObject),
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                        document.getElementById("loadingMessage").style.display = "none";
                        alert(data);
                        location.reload();
                        //if(!data.contains("error")) {
                        //    location.reload();
                        //}

                    }
                });
            }
        });
    });

    getServerList();
</script>

</body>
</html>