const REQ_MESSAGE = "Request mock work";

var WS;

window.onload = function() {
    WS = new WebSocket("ws://localhost:8090/cache-info");

    WS.onopen = function() {
        console.log("WebSocket connection established");
        document.getElementById("reqWork").style.visibility = "visible";
    }

    WS.onclose = function(event) {
        if(event.wasClean) {
            alert("Connection was closed properly");
        } else {
            alert("Connection was interrupted");
        }
        alert("Code: " + event.code + " reason: " + event.reason);
    }

    WS.onmessage = function(event) {
        var info = JSON.parse(event.data);
        var container = document.getElementById("cacheInfo");
        var outerList = document.createElement("ul");

        var regNames = outerList.appendChild(document.createElement("li"));
        regNames.textContent = "Cache regions: ";
        var innerList = regNames.appendChild(document.createElement("ul"));

        var regNum = info.regNames.length;
        for(i = 0; i < regNum; i++) {
            var listPoint = innerList.appendChild(document.createElement("li"));
            listPoint.textContent = info.regNames[i];
        }

        outerList.appendChild(document.createElement("li")).textContent = "Number of puts: " + info.putNum;
        outerList.appendChild(document.createElement("li")).textContent = "Number of hits: " + info.hitNum;
        outerList.appendChild(document.createElement("li")).textContent = "Number of misses: " + info.missNum;

        container.innerHTML = "";
        container.appendChild(outerList);
    }

    WS.onerror = function(error) {
        alert("Error: " + error.message);
    }
}

function reqWork() {
    WS.send(REQ_MESSAGE);
    document.getElementById("reqWork").disabled = true;
}