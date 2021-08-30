<p>Hello <span id="test"><% out.write("world"); %></span></p>

<button onclick="sendName();">Click Me</button>
<script src="js/jquery-1.11.2.min.js"></script>
<script src="js/stomp.min.js"></script>
<script src="js/sockjs.min.js"></script>
<script>

var ws = null;

function connect() {
    var url = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/'+window.location.pathname.split('/')[1]+'/socket';
    var socket = new SockJS(url);
    ws = Stomp.over(socket);
    
    console.log('connect');

    ws.connect({}, function(frame) {
           ws.subscribe("/queue/errors", function(message) {
                  alert("Error " + message.body);
           });

           ws.subscribe("/queue/reply", function(message) {                   
                  $("#test").text(message.body);
                  
           });
    }, function(error) {
           //alert("STOMP error " + error);
           console.log('re-connect...');
           setTimeout(function() {connect()}, 1000*30);
    });
}

function disconnect() {
 if (ws != null) {
     ws.close();
 }
 setConnected(false);
 console.log("Disconnected");
}

function sendName() {
    ws.send("/ws/hello", {}, JSON.stringify({'name': 'Stomp JSON'})); // path /ws setup in WebSocketConfig.class
}

$(document).ready(function(){
	connect();
});



</script>